package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;

import com.milwaukeetool.mymilwaukee.util.Typefaces;

/**
 * Created by scott.hopfensperger on 11/20/2014.
 */
public class MTSwitch extends Switch {
    public MTSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public MTSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MTSwitch(Context context) {
        super(context);
        init();
    }


    private void init() {
        setTypeface(Typefaces.get(getContext(), "fonts/Arial.ttf"));
    }
}
