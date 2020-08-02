package com.grappetite.zoya.services;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;


public class TokenService extends FirebaseInstanceIdService {
    private static final String TAG = "TokenService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String authToken = LocalStoragePreferences.getAuthToken();

        if (authToken!=null) {
            PostBoy postBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL+WebUrls.METHOD_UPDATE_USER).create();
            postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN,authToken);
            postBoy.setPOSTValues(PostMaps.updateUser(refreshedToken));
            postBoy.setListener(new UpdateProfileListener());
            postBoy.call();
        }
    }

    private class UpdateProfileListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {

        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            Log.e(TAG, "onPostBoyConnected: " + json );
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {

        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
