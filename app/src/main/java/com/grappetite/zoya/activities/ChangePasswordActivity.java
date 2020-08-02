package com.grappetite.zoya.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grappetite.zoya.customclasses.CommonMethods;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.FieldValidators;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.SessionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePasswordActivity extends CustomActivity {

    @BindView(R.id.et_old_password)
    EditText et_oldPassword;

    @BindView(R.id.et_new_password)
    EditText et_newPassword;

    @BindView(R.id.et_confirm_password)
    EditText et_confirmPassword;

    private PostBoy changePasswordPostBoy;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        changePasswordPostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_CHANGE_PASSWORD).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("CHANGE PASSWORD");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        changePasswordPostBoy.setListener(new ChangePasswordListener());
        changePasswordPostBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }


    private boolean isValid() {
        boolean isValid = true;
        if (!FieldValidators.isValidPassword(et_oldPassword))
            isValid = false;
        if (!FieldValidators.isValidPassword(et_newPassword))
            isValid = false;
        if (!FieldValidators.isValidPassword(et_confirmPassword))
            isValid = false;

        if (isValid && !et_confirmPassword.getText().toString().equals(et_newPassword.getText().toString())) {
            et_confirmPassword.setError("Password does not match");
            isValid = false;
        }
        return isValid;
    }

    @OnClick({R.id.btn_change_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                if (isValid()) {
                    changePasswordPostBoy.setPOSTValues(PostMaps.changePassword(et_newPassword.getText().toString(), et_oldPassword.getText().toString()));
                    changePasswordPostBoy.call();
                }
                break;
        }
    }

    private class ChangePasswordListener implements PostBoyListener {
        private ProgressDialog pd;

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(ChangePasswordActivity.this, "Changing password");
            pd.show();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String encPass = CommonMethods.convertPassMd5(et_newPassword.getText().toString());

                    if(firebaseUser != null){
                        firebaseUser.updatePassword(encPass)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        SharedPreferences sp = getSharedPreferences("UD", MODE_PRIVATE);

                                        if (sp.contains("password")) {
                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users")
                                                    .child(firebaseAuth.getCurrentUser().getUid());

                                            db.child("userPassword").setValue(encPass)
                                                    .addOnCompleteListener(task11 -> {
                                                        if (task11.isSuccessful()) {
                                                            DialogUtils.dismiss(pd);
                                                            sp.edit().putString("password", et_newPassword.getText().toString()).apply();
                                                            onBackPressed();
                                                            Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            DialogUtils.dismiss(pd);
                                                            Toast.makeText(ChangePasswordActivity.this, "Some error occurred: Database", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        } else {
                                            DialogUtils.dismiss(pd);
                                            Toast.makeText(ChangePasswordActivity.this, "Some error occurred: Password", Toast.LENGTH_LONG).show();
                                        }


                                    } else {
                                        DialogUtils.dismiss(pd);
                                        Toast.makeText(ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        Toast.makeText(ChangePasswordActivity.this, "USER IS NULL", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 401:
                    DialogUtils.dismiss(pd);
                    SessionUtils.logout(ChangePasswordActivity.this, true);
                    break;
                case 409:
                    et_oldPassword.setError("Invalid old password");
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
            Toast.makeText(ChangePasswordActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }

}
