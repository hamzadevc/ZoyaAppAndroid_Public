package com.grappetite.zoya.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.MainActivity;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;

import java.util.Calendar;
import java.util.Locale;

public class PeriodUtils {

    public static void setPeriodAlarm(Context context) {
        //noinspection ConstantConditions
        if (context==null || LocalStoragePreferences.getProfileData()==null || LocalStoragePreferences.getProfileData().getLastPeriodStartDate()==null)
            return;

        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager!=null)
        {
            Intent i = new Intent(CommonConstants.ACTION_PERIOD_ALARM);
            PendingIntent pi = PendingIntent.getBroadcast(context,31,i,PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.getDateOnly(calendar.getTime()));
            calendar.set(Calendar.HOUR_OF_DAY,8);
            if (Calendar.getInstance().getTimeInMillis()>calendar.getTimeInMillis())
                calendar.add(Calendar.DAY_OF_MONTH,1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);
        }
    }

    public static void showPeriodReminderNotification(Context context, int daysRemaining) {
        if (LocalStoragePreferences.getAuthToken()==null)
            return;

        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(CommonConstants.EXTRA_SCREEN_TO_OPEN,CommonConstants.VALUE_SCREEN_PERIOD_TRACKER);
        PendingIntent pi = PendingIntent.getActivity(context,30,i,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder n = new NotificationCompat.Builder(context,NotificationUtils.CHANNEL_PERIOD_TRACKER)
                .setContentTitle("Reminder for your Period.")
                .setContentText(String.format(Locale.getDefault(),"Your period is expected to start in %d %s!",daysRemaining,daysRemaining==1?"day":"days"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setColor(ContextCustom.getColor(context,R.color.blue))
                .setSmallIcon(R.drawable.ic_notification)
                .setLights(ContextCustom.getColor(context,R.color.blue),1000,1000)
                .setGroup(ContextCustom.getString(context,R.string.app_name))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.profile_pic_placeholder))
                .setAutoCancel(true)
                .setContentIntent(pi);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(33, n.build());
    }
}
