package com.milwaukeetool.mymilwaukee.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;
import com.milwaukeetool.mymilwaukee.R;
import com.milwaukeetool.mymilwaukee.interfaces.MTFinishedListener;
import com.milwaukeetool.mymilwaukee.interfaces.MTLaunchListener;
import com.milwaukeetool.mymilwaukee.interfaces.Postable;
import com.milwaukeetool.mymilwaukee.model.MTUserProfile;
import com.milwaukeetool.mymilwaukee.model.event.MTLaunchEvent;
import com.milwaukeetool.mymilwaukee.model.event.MTimeActionEvent;
import com.milwaukeetool.mymilwaukee.model.request.MTPasswordRequest;
import com.milwaukeetool.mymilwaukee.services.MTWebInterface;
import com.milwaukeetool.mymilwaukee.util.MTUtils;
import com.milwaukeetool.mymilwaukee.util.MiscUtils;
import com.milwaukeetool.mymilwaukee.util.UIUtils;
import com.milwaukeetool.mymilwaukee.view_custom.MTChangePasswordPopupView;
import com.milwaukeetool.mymilwaukee.view.MTLaunchableFieldView;
import com.milwaukeetool.mymilwaukee.view.MTListItemHeaderView;
import com.milwaukeetool.mymilwaukee.view.MTSelectableFieldView;
import com.milwaukeetool.mymilwaukee.view.MTSimpleFieldView;
import com.milwaukeetool.mymilwaukee.view.MTSwitchListItemView;
import com.milwaukeetool.mymilwaukee.view.MTToastView;

import java.util.LinkedList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by scott.hopfensperger on 11/18/2014.
 */
public class MyProfileActivity extends MTActivity implements Postable, MTLaunchListener {

    private LinearLayout mMyProfileLayout;

    private static final String TAG = makeLogTag(MyProfileActivity.class);

    private MTListItemHeaderView userInformation;
    private MTSimpleFieldView mEmailFieldView;
    private MTSimpleFieldView mFirstNameFieldView;
    private MTSimpleFieldView mLastNameFieldView;
    private MTSelectableFieldView mTradeOccupationFieldView;
    private MTLaunchableFieldView mPassword;

    private MTListItemHeaderView companyInformation;
    private MTSimpleFieldView title;
    private MTSimpleFieldView companyName;
    private MTSimpleFieldView address1;
    private MTSimpleFieldView address2;
    private MTSimpleFieldView city;
    private MTSimpleFieldView stateProvince;
    private MTSimpleFieldView zipcode;
    private MTSimpleFieldView country;

    private MTListItemHeaderView contactInformation;
    private MTSimpleFieldView phone;
    private MTSimpleFieldView cellPhone;
    private MTSimpleFieldView fax;
    private MTSwitchListItemView emailCommunications;

    private AlertDialog mChangePasswordDialog;
    private MTChangePasswordPopupView mChangePasswordPopupView;

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

