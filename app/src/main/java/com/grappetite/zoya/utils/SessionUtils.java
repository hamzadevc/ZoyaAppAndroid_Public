package com.grappetite.zoya.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grappetite.zoya.activities.LoginActivity;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.helpers.ChatHelper;

public class SessionUtils {
    public static void logout(Context context, boolean showToast) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

        db.child("isOnline").setValue("false");
        firebaseAuth.signOut();

        LocalStoragePreferences.clear();
        ChatHelper.terminate();
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        if (showToast)
            Toast.makeText(context, "Please Login Again", Toast.LENGTH_SHORT).show();
    }
}
