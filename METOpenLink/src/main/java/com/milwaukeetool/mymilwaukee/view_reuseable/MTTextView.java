package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.util.Typefaces;

/**
 * Created by cent146 on 10/24/14.
 */
public class MTTextView extends TextView {
    public MTTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public MTTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MTTextView(Context context) {
        super(context);
        init();
    }


    private void init() {
        setTypeface(Typefaces.get(getContext(), "fonts/Arial.ttf"));
    }
}
