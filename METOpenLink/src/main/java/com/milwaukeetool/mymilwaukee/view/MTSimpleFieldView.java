package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.util.StringHelper;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTSimpleFieldView extends RelativeLayout {
    private EditText mEditText;
    private FieldType mFieldType;
    private boolean mRequired;

    public MTSimpleFieldView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_create_account_content, this);
        this.mEditText = (EditText) this.findViewById(R.id.editTextField);
        this.mFieldType = FieldType.STANDARD;
        this.mRequired = true;
    }

    public void setFieldName(String fieldName) {
        this.mEditText.setHint(fieldName);
    }

    public String getFieldValue() {
        return this.mEditText.getText().toString();
    }

    public FieldType getFieldType() {
        return this.mFieldType;
    }

    public boolean isRequired() {
        return mRequired;
    }

    public void setRequired(boolean isRequired) {
        this.mRequired = isRequired;
    }

    public MTSimpleFieldView setFieldType(FieldType fieldType) {
        switch (fieldType) {
            case EMAIL:
                mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                this.mFieldType = fieldType;
                break;
            case PASSWORD:
                //mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mEditText.setTransformationMethod(new PasswordTransformationMethod());
                mEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                this.mFieldType = fieldType;
                break;
        }

        return this;
    }

    public static MTSimpleFieldView createSimpleFieldView(Context context, String fieldName) {
        MTSimpleFieldView simpleFieldView = new MTSimpleFieldView(context);
        simpleFieldView.setFieldName(fieldName);
        return simpleFieldView;
    }

    public enum FieldType {
        STANDARD,
        PASSWORD,
        EMAIL
    }

    public MTSimpleFieldView updateFocus() {
        mEditText.requestFocus();
        return this;
    }

    public boolean isValid() {
        if (this.isRequired()) {
            if (TextUtils.isEmpty(this.getFieldValue()) || this.getFieldValue().length() > 1024) {
                return false;
            }

            switch (this.getFieldType()) {
                case EMAIL:
                    if (!StringHelper.isEmailValid(this.getFieldValue())) {
                        return false;
                    }
                    break;
                case PASSWORD:
                    if (this.getFieldValue().length() < 8) {
                        return false;
                    }
                    if (!StringHelper.containsLowercase(this.getFieldValue()) ||
                            !StringHelper.containsNumber(this.getFieldValue()) ||
                            !StringHelper.containsUppercase(this.getFieldValue())) {
                        return false;
                    }
                    break;
            }
        }

        return true;
    }
}
