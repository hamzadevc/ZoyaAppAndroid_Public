package com.grappetite.zoya.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.ExpertChatActivity;
import com.grappetite.zoya.activities.ExpertChatThreadActivity;
import com.grappetite.zoya.activities.MainActivity;
import com.grappetite.zoya.activities.NewsFeedActivity;
import com.grappetite.zoya.activities.NewsFeedDetailActivity;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.utils.NotificationUtils;

import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {
    private static final String TAG = "PushNotificationService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (LocalStoragePreferences.getAuthToken() == null || !LocalStoragePreferences.getSettingsData().isNotificationEnabled())
            return;

//        String object = remoteMessage.getData().get("notification");

        switch (remoteMessage.getData().get("notification_type")) {
            case "article":
                generateNotificationForArticle(remoteMessage);
                break;
//            case "message":
//                generateNotificationForMessage(remoteMessage);
            default:
        }
    }

    private void generateNotificationForMessage(RemoteMessage remoteMessage) {

        String nTitle = remoteMessage.getNotification().getTitle();
        String nBody = remoteMessage.getNotification().getBody();
        String nExpID = remoteMessage.getData().get("expID");
        String nExpName = remoteMessage.getData().get("expName");
        String nExpSpecial = remoteMessage.getData().get("expSpecial");

        final String CHANNEL_ID = "CHANNEL_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications";
            String description = "Notifications Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent[] i = new Intent[3];
        {
            i[0] = new Intent(this, MainActivity.class);
            i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            i[1] = new Intent(this, ExpertChatActivity.class);

            i[2] = new Intent(this, ExpertChatThreadActivity.class);
            i[2].putExtra("expID",nExpID);
            i[2].putExtra("expName",nExpName);
            i[2].putExtra("expSpecial",nExpSpecial);
        }

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(nTitle)
                .setContentText(nBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        int notificationId = (int) System.currentTimeMillis();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, mBuilder.build());

    }

    private void generateNotificationForArticle(RemoteMessage remoteMessage) {
        Map<String,String> map = remoteMessage.getData();
//        String tags = map.get("tags");
//        JsonArray ja = new Gson().fromJson(tags,JsonArray.class);
//        map.remove("tags");
//        String notificationJson = new Gson().toJson(map);
//        NewsFeedData data = new Gson().fromJson(notificationJson, NewsFeedData.class);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        for(JsonElement je : ja) {
//            data.addTag(new Gson().fromJson(je, TagData.class));
//        }
        Intent[] i = new Intent[3];
        {
            i[0] = new Intent(this, MainActivity.class);
            i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            i[1] = new Intent(this, NewsFeedActivity.class);

            i[2] = new Intent(this, NewsFeedDetailActivity.class);
            String id = String.valueOf(map.get("id"));

            i[2].putExtra("id",id);
        }
        PendingIntent pi = PendingIntent.getActivities(this, 30, i, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder n = new NotificationCompat.Builder(this, NotificationUtils.CHANNEL_ARTICLE)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setColor(ContextCustom.getColor(this, R.color.blue))
                .setSmallIcon(R.drawable.ic_notification)
                .setLights(ContextCustom.getColor(this, R.color.blue), 1000, 1000)
//                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentIntent(pi);

        notificationManager.notify(34, n.build());
    }

//    private void generateNotificationForMessage(RemoteMessage remoteMessage) {
//        Bitmap expertBitmap=null;
//        String expertImageUrl = remoteMessage.getData().get("expert_image_url");
//        if (!TextUtils.isEmpty(expertImageUrl) && !expertImageUrl.equals("http://zoya.grappetite.com/uploads/experts/placeholder.png"))
//        {
//            try {
//                InputStream is  = new URL(expertImageUrl).openConnection().getInputStream();
//                expertBitmap = BitmapFactory.decodeStream(is);
//                is.close();
//            } catch (IOException e) {
//                Log.e(TAG, "onMessageReceived: "+ e.toString());
//            }
//        }
//        if (expertBitmap==null)
//            expertBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.profile_pic_placeholder_expert);
//        String title = remoteMessage.getData().get("expert_name");
//        String msg = remoteMessage.getData().get("message");
//        ExpertData expertData = new ExpertData(
//                NumberUtils.toInt(remoteMessage.getData().get("expert_id")),
//                title,
//                expertImageUrl,
//                remoteMessage.getData().get("expert_specialization")
//        );
//        generateNotificationForMessage(title,msg,expertBitmap,expertData);//   }

//    private void generateNotificationForMessage(String title, String msg, Bitmap largeIcon, ExpertData expertData) {
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//        Intent i[] = new Intent[3];
//        {
//            i[0] = new Intent(this, MainActivity.class);
//            i[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//            i[1] = new Intent(this, AskExpertActivity.class);
//
//            i[2] = new Intent(this, AskExpertThreadActivity.class);
//            i[2].putExtra(CommonConstants.DATA_EXPERT,expertData);
//        }
//        PendingIntent pi = PendingIntent.getActivities(this, 30, i, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder n = new NotificationCompat.Builder(this, NotificationUtils.CHANNEL_MESSAGING)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setColor(ContextCustom.getColor(this, R.color.blue))
//                .setSmallIcon(R.drawable.ic_notification)
//                .setLights(ContextCustom.getColor(this, R.color.blue), 1000, 1000)
//                .setLargeIcon(largeIcon)
//                .setAutoCancel(true)
//                .setContentIntent(pi);
//
//        notificationManager.notify(33, n.build());
//    }

}
