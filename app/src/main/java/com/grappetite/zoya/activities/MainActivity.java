package com.grappetite.zoya.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;

import butterknife.ButterKnife;

public class MainActivity extends CustomActivity {
    FirebaseAuth auth;
    String uID;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        uID = auth.getCurrentUser().getUid();


        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (pd != null) {
            if (pd.isSocialLogin()) {
//                Toast.makeText(this,"Its a social login",Toast.LENGTH_LONG).show();
            } else {
                SharedPreferences sp = getSharedPreferences("UD", MODE_PRIVATE);
                if (!sp.contains("password") && !sp.contains("email")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
