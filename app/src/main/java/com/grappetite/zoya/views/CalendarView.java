package com.grappetite.zoya.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarView extends FrameLayout {

    @BindView(R.id.ll_calendar)
    LinearLayout ll_calendar;
    @BindView(R.id.tv_month)
    TextView tv_month;

    private ArrayList<String> periodDates;
    private Calendar calendar;
    private Date periodStartDate;
    private int periodLast,menstrualCycle;

    public CalendarView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_calendar, this, false);
        this.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
        periodDates = new ArrayList<>();
        if (!this.isInEditMode()) {
            calendar = Calendar.getInstance();
            populateDates();
        }
    }


    private void populateDates() {
        clearAll();
        resetDayOfCalendar();
        tv_month.setText(new SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(calendar.getTime()));

        outerLoop:
        for (int week = 0; week < 6; week++) {
            LinearLayout llRow = (LinearLayout) ll_calendar.getChildAt(week);
            for (int weekDay = 0; weekDay < 7; weekDay++) {
                TextView tv = ((TextView) llRow.getChildAt(weekDay));
                boolean doDateIncrement = false;
                switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY:
                        if (weekDay == 0)
                            doDateIncrement = true;
                        break;
                    case Calendar.MONDAY:
                        if (weekDay == 1)
                            doDateIncrement = true;
                        break;
                    case Calendar.TUESDAY:
                        if (weekDay == 2)
                            doDateIncrement = true;
                        break;
                    case Calendar.WEDNESDAY:
                        if (weekDay == 3)
                            doDateIncrement = true;
                        break;
                    case Calendar.THURSDAY:
                        if (weekDay == 4)
                            doDateIncrement = true;
                        break;
                    case Calendar.FRIDAY:
                        if (weekDay == 5)
                            doDateIncrement = true;
                        break;
                    case Calendar.SATURDAY:
                        if (weekDay == 6)
                            doDateIncrement = true;
                        break;
                }
                if (doDateIncrement) {
                    tv.setText(String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.DAY_OF_MONTH)));
                    if (isToday()) {
                        tv.setBackgroundResource(R.drawable.s_round_white_stroke_pink);
                        tv.setTextColor(ContextCustom.getColor(getContext(),R.color.pink));
                    }
                    if (showPinkMarker()) {
                        tv.setBackgroundResource(R.drawable.s_round_pink_stroke_white);
                        tv.setTextColor(Color.WHITE);
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                if (hasMonthChanged()) {
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    break outerLoop;
                }
            }
        }
    }

    @WorkerThread
    @UiThread
    @Nullable
    public Integer getDaysLeft() {
        Integer daysLeft = null;
        Calendar today = Calendar.getInstance();
        for(int i=0 ; i<periodDates.size() ; i++) {
            if (daysLeft==null)
                daysLeft=0;
            else
                daysLeft++;
            if (periodDates.contains(DateUtils.getDate(today.getTime())))
                break;
            today.add(Calendar.DAY_OF_MONTH,1);
        }
        if (daysLeft!=null && daysLeft == 0) {
            for(int i = 0 ; i< periodLast; i++) {
                today.add(Calendar.DAY_OF_MONTH,-1);
                if (periodDates.contains(DateUtils.getDate(today.getTime())))
                    daysLeft--;
                else
                    break;
            }
        }
        return daysLeft;
    }

    private boolean isToday() {
        String today = DateUtils.getDate(Calendar.getInstance().getTime());
        String c = DateUtils.getDate(calendar.getTime());
        return today.equals(c);
    }

    private boolean showPinkMarker() {
        return periodDates.contains(DateUtils.getDate(calendar.getTime()));
    }

    private void clearAll() {
        for (int week = 0; week < 6; week++) {
            LinearLayout llRow = (LinearLayout) ll_calendar.getChildAt(week);
            for (int weekDay = 0; weekDay < 7; weekDay++) {
                TextView tv = ((TextView) llRow.getChildAt(weekDay));
                tv.setText(null);
                tv.setBackgroundColor(Color.TRANSPARENT);
                tv.setTextColor(ContextCustom.getColor(getContext(), R.color.blue));
            }
        }
    }


    private int initialMonth;

    private void resetDayOfCalendar() {
        initialMonth = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    private boolean hasMonthChanged() {
        return initialMonth != calendar.get(Calendar.MONTH);
    }


    public void notifyDataSetChanged() {
        populateDates();
    }

    @WorkerThread
    @UiThread
    public void setSettings(Date startDate, int periodLast, int menstrualCycle) {
        this.periodStartDate = startDate;
        this.periodLast = periodLast;
        this.menstrualCycle = menstrualCycle;
        if (startDate == null)
            return;
        periodDates.clear();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        for (int i = 0; i < 500; i++) {
            for (int lasts = 0; lasts < periodLast; lasts++) {
                periodDates.add(DateUtils.getDate(startCalendar.getTime()));
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            startCalendar.add(Calendar.DAY_OF_MONTH, menstrualCycle < periodLast ? 0 : menstrualCycle-periodLast);
        }
    }


    @OnClick({R.id.iv_next_month, R.id.iv_previous_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_next_month:
                calendar.add(Calendar.MONTH, 1);
                populateDates();
                break;
            case R.id.iv_previous_month:
                calendar.add(Calendar.MONTH, -1);
                populateDates();
                break;
        }
    }
}
