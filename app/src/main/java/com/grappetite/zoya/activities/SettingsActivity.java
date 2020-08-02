package com.grappetite.zoya.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.dataclasses.SettingData;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.utils.SessionUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SettingsActivity extends CustomActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.iv_profile_pic)  ImageView iv_profilePic;
    @BindView(R.id.tv_profile_name) TextView tv_profileName;
    @BindView(R.id.tv_email)        TextView tv_email;
    @BindView(R.id.s_notification)  SwitchCompat s_notification;
    @BindView(R.id.v_divider_change_password)   View v_dividerChangePassword;
    @BindView(R.id.tv_change_password)  View tv_changePassword;
    @BindView(R.id.v_divider_promo)   View v_divider_promo;
    @BindView(R.id.tv_promo)  View tv_promo;

    private ProfileDataChangedReceiver profileDataChangedReceiver;
    private PostBoy postBoy;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        profileDataChangedReceiver = new ProfileDataChangedReceiver();
        this.getLocalBroadcastManager().registerReceiver(profileDataChangedReceiver,new IntentFilter(CommonConstants.ACTION_PROFILE_DATA_CHANGED));
        postBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL+WebUrls.METHOD_UPDATE_USER).create();

    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("سیٹنگ");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));
        s_notification.setOnCheckedChangeListener(this);
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN,LocalStoragePreferences.getAuthToken());
    }

    @Override
    protected void populate() {
        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (pd!=null) {
            Picasso.get()
                    .load(pd.getProfilePicUrl())
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .into(iv_profilePic);
            tv_profileName.setText(pd.getFullName());
            tv_email.setText(pd.getEmail());
            tv_changePassword.setVisibility(pd.isSocialLogin()?View.GONE:View.VISIBLE);
            v_dividerChangePassword.setVisibility(pd.isSocialLogin()?View.GONE:View.VISIBLE);
        }

        v_divider_promo.setVisibility(LocalStoragePreferences.getCheckPromo()?View.GONE:View.VISIBLE);
        tv_promo.setVisibility(LocalStoragePreferences.getCheckPromo()?View.GONE:View.VISIBLE);

        SettingData sd = LocalStoragePreferences.getSettingsData();
        s_notification.setChecked(sd.isNotificationEnabled());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up,R.anim.right_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(profileDataChangedReceiver!=null)
            this.getLocalBroadcastManager().unregisterReceiver(profileDataChangedReceiver);
    }

    @OnClick({
            R.id.tv_signout,
            R.id.tv_change_password,
            R.id.tv_edit_profile,
            R.id.tv_rate_this_app,
            R.id.tv_tutorial,
            R.id.tv_terms_of_use,
            R.id.tv_promo
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_signout:
            {
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", (dialog, which) -> SessionUtils.logout(this,false))
                        .setNegativeButton("No",null)
                        .show();
            }
                break;
            case R.id.tv_edit_profile:
            {
                Intent i = new Intent(this, EditProfileActivity.class);
                this.startActivity(i);
                this.overridePendingTransition(R.anim.right_enter,R.anim.scale_down);
            }
                break;
            case R.id.tv_change_password:
            {
                Intent i = new Intent(this, ChangePasswordActivity.class);
                this.startActivity(i);
                this.overridePendingTransition(R.anim.right_enter,R.anim.scale_down);
            }
                break;
            case R.id.tv_tutorial:
            {
                Intent i = new Intent(this, TutorialActivity.class);
                this.startActivity(i);
            }
                break;
            case R.id.tv_rate_this_app:
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.grappetite.zoya"));
                this.startActivity(i);
            }
                break;
            case R.id.tv_terms_of_use:
            {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://heyzoya.com/terms-conditions/"));
                this.startActivity(i);
            }
                break;
            case R.id.tv_promo:
            {
                Intent i = new Intent(this, PromoCodeActivity.class);
                this.startActivity(i);
            }
            break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton view , boolean isChecked) {
        switch (view.getId()) {
            case R.id.s_notification:
            {
                postBoy.setListener(new UpdateProfileListener(isChecked));
                postBoy.addPOSTValue("flag_push_notification",String.valueOf(isChecked?1:0));
                postBoy.call();
            }
                break;
        }
    }

    private class ProfileDataChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            repopulate();
        }
    }

    private class UpdateProfileListener implements PostBoyListener {
        private boolean enableNotification;

        private UpdateProfileListener(boolean enableNotification) {
            this.enableNotification = enableNotification;
        }

        @Override
        public void onPostBoyConnecting() throws PostBoyException {

        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            Parser parser  = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    SettingData sd = LocalStoragePreferences.getSettingsData();
                    sd.setNotificationEnabled(enableNotification);
                    LocalStoragePreferences.setSettingsData(sd);
                    break;
                case 401:
                    SessionUtils.logout(SettingsActivity.this,true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            s_notification.setOnCheckedChangeListener(null);
            s_notification.setChecked(!enableNotification);
            s_notification.setOnCheckedChangeListener(SettingsActivity.this);
            Toast.makeText(SettingsActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
