package com.grappetite.zoya.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
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
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.SessionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends CustomActivity {

    @BindView(R.id.et_subject)
    EditText et_subject;

    @BindView(R.id.et_email)
    EditText et_email;

    private PostBoy contactUsPostBoy;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        contactUsPostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_CONTACT_US).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("رابطہ کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        contactUsPostBoy.setListener(new ContactUsListener());
        contactUsPostBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }

    private boolean isValid() {
        boolean isValid = true;
        if (!FieldValidators.isSubjectValid(et_subject))
            isValid = false;
        if (!FieldValidators.isContactUsMailValid(et_email))
            isValid = false;
        return isValid;
    }

    @OnClick({R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (isValid()) {
                    contactUsPostBoy.setPOSTValues(PostMaps.contactUs(et_subject.getText().toString(), et_email.getText().toString()));
                    contactUsPostBoy.call();
                }
                break;
        }
    }

    private class ContactUsListener implements PostBoyListener {
        private ProgressDialog pd;

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(ContactActivity.this, "Sending email...");
            pd.show();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            DialogUtils.dismiss(pd);
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    new AlertDialog.Builder(ContactActivity.this)
                            .setTitle("Thank you!")
                            .setMessage("Thank you for contacting us. We will reach you soon.")
                            .setPositiveButton("Ok", (dialog, which) -> onBackPressed())
                            .setOnCancelListener(dialog -> onBackPressed())
                            .show();
                    break;
                case 401:
                    SessionUtils.logout(ContactActivity.this,true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
            Toast.makeText(ContactActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
