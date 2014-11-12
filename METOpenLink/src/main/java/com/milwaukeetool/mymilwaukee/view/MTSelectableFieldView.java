package com.milwaukeetool.mymilwaukee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.util.MTTouchListener;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Delivery;
import com.r0adkll.postoffice.model.Design;
import com.r0adkll.postoffice.styles.ListStyle;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 11/8/14.
 */
public class MTSelectableFieldView extends MTSimpleFieldView {

    private static final String TAG = makeLogTag(MTSelectableFieldView.class);
    private String[] mSelectableOptionArray;

    public MTSelectableFieldView(Activity activity, String[] selectableOptionArray) {
        super(activity);

        this.mSelectableOptionArray = selectableOptionArray;

        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(true);
        mEditText.setKeyListener(null);

        this.mFieldType = FieldType.SELECTABLE;

        this.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view");
                showSelectableOptions();
            }
        });
        mEditText.setOnTouchListener(new MTTouchListener(mCallingActivity) {
            @Override
            public void didTapView(MotionEvent event) {
                // Launch post office selector
                LOGD(TAG, "Touched selectable field view - edittext");
                showSelectableOptions();
            }
        });
//        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                LOGD(TAG, "Has focus: " + hasFocus);
//                if (hasFocus) {
//                    //mEditText.clearFocus();
//                }
//            }
//        });
//        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                // Do some stuff
//                return false;
//            }
//        });
    }

    public void showSelectableOptions() {

        // Request focus
        updateFocus();

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager)mCallingActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

        // Create the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCallingActivity, R.layout.view_popup_list_item, mSelectableOptionArray);
        
        // Create list popup
        Delivery delivery = PostOffice.newMail(mCallingActivity)
                .setTitle(this.getFieldName())
                .setThemeColorFromResource(R.color.mt_red)
                .setDesign(Design.HOLO_LIGHT)
                .setCanceledOnTouchOutside(true)
                .setCancelable(true)
                .setStyle(new ListStyle.Builder(mCallingActivity)
                        .setOnItemAcceptedListener(new ListStyle.OnItemAcceptedListener<CharSequence>() {
                            @Override
                            public void onItemAccepted(CharSequence item, int position) {
                                Postable postable = (Postable) mCallingActivity;
                                postable.post(item);
                            }
                        })
                        .setDividerHeight(1)
                        .setDivider(new ColorDrawable(mCallingActivity.getResources().getColor(R.color.mt_common_gray)))
                        .build(adapter))
                .build();

        delivery.show(mCallingActivity.getFragmentManager());
    }

    public static MTSelectableFieldView createSelectableFieldView(Activity activity, String fieldName, String[] selectableOptionArray) {
        MTSelectableFieldView selectableFieldView = new MTSelectableFieldView(activity, selectableOptionArray);
        selectableFieldView.setFieldName(fieldName);
        return selectableFieldView;
    }

    public MTSelectableFieldView setRequired(boolean isRequired) {
        super.setRequired(isRequired());
        return this;
    }

    public MTSelectableFieldView setMinLength(int minLength) {
        super.setMinLength(minLength);
        return this;
    }

    public MTSelectableFieldView setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        return this;
    }

    public MTSelectableFieldView setFieldType(FieldType fieldType) {
        super.setFieldType(fieldType);
        return this;
    }

    public MTSelectableFieldView updateFocus() {
        super.updateFocus();
        return this;
    }
}
