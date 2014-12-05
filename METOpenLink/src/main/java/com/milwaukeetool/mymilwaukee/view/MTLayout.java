package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.milwaukeetool.mymilwaukee.R;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/11/14.
 */
public class MTLayout extends FrameLayout {

    private static final String TAG = makeLogTag(MTLayout.class);

    private Context mContext = null;
    private boolean mKeyboardVisible = false;

    public MTLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MTLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MTLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        this.setId(R.id.mtBaseLayout);

        mKeyboardVisible = false;
        setListenerToRootView();
    }

    public boolean isKeyboardVisible() {
        return mKeyboardVisible;
    }

    public void keyboardVisible() {
        // Keyboard is shown
        mKeyboardVisible = true;
        //LOGD(TAG, "Keyboard listener: Shown");
        //EventBus.getDefault().post(new MTKeyboardEvent(this,true));
    }

    public void keyboardHidden() {
        // Keyboard is hidden
        mKeyboardVisible = false;
        //LOGD(TAG, "Keyboard listener: Hidden");
        //EventBus.getDefault().post(new MTKeyboardEvent(this,false));
    }

    public void setListenerToRootView(){
        final View activityRootView = ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
            int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
            if (heightDiff > 100 ) { // 99% of the time the height diff will be due to a keyboard.
                keyboardVisible();
            }else if(mKeyboardVisible == true){
                keyboardHidden();
            }
            }
        });
    }
}
