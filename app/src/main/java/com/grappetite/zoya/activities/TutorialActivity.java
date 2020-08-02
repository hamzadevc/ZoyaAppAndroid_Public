package com.grappetite.zoya.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.TutorialPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends CustomActivity {

    @BindView(R.id.vp)  ViewPager vp;
    private TutorialPagerAdapter adb;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tutorial;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        adb = new TutorialPagerAdapter(this.getSupportFragmentManager());
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        //this.showBackButton(true);
        vp.setOffscreenPageLimit(1);
        vp.setAdapter(adb);
    }

    @OnClick({R.id.iv_next,R.id.iv_previous,R.id.tv_skip})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_next:
                if (vp.getCurrentItem()+1<adb.getCount())
                    vp.setCurrentItem(vp.getCurrentItem()+1);
                else
                    onBackPressed();
                break;
            case R.id.iv_previous:
                if (vp.getCurrentItem()>0)
                    vp.setCurrentItem(vp.getCurrentItem()-1);
                break;
            case R.id.tv_skip:
                onBackPressed();
                break;
        }
    }
}
