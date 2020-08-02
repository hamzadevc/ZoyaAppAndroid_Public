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
import com.erraticsolutions.framework.utils.UIUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.FieldValidators;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends CustomActivity {

    @BindView(R.id.et_email)
    EditText et_email;
    private PostBoy postBoy;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        postBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_FORGOT_PASSWORD).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        postBoy.setListener(new ForgotPasswordListener());
    }

    private boolean isValid() {
        boolean isValid = true;
        if (!FieldValidators.isEmailValid(et_email))
            isValid = false;
        return isValid;
    }

    @OnClick({R.id.btn_reset_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_password:
                if (isValid()) {
                    postBoy.addPOSTValue("email", et_email.getText().toString());
                    postBoy.call();
                }
                break;
        }
    }

    private class ForgotPasswordListener implements PostBoyListener {
        private ProgressDialog pd;

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(ForgotPasswordActivity.this, "Reseting password ...");
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

                    JsonElement element = new JsonParser().parse(json);

                    String newPass = element.getAsJsonObject().get("newPass").getAsString();
                    SharedPreferences sp = getSharedPreferences("UD", MODE_PRIVATE);
                    String userEmail = sp.getString("email", null);
                    String userPass = sp.getString("password", null);

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    firebaseUser.updatePassword(newPass)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    if (sp.contains("password")) {
                                                        sp.edit().putString("password", newPass).apply();

                                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users")
                                                                .child(firebaseAuth.getCurrentUser().getUid());

                                                        db.child("userPassword").setValue(newPass)
                                                                .addOnCompleteListener(task11 -> {
                                                                    if (task11.isSuccessful()) {
                                                                        DialogUtils.dismiss(pd);
                                                                        UIUtils.hideKeyboard(et_email);
                                                                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                                                                .setTitle("Password Reset Successfully")
                                                                                .setMessage(parser.getResponseMessage())
                                                                                .setPositiveButton("Ok", (dialog, which) -> onBackPressed())
                                                                                .setOnCancelListener(dialog -> onBackPressed())
                                                                                .show();
                                                                    } else {
                                                                        DialogUtils.dismiss(pd);
                                                                        Toast.makeText(ForgotPasswordActivity.this, "Some error occurred: Database", Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                    } else {
                                                        DialogUtils.dismiss(pd);
                                                        Toast.makeText(ForgotPasswordActivity.this, "Some error occurred: Password", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    DialogUtils.dismiss(pd);
                                                    Toast.makeText(ForgotPasswordActivity.this, "Some error occurred: Change", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                } else {
                                    DialogUtils.dismiss(pd);
                                    Toast.makeText(ForgotPasswordActivity.this, "Some error occurred: Auth", Toast.LENGTH_LONG).show();
                                }
                            });

                    break;
                case 400:
                    DialogUtils.dismiss(pd);
                    et_email.setError(parser.getResponseMessage());
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
            Toast.makeText(ForgotPasswordActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
