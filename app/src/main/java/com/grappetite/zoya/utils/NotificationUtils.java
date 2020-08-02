package com.grappetite.zoya.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;

public class NotificationUtils {
    public static final String CHANNEL_ARTICLE = "article";
    public static final String CHANNEL_MESSAGING = "messaging";
    public static final String CHANNEL_PERIOD_TRACKER = "period_tracker";

    public static void createArticleChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ARTICLE,"Article", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setDescription("This category is related to all notifications about new articles");
            channel.setLightColor(ContextCustom.getColor(context, R.color.blue));
            NotificationManager nm = ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE));
            if (nm!=null)
                nm.createNotificationChannel(channel);
        }
    }

    public static void createMessagingChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_MESSAGING,"Messaging", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setDescription("This category is related to all notifications about new messages");
            channel.setLightColor(ContextCustom.getColor(context, R.color.blue));
            NotificationManager nm = ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE));
            if (nm!=null)
                nm.createNotificationChannel(channel);
        }
    }
    public static void createPeriodTrackerChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_PERIOD_TRACKER,"Period Tracker", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setDescription("This category is related to all notifications about period tracker");
            channel.setLightColor(ContextCustom.getColor(context, R.color.blue));
            NotificationManager nm = ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE));
            if (nm!=null)
                nm.createNotificationChannel(channel);
        }
    }
}
