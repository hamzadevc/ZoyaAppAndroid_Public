package com.grappetite.zoya.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.parsers.SignupParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.DateUtils;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.PeriodUtils;
import com.grappetite.zoya.utils.SessionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PeriodTrackerEditActivity extends CustomActivity implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.ll_date) View ll_date;
    @BindView(R.id.tv_day)  TextView tv_day;
    @BindView(R.id.tv_week_day) TextView tv_weekDay;
    @BindView(R.id.tv_month)    TextView tv_month;

    @BindView(R.id.tv_period_last)  TextView tv_periodLast;
    @BindView(R.id.tv_menstrual_cycle)  TextView tv_menstrualCycle;

    private PostBoy updatePeriodTrackerPostBoy;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_period_tracker_edit;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        updatePeriodTrackerPostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA,WebUrls.BASE_URL+WebUrls.METHOD_UPDATE_USER).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("PERIOD TRACKER");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        updatePeriodTrackerPostBoy.setListener(new UpdatePeriodTrackerListener());
        updatePeriodTrackerPostBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN,LocalStoragePreferences.getAuthToken());
    }

    @Override
    protected void populate() {
        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (pd!=null)
        {
            onDateSet(pd.getLastPeriodStartDate());
            tv_periodLast.setText(String.format(Locale.getDefault(),"%02d",pd.getLastPeriodStartDate()!=null?pd.getPeriodLast():1));
            tv_menstrualCycle.setText(String.format(Locale.getDefault(),"%02d",pd.getLastPeriodStartDate()!=null?pd.getPeriodMenstrualCycle():1));
        }
    }

    private void onDateSet(@Nullable Date date) {
        Calendar c = Calendar.getInstance();
        if (date!=null)
            c.setTime(date);
        onDateSet(null,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Date d = DateUtils.toServerDate("dd-MM-yyyy",dayOfMonth+"-"+(month+1)+"-"+year);
        ll_date.setTag(d);
        String[] date = new SimpleDateFormat("dd|EEEE|MMMM",Locale.getDefault()).format(d).split("\\|");
        tv_day.setText(date[0]);
        tv_weekDay.setText(date[1]);
        tv_month.setText(date[2]);

        ll_date.setTag(R.string.year, year);
        ll_date.setTag(R.string.month, month);
        ll_date.setTag(R.string.day, dayOfMonth);
    }


    @OnClick({R.id.ll_date,R.id.btn_track_it, R.id.tv_plus_period_last, R.id.tv_minus_period_last,R.id.tv_plus_menstrual_cycle,R.id.tv_minus_menstrual_cycle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_date:
            {
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
                updatePeriodTrackerPostBoy.setPOSTValues(PostMaps.updatePeriodTracker((Date)ll_date.getTag(),Integer.parseInt(tv_periodLast.getText().toString()),Integer.parseInt(tv_menstrualCycle.getText().toString())));
                updatePeriodTrackerPostBoy.call();
                break;
            case R.id.tv_plus_period_last:
            {
                int value = Integer.parseInt(tv_periodLast.getText().toString());
                tv_periodLast.setText(String.format(Locale.getDefault(),"%02d",++value));
            }
                break;
            case R.id.tv_minus_period_last:
            {
                int value = Integer.parseInt(tv_periodLast.getText().toString());
                if (value>1)
                    tv_periodLast.setText(String.format(Locale.getDefault(),"%02d",--value));
            }
                break;
            case R.id.tv_plus_menstrual_cycle:
            {
                int value = Integer.parseInt(tv_menstrualCycle.getText().toString());
                tv_menstrualCycle.setText(String.format(Locale.getDefault(),"%02d",++value));
            }
                break;
            case R.id.tv_minus_menstrual_cycle:
                int value = Integer.parseInt(tv_menstrualCycle.getText().toString());
                if (value>1)
                    tv_menstrualCycle.setText(String.format(Locale.getDefault(),"%02d",--value));
                break;
        }
    }

    private class UpdatePeriodTrackerListener implements PostBoyListener {
        private ProgressDialog pd;
        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(PeriodTrackerEditActivity.this,"Updating period tracker ....");
            pd.show();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            SignupParser parser = new SignupParser(json);
            if (parser.getResponseCode()==200) {
                LocalStoragePreferences.setProfileData(parser.getProfileData());
            }
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            DialogUtils.dismiss(pd);
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200:
                {
                    setResult(RESULT_OK);
                    PeriodUtils.setPeriodAlarm(PeriodTrackerEditActivity.this);
                    onBackPressed();
                }
                break;
                case 401:
                    SessionUtils.logout(PeriodTrackerEditActivity.this,true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
            Toast.makeText(PeriodTrackerEditActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {
        }
    }

}
