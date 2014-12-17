package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view_reuseable.MTTextView;

/**
 * Created by scott.hopfensperger on 11/14/2014.
 */
public class MTNoNetworkView extends RelativeLayout {

    private MTTextView noNetworkMessageTextView;
    private ImageButton mCloseImageButton;

    public MTNoNetworkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MTNoNetworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTNoNetworkView(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_no_network, this);
        this.setId(R.id.mtBaseLayout);

        this.noNetworkMessageTextView = (MTTextView) this.findViewById(R.id.noNetworkConnectivityTextView);

        this.mCloseImageButton = (ImageButton) this.findViewById(R.id.noNetworkConnectivityCloseButton);
        mCloseImageButton.setImageDrawable(new IconDrawable(context, Iconify.IconValue.fa_times_circle).colorRes(R.color.mt_white));
        mCloseImageButton.setOnTouchListener(new MTTouchListener(context) {
            @Override
            public void didTapView(MotionEvent event) {
                UIUtils.hideView(MTNoNetworkView.this, 500);
            }
        });

        this.setOnTouchListener(new MTTouchListener(context) {
            @Override
            public void didTapView(MotionEvent event) {
                UIUtils.hideView(MTNoNetworkView.this, 500);
            }
        });
    }

    public void showMessage() {
        UIUtils.showView(this, 500);
    }
    public void hideMessage() {
        UIUtils.hideView(this, 500);
    }
}
