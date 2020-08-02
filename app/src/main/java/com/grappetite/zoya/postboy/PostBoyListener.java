package com.grappetite.zoya.postboy;


import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

public interface PostBoyListener {
    /**
     * It is called when you start connection with server. It is called on UIThread
     * @throws PostBoyException  Handle exceptions instead of crashing application
     */
    @UiThread
    void onPostBoyConnecting() throws PostBoyException;


    /**
     * It is called on Working tread when you get response from server. You can do time cosuming processing in that methods. Like parsing json and saving it to local database etc.
     * @param json Response from server
     * @param responseCode Response code from server
     * @throws PostBoyException Handle exceptions instead of crashing application
     */
    @WorkerThread
    void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException;

    /**
     * It is called on UIThread. It will be called after you are done with processing in  onPostBoyAsyncConnected method.
     * @param json Response from server
     * @param responseCode Response code from server
     * @throws PostBoyException  Handle exceptions instead of crashing application
     */
    @UiThread
    void onPostBoyConnected(String json, int responseCode) throws PostBoyException;

    /**
     * It is called when you connection with server is failed.
     * @throws PostBoyException Handle exceptions instead of crashing application
     */
    @UiThread
    void onPostBoyConnectionFailure() throws PostBoyException;

    /**
     * It is called whenever any of the methods onPostBoyConnecting,  onPostBoyAsyncConnected, onPostBoyConnected and onPostBoyConnectionFailure throws exception.
     * @param e Exception thrown by any of these methods: onPostBoyConnecting,  onPostBoyAsyncConnected, onPostBoyConnected and onPostBoyConnectionFailure.
     */
    @UiThread
    void onPostBoyError(PostBoyException e);
}
