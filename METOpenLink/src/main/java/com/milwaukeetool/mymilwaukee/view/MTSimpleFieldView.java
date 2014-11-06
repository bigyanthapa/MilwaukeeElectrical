package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTSimpleFieldView extends RelativeLayout {
    private EditText mEditText;

    public MTSimpleFieldView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_create_account_content, this);
        this.mEditText = (EditText) this.findViewById(R.id.editTextField);
    }

    public void setFieldName(String fieldName) {

        this.mEditText.setHint(fieldName);
    }

    public void getFieldValue() {

        this.mEditText.getText();
    }
}
