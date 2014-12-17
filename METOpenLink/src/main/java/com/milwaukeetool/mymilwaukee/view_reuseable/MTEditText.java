package com.milwaukeetool.mymilwaukee.view_reuseable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.milwaukeetool.mymilwaukee.util.Typefaces;

/**
 * Created by cent146 on 10/24/14.
 */
public class MTEditText extends EditText {
    public MTEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public MTEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MTEditText(Context context) {
        super(context);
        init();
    }


    private void init() {
        setTypeface(Typefaces.get(getContext(), "fonts/Arial.ttf"));
    }
}
