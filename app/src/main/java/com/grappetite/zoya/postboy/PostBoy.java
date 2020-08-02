package com.grappetite.zoya.postboy;


import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PostBoy {

    private final RequestType requestType;
    private PostBoyFragment postBoyFragment;

    private PostBoy(@Nullable Context context, String link, boolean keepPersistent, RequestType requestType, String tag, int connectionTimeout, int readTimeout) {
        this.requestType = requestType;
        if (context!=null && context instanceof AppCompatActivity && keepPersistent)
        {
            postBoyFragment = (PostBoyFragment) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(tag);
            if (postBoyFragment ==null)
            {
                postBoyFragment = getNewPostFragment(link, requestType,connectionTimeout,readTimeout);
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().add(postBoyFragment,tag).commit();
            }
        }
        else if (context!=null && context instanceof Activity && keepPersistent)
            throw new IllegalStateException("PostBoy feature does not support Activity for keep persistent. Please use AppCompactActivity or turn off keep persistence feature from PostBoyConfig or PostBoyBuilder object");
        else
        {
            postBoyFragment = getNewPostFragment(link, requestType, connectionTimeout, readTimeout);
        }
    }

    /**
     * Use this method to call webservice.
     * @return  It will return false if  postboy's current object is already trying to connect to server to get response. It will return true if  webservice has started calling webservice.
     */
    public boolean call() {
        return postBoyFragment.call();
    }

    private PostBoyFragment getNewPostFragment(String link, RequestType requestType, int connectionTimeout, int readTimeout) {
        postBoyFragment = new PostBoyFragment();
        postBoyFragment.link = link;
        postBoyFragment.requestType = requestType;
        postBoyFragment.connectionTimeout = connectionTimeout;
        postBoyFragment.readTimeout = readTimeout;
        return postBoyFragment;
    }



    //*************************GETTER AND SETTER METHODS  START***************************/


    /**
     * @param listener Set listener to get callbacks from postboy.
     */
    public void setListener(@Nullable PostBoyListener listener) {
        postBoyFragment.listener = listener;
    }

    /**
     * @return  returns all keys and values that are set for sending to server using GET.
     */
    @Nullable
    public Map<String, String> getGETValues() {
        return postBoyFragment.mapGet;
    }

    /**
     * @param mapGet set all GET keys and values that will be sent to server using get method;
     */
    public void setGETValues(@Nullable Map<String, String> mapGet) {
        postBoyFragment.mapGet = mapGet;
    }

    /**
     * Add key along with its value to sent to server via GET method
     * @param key set key to add in GET method
     * @param value set value to add in GET method
     */
    public void addGETValue(@NonNull String key,@NonNull String value) {
        if (postBoyFragment.mapGet==null)
            postBoyFragment.mapGet = new HashMap<>();
        postBoyFragment.mapGet.put(key,value);
    }


    /**
     * remove GET value
     * @param key whose value and key you want to remove
     */
    public void removeGETValue(@NonNull String key)
    {
        if (postBoyFragment.mapGet!=null)
            postBoyFragment.mapGet.remove(key);
    }

    /**
     * Clear all GET values
     */
    public void removeAllGETValues() {
        setGETValues(null);
    }

    /**
     * @return  returns all keys and values that are set for sending to server using POST.
     */
    @Nullable
    public Map<String, String> getPOSTValues() {
        return postBoyFragment.mapPost;
    }

    /**
     * @param mapPost set all post keys and values that will be sent to server using post method;
     */
    public void setPOSTValues(@Nullable Map<String, String> mapPost) {
        if (requestType.equals(RequestType.GET))
            throw new IllegalArgumentException("You can't set post values when RequestType is RequestType.TYPE_GET");
        postBoyFragment.mapPost = mapPost;
    }


    /**
     * Add key along with its value to sent to server via POST method
     * @param key set key to add in POST method
     * @param value set value to add in POST method
     */
    public void addPOSTValue(@NonNull String key,@NonNull String value) {
        if (requestType.equals(RequestType.GET))
            throw new IllegalArgumentException("You can't add post values when RequestType is RequestType.GET");
        if (postBoyFragment.mapPost ==null)
            postBoyFragment.mapPost = new HashMap<>();
        postBoyFragment.mapPost.put(key,value);
    }


    /**
     * remove POST value
     * @param key whose value and key you want to remove
     */
    public void removePOSTValue(@NonNull String key)
    {
        if (postBoyFragment.mapPost!=null)
            postBoyFragment.mapPost.remove(key);
    }

    /**
     * Clear all POST values
     */
    public void removeAllPOSTValues()
    {
        setPOSTValues(null);
    }


    /**
     * @return  returns all keys and values that are set for sending to server using Header.
     */
    @Nullable
    public Map<String, String> getHeader() {
        return postBoyFragment.mapHeaders;
    }

    /**
     * @param mapHeaders set all header keys and values that will be sent to server in Header;
     */
    public void setHeader(@Nullable  Map<String, String> mapHeaders) {
        postBoyFragment.mapHeaders = mapHeaders;
    }

    /**
     * Add key along with its value to sent to server via Header
     * @param key set key to add in Header
     * @param value set value to add in Header
     */
    public void addHeader(@NonNull  String key,@NonNull  String value) {
        if(postBoyFragment.mapHeaders==null)
            postBoyFragment.mapHeaders = new HashMap<>();
        postBoyFragment.mapHeaders.put(key,value);
    }

    /**
     * remove Header value
     * @param key whose value and key you want to remove
     */
    public void removeHeader(@NonNull String key)
    {
        if (postBoyFragment.mapHeaders!=null)
            postBoyFragment.mapHeaders.remove(key);
    }

    /**
     * Clear all Header values
     */
    public void removeAllHeaders()
    {
        setHeader(null);
    }


    /**
     * @return  returns all keys and file objects that are set for sending to server using multipart.
     */
    @Nullable
    public Map<String, File> getFiles() {
        return postBoyFragment.mapFiles;
    }

    /**
     * @param mapFiles set all files keys and file objects that will be sent to server using post method;
     */
    public void setFiles(@Nullable  Map<String, File> mapFiles) {
        if (requestType.equals(RequestType.GET))
            throw new IllegalArgumentException("You can't upload file when RequestType is RequestType.TYPE_GET");
        if (requestType.toString().contains(Common.X_WWW_FORM_URLENCODED))
            throw new IllegalArgumentException("You can't upload file when RequestType is of X_WWW_FORM_URLENCODED");
        postBoyFragment.mapFiles = mapFiles;
    }

    /**
     * Add key along with its file object that you want to send via server
     * @param key set key of file that you want to upload to server
     * @param file set file object that you want to send to server
     */
    public void addFile(@NonNull  String key,@NonNull  File file) {
        if (requestType.equals(RequestType.GET))
            throw new IllegalArgumentException("You can't upload file when RequestType is RequestType.TYPE_GET");
        if (requestType.toString().contains(Common.X_WWW_FORM_URLENCODED))
            throw new IllegalArgumentException("You can't upload file when RequestType is of X_WWW_FORM_URLENCODED");

        if (postBoyFragment.mapFiles==null)
            postBoyFragment.mapFiles = new HashMap<>();
        postBoyFragment.mapFiles.put(key,file);
    }

    /**
     * If will only remove File value. It won't delete file itself.
     * @param key whose value and key you want to remove
     */
    public void removeFile(@NonNull String key)
    {
        if (postBoyFragment.mapFiles!=null)
            postBoyFragment.mapFiles.remove(key);
    }

    /**
     * If will only clear values of all files. It won't delete files itself.
     * @param key whose value and key you want to remove
     */
    public void removeAllFiles(@NonNull String key)
    {
        if (postBoyFragment.mapFiles!=null)
            postBoyFragment.mapFiles.remove(key);
    }



    /**
     * @return  returns post type.
     */
    public RequestType getPostType() {
        return postBoyFragment.requestType;
    }

    /**
     * @return  returns link that will be used to call webservice.
     */
    public String getLink() {
        return postBoyFragment.link;
    }

    public static void setDefaultConfigs(@NonNull PostBoyConfig postBoyConfig) {
        PostBoyConfig.CONNECTION_TIMEOUT = postBoyConfig.connectionTimeout;
        PostBoyConfig.READ_TIMEOUT = postBoyConfig.readTimeout;
        PostBoyConfig.KEEP_PERSISTENT = postBoyConfig.keepPersistent;
    }
    //*************************GETTER AND SETTER METHODS  END***************************/


    public static class Builder {
        @Nullable
        private Context context;
        private String link;
        private boolean keepPersistent = PostBoyConfig.KEEP_PERSISTENT;
        private int connectionTimeout = PostBoyConfig.CONNECTION_TIMEOUT, readTimeout = PostBoyConfig.READ_TIMEOUT;
        private RequestType requestType;


        /**
         * @param context Reference of your context, Don't use Application context here.
         * @param requestType Type of your request. e.g GET,POST,PUT etc.
         * @param link  link you want to call to get response from server
         */
        public Builder(@Nullable  Context context, @NonNull RequestType requestType, @NonNull  String link) {
            this.link = link;
            this.context = context;
            this.requestType= requestType;
        }

        /**
         * (Its default value true. You can also change its default value from {@link PostBoy}.setDefaultConfigs({@link PostBoyConfig}).
         * @param keepPersistent If it is true than object of PostBoy will be attached to activity and won't reinitialize when you change screen orientation.
         *                       If you try to reinitialize same object with same link, then you will get old object with old settings that you passed to Builder object.
         *                       It is helpful when you don't want to reconnect with server if your activity is recreated because of configuration changes.
         *                       For example: You have called webservice and waiting for response from server and user change orientation of screen, then you don't need to call webservice again.
         * @return returns builder object
         */
        public Builder setKeepPersistent(boolean keepPersistent) {
            this.keepPersistent = keepPersistent;
            return this;
        }

        /**
         * Set method type to either post [form-date or x-www-urlencoded] , GET or POST etc.
         * @param requestType {@link RequestType}
         * @return returns builder object
         */
        public Builder setRequestType(@NonNull RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        /**
         * Set Link of webservice
         * @param link  link for webservice
         * @return  returns current builder object
         */
        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        /**
         * (Its default value 15000 milliseconds. You can also change its default value from {@link PostBoy}.setDefaultConfigs({@link PostBoyConfig}).
         * Sets a specified timeout value, in milliseconds, to be used when opening a communications link to the resource referenced by this URLConnection. If the timeout expires before there is data available for read, a connectionFail callback is called. A timeout of zero is interpreted as an infinite timeout.
         * @param connectionTimeout an int that specifies the timeout value to be used in milliseconds
         * @return  returns current builder object
         */
        public Builder setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * (Its default value 15000 milliseconds. You can also change its default value from {@link PostBoy}.setDefaultConfigs({@link PostBoyConfig}).
         * Sets the read timeout to a specified timeout, in milliseconds. A non-zero value specifies the timeout when reading from Input stream when a connection is established to a resource. If the timeout expires before there is data available for read, a connectionFail callback is called . A timeout of zero is interpreted as an infinite timeout.
         * @param readTimeout an int that specifies the timeout value to be used in milliseconds
         * @return  returns current builder object
         */
        public Builder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * @return Returns object of  postboy.
         */
        public PostBoy create()
        {
            String[] l = link.split("\\/");
            String tag = link;
            if (l.length>0)
                tag = l[l.length-1];
            return new PostBoy(
                    context,
                    link,
                    keepPersistent,
                    requestType,
                    tag+"PostBoy",
                    connectionTimeout,
                    readTimeout);
        }
    }
}