            Callback<MTUserProfile> responseCallback = new Callback<MTUserProfile>() {
                @Override
                public void success(MTUserProfile result, Response response) {

                    mEditInProgress = true;

                    mProgressView.stopProgress();

                    // Update fields to reflect server data
                    populateView(result);
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
        return getResources().getString(R.string.mt_screen_name_my_profile);
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
    public void onBackPressed() {
        if (mSaveInProgress) {
            return;
        }
        super.onBackPressed();
    }

    private boolean isTextFieldsValid() {
        boolean valid = true;

        for (View view : this.mViews) {
            if (view instanceof MTSimpleFieldView && !((MTSimpleFieldView) view).isValid()) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mProgressView.isDisplayed()) {
            return super.onOptionsItemSelected(item);
        }

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!mSaveInProgress) {
                    finish();
                }
                return true;
            case R.id.profileActionSave:

                if (mEditInProgress) {

                    LOGD(TAG, "Saving MyProfile updates...");

                    // Always hide the keyboard
                    UIUtils.hideKeyboard(this);

                    Callback<MTUserProfile> responseCallback = new Callback<MTUserProfile>() {
                        @Override
                        public void success(MTUserProfile result, Response response) {
                            mSaveInProgress = false;
                            mProgressView.stopProgress();

                            final IconDrawable successDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                                    Iconify.IconValue.fa_check_circle).colorRes(R.color.mt_black).sizeDp(40);

                            MTToastView.showMessage(MyProfileActivity.this,
                                    MiscUtils.getString(R.string.message_success_updated_profile),
                                    MTToastView.MT_TOAST_SHORT,
                                    successDrawable,
                                    new MTFinishedListener() {
                                @Override
                                public void didFinish() {
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            LOGD(TAG, "Failed to update my profile");

                            mSaveInProgress = false;

                            // Hide progress indicator
                            mProgressView.stopProgress();
                            MTUtils.handleRetrofitError(retrofitError, MyProfileActivity.this,
                                    MiscUtils.getString(R.string.dialog_title_update_profile_failure));
                        }
                    };

                    if (this.isTextFieldsValid()) {

                        mSaveInProgress = true;

                        mProgressView.updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_saving_user_details));

                        MTUserProfile userProfile = this.constructMTUserProfile();

                        MTWebInterface.sharedInstance().getUserService().updateProfile(MTUtils.getAuthHeaderForBearerToken(),
                                userProfile,
                                responseCallback);
                    }
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews() {

        mViews = new LinkedList<View>();

        this.setupUserInformation(mViews);
        this.setupCompanyInformation(mViews);
        this.setupContactInformation(mViews);

        mMyProfileLayout = (LinearLayout)findViewById(R.id.myProfileLayout);

        for(View view : mViews) {
            if (view instanceof MTListItemHeaderView) {
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getPixels(50)));
            } else if (view == mPassword || view == country || view == emailCommunications) {
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getPixels(55)));
            } else if (view instanceof MTSimpleFieldView || view instanceof MTSelectableFieldView || view instanceof MTLaunchableFieldView || view instanceof MTSwitchListItemView) {
                view.setLayoutParams(getStandardLayoutParams());
            }
            mMyProfileLayout.addView(view);
        }
    }

    private LinearLayout.LayoutParams getStandardLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.getPixels(45));
    }

    private void populateView(MTUserProfile response) {
        populateUserInformation(response);
        populateContactInformation(response);
        populateCompanyInformation(response);
    }

    private void populateUserInformation(MTUserProfile response) {
        mEmailFieldView.setFieldValue(response != null ? response.getEmail() : null);
        mFirstNameFieldView.setFieldValue(response != null ? response.getFirstName() : null);
        mLastNameFieldView.setFieldValue(response != null ? response.getLastName() : null);
        mTradeOccupationFieldView.setFieldValue(response != null ? response.getOccupation() : null);
    }

    private void populateContactInformation(MTUserProfile response) {
        this.phone.setFieldValue(response != null ? response.getPhone() : null);
        this.cellPhone.setFieldValue(response != null ? response.getCellPhone() : null);
        this.fax.setFieldValue(response != null ? response.getFax() : null);
        this.emailCommunications.setSwitchOn(response.isOptInForCommunication());
    }

    private void populateCompanyInformation(MTUserProfile response) {
        this.title.setFieldValue(response != null ? response.getTitle() : null);
        this.companyName.setFieldValue(response != null ? response.getCompanyName() : null);
        this.address1.setFieldValue(response != null ? response.getAddress() : null);
        this.address2.setFieldValue(response != null ? response.getAddress2() : null);
        this.city.setFieldValue(response != null ? response.getCity() : null);
        this.stateProvince.setFieldValue(response != null ? response.getState() : null);
        this.zipcode.setFieldValue(response != null ? response.getZip() : null);
        this.country.setFieldValue(response != null ? response.getCountry() : null);
    }

    private MTUserProfile constructMTUserProfile() {
        MTUserProfile userProfile = new MTUserProfile();
        userProfile.setAddress(this.address1.getFieldValue());
        userProfile.setAddress2(this.address2.getFieldValue());
        userProfile.setCellPhone(this.cellPhone.getFieldValue());
        userProfile.setCity(this.city.getFieldValue());
        userProfile.setCompanyName(this.companyName.getFieldValue());
        userProfile.setCountry(this.country.getFieldValue());
        userProfile.setEmail(this.mEmailFieldView.getFieldValue());
        userProfile.setFax(this.fax.getFieldValue());
        userProfile.setFirstName(this.mFirstNameFieldView.getFieldValue());
        userProfile.setLastName(this.mLastNameFieldView.getFieldValue());
        userProfile.setOccupation(this.mTradeOccupationFieldView.getFieldValue());
        userProfile.setPhone(this.phone.getFieldValue());
        userProfile.setOptInForCommunication(this.emailCommunications.isSwitchOn());
        userProfile.setState(this.stateProvince.getFieldValue());
        userProfile.setTitle(this.title.getFieldValue());
        userProfile.setZip(this.zipcode.getFieldValue());

        return userProfile;
    }

    private void setupDefaultView(MTSimpleFieldView fieldView) {
        fieldView.setTextColorResource(R.color.mt_black);
        fieldView.setHintColorTextResource(R.color.mt_common_gray);
    }

    private MTSimpleFieldView addDefaultFieldView(final MTSimpleFieldView fieldView) {
        setupDefaultView(fieldView);
        mViews.add(fieldView);
        return fieldView;
    }

    protected void setupCompanyInformation(LinkedList<View> views) {
        this.companyInformation = new MTListItemHeaderView(this);
        this.companyInformation.setHeader(MiscUtils.getString(R.string.company_information));
        views.add(this.companyInformation);

        this.title = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_title)));

        this.companyName = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_company_name)));

        this.address1 = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_address_1)));

        this.address2 = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_address_2)));

        this.city = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_city)));

        this.stateProvince = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_state_province)));

        this.zipcode = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_zip_code)));
        this.zipcode.setFieldType(MTSimpleFieldView.FieldType.ZIP_POSTAL_CODE);

        this.country = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_country)));
        this.country.setLastGroupItem();
    }

    protected void setupContactInformation(LinkedList<View> views) {
        this.contactInformation = new MTListItemHeaderView(this);
        this.contactInformation.setHeader(MiscUtils.getString(R.string.contact_information));
        views.add(contactInformation);

        this.phone = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_phone)));
        this.phone.setFieldType(MTSimpleFieldView.FieldType.PHONE);

        this.cellPhone = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_cell_phone)));
        this.cellPhone.setFieldType(MTSimpleFieldView.FieldType.PHONE);

        this.fax = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.update_profile_fax)));
        this.fax.setFieldType(MTSimpleFieldView.FieldType.PHONE);

        this.emailCommunications = new MTSwitchListItemView(this);
        this.emailCommunications.setText(MiscUtils.getString(R.string.email_communications));
        this.emailCommunications.setSwitchOn(false);
        this.emailCommunications.setLastGroupItem();
        views.add(this.emailCommunications);
    }

    protected void setupUserInformation(LinkedList<View> views) {
        this.userInformation = new MTListItemHeaderView(this);
        this.userInformation.setHeader(MiscUtils.getString(R.string.user_information));
        this.userInformation.setMargins(0, 0, 0, UIUtils.getPixels(5));
        views.add(userInformation);

        mEmailFieldView = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_email))
                .setFieldType(MTSimpleFieldView.FieldType.EMAIL).setRequired(true));

        mFirstNameFieldView = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_first_name)).setRequired(true));

        mLastNameFieldView = addDefaultFieldView(MTSimpleFieldView.createSimpleFieldView(this, MiscUtils.getString(R.string.create_account_field_last_name)).setRequired(true));

        String[] selectableOptionArray = this.getResources().getStringArray(R.array.trade_occupation_array);
        mTradeOccupationFieldView = MTSelectableFieldView.createSelectableFieldView(this, MiscUtils.getString(R.string.create_account_field_trade), selectableOptionArray);
        mTradeOccupationFieldView.setTextColorResource(R.color.mt_black);
        mTradeOccupationFieldView.setHintColorTextResource(R.color.mt_common_gray);
        mTradeOccupationFieldView.setNextActionDone();
        mTradeOccupationFieldView.setRequired(true);
        views.add(mTradeOccupationFieldView);

        this.mPassword = MTLaunchableFieldView.createLaunchableFieldView(this, MiscUtils.getString(R.string.update_profile_change_password));
        this.mPassword.setTextColorResource(R.color.mt_black);
        this.mPassword.setHintColorTextResource(R.color.mt_black);
        this.mPassword.setLastGroupItem();
        views.add(this.mPassword);
    }

    @Override
    public void post(CharSequence option) {
        mTradeOccupationFieldView.setFieldValue(option.toString());
    }

    public void onEvent(MTimeActionEvent event) {
        if (event.callingActivity == this) {
            if (event.action == EditorInfo.IME_ACTION_NEXT && event.fieldName.equalsIgnoreCase(mLastNameFieldView.getFieldName())) {
                mTradeOccupationFieldView.showSelectableOptions();
            } else if (event.action == EditorInfo.IME_ACTION_GO) {
                changePassword();
            }
        }
    }

    public void launched(MTLaunchEvent launchEvent) {

        if (launchEvent.getSource() == mPassword) {

        }

        LayoutInflater inflater = this.getLayoutInflater();
        mChangePasswordPopupView = new MTChangePasswordPopupView(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(MiscUtils.getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UIUtils.hideKeyboard(MyProfileActivity.this, mChangePasswordPopupView.getCurrent());
                UIUtils.hideKeyboard(MyProfileActivity.this, mChangePasswordPopupView.getUpdate());
                UIUtils.hideKeyboard(MyProfileActivity.this, mChangePasswordPopupView.getConfirm());
            }
        });
        builder.setPositiveButton(MiscUtils.getString(R.string.action_save),null);
        builder.setView(mChangePasswordPopupView);

        mChangePasswordDialog = builder.create();

        if (mChangePasswordDialog != null) {

            // Change the soft input mode to make sure the keyboard is visible for the popup
            mChangePasswordDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            mChangePasswordDialog.show();

            Button changeButton = mChangePasswordDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (changeButton != null) {
                changeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        MyProfileActivity.this.changePassword();
                    }
                });
            }
        }
    }


    public void changePassword() {

        // Check to see if we have a dialog
        if (mChangePasswordPopupView == null || mChangePasswordDialog == null)
            return;

        // Hide the keyboard for any of the edit text items
        UIUtils.hideKeyboard(MyProfileActivity.this, mChangePasswordPopupView.getCurrent());
        UIUtils.hideKeyboard(MyProfileActivity.this, mChangePasswordPopupView.getUpdate());
        UIUtils.hideKeyboard(MyProfileActivity.this, mChangePasswordPopupView.getConfirm());

        MTSimpleFieldView current = mChangePasswordPopupView.getCurrent();
        MTSimpleFieldView update = mChangePasswordPopupView.getUpdate();
        MTSimpleFieldView confirm = mChangePasswordPopupView.getConfirm();

        if (current.isValid() && update.isValid() && confirm.isValid()) {
            if (update.getFieldValue().equals(confirm.getFieldValue())) {

                Callback<Response> responseCallback = new Callback<Response>() {

                    @Override
                    public void success(Response result, Response response) {

                        // Hide progress indicator
                        mProgressView.stopProgress();

                        mChangePasswordDialog.dismiss();
                        mChangePasswordDialog = null;

                        final IconDrawable successDrawable = new IconDrawable(MilwaukeeToolApplication.getAppContext(),
                                Iconify.IconValue.fa_check_circle).colorRes(R.color.mt_black).sizeDp(40);

                        // Show success
                        MTToastView.showMessage(MyProfileActivity.this,
                                MiscUtils.getString(R.string.change_password_success),
                                MTToastView.MT_TOAST_SHORT,
                                successDrawable,
                                new MTFinishedListener() {
                                    @Override
                                    public void didFinish() {

                                    }
                                });
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                        LOGD(TAG, "Failed to change password");

                        // Hide progress indicator
                        mProgressView.stopProgress();

                        MTUtils.handleRetrofitError(retrofitError, MyProfileActivity.this,
                                MiscUtils.getString(R.string.change_password_error));
                    }
                };

                MTPasswordRequest request = new MTPasswordRequest();
                request.setCurrent(current.getFieldValue());
                request.setUpdated(update.getFieldValue());
                request.setConfirm(confirm.getFieldValue());

                mProgressView.updateMessageAndStart(MiscUtils.getString(R.string.progress_bar_registering));

                MTWebInterface.sharedInstance().getUserService().updatePassword(MTUtils.getAuthHeaderForBearerToken(),
                        request,responseCallback);
            } else {
                // Show error
                MTUtils.showDialogMessage(MyProfileActivity.this,
                        MiscUtils.getString(R.string.change_password_error),
                        MiscUtils.getString(R.string.create_account_no_password_match));
            }
        }
    }
}
