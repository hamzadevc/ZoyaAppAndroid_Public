package com.grappetite.zoya.helpers;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.grappetite.zoya.R;

public class GoogleLoginHelper {
    private static final int GOOGLE_SIGN_IN = 2994;
    private static final String TAG = "GoogleLoginHelper";
    private Activity activity;
    private GoogleSignInClient googleSignInClient;
    private SocialLoginListener listener;

    public GoogleLoginHelper(Activity activity, SocialLoginListener listener) {
        this.listener = listener;
        this.activity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestProfile()
                .requestEmail()
                .requestIdToken(ContextCustom.getString(activity,R.string.default_web_client_id))
                .build();
        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public void login() {
        activity.startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                if (listener!=null)
                    listener.onSocialTokenReceived(account.getIdToken());
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case GoogleSignInStatusCodes.NETWORK_ERROR:
                        Toast.makeText(activity, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
                        break;
                    case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                        break;
                    default:
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

}
