package com.grappetite.zoya.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.erraticsolutions.framework.utils.UIUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonMethods;
import com.grappetite.zoya.customclasses.FieldValidators;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.dataclasses.User;
import com.grappetite.zoya.helpers.ImageSelectorHelper;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.parsers.SignupParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.ActivityUtils;
import com.grappetite.zoya.utils.DateUtils;
import com.grappetite.zoya.utils.DialogUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SignupActivity extends CustomActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, ImageSelectorHelper.Listener {

    private static final String TAG = "SignupActivity";
    private static final String IS_ONLINE = "true";
    private static final String IS_ANONYMOUS = "false";


    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    String encPassword;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confirm_password)
    EditText et_confirmPassword;

//    @BindView(R.id.et_age)
//    EditText et_age;

    @BindView(R.id.tv_female)
    TextView tv_female;
    @BindView(R.id.tv_male)
    TextView tv_male;
    @BindView(R.id.tv_dob)
    TextView tv_dob;
    @BindView(R.id.tv_single)
    TextView tv_single;
    @BindView(R.id.tv_married)
    TextView tv_married;
    //@BindView(R.id.cb_terms_and_conditions)
    //CheckBox cb_termsAndConditions;
    @BindView(R.id.iv_profile_pic)
    ImageView iv_profilePic;

    private PostBoy postBoy;
    private ImageSelectorHelper imageSelectorHelper;
    private String deviceToken;

    // TODO:: NEEDS REFACOTRING

    @Override
    protected int getContentViewId() {
        return R.layout.activity_signup;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        imageSelectorHelper = new ImageSelectorHelper(this);
        postBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_CREATE_USER).create();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        deviceToken = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle(null);
        postBoy.setListener(new SignupListener());
        imageSelectorHelper.setListener(this);
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




    private boolean isValid() {
        boolean isValid = true;
        if (!FieldValidators.isNameValid(et_name))
            isValid = false;
        if (!FieldValidators.isEmailValid(et_email))
            isValid = false;
        if (!FieldValidators.isValidPassword(et_password))
            isValid = false;
        if (!FieldValidators.isValidPassword(et_confirmPassword))
            isValid = false;
        if (isValid && !et_confirmPassword.getText().toString().equals(et_password.getText().toString())) {
            et_confirmPassword.setError("Password does not match");
            isValid = false;
        }

//        if (et_age.getText().toString().isEmpty()) {
//            isValid = false;
//            Toast.makeText(this, "Please enter your age", Toast.LENGTH_SHORT).show();
//        }

        if (tv_male.getTag() == null && tv_female.getTag() == null) {
            isValid = false;
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
        }

        if (tv_single.getTag() == null && tv_married.getTag() == null) {
            isValid = false;
            Toast.makeText(this, "Select marital status", Toast.LENGTH_SHORT).show();
        }

//        if (isValid && !cb_termsAndConditions.isChecked()) {
//            Toast.makeText(this, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show();
//            isValid = false;
//        }

        return isValid;
    }

    @OnClick({
            R.id.btn_signup,
            R.id.tv_male,
            R.id.tv_female,
            R.id.tv_single,
            R.id.tv_married,
            R.id.tv_dob,
            R.id.root,
            R.id.iv_profile_pic,
            R.id.tv_terms_and_conditions
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup: {
                if (isValid()) {

                    encPassword = CommonMethods.convertPassMd5(et_password.getText().toString());

                    postBoy.setPOSTValues(PostMaps.createUser(
                            et_name.getText().toString(),
                            et_email.getText().toString(),
                            et_password.getText().toString(),
                            (boolean) tv_male.getTag(),
                            (String) tv_dob.getTag(),
                            (Boolean) tv_single.getTag()));
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
            case R.id.root:
                UIUtils.hideKeyboard(tv_dob);
                break;
                //
            case R.id.tv_terms_and_conditions: {
                            ActivityUtils.openTermsAndConditions(this);
                        break;
                    }

            //
        }
    }


    private void selectGender(boolean isMaleSelected) {
        tv_male.setBackgroundResource(isMaleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_male.setCompoundDrawablesWithIntrinsicBounds(ContextCustom.getDrawable(this, isMaleSelected ? R.drawable.ic_male_active : R.drawable.ic_male_inactive), null, null, null);
        tv_male.setTextColor(ContextCustom.getColor(this, isMaleSelected ? android.R.color.white : R.color.blue));
        tv_male.setTag(isMaleSelected);
        tv_female.setBackgroundResource(!isMaleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_female.setCompoundDrawablesWithIntrinsicBounds(!isMaleSelected ? R.drawable.ic_female_active : R.drawable.ic_female_inactive, 0, 0, 0);
        tv_female.setTextColor(ContextCustom.getColor(this, !isMaleSelected ? android.R.color.white : R.color.blue));
        tv_male.setTag(!isMaleSelected);
    }

    private void selectMaritalStatus(boolean isSingleSelected) {
        tv_single.setBackgroundResource(isSingleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_single.setTextColor(ContextCustom.getColor(this, isSingleSelected ? android.R.color.white : R.color.blue));
        tv_single.setTag(isSingleSelected);
        tv_married.setBackgroundResource(!isSingleSelected ? R.drawable.s_round_blue : R.drawable.s_round_stroke_blue_transparent_thin);
        tv_married.setTextColor(ContextCustom.getColor(this, !isSingleSelected ? android.R.color.white : R.color.blue));
        tv_married.setTag(!isSingleSelected);
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
    }

    @Override
    public void onImageRemoved() {
        iv_profilePic.setImageResource(R.drawable.camera_signup);
        iv_profilePic.setTag(null);
        postBoy.removeFile(PostMaps.KEY_IMAGE);
    }


    private class SignupListener implements PostBoyListener {
        private ProgressDialog pd;

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(SignupActivity.this, "Signing up....");
            pd.show();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            SignupParser parser = new SignupParser(json);
            if (parser.getResponseCode() == 200) {
                LocalStoragePreferences.setProfileData(parser.getProfileData());
                LocalStoragePreferences.setSettingsData(parser.getSettingData());
                LocalStoragePreferences.setAuthToken(parser.getAuthToken());
            }
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200: {
//                    Toast.makeText(getApplicationContext(),"User created!",Toast.LENGTH_LONG).show();

                    firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(), encPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        db = db.child("Users/" + firebaseAuth.getCurrentUser().getUid());


                                        SharedPreferences sp = getSharedPreferences("UD", MODE_PRIVATE);
                                        sp.edit().putString("email", et_email.getText().toString()).apply();
                                        sp.edit().putString("password", encPassword).apply();

                                        ProfileData ud = LocalStoragePreferences.getProfileData();

                                        User user = new User(ud.getFullName(), ud.getEmail(), encPassword, ud.getProfilePicUrl(), IS_ONLINE, deviceToken, IS_ANONYMOUS);

                                        db = db.child("Users/" + firebaseAuth.getCurrentUser().getUid());

                                        db.setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent[] i = new Intent[2];
                                                            i[0] = new Intent(SignupActivity.this, MainActivity.class);
                                                            i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            i[1] = new Intent(SignupActivity.this, TutorialActivity.class);
                                                            startActivities(i);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                }
                            });
                }
                break;
                case 409:
                    DialogUtils.dismiss(pd);
                    et_email.setError(parser.getResponseMessage());
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
            Toast.makeText(SignupActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {
            DialogUtils.dismiss(pd);
            Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
