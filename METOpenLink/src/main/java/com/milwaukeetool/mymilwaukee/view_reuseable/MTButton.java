package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.milwaukeetool.mymilwaukee.util.Typefaces;

/**
 * Created by cent146 on 10/24/14.
 */
public class MTButton extends Button {

    public MTButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public MTButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MTButton(Context context) {
        super(context);
        init();
    }


    private void init() {
        setTypeface(Typefaces.get(getContext(), "fonts/Arial.ttf"));
    }
}
