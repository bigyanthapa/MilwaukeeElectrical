package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.activity.MTActivity;
import com.milwaukeetool.mymilwaukee.interfaces.MTFinishedListener;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;

/**
 * Created by cent146 on 11/24/14.
 */
public class MTToastView extends RelativeLayout {

    public static int getLongDuration() {
        return 5000;
    }

    public static int getShortDuration() {
        return 2000;
    }

    private Activity mCallingActivity;

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

        this.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            // Nothing to do, just override touches
        });

        mImageView = (ImageView)findViewById(R.id.mtToastImageView);
        mTextView = (MTTextView)findViewById(R.id.mtToastTextView);
    }

    public static void showMessage(final MTActivity activity, String message, int duration, Drawable iconDrawable, final MTFinishedListener listener) {

        final MTLayout rootLayout = activity.getRootLayout();

        // create this
        MTToastView toastView = new MTToastView(activity);
        toastView.mTextView.setText(message);
        toastView.mImageView.setImageDrawable(iconDrawable);

        // show this
        //View.inflate(activity, R.layout.view_mt_toast, rootLayout);
        rootLayout.addView(toastView, rootLayout.getChildCount());

        MiscUtils.runDelayed(duration, new MiscUtils.RunDelayedCallback() {
            @Override
            public void onFinished() {
                // hide this
                activity.getRootLayout().removeView(rootLayout.findViewById(R.id.mtToastLayout));

                if (listener != null) {
                    listener.didFinish();
                }
            }
        });
    }

}
