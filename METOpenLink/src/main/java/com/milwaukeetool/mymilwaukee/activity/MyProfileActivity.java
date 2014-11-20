package com.milwaukeetool.mymilwaukee.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.NetworkUtil;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTMyProfileSectionView;
import com.milwaukeetool.mymilwaukee.view.MTSelectableFieldView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;

import java.util.LinkedList;
import java.util.List;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/18/2014.
 */
public class MyProfileActivity extends MTActivity implements Postable {

    private static final String TAG = makeLogTag(MyProfileActivity.class);

    private ListView mListView;
    private MyProfileAdapter mMyProfileAdapter;

    private MTMyProfileSectionView userInformation;
    private MTSimpleFieldView mEmailFieldView;
    private MTSimpleFieldView mFirstNameFieldView;
    private MTSimpleFieldView mLastNameFieldView;
    private MTSelectableFieldView mTradeOccupationFieldView;

    private MTMyProfileSectionView companyInformation;
    private MTSimpleFieldView title;
    private MTSimpleFieldView companyName;
    private MTSimpleFieldView address1;
    private MTSimpleFieldView address2;
    private MTSimpleFieldView city;
    private MTSimpleFieldView stateProvince;
    private MTSimpleFieldView zipcode;
    private MTSimpleFieldView country;

    private MTMyProfileSectionView contactInformation;
    private MTSimpleFieldView phone;
    private MTSimpleFieldView cellPhone;
    private MTSimpleFieldView fax;

    private LinkedList<View> mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setupViews();
    }

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_my_profile);
    }

    protected void setupViews() {
        mListView = (ListView)findViewById(R.id.my_profile_list_view);
        mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        mListView.setStackFromBottom(true);
        mListView.setSelectionAfterHeaderView();

        mViews = new LinkedList<View>();
        this.setupUserInformation(mViews);
        this.setupCompanyInformation(mViews);
        this.setupContactInformation(mViews);

        mMyProfileAdapter = new MyProfileAdapter(mViews);

        if (mListView != null) {
            mListView.setAdapter(mMyProfileAdapter);
            mListView.setFocusable(true);
        }
    }

    protected void setupCompanyInformation(LinkedList<View> views) {
        this.companyInformation = new MTMyProfileSectionView(this);
        this.companyInformation.setHeader("Company Information");
        views.add(this.companyInformation);

        this.title = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_title));
        this.title.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.title.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(this.title);

        this.companyName = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_company_name));
        this.companyName.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.companyName.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(this.companyName);

        this.address1 = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_address_1));
        this.address1.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.address1.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(this.address1);

        this.address2 = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_address_2));
        this.address2.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.address2.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(address2);

        this.city = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_city));
        this.city.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.city.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(city);

        this.stateProvince = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_state_province));
        this.stateProvince.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.stateProvince.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(this.stateProvince);

        this.zipcode = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_zip_code));
        this.zipcode.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.zipcode.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(this.zipcode);

        this.country = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_country));
        this.country.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.country.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        this.country.setLastGroupItem();
        views.add(this.country);
    }

    protected void setupContactInformation(LinkedList<View> views) {
        this.contactInformation = new MTMyProfileSectionView(this);
        this.contactInformation.setHeader("Contact Information");
        views.add(contactInformation);

        this.phone = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_phone));
        this.phone.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.phone.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        this.phone.setFieldType(MTSimpleFieldView.FieldType.PHONE);
        views.add(this.phone);

        this.cellPhone = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_cell_phone));
        this.cellPhone.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.cellPhone.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        this.cellPhone.setFieldType(MTSimpleFieldView.FieldType.PHONE);
        views.add(this.cellPhone);

        this.fax = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_fax));
        this.fax.setTextColor(this.getResources().getColor(R.color.mt_black));
        this.fax.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        this.fax.setLastGroupItem();
        views.add(this.fax);
    }

    protected void setupUserInformation(LinkedList<View> views) {
        this.userInformation = new MTMyProfileSectionView(this);
        this.userInformation.setHeader("User Information");
        this.userInformation.setMargins(0, 0, 0, UIUtils.getPixels(5));
        views.add(userInformation);

        mEmailFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_email))
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).updateFocus();
        mEmailFieldView.setTextColor(this.getResources().getColor(R.color.mt_black));
        mEmailFieldView.setRequired(true);
        mEmailFieldView.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        views.add(mEmailFieldView);

        mFirstNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_first_name));
        mFirstNameFieldView.setTextColor(this.getResources().getColor(R.color.mt_black));
        mFirstNameFieldView.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        mFirstNameFieldView.setRequired(true);
        views.add(mFirstNameFieldView);

        mLastNameFieldView = MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_last_name));
        mLastNameFieldView.setTextColor(this.getResources().getColor(R.color.mt_black));
        mLastNameFieldView.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        mLastNameFieldView.setRequired(true);
        views.add(mLastNameFieldView);

        String[] selectableOptionArray = this.getResources().getStringArray(R.array.trade_occupation_array);
        mTradeOccupationFieldView = MTSelectableFieldView.createSelectableFieldView(this, MiscUtils.getString(R.string.create_account_field_trade),selectableOptionArray);
        mTradeOccupationFieldView.setTextColor(this.getResources().getColor(R.color.mt_black));
        mTradeOccupationFieldView.setHintColorText(this.getResources().getColor(R.color.mt_common_gray));
        mTradeOccupationFieldView.setNextActionDone();
        mTradeOccupationFieldView.setRequired(true);
        mTradeOccupationFieldView.setLastGroupItem();
        views.add(mTradeOccupationFieldView);


        mMyProfileAdapter = new MyProfileAdapter(views);

        if (mListView != null) {
            mListView.setAdapter(mMyProfileAdapter);
            mListView.setFocusable(true);
        }
    }

    @Override
    public void post(CharSequence option) {
        mTradeOccupationFieldView.setFieldValue(option.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected String getScreenName() {
        return getResources().getString(R.string.mt_screen_name_create_account);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    private class MyProfileAdapter extends SackOfViewsAdapter {

        private EditText mText = null;
        private long mTextLostFocusTimestamp;

        public MyProfileAdapter(List<View> views) {
            super(views);
            mTextLostFocusTimestamp = -1;
        }

        @Override
        protected View newView(int position, ViewGroup parent) {
            View view = super.newView(position, parent);
            return view;
        }

        @Override
        public boolean isEnabled(int position) {

            // Get the view at position
            View view = mViews.get(position);

            if (view != null && view instanceof MTMyProfileSectionView) {
                return false;
            }

            return true;
        }
    }
}
