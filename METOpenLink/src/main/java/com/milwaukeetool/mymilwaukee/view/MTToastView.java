package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.interfaces.MTFinishedListener;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

/**
 * Created by cent146 on 11/24/14.
 */
public class MTToastView extends RelativeLayout {

    public static int MT_TOAST_LONG = 5000;

    public static int MT_TOAST_SHORT = 2000;

    private Activity mCallingActivity;
    private MTFinishedListener mFinishedListener;

    private ImageView mImageView;
    private MTTextView mTextView;

    public MTToastView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MTToastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTToastView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_mt_toast, this);

        if (mCallingActivity instanceof MTActivity) {

            this.setOnTouchListener( new MTTouchListener(mCallingActivity) {

                @Override
                public void didTapView(MotionEvent event) {
                    ((ViewGroup)MTToastView.this.getParent()).removeView(MTToastView.this);

                    if (mFinishedListener != null) {
                        mFinishedListener.didFinish();
                    }
                }
            });
        } else {
            this.setOnTouchListener(new MTTouchListener(mCallingActivity) {});
        }

        mImageView = (ImageView)findViewById(R.id.mtToastImageView);
        mTextView = (MTTextView)findViewById(R.id.mtToastTextView);
    }

    public static void showMessage(final MTActivity activity, String message, int duration, Drawable iconDrawable, final MTFinishedListener listener) {

        final MTLayout rootLayout = activity.getRootLayout();

        // create this
        final MTToastView toastView = new MTToastView(activity);
        toastView.mCallingActivity = activity;
        toastView.mFinishedListener = listener;
        toastView.mTextView.setText(message);
        toastView.mImageView.setImageDrawable(iconDrawable);

        // show this
        rootLayout.addView(toastView, rootLayout.getChildCount());

        MiscUtils.runDelayed(duration, new MiscUtils.RunDelayedCallback() {
            @Override
            public void onFinished() {
                // hide this
                ((ViewGroup)toastView.getParent()).removeView(toastView);

                if (listener != null) {
                    listener.didFinish();
                }
            }
        });
    }

    public static void showSuccessMessage(final MTActivity activity, String message) {
        final IconDrawable successDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                Iconify.IconValue.fa_check_circle).colorRes(R.color.mt_black).sizeDp(40);

        // Show success
        MTToastView.showMessage(activity,
                message,
                MTToastView.MT_TOAST_SHORT,
                successDrawable,
                new MTFinishedListener() {
                    @Override
                    public void didFinish() {

                    }
                });
    }

}
