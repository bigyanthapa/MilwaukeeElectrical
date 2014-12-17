package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.util.Typefaces;

/**
 * Created by cent146 on 11/19/14.
 */
public class MTHeaderTextView extends TextView {

    public MTHeaderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public MTHeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MTHeaderTextView(Context context) {
        super(context);
        init();
    }


    private void init() {
        setTypeface(Typefaces.get(getContext(), "fonts/Arial Black.ttf"));
    }
}
