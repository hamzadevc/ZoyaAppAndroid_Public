package com.grappetite.zoya.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;

import butterknife.ButterKnife;

public class FinderActivity extends CustomActivity {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_finder;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("تلاش کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }
}
