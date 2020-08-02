package com.grappetite.zoya.postboy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.grappetite.zoya.restapis.urls.WebUrls;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dimitrovskif.smartcache.BasicCaching;
import dimitrovskif.smartcache.SmartCallFactory;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.facebook.FacebookSdk.getCacheDir;

public class RetrofitInstance {


    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(Context context) {

        SmartCallFactory smartFactory = new SmartCallFactory(BasicCaching.fromCtx(context));
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        Cache cache = new Cache(getCacheDir(), 50 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(logging)
//                .cache(cache)
                .build();

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(WebUrls.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(smartFactory)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
