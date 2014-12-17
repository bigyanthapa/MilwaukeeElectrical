package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.milwaukeetool.mymilwaukee.util.Typefaces;

/**
 * Created by cent146 on 11/6/14.
 */
public class MTCheckBox extends CheckBox {

    public MTCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public MTCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MTCheckBox(Context context) {
        super(context);
        init();
    }


    private void init() {
        setTypeface(Typefaces.get(getContext(), "fonts/Arial.ttf"));
    }
}
