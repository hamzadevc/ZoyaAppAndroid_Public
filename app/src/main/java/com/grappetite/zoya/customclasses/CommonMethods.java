package com.grappetite.zoya.customclasses;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.erraticsolutions.framework.utils.UIUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.PostBoy;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonMethods {
    public static String getOptimisedImageUrl(Context context,  String imageUrl) {
        if (TextUtils.isEmpty(imageUrl))
            return null;
        else
            return new ThumbLinkBuilder(imageUrl).setWidth(UIUtils.getScreenWidth(context)).create();
    }


    public static void refreshByCity(TextView tv_city, PostBoy postBoy) {
        String selectedCity = LocalStoragePreferences.getSelectedCity();
        if (!selectedCity.equals(tv_city.getText()))
        {
            tv_city.setText(selectedCity);
            tv_city.setTag(selectedCity);
            postBoy.addGETValue("city",LocalStoragePreferences.getSelectedCity());
            postBoy.call();
        }
    }

    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}
