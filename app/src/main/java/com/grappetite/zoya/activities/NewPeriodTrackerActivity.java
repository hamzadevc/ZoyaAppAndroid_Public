package com.grappetite.zoya.activities;

import android.app.ProgressDialog;

import androidx.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.gson.Gson;
import com.grappetite.zoya.R;
import com.applandeo.materialcalendarview.CalendarView;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.PeriodData;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.PeriodDataParser;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.SessionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPeriodTrackerActivity extends CustomActivity implements OnSelectDateListener, Callback<String> {

    private static final String TAG = "LOG";
    private static final int OVULATION = 13; // Ovulation starts from the 14th day of cycle

    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.tv_days_remaining)
    TextView tv_days_remaining;



    //This list will contains the data that we get from server
    private List<PeriodData> periodData = new ArrayList<>();
    private ZoyaAPI zoyaAPI;

    private ProgressDialog pd;

    //Current profile data of the user
    private ProfileData profileData;

    //Current month calender object
    private Calendar curCalander;

    private int indexWeNeed;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_new_period_tracker;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        calendarView.setHeaderColor(R.color.white_a50);
        calendarView.setHeaderLabelColor(R.color.colorPrimary);

        pd = DialogUtils.getLoadingDialog(NewPeriodTrackerActivity.this, "Getting periods data ....");
        profileData = LocalStoragePreferences.getProfileData();

        zoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);
        zoyaAPI.getPeriodData(LocalStoragePreferences.getAuthToken()).enqueue(this);

        pd.show();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("PERIOD TRACKER");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));

    }

    @OnClick({R.id.showCalender})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showCalender: {

                //This list will have the current period dates that can be edited when clicking on edit date.
                List<Calendar> selectedDays = new ArrayList<>();
                Date startDate;
                int periodLength;
                /* TODO:: NOT SO GOOD CHUNK OF CODE, CAUSES REDUDENCY AND SHOULD BE IMPROVED IN FUTURE ---- */

                startDate = new Date(periodData.get(getIndexWeNeed()).getStartDate());
                periodLength = Integer.parseInt(periodData.get(getIndexWeNeed()).getPeriodSize());


                // Populating selected days array.
                for (int i = 0; i < periodLength; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    calendar.add(Calendar.DAY_OF_MONTH, i);
                    selectedDays.add(calendar);
                }

                showDatePicker(selectedDays);
            }

            break;
        }
    }

    List<EventDay> getEvent(List<PeriodData> periodData) {

        List<EventDay> events = new ArrayList<>();

        for (int i = 0; i < periodData.size(); i++) {

            //Period markings

            for (int j = 0; j < Integer.parseInt(periodData.get(i).getPeriodSize()); j++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(periodData.get(i).getStartDate()));
                cal.add(Calendar.DAY_OF_MONTH, j);
                events.add(new EventDay(cal, R.drawable.blood));
            }

            //ovulation markings
            for (int j = 0; j < 5; j++) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(periodData.get(i).getStartDate()));
                cal.add(Calendar.DAY_OF_MONTH, OVULATION + j);
                events.add(new EventDay(cal, R.drawable.ovulation));
            }
        }

        return events;
    }

    @Override
    public void onSelect(List<Calendar> calendars) {

        Calendar oldCal = Calendar.getInstance();
        Calendar newCal = Calendar.getInstance();

        //setting old and new calender to get lateByDays value.
        oldCal.setTime(new Date(periodData.get(getIndexWeNeed()).getStartDate()));
        newCal.setTime(calendars.get(0).getTime());

        int lateByDays = newCal.get(Calendar.DAY_OF_MONTH) - oldCal.get(Calendar.DAY_OF_MONTH);

        //This list contains the new period_data after we edit the period dates.
        List<PeriodData> periodDataUpdate = new ArrayList<>();

        Calendar updateCalender = Calendar.getInstance();
        updateCalender.setTime(calendars.get(0).getTime());

        for (int i = getIndexWeNeed(); i < periodData.size(); i++) {

            String uMonth = String.valueOf(updateCalender.get(Calendar.MONTH));
            String uYear = String.valueOf(updateCalender.get(Calendar.YEAR));
            String uUniqueId = uMonth.concat(uYear).concat(String.valueOf(profileData.getId()));
            PeriodData periodObject;

            if (i == indexWeNeed) {

                //This is the object that is edited hence we have to add its new period length and also late by days value

                periodObject = new PeriodData(String.valueOf(profileData.getId()), uUniqueId, String.valueOf(calendars.size()), updateCalender.getTime().toString());
                periodObject.setLateByDays(String.valueOf(lateByDays));
            } else {
                periodObject = new PeriodData(String.valueOf(profileData.getId()), uUniqueId, String.valueOf(profileData.getPeriodLast()), updateCalender.getTime().toString());
                periodObject.setLateByDays(periodData.get(i).getLateByDays());
            }
            periodObject.setPeriod_data_id(periodData.get(i).getPeriod_data_id());
            periodDataUpdate.add(periodObject);
            updateCalender.add(Calendar.DAY_OF_MONTH, profileData.getPeriodMenstrualCycle());
        }

        zoyaAPI.updatePeriodData(LocalStoragePreferences.getAuthToken(),new Gson().toJson(periodDataUpdate)).enqueue(this);
        pd.show();

    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        DialogUtils.dismiss(pd);

        PeriodDataParser parser = new PeriodDataParser(response.body());

        switch (parser.getResponseCode()) {
            case 200:
                periodData.clear();
                periodData.addAll(parser.getPeriodData());
                calendarView.setEvents(getEvent(periodData));
                setIndexWeNeed();
                setDaysRemaining(getIndexWeNeed());

                break;
            case 204:
                // show nothing dialog.
                break;
            case 401:
                SessionUtils.logout(NewPeriodTrackerActivity.this, true);
                break;
            case 7777:
                Toast.makeText(this,"Cannot update period data",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(this,"Network request failed", Toast.LENGTH_SHORT).show();
    }

    public void showDatePicker(List<Calendar> selectedDays) {
        DatePickerBuilder rangeBuilder = new DatePickerBuilder(this, this)
                .pickerType(CalendarView.RANGE_PICKER)
                .headerColor(R.color.white_a50)
                .headerLabelColor(R.color.colorPrimary)
                .selectionColor(R.color.pink)
                .selectionLabelColor(android.R.color.white)
                .todayLabelColor(R.color.pink)
                .dialogButtonsColor(R.color.pink)
                .daysLabelsColor(R.color.colorPrimary)
                .anotherMonthsDaysLabelsColor(R.color.gray_bg_card)
                .selectedDays(selectedDays)
                .previousButtonSrc(R.drawable.v_carrot_left_blue_24)
                .forwardButtonSrc(R.drawable.v_carrot_right_blue_24);

        com.applandeo.materialcalendarview.DatePicker rangePicker = rangeBuilder.build();
        rangePicker.show();
    }


    public void setDaysRemaining(int indexWeNeed){

        if(indexWeNeed != -1){
            Calendar periodStartCal = Calendar.getInstance();
            periodStartCal.setTime(new Date(periodData.get(indexWeNeed).getStartDate()));

            int periodStartDay = periodStartCal.get(Calendar.DAY_OF_MONTH);

            curCalander = Calendar.getInstance();
            int curDay = curCalander.get(Calendar.DAY_OF_MONTH);


            int daysRemaining = periodStartDay - curDay;

            if(daysRemaining > 0){
                tv_days_remaining.setText(String.format(Locale.getDefault(), "Next Period in %02d %s.", daysRemaining,daysRemaining == 1 ? "day" : "days"));
            }else if(daysRemaining ==0){
                tv_days_remaining.setText("Today is your period day");
            }else if(daysRemaining > -Integer.parseInt(periodData.get(indexWeNeed).getPeriodSize())){
                tv_days_remaining.setText(String.format(Locale.getDefault(), "Today is day %s  of your period", -daysRemaining));
            }else {
                Calendar periodNextStartCal = Calendar.getInstance();
                periodNextStartCal.setTime(new Date(periodData.get(indexWeNeed+1).getStartDate()));
                curCalander = Calendar.getInstance();
                long compare =  TimeUnit.MILLISECONDS.toDays(Math.abs(curCalander.getTimeInMillis() - periodNextStartCal.getTimeInMillis())) + 1;
                tv_days_remaining.setText(String.format(Locale.getDefault(), "Next Period in %02d %s.", compare,compare == 1 ? "day" : "days"));

            }
        }
    }

    public void setIndexWeNeed(){
        int indexWeNeed = -1;

        curCalander = Calendar.getInstance();
        String month = String.valueOf(curCalander.get(Calendar.MONTH));
        String year = String.valueOf(curCalander.get(Calendar.YEAR));
        String id = String.valueOf(profileData.getId());
        String uniqueId = month.concat(year).concat(id);

        /* ---- TODO::  NOT SO GOOD CHUNK OF CODE, CAUSES REDUDENCY AND SHOULD BE IMPROVED IN FUTURE ---- */

        boolean isFirst = true;

        for (int i = 0; i < periodData.size(); i++) {
            if (periodData.get(i).getUniqueId().equals(uniqueId)) {
                if (isFirst) {
                    indexWeNeed = i;
                    isFirst = false;
                }
            }
        }
        this.indexWeNeed = indexWeNeed;

        Log.d(TAG,String.valueOf(indexWeNeed));
    }


    public int getIndexWeNeed(){
        return indexWeNeed;
    }
}
