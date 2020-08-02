package com.grappetite.zoya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;

public class SplashActivity extends CustomActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, TextUtils.isEmpty(LocalStoragePreferences.getAuthToken()) ? LoginActivity.class : MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }, 1500);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {

    }
}
