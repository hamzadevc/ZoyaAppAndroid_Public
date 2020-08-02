package com.grappetite.zoya.application;

import android.app.Application;
import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.utils.NotificationUtils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class CustomApplication extends Application {

//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser firebaseUser;
//    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        LocalStoragePreferences.initialize(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso build = builder.build();
        build.setLoggingEnabled(true);
        Picasso.setSingletonInstance(build);


//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        if (firebaseUser != null) {
//            String onlineUserID = firebaseAuth.getCurrentUser().getUid();
//            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserID);
//            databaseReference.child("isOnline").setValue("true");
//            databaseReference.keepSynced(true);
//
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    databaseReference.child("isOnline").onDisconnect().setValue("false");
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

//        }


//        ChatHelper.initialize();
        NotificationUtils.createArticleChannel(this);
//        NotificationUtils.createMessagingChannel(this);
        NotificationUtils.createPeriodTrackerChannel(this);
    }
}
