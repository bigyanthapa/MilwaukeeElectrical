package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.milwaukeetool.mymilwaukee.util.UIUtils;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by cent146 on 11/10/14.
 */
public class MTProgressView extends RelativeLayout {

    private Activity mCallingActivity;

    private SmoothProgressBar mProgressBar;
    private MTTextView mProgressMessageTextView;

    private boolean mDisplayed = false;

    public MTProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MTProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTProgressView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_progress_indicator, this);

        this.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            // Nothing to do, just override touches
        });

        mProgressBar = (SmoothProgressBar)findViewById(R.id.progressBar);
        mProgressMessageTextView = (MTTextView)findViewById(R.id.progressMessageTextView);
    }

    public void updateMessage(String message) {
        mProgressMessageTextView.setText(message);
    }

    public void updateMessageAndStart(String message) {
        updateMessage(message);
        startProgress();
    }

    public void startProgress() {
        UIUtils.showView(this, 500);
        mDisplayed = true;
        mProgressBar.progressiveStart();
    }

    public void stopProgress() {
        mProgressBar.progressiveStop();
        mDisplayed = false;
        UIUtils.hideView(this,500);
    }

    public boolean isDisplayed() {
        return mDisplayed;
    }
}
