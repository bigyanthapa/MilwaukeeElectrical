package com.milwaukeetool.mymilwaukee.view_reuseable;

/**
 * Created by cent146 on 11/20/14.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

/**
 * It helps you keep focused.
 *
 * For use as a parent of {@link android.widget.ListView}s that need to use EditText
 * children for inline editing.
 */
public class RitalinLayout extends RelativeLayout {
    View sticky;

    public RitalinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        ViewTreeObserver vto = getViewTreeObserver();

        vto.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (newFocus == null) return;

                View baby = getChildAt(0);

                if (newFocus != baby) {
                    ViewParent parent = newFocus.getParent();
                    while (parent != null && parent != parent.getParent()) {
                        if (parent == baby) {
                            sticky = newFocus;
                            break;
                        }
                        parent = parent.getParent();
                    }
                }
            }
        });

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                if (sticky != null) {
                    sticky.requestFocus();
                }
            }
        });
    }
}