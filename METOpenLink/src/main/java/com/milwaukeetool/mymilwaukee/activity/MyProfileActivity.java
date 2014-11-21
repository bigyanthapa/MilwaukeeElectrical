package com.milwaukeetool.mymilwaukee.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import com.commonsware.cwac.sacklist.SackOfViewsAdapter;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.response.MTUserProfileResponse;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view.MTMyProfileSectionView;
import com.milwaukeetool.mymilwaukee.view.MTSelectableFieldView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.milwaukeetool.mymilwaukee.view.MTSwitchListItemView;
import com.milwaukeetool.mymilwaukee.view.RitalinLayout;

import java.util.LinkedList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/18/2014.
 */
public class MyProfileActivity extends MTActivity implements Postable {

    private static final String TAG = makeLogTag(MyProfileActivity.class);

    private RitalinLayout mListViewContainerLayout;
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
    private MTSwitchListItemView emailCommunications;

    private LinkedList<View> mViews;

    private boolean mSaveInProgress = false;
    private boolean mEditInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setupViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mEditInProgress) {
            mProgressView.updateMessage(MiscUtils.getString(R.string.progress_bar_getting_user_details));

            Callback<MTUserProfileResponse> responseCallback = new Callback<MTUserProfileResponse>() {
                @Override
                public void success(MTUserProfileResponse result, Response response) {

                    mEditInProgress = true;

                    mProgressView.stopProgress();

                    // Update fields to reflect server data
                    populateView(result);

                    //mListView.setSelectionAfterHeaderView();
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    mEditInProgress = false;

                    LOGD(TAG, "Failed to update my profile");

                    // Hide progress indicator
                    mProgressView.stopProgress();

                    MTUtils.handleRetrofitError(retrofitError, MyProfileActivity.this,
                            MiscUtils.getString(R.string.dialog_title_get_profile_failure));

                }
            };

            mProgressView.startProgress();

            // Get user details from webservice
            MTWebInterface.sharedInstance().getUserService().getProfile(MTUtils.getAuthHeaderForBearerToken(), responseCallback);
        }
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

    @Override
    protected void setupActivityView() {
        setContentView(R.layout.activity_my_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.profileActionSave:

                LOGD(TAG, "Saving MyProfile updates...");

                UIUtils.hideKeyboard(this);
                mProgressView.updateMessage(MiscUtils.getString(R.string.progress_bar_saving_user_details));
                mProgressView.startProgress();

                // TODO:Validate user information, looping through all fields

                // TODO:If validated, write out to server (Replace fake call)

                MiscUtils.runDelayed(2000,new MiscUtils.RunDelayedCallback() {
                    @Override
                    public void onFinished() {
                        mProgressView.stopProgress();

                        // TODO: if saved successfully
                        finish();

                        // TODO: if error show error to user, allow corrections
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setupViews() {

        mListViewContainerLayout = (RitalinLayout)findViewById(R.id.myProfileListViewContainer);

        mListView = (ListView)findViewById(R.id.my_profile_list_view);
//        mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
//        mListView.setItemsCanFocus(true);
//        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//        mListView.setStackFromBottom(true);

        mViews = new LinkedList<View>();

        this.setupUserInformation(mViews);
        this.setupCompanyInformation(mViews);
        this.setupContactInformation(mViews);

        mMyProfileAdapter = new MyProfileAdapter(mViews);

        if (mListView != null) {
            mListView.setAdapter(mMyProfileAdapter);
            mListView.setFocusable(true);
        }

        mListViewContainerLayout.requestFocus();
    }

    protected void populateView(MTUserProfileResponse response) {
        populateUserInformation(response);
        populateContactInformation(response);
        populateCompanyInformation(response);
    }

    private void populateUserInformation(MTUserProfileResponse response) {
        mEmailFieldView.setFieldValue(response != null ? response.getEmail() : null);
        mFirstNameFieldView.setFieldValue(response != null ? response.getFirstName() : null);
        mLastNameFieldView.setFieldValue(response != null ? response.getLastName() : null);
        mTradeOccupationFieldView.setFieldValue(response != null ? response.getOccupation() : null);
    }

    private void populateContactInformation(MTUserProfileResponse response) {
        this.phone.setFieldValue(response != null ? response.getPhone() : null);
        this.cellPhone.setFieldValue(response != null ? response.getCellPhone() : null);
        this.fax.setFieldValue(response != null ? response.getFax() : null);
        if (response.isOptInForCommunication()) {
            this.emailCommunications.setSwitchOn(true);
        }
    }

    private void populateCompanyInformation(MTUserProfileResponse response) {
        this.title.setFieldValue(response != null ? response.getTitle() : null);
        this.companyName.setFieldValue(response != null ? response.getCompanyName() : null);
        this.address1.setFieldValue(response != null ? response.getAddress() : null);
        this.address2.setFieldValue(response != null ? response.getAddress2() : null);
        this.city.setFieldValue(response != null ? response.getCity() : null);
        this.stateProvince.setFieldValue(response != null ? response.getState() : null);
        this.zipcode.setFieldValue(response != null ? response.getZip() : null);
        this.country.setFieldValue(response != null ? response.getCountry() : null);
    }

    protected void setupCompanyInformation(LinkedList<View> views) {
        this.companyInformation = new MTMyProfileSectionView(this);
        this.companyInformation.setHeader(MiscUtils.getString(R.string.company_information));
        views.add(this.companyInformation);

        this.title = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_title)));
        views.add(this.title);

        this.companyName = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_company_name)));
        views.add(this.companyName);

        this.address1 = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_address_1)));
        views.add(this.address1);

        this.address2 = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_address_2)));
        views.add(address2);

        this.city = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_city)));
        views.add(city);

        this.stateProvince = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_state_province)));
        views.add(this.stateProvince);

        this.zipcode = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_zip_code)));
        views.add(this.zipcode);

        this.country = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_country)));
        this.country.setLastGroupItem();
        views.add(this.country);
    }

    protected void setupContactInformation(LinkedList<View> views) {
        this.contactInformation = new MTMyProfileSectionView(this);
        this.contactInformation.setHeader(MiscUtils.getString(R.string.contact_information));
        views.add(contactInformation);

        this.phone = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_phone)));
        this.phone.setFieldType(MTSimpleFieldView.FieldType.PHONE);

        views.add(this.phone);

        this.cellPhone = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_cell_phone)));
        this.cellPhone.setFieldType(MTSimpleFieldView.FieldType.PHONE);
        views.add(this.cellPhone);

        this.fax = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_fax)));
        this.fax.setFieldType(MTSimpleFieldView.FieldType.PHONE);
        views.add(this.fax);

        this.emailCommunications = new MTSwitchListItemView(this);
        this.emailCommunications.setText(MiscUtils.getString(R.string.email_communications));
        this.emailCommunications.setSwitchOn(false);
        this.emailCommunications.setLastGroupItem();
        views.add(this.emailCommunications);
    }

    private MTSimpleFieldView setupDefaultView(MTSimpleFieldView fieldView) {
        fieldView.setTextColorResource(R.color.mt_black);
        fieldView.setHintColorTextResource(R.color.mt_common_gray);
        return fieldView;
    }

    protected void setupUserInformation(LinkedList<View> views) {
        this.userInformation = new MTMyProfileSectionView(this);
        this.userInformation.setHeader(MiscUtils.getString(R.string.user_information));
        this.userInformation.setMargins(0, 0, 0, UIUtils.getPixels(5));
        views.add(userInformation);

        mEmailFieldView = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_email))
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL));
        mEmailFieldView.setRequired(true);

        views.add(mEmailFieldView);

        mFirstNameFieldView = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_first_name)));
        mFirstNameFieldView.setRequired(true);

        views.add(mFirstNameFieldView);

        mLastNameFieldView = setupDefaultView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_last_name)));
        mLastNameFieldView.setRequired(true);
        views.add(mLastNameFieldView);

        String[] selectableOptionArray = this.getResources().getStringArray(R.array.trade_occupation_array);
        mTradeOccupationFieldView = MTSelectableFieldView.createSelectableFieldView(this, MiscUtils.getString(R.string.create_account_field_trade), selectableOptionArray);
        mTradeOccupationFieldView.setTextColorResource(R.color.mt_black);
        mTradeOccupationFieldView.setHintColorTextResource(R.color.mt_common_gray);
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

    public void onEvent(MTimeActionEvent event) {
        if (event.callingActivity == this) {
            if (event.action == EditorInfo.IME_ACTION_NEXT && event.fieldName.equalsIgnoreCase(mLastNameFieldView.getFieldName())) {
                mTradeOccupationFieldView.showSelectableOptions();
            }
        }
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
