package com.grappetite.zoya.postboy;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.Map;

public class PostBoyFragment extends Fragment{
    private static final String TAG = "PostBoyFragment";

    @Nullable
    PostBoyListener listener;
    String link;
    RequestType requestType;
    int connectionTimeout, readTimeout;


    @Nullable
    Map<String,File> mapFiles=null;
    @Nullable
    Map<String , String> mapHeaders=null;
    @Nullable
    Map<String , String> mapPost=null;
    @Nullable
    Map<String , String> mapGet=null;
    private boolean connectionInProgress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    boolean call() {
        if (connectionInProgress)
            return false;
        else {
            connectionInProgress = true;
            _connecting();
            run();
            return true;
        }
    }

    private void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResponseData rd = getJsonFromInternet();
                if (rd!=null)
                {
                    if(_connectedAsync(rd.getString(),rd.getCode()))
                        _connected(rd.getString(),rd.getCode());
                }
                else
                    _connectionFailure();
                connectionInProgress = false;
            }
        }).start();
    }

    private ResponseData getJsonFromInternet() {
        if (requestType==null)
            return null;
        else if (requestType == RequestType.GET)
            return getGETData();
        else if (requestType.toString().contains(Common.FORM_DATA))
            return  getFormData();
        else if (requestType.toString().contains(Common.X_WWW_FORM_URLENCODED))
            return getXWwwFormUrlencoded();
        else
            throw new IllegalStateException("Request type not supported");
    }

    private ResponseData getFormData() {
        try {
            HttpURLConnection conn = NetworkUtils.getHttpURLConnection(link+NetworkUtils.convertMapToGETString(mapGet),connectionTimeout,readTimeout,true);
            conn.setRequestMethod(Common.getFirstPartRequestType(requestType));
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetworkUtils.BOUNDARY);
            NetworkUtils.addHeadersToHttpURLConnection(conn,mapHeaders);
            NetworkUtils.addFilesToHttpURLConnection(conn,mapFiles);
            NetworkUtils.addFormDataPostToHttpURLConnection(conn,mapPost);
            return NetworkUtils.getResponseFromHttpURLConnection(conn);
        }catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
            return null;
        }
    }

    private ResponseData getXWwwFormUrlencoded() {
        try {
            String postParams = NetworkUtils.convertMapToPostXXXString(mapPost);
            HttpURLConnection conn = NetworkUtils.getHttpURLConnection(link+NetworkUtils.convertMapToGETString(mapGet),connectionTimeout,readTimeout,true);
            conn.setRequestMethod(Common.getFirstPartRequestType(requestType));
            conn.setFixedLengthStreamingMode(postParams.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            NetworkUtils.addHeadersToHttpURLConnection(conn,mapHeaders);
            NetworkUtils.addPostXWwwFormUrlencodedPostToHttpURLConnection(conn,postParams);
            return NetworkUtils.getResponseFromHttpURLConnection(conn);
        }catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
            return null;
        }
    }
    private ResponseData getGETData() {
        try {
            HttpURLConnection conn = NetworkUtils.getHttpURLConnection(link+NetworkUtils.convertMapToGETString(mapGet),connectionTimeout,readTimeout,false);
            NetworkUtils.addHeadersToHttpURLConnection(conn,mapHeaders);
            return NetworkUtils.getResponseFromHttpURLConnection(conn);
        }catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
            return null;
        }
    }

    private void _connecting()
    {
        if (listener!=null)
        {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        listener.onPostBoyConnecting();
                    } catch (Exception e) {
                        listener.onPostBoyError(new PostBoyException(e));
                    }
                }
            });
        }
    }

    private boolean _connectedAsync(String json, int responseCode)
    {
        if (listener!=null) {
            try {
                listener.onPostBoyAsyncConnected(json,responseCode);
                return true;
            } catch (final Exception e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onPostBoyError(new PostBoyException(e));
                    }
                });
                return false;
            }
        }
        else
            return true;
    }
    private void _connected(final String json, final int responseCode)
    {
        if (listener!=null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        listener.onPostBoyConnected(json,responseCode);
                    } catch (Exception e) {
                        listener.onPostBoyError(new PostBoyException(e));
                    }
                }
            });
        }
    }

    private void _connectionFailure()
    {
        if (listener!=null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        listener.onPostBoyConnectionFailure();
                    } catch (Exception e) {
                        listener.onPostBoyError(new PostBoyException(e));
                    }
                }
            });
        }
    }
}
