package com.grappetite.zoya.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.utils.CMathUtils;
import com.grappetite.zoya.views.CalendarView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PeriodTrackerActivity extends CustomActivity {

    @BindView(R.id.v_calender)
    CalendarView v_calendar;
    @BindView(R.id.tv_days_remaining)
    TextView tv_dayRemaining;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_period_tracker;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("PERIOD TRACKER");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
    }

    @Nullable
    Integer daysLeft = -1;

    @Override
    protected void asyncPopulate() {
        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (pd != null && pd.getLastPeriodStartDate() != null) {
            v_calendar.setSettings(pd.getLastPeriodStartDate(), pd.getPeriodLast(), pd.getPeriodMenstrualCycle());
            daysLeft = v_calendar.getDaysLeft();
        }

    }

    @Override
    protected void populate() {
        v_calendar.notifyDataSetChanged();
        daysLeft(daysLeft);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29 && resultCode == RESULT_OK) {
            repopulate();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }


    @OnClick({R.id.btn_set_tracker})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_tracker: {
                Intent i = new Intent(this, PeriodTrackerEditActivity.class);
                startActivityForResult(i, 29);
            }
            break;
        }
    }

    private void daysLeft(@Nullable Integer days) {
        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (pd == null || days == null) {
            tv_dayRemaining.setVisibility(View.GONE);
            return;
        }

        if (days > 0)
            tv_dayRemaining.setText(String.format(Locale.getDefault(), "Next Period in %02d %s.", days, days == 1 ? "Day" : "Days"));
        else if (days==0)
            tv_dayRemaining.setText("Your Period is Today.");
        else {
            days = Math.abs(days);
            tv_dayRemaining.setText(String.format(Locale.getDefault(), "Today is %s day of your period", CMathUtils.ordinal(days+1)));
        }

        tv_dayRemaining.setVisibility(View.VISIBLE);
    }
}
