package com.milwaukeetool.mymilwaukee.view;

import android.content.Context;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.milwaukeetool.mymilwaukee.R;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTSimpleFieldView extends RelativeLayout {
    private EditText mEditText;
    private boolean mFieldIsRequired;

    public MTSimpleFieldView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_create_account_content, this);
        this.mEditText = (EditText) this.findViewById(R.id.editTextField);
    }

    public void setFieldName(String fieldName) {

        this.mEditText.setHint(fieldName);
    }

    public String getFieldValue() {
        return this.mEditText.getText().toString();
    }

    public MTSimpleFieldView setFieldIsRequired(boolean isRequired) {
        mFieldIsRequired = isRequired;
        return this;
    }

    public MTSimpleFieldView setFieldType(FieldType fieldType) {
        switch (fieldType) {
            case FIELD_TYPE_EMAIL:
                mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                break;
            case FIELD_TYPE_PASSWORD:
                //mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                mEditText.setTransformationMethod(new PasswordTransformationMethod());
                mEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                break;
            case FIELD_TYPE_STANDARD:
            default:
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
        FIELD_TYPE_STANDARD,
        FIELD_TYPE_PASSWORD,
        FIELD_TYPE_EMAIL
    }

    public MTSimpleFieldView updateFocus() {
        mEditText.requestFocus();
        return this;
    }
}
