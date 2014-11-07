package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
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
    private int mMinLength;

    public enum FieldType {
        STANDARD,
        PASSWORD,
        EMAIL
    }

    public MTSimpleFieldView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_create_account_content, this);
        this.mEditText = (EditText) this.findViewById(R.id.editTextField);
        this.mFieldType = FieldType.STANDARD;
        this.mRequired = true;
        this.mMinLength = 0;
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

    public MTSimpleFieldView setRequired(boolean isRequired) {
        this.mRequired = isRequired;
        return this;
    }

    public MTSimpleFieldView setMinLength(int minLength) {
        this.mMinLength = minLength;
        return this;
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
            default:
                this.mFieldType = FieldType.STANDARD;
        }

        return this;
    }

    public static MTSimpleFieldView createSimpleFieldView(Context context, String fieldName) {
        MTSimpleFieldView simpleFieldView = new MTSimpleFieldView(context);
        simpleFieldView.setFieldName(fieldName);
        return simpleFieldView;
    }



    public MTSimpleFieldView updateFocus() {
        mEditText.requestFocus();
        return this;
    }

    public boolean isValid() {
        if (this.isRequired()) {
            if (TextUtils.isEmpty(this.getFieldValue())) {
                mEditText.setError("Field is required");
                return false;
            }
        }

        if (this.getFieldValue().length() > 1024) {
            mEditText.setError("Field exceeds 1024 character limit");
        }

        if (mMinLength > 0) {
            if (this.getFieldValue().length() < mMinLength) {
                mEditText.setError("Field must be at least " + mMinLength + " characters");
                return false;
            }
        }

        switch (this.getFieldType()) {
            case EMAIL:
                if (!StringHelper.isEmailValid(this.getFieldValue())) {
                    mEditText.setError("Invalid email address");
                    return false;
                }
                break;
            case PASSWORD:
                if (!StringHelper.containsLowercase(this.getFieldValue()) ||
                        !StringHelper.containsNumber(this.getFieldValue()) ||
                        !StringHelper.containsUppercase(this.getFieldValue())) {
                    mEditText.setError("Invalid password");
                    return false;
                }
                break;
            default:
                // Standard validation?
                break;
        }

        mEditText.setError(null);

        return true;
    }
}
