package com.grappetite.zoya.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.FieldValidators;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.enums.Gender;
import com.grappetite.zoya.enums.MaritalStatus;
import com.grappetite.zoya.helpers.ImageSelectorHelper;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.parsers.SignupParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.DateUtils;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.SessionUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EditProfileActivity extends CustomActivity implements ImageSelectorHelper.Listener, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tv_female)
    TextView tv_female;
    @BindView(R.id.tv_male)
    TextView tv_male;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_dob)
    TextView tv_dob;
    @BindView(R.id.tv_single)
    TextView tv_single;
    @BindView(R.id.tv_married)
    TextView tv_married;
    @BindView(R.id.iv_profile_pic)
    ImageView iv_profilePic;

    private PostBoy postBoy;
    private ImageSelectorHelper imageSelectorHelper;
    private ProfileData profileData;
    private boolean removeProfilePic;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        profileData = LocalStoragePreferences.getProfileData();
        imageSelectorHelper = new ImageSelectorHelper(this);
        postBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_UPDATE_USER).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("EDIT PROFILE");
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.setListener(new UpdateProfileListener());
        imageSelectorHelper.setListener(this);
    }

    @Override
    protected void populate() {
        Picasso.get()
                .load(profileData.getProfilePicUrl())
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.profile_pic_placeholder)
                .into(iv_profilePic);
        et_name.setText(profileData.getFullName());
        tv_email.setText(profileData.getEmail());
        tv_age.setText(profileData.getAge());
        selectMaritalStatus(profileData.getMaritalStatus()!=null?profileData.getMaritalStatus() == MaritalStatus.SINGLE:null);
        selectGender(profileData.getGender()!=null?profileData.getGender() == Gender.MALE:null);
        onDateSet(profileData.getDateOfBirth());
        iv_profilePic.setTag(profileData.getProfilePicUrl());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageSelectorHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imageSelectorHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }

    private boolean isValid() {
        boolean isValid = true;
        if (!FieldValidators.isNameValid(et_name))
            isValid = false;
        if (!FieldValidators.isEmailValid(tv_email))
            isValid = false;

        if (tv_male.getTag() == null && tv_female.getTag() == null) {
            isValid = false;
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
        }

        if (tv_single.getTag() == null && tv_married.getTag() == null) {
            isValid = false;
            Toast.makeText(this, "Select marital status", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    private void selectGender(@Nullable Boolean isMaleSelected) {
        tv_male.setBackgroundResource(isMaleSelected!=null && isMaleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_male.setCompoundDrawablesWithIntrinsicBounds(ContextCustom.getDrawable(this, isMaleSelected!=null && isMaleSelected ? R.drawable.ic_male_active : R.drawable.ic_male_inactive), null, null, null);
        tv_male.setTextColor(ContextCustom.getColor(this, isMaleSelected!=null && isMaleSelected ? android.R.color.white : R.color.blue));
        tv_male.setTag(isMaleSelected);
        tv_female.setBackgroundResource(isMaleSelected!=null && !isMaleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_female.setCompoundDrawablesWithIntrinsicBounds(isMaleSelected!=null && !isMaleSelected ? R.drawable.ic_female_active : R.drawable.ic_female_inactive, 0, 0, 0);
        tv_female.setTextColor(ContextCustom.getColor(this, isMaleSelected!=null && !isMaleSelected ? android.R.color.white : R.color.blue));
        tv_female.setTag(isMaleSelected!=null?!isMaleSelected:null);
    }

    private void selectMaritalStatus(@Nullable Boolean isSingleSelected) {
        tv_single.setBackgroundResource(isSingleSelected!=null && isSingleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_single.setTextColor(ContextCustom.getColor(this, isSingleSelected!=null && isSingleSelected ? android.R.color.white : R.color.blue));
        tv_single.setTag(isSingleSelected);
        tv_married.setBackgroundResource(isSingleSelected!=null && !isSingleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_married.setTextColor(ContextCustom.getColor(this,isSingleSelected!=null &&  !isSingleSelected ? android.R.color.white : R.color.blue));
        tv_married.setTag(isSingleSelected!=null?!isSingleSelected:null);
    }


    @OnClick({
            R.id.btn_update,
            R.id.tv_male,
            R.id.tv_female,
            R.id.tv_single,
            R.id.tv_married,
            R.id.tv_dob,
            R.id.iv_profile_pic
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update: {
                if (isValid()) {
                    postBoy.setPOSTValues(PostMaps.updateUser(
                            et_name.getText().toString(),
                            tv_email.getText().toString(),
                            (boolean) tv_male.getTag(),
                            (String) tv_dob.getTag(),
                            (Boolean) tv_single.getTag(),
                            removeProfilePic
                    ));
                    postBoy.call();
                }
            }
            break;
            case R.id.tv_male:
                selectGender(true);
                break;
            case R.id.tv_female:
                selectGender(false);
                break;
            case R.id.tv_single:
                selectMaritalStatus(true);
                break;
            case R.id.tv_married:
                selectMaritalStatus(false);
                break;
            case R.id.tv_dob: {
                DatePickerDialog dpd = new DatePickerDialog(this, this,
                        tv_dob.getTag(R.string.year) == null ? 1990 : (int) tv_dob.getTag(R.string.year),
                        tv_dob.getTag(R.string.month) == null ? 0 : (int) tv_dob.getTag(R.string.month),
                        tv_dob.getTag(R.string.day) == null ? 1 : (int) tv_dob.getTag(R.string.day));
                dpd.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dpd.show();
            }
            break;
            case R.id.iv_profile_pic:
                imageSelectorHelper.getImage(iv_profilePic.getTag() != null);
                break;
        }
    }

    private void onDateSet(@Nullable Date date) {
        if (date == null)
            return;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        onDateSet(null, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        tv_dob.setError(null);
        tv_dob.setText(String.format(Locale.getDefault(), "%02d / %02d / %d", dayOfMonth, month + 1, year));
        tv_dob.setTag(DateUtils.toServerFormat(dayOfMonth, month + 1, year));
        tv_dob.setTag(R.string.year, year);
        tv_dob.setTag(R.string.month, month);
        tv_dob.setTag(R.string.day, dayOfMonth);
    }

    @Override
    public void onImageSelected(File file) {
        Picasso.get()
                .load(file)
                .transform(new CropCircleTransformation())
                .into(iv_profilePic);
        iv_profilePic.setTag(file);
        postBoy.addFile(PostMaps.KEY_IMAGE, file);
        removeProfilePic = false;
    }

    @Override
    public void onImageRemoved() {
        iv_profilePic.setImageResource(R.drawable.profile_pic_placeholder);
        iv_profilePic.setTag(null);
        postBoy.removeFile(PostMaps.KEY_IMAGE);
        removeProfilePic = true;
    }

    private class UpdateProfileListener implements PostBoyListener {
        private ProgressDialog pd;

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(EditProfileActivity.this, "Updating profile....");
            pd.show();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            SignupParser parser = new SignupParser(json);
            if (parser.getResponseCode() == 200) {
                LocalStoragePreferences.setProfileData(parser.getProfileData());
                LocalStoragePreferences.setAuthToken(parser.getAuthToken());
            }
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            DialogUtils.dismiss(pd);
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200: {
                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    getLocalBroadcastManager().sendBroadcast(new Intent(CommonConstants.ACTION_PROFILE_DATA_CHANGED));
                    onBackPressed();
                }
                break;
                case 409:
                    tv_email.setError(parser.getResponseMessage());
                    break;
                case 401:
                    SessionUtils.logout(EditProfileActivity.this, true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
            Toast.makeText(EditProfileActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {
            DialogUtils.dismiss(pd);
            Toast.makeText(EditProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
