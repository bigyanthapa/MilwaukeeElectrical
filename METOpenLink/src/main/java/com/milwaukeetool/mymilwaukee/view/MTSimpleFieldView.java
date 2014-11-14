package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.util.StringHelper;

import de.greenrobot.event.EventBus;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/6/2014.
 */
public class MTSimpleFieldView extends RelativeLayout {

    private static final String TAG = makeLogTag(MTSimpleFieldView.class);

    protected Activity mCallingActivity;

    protected MTEditText mEditText;
    protected FieldType mFieldType;
    protected boolean mRequired;
    protected int mMinLength;
    protected int mMaxLength;
    protected String mFieldName;
    protected boolean mResetField;

    public enum FieldType {
        STANDARD,
        SELECTABLE,
        PASSWORD,
        EMAIL
    }

    public MTSimpleFieldView(Activity activity) {
        super(activity);

        mCallingActivity = activity;

        LayoutInflater.from(activity).inflate(R.layout.view_simple_field, this);
        this.mEditText = (MTEditText) this.findViewById(R.id.editTextField);
        this.mFieldType = FieldType.STANDARD;
        this.mRequired = false;
        this.mMinLength = 0;
        this.mMaxLength = 0;
        this.mResetField = false;

        mEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EventBus.getDefault().post(new MTimeActionEvent(this, EditorInfo.IME_ACTION_DONE, mCallingActivity, mFieldName));
                    return false;
                } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    EventBus.getDefault().post(new MTimeActionEvent(this, EditorInfo.IME_ACTION_NEXT, mCallingActivity, mFieldName));
                    return false;
                } else if (actionId == EditorInfo.IME_ACTION_GO) {
                    EventBus.getDefault().post(new MTimeActionEvent(this, EditorInfo.IME_ACTION_GO, mCallingActivity, mFieldName));
                    return true;
                }
                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (mResetField && mFieldType == FieldType.PASSWORD) {
                    mResetField = false;
                    mEditText.setText("");
                    mEditText.append(s.subSequence(start, start + count).toString());
                }
            }
        });
    }

    public void fieldRequiresReset(boolean requiresReset) {
        mResetField = requiresReset;
    }

    public void setFieldName(String fieldName) {
        mFieldName = fieldName;
        this.mEditText.setHint(fieldName);
    }

    public String getFieldName() {
        return this.mFieldName;
    }

    public String getFieldValue() {
        return this.mEditText.getText().toString();
    }

    public void setFieldValue(String fieldValue) {
        if (!TextUtils.isEmpty(fieldValue)) {
            this.mEditText.setText(fieldValue);
        }
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

    public MTSimpleFieldView setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
        return this;
    }

    public MTSimpleFieldView setFieldType(FieldType fieldType) {
        switch (fieldType) {
            case EMAIL:
                mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                this.mFieldType = fieldType;
                break;
            case PASSWORD:
                mEditText.setTransformationMethod(new PasswordTransformationMethod());
                mEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                this.mFieldType = fieldType;
                break;
            default:
                this.mFieldType = FieldType.STANDARD;
        }

        return this;
    }

    public static MTSimpleFieldView createSimpleFieldView(Activity activity, String fieldName) {
        MTSimpleFieldView simpleFieldView = new MTSimpleFieldView(activity);
        simpleFieldView.setFieldName(fieldName);
        return simpleFieldView;
    }

    public MTSimpleFieldView updateFocus() {
        mEditText.requestFocus();
        return this;
    }

    public void setNextActionDone() {
        mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void setNextActionGo() {
        mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
    }

    public boolean isValid() {

        if (this.isRequired()) {
            if (TextUtils.isEmpty(this.getFieldValue())) {
                showError("The " + this.mFieldName + " field is required");
                return false;
            }
        }

        if (mMaxLength > 0) {
            if (this.getFieldValue().length() > mMaxLength) {
                showError(mFieldName + " exceeds " + mMaxLength + " character limit");
            }
        }


        if (mMinLength > 0) {
            if (this.getFieldValue().length() < mMinLength) {
                showError(mFieldName + " must be at least " + mMinLength + " characters");
                return false;
            }
        }

        switch (this.getFieldType()) {
            case EMAIL:
                if (!StringHelper.isEmailValid(this.getFieldValue())) {
                    showError("The " + mFieldName + " is invalid");
                    return false;
                }
                break;
            case PASSWORD:

                break;
            default:
                // Standard validation?
                break;
        }

        mEditText.setError(null);

        return true;
    }

    public void showError(String errorMessage) {
        final IconDrawable customErrorDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(), Iconify.IconValue.fa_exclamation_circle).colorRes(R.color.mt_white).sizeDp(20);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        mEditText.setError(errorMessage, customErrorDrawable);
        updateFocus();
    }
}
