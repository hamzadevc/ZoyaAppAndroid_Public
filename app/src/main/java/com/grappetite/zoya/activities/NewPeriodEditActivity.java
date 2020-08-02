package com.grappetite.zoya.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.gson.Gson;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.PeriodData;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.SignupParser;
import com.grappetite.zoya.utils.DateUtils;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.SessionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPeriodEditActivity extends CustomActivity implements DatePickerDialog.OnDateSetListener, Callback<String> {

    private static final String TAG = "TAG";


    @BindView(R.id.ll_date)
    View ll_date;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.tv_week_day)
    TextView tv_weekDay;
    @BindView(R.id.tv_month)
    TextView tv_month;

    @BindView(R.id.tv_period_last)
    TextView tv_periodLast;
    @BindView(R.id.tv_menstrual_cycle)
    TextView tv_menstrualCycle;

    List<PeriodData> periodData = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    ProfileData profileData;


    private ZoyaAPI zoyaAPI;
    private ProgressDialog pd;

    private String periodStartDate;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_period_tracker_edit;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        zoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);
        profileData = LocalStoragePreferences.getProfileData();
        pd = DialogUtils.getLoadingDialog(NewPeriodEditActivity.this, "Updating period tracker ....");
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("PERIOD TRACKER");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
    }

    @Override
    protected void populate() {
        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (pd != null) {
            onDateSet(pd.getLastPeriodStartDate());
            tv_periodLast.setText(String.format(Locale.getDefault(), "%02d", pd.getLastPeriodStartDate() != null ? pd.getPeriodLast() : 7));
            tv_menstrualCycle.setText(String.format(Locale.getDefault(), "%02d", pd.getLastPeriodStartDate() != null ? pd.getPeriodMenstrualCycle() : 30));
        }
    }

    private void onDateSet(@Nullable Date date) {
        Calendar c = Calendar.getInstance();
        if (date != null)
            c.setTime(date);
        onDateSet(null, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date d = DateUtils.toServerDate("dd-MM-yyyy", dayOfMonth + "-" + (month + 1) + "-" + year);
        periodStartDate = d.toString();
        ll_date.setTag(d);
        String[] date = new SimpleDateFormat("dd|EEEE|MMMM", Locale.getDefault()).format(d).split("\\|");
        tv_day.setText(date[0]);
        tv_weekDay.setText(date[1]);
        tv_month.setText(date[2]);

        ll_date.setTag(R.string.year, year);
        ll_date.setTag(R.string.month, month);
        ll_date.setTag(R.string.day, dayOfMonth);

        calendar.set(year, month, dayOfMonth);

    }


    @OnClick({R.id.ll_date, R.id.btn_track_it, R.id.tv_plus_period_last, R.id.tv_minus_period_last, R.id.tv_plus_menstrual_cycle, R.id.tv_minus_menstrual_cycle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_date: {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this, this,
                        ll_date.getTag(R.string.year) == null ? calendar.get(Calendar.YEAR) : (int) ll_date.getTag(R.string.year),
                        ll_date.getTag(R.string.month) == null ? calendar.get(Calendar.MONTH) : (int) ll_date.getTag(R.string.month),
                        ll_date.getTag(R.string.day) == null ? calendar.get(Calendar.DAY_OF_MONTH) : (int) ll_date.getTag(R.string.day));
                dpd.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dpd.show();
            }
            break;
            case R.id.btn_track_it:

                for (int i = 0; i < 24; i++) {
                    String month = String.valueOf(calendar.get(Calendar.MONTH));
                    String year = String.valueOf(calendar.get(Calendar.YEAR));
                    String userId = String.valueOf(profileData.getId());
                    PeriodData periodDataObj = new PeriodData(userId, month.concat(year).concat(userId), tv_periodLast.getText().toString(), calendar.getTime().toString());
                    periodData.add(periodDataObj);
                    calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(tv_menstrualCycle.getText().toString()));
                }

                zoyaAPI.setPeriodData(LocalStoragePreferences.getAuthToken(),new Gson().toJson(periodData),periodStartDate,tv_periodLast.getText().toString(),tv_menstrualCycle.getText().toString())
                        .enqueue(this);

                pd.show();

                break;
            case R.id.tv_plus_period_last: {
                int value = Integer.parseInt(tv_periodLast.getText().toString());
                tv_periodLast.setText(String.format(Locale.getDefault(), "%02d", ++value));
            }
            break;
            case R.id.tv_minus_period_last: {
                int value = Integer.parseInt(tv_periodLast.getText().toString());
                if (value > 1)
                    tv_periodLast.setText(String.format(Locale.getDefault(), "%02d", --value));
            }
            break;
            case R.id.tv_plus_menstrual_cycle: {
                int value = Integer.parseInt(tv_menstrualCycle.getText().toString());
                tv_menstrualCycle.setText(String.format(Locale.getDefault(), "%02d", ++value));
            }
            break;
            case R.id.tv_minus_menstrual_cycle:
                int value = Integer.parseInt(tv_menstrualCycle.getText().toString());
                if (value > 1)
                    tv_menstrualCycle.setText(String.format(Locale.getDefault(), "%02d", --value));
                break;
        }
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        DialogUtils.dismiss(pd);

        SignupParser parser = new SignupParser(response.body());
        switch (parser.getResponseCode()) {
            case 200:
                LocalStoragePreferences.setProfileData(parser.getProfileData());

                Intent[] i = new Intent[2];
                i[0] = new Intent(this,MainActivity.class);
                i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i[1] = new Intent(this,NewPeriodTrackerActivity.class);
                this.startActivities(i);
                break;
            case 204:
                Toast.makeText(this, "Nothing found!", Toast.LENGTH_SHORT).show();
                break;
            case 401:
                SessionUtils.logout(NewPeriodEditActivity.this, true);
                break;
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(NewPeriodEditActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
    }
}
