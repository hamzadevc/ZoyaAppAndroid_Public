package com.grappetite.zoya.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ProfileData;
import com.grappetite.zoya.utils.DateUtils;
import com.grappetite.zoya.utils.PeriodUtils;

import java.util.Calendar;
import java.util.Date;

public class PeriodAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ProfileData pd = LocalStoragePreferences.getProfileData();
        if (pd != null && pd.getLastPeriodStartDate() != null) {
            Date today;
            {
                Calendar todayCalendar = Calendar.getInstance();
                today = DateUtils.getDateOnly(todayCalendar.getTime());
            }
            Calendar lastPeriod = Calendar.getInstance();
            lastPeriod.setTime(pd.getLastPeriodStartDate());
            long daysRemaining;

            while (true) {
                daysRemaining = DateUtils.daysBetween(Calendar.getInstance().getTime(),lastPeriod.getTime());
                if (lastPeriod.getTimeInMillis()>=today.getTime() && daysRemaining<=(pd.getPeriodMenstrualCycle()+pd.getPeriodLast()-1))
                    break;
                else
                    lastPeriod.add(Calendar.DAY_OF_MONTH,pd.getPeriodMenstrualCycle()+ pd.getPeriodLast());
            }

            {
                lastPeriod.add(Calendar.DAY_OF_MONTH,-(pd.getPeriodMenstrualCycle()+ pd.getPeriodLast()));
                long dr = DateUtils.daysBetween(Calendar.getInstance().getTime(),lastPeriod.getTime());
                if (Math.abs(dr)<pd.getPeriodLast())
                    daysRemaining=0;
            }
            if (daysRemaining<=2 && daysRemaining>0)
                PeriodUtils.showPeriodReminderNotification(context, (int) daysRemaining);
        }
    }
}
