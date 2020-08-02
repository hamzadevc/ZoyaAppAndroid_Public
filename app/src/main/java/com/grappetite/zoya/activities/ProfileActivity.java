package com.grappetite.zoya.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.utils.SessionUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends CustomActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_profile;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
    }


    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("Zoya Khan");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up,R.anim.right_exit);
    }

    @OnClick({R.id.btn_signout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signout:
            {
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", (dialog, which) -> { SessionUtils.logout(this,false); })
                        .setNegativeButton("No",null)
                        .show();
            }
                break;
        }
    }
}
