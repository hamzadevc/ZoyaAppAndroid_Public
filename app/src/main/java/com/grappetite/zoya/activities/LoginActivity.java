package com.grappetite.zoya.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.utils.UIUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonMethods;
import com.grappetite.zoya.customclasses.FieldValidators;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.dataclasses.User;
import com.grappetite.zoya.helpers.FacebookHelper;
import com.grappetite.zoya.helpers.GoogleLoginHelper;
import com.grappetite.zoya.helpers.SocialLoginListener;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.firebasemaps.FirebaseMaps;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.parsers.SignupParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends CustomActivity {
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    private PostBoy postBoy, fbPostBoy, googlePostBoy;
    private GoogleLoginHelper googleLoginHelper;
    private FacebookHelper facebookHelper;

    FirebaseAuth firebaseAuth;
    DatabaseReference db;
    String encPassword;

    int signInMethod; //Email =1, Facebook =2, GMail=3.
    String socialTokenID;

    String deviceToken;

    final static String IS_ONLINE = "true";
    final static String IS_ANONYMOUS = "false";

    /*
    TODO:: Needs lots of refactoring
     */


    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        postBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_LOGIN_USER).create();
        fbPostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_LOGIN_FACEBOOK).create();
        googlePostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_LOGIN_GOOGLE).create();

        facebookHelper = new FacebookHelper(this, new FacebookTokenListener());
        googleLoginHelper = new GoogleLoginHelper(this, new GoogleTokenListener());

        firebaseAuth = FirebaseAuth.getInstance();
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        db = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        postBoy.setListener(new LoginListener(false));
        fbPostBoy.setListener(new LoginListener(true));
        googlePostBoy.setListener(new LoginListener(true));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookHelper.onActivityResult(requestCode, resultCode, data);
        googleLoginHelper.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValid() {
        boolean isValid = true;
        if (!FieldValidators.isEmailValid(et_email))
            isValid = false;
        if (!FieldValidators.isValidPassword(et_password))
            isValid = false;

        return isValid;
    }


    @OnClick({
            R.id.btn_login,
            R.id.btn_signup,
            R.id.tv_forgot_password,
            R.id.btn_fb,
            R.id.btn_login_google,
            R.id.root
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                if (isValid()) {
                    signInMethod = 1;
                    encPassword = CommonMethods.convertPassMd5(et_password.getText().toString());
                    postBoy.setPOSTValues(PostMaps.loginUser(et_email.getText().toString(), et_password.getText().toString()));
                    postBoy.call();
                }
            }
            break;
            case R.id.btn_signup: {
                Intent i = new Intent(this, SignupActivity.class);
                startActivity(i);
            }
            break;
            case R.id.tv_forgot_password: {
                Intent i = new Intent(this, ForgotPasswordActivity.class);
                startActivity(i);
            }
            break;
            case R.id.btn_fb:
                facebookHelper.login();
                signInMethod = 2;
                break;
            case R.id.btn_login_google: {
                signInMethod = 3;
                googleLoginHelper.login();
            }
            break;
            case R.id.root:
                UIUtils.hideKeyboard(this.findViewById(R.id.root));
                break;
        }
    }

    private class FacebookTokenListener implements SocialLoginListener {
        @Override
        public void onSocialTokenReceived(String socialToken) {
            socialTokenID = socialToken;
            fbPostBoy.setPOSTValues(PostMaps.facebookLogin(socialToken));
            fbPostBoy.call();
        }
    }

    private class GoogleTokenListener implements SocialLoginListener {
        @Override
        public void onSocialTokenReceived(String socialToken) {
            socialTokenID = socialToken;
            googlePostBoy.setPOSTValues(PostMaps.googleLogin(socialToken));
            googlePostBoy.call();
        }
    }

    private class LoginListener implements PostBoyListener {
        private boolean isSocial;

        private LoginListener(boolean isSocial) {
            this.isSocial = isSocial;
        }

        private ProgressDialog pd;

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(LoginActivity.this, "Logging in, It may take upto a minute.");
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
                    switch (signInMethod) {
                        case 1:
                            SignInWithEmail(parser);
                            break;
                        case 2:
                            SignInWithFacebook(socialTokenID, parser);
                            break;
                        case 3:
                            SignInWithGMail(socialTokenID, parser);
                            break;
                    }
                }
                break;
                case 401:
                    if (parser.getResponseMessage().equals("Your account is not activated.")) {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("The user account has been deactivated by the admin.")
                                .setTitle(null)
                                .setPositiveButton("Ok", null)
                                .show();
                    } else if (isSocial) {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    } else {
                        et_email.setError("Invalid email/password");
                        et_password.setError("Invalid email/password");
                    }
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
            Toast.makeText(LoginActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {
            DialogUtils.dismiss(pd);
        }

        void SignUp(String email, String password, Parser data) {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            ProfileData ud = LocalStoragePreferences.getProfileData();

                            User user = new User(ud.getFullName(), ud.getEmail(), encPassword, ud.getProfilePicUrl(), IS_ONLINE, deviceToken, IS_ANONYMOUS);

                            db = db.child("Users/" + firebaseAuth.getCurrentUser().getUid());

                            db.setValue(user).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DialogUtils.dismiss(pd);

                                    SharedPreferences sp = getSharedPreferences("UD", MODE_PRIVATE);
                                    sp.edit().putString("email", et_email.getText().toString()).apply();
                                    sp.edit().putString("password", et_password.getText().toString()).apply();

                                    if (ud != null && ud.getIsNew()) {
                                        Intent[] i = new Intent[2];
                                        i[0] = new Intent(LoginActivity.this, MainActivity.class);
                                        i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i[1] = new Intent(LoginActivity.this, TutorialActivity.class);
                                        startActivities(i);
                                        finish();
                                    } else {
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    DialogUtils.dismiss(pd);

                                    Toast.makeText(getApplicationContext(), task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            DialogUtils.dismiss(pd);

                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }

        void SignInWithEmail(Parser parser) {
            firebaseAuth.signInWithEmailAndPassword(et_email.getText().toString(), encPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                db = db.child("Users/" + firebaseAuth.getCurrentUser().getUid());
                                db.updateChildren(FirebaseMaps.LoginUser(deviceToken))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DialogUtils.dismiss(pd);

                                                    SharedPreferences sp = getSharedPreferences("UD", MODE_PRIVATE);
                                                    sp.edit().putString("email", et_email.getText().toString()).apply();
                                                    sp.edit().putString("password", et_password.getText().toString()).apply();

                                                    ProfileData pd = LocalStoragePreferences.getProfileData();
                                                    if (pd != null && pd.getIsNew()) {
                                                        Intent[] i = new Intent[2];
                                                        i[0] = new Intent(LoginActivity.this, MainActivity.class);
                                                        i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        i[1] = new Intent(LoginActivity.this, TutorialActivity.class);
                                                        startActivities(i);
                                                        finish();
                                                    } else {
                                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                } else {
                                                    DialogUtils.dismiss(pd);

                                                    Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                switch (errorCode) {
                                    case "ERROR_WRONG_PASSWORD":
                                        DialogUtils.dismiss(pd);
                                        Toast.makeText(LoginActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "ERROR_USER_NOT_FOUND":
                                        DialogUtils.dismiss(pd);
                                        SignUp(et_email.getText().toString(), encPassword, parser);
                                        break;
                                    default:
                                        DialogUtils.dismiss(pd);
                                        Toast.makeText(LoginActivity.this, "Unexpected error, Contact us", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }

        void SignInWithGMail(String socialToken, Parser parser) {

            AuthCredential credential = GoogleAuthProvider.getCredential(socialToken, null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                db = db.child("Users/" + firebaseAuth.getCurrentUser().getUid());

                                ProfileData ud = LocalStoragePreferences.getProfileData();

                                User user = new User(ud.getFullName(), ud.getEmail(), "", ud.getProfilePicUrl(), IS_ONLINE, deviceToken, IS_ANONYMOUS);

                                db.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DialogUtils.dismiss(pd);
                                            ProfileData pd = LocalStoragePreferences.getProfileData();
                                            if (pd != null && pd.getIsNew()) {
                                                Intent[] i = new Intent[2];
                                                i[0] = new Intent(LoginActivity.this, MainActivity.class);
                                                i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                i[1] = new Intent(LoginActivity.this, TutorialActivity.class);
                                                startActivities(i);
                                                finish();
                                            } else {
                                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                        } else {
                                            DialogUtils.dismiss(pd);
                                            Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                DialogUtils.dismiss(pd);
                                Toast.makeText(LoginActivity.this, "Failure:Google Sign In", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

        void SignInWithFacebook(String socialToken, Parser parser) {


            AuthCredential credential = FacebookAuthProvider.getCredential(socialToken);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                db = db.child("Users/" + firebaseAuth.getCurrentUser().getUid());

                                ProfileData ud = LocalStoragePreferences.getProfileData();

                                User user = new User(ud.getFullName(), ud.getEmail(), "", ud.getProfilePicUrl(), IS_ONLINE, deviceToken, IS_ANONYMOUS);

                                db.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DialogUtils.dismiss(pd);

                                            ProfileData pd = LocalStoragePreferences.getProfileData();
                                            if (pd != null && pd.getIsNew()) {
                                                Intent[] i = new Intent[2];
                                                i[0] = new Intent(LoginActivity.this, MainActivity.class);
                                                i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                i[1] = new Intent(LoginActivity.this, TutorialActivity.class);
                                                startActivities(i);
                                                finish();
                                            } else {
                                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                            }
                                        } else {
                                            DialogUtils.dismiss(pd);

                                            Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                DialogUtils.dismiss(pd);

                                Toast.makeText(LoginActivity.this, "Failure:Facebook Sign In", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }


    }

    public void DisplayMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

}
