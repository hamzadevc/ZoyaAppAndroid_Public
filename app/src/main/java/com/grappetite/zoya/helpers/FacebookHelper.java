package com.grappetite.zoya.helpers;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.grappetite.zoya.R;

import java.util.Arrays;
import java.util.Set;

public class FacebookHelper implements FacebookCallback<LoginResult> {
    private static final String TAG = "FacebookHelper";
    private static final String PERMISSION_EMAIL = "email";
    private static final String PERMISSION_PUBLIC_PROFILE = "public_profile";
    private final Activity activity;
    private CallbackManager callbackManager;
    private SocialLoginListener listener;

    public FacebookHelper(Activity activity, SocialLoginListener listener) {
        this.listener = listener;
        this.activity = activity;
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    public void login() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(PERMISSION_EMAIL, PERMISSION_PUBLIC_PROFILE));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isValid(Set<String> allowedPermissions) {
        boolean isValid = true;
        if (!allowedPermissions.contains(PERMISSION_EMAIL)) {
            isValid = false;
            Toast.makeText(activity, "Email permission is required. Please login again", Toast.LENGTH_LONG).show();
        }
        return isValid;

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        if (listener!=null)
            listener.onSocialTokenReceived(loginResult.getAccessToken().getToken());
    }

    @Override
    public void onCancel() {
        Log.e(TAG, "onCancel");
    }

    @Override
    public void onError(FacebookException error) {
        if (error != null && error.toString().equals("CONNECTION_FAILURE: CONNECTION_FAILURE"))
            Toast.makeText(activity,"Facebook: Internet fail", Toast.LENGTH_SHORT).show();
        else if (error != null)
            Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
    }
}
