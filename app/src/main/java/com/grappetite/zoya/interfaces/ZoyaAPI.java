package com.grappetite.zoya.interfaces;

import com.grappetite.zoya.dataclasses.JSONResponse;
import com.grappetite.zoya.dataclasses.Newsfeed;
import com.grappetite.zoya.restapis.urls.WebUrls;

import dimitrovskif.smartcache.SmartCall;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ZoyaAPI {


    @GET("/api_v1/get_newsfeeds")
    SmartCall<String> getNewsfeedData(@Header("Authtoken") String authToken);

    @GET("/api_v1/get_remedies")
    SmartCall<String> getRemedies(@Header("Authtoken") String authToken);

    @GET("/api_v1/get_places_types")
    SmartCall<String> getPlacesTypes(@Header("Authtoken") String authToken);

    @GET("/api_v1/get_cities")
    SmartCall<String> getCities(@Header("Authtoken") String authToken, @Query("city") String city);

    @GET("/api_v1/get_doctors")
    SmartCall<String> getDoctors(@Header("Authtoken") String authToken,
                                 @Query("city") String city,
                                 @Query("order_by") String order_by,
                                 @Query("group_by") String group_by,
                                 @Query("direction") String asc,
                                 @Query("place_name") String place_name,
                                 @Query("specialization") String specialization);

    @POST("/api_v1/" + WebUrls.METHOD_UPDATE_USER)
    SmartCall<String> setPeriodData(@Header("Authtoken") String authToken,
                                    @Query("data") String data,
                                    @Query("last_period_date") String last_period_date,
                                    @Query("no_of_period_days") String no_of_period_days,
                                    @Query("menstrual_cycle_days") String menstrual_cycle_days);

    @GET("/api_v1/get_period_data")
    SmartCall<String> getPeriodData(@Header("Authtoken") String authToken);

    @POST("/api_v1/update_period_data")
    SmartCall<String> updatePeriodData(@Header("Authtoken") String authToken, @Query("data") String data);

    @POST("/api_v1/check_promo")
    SmartCall<String> setPromo(@Header("Authtoken") String authToken, @Query("promo_code") String promo_code);

    @GET("/api_v1/get_newsfeedbyid")
    SmartCall<String> getNewsfeedById(@Header("Authtoken") String authToken, @Query("id") String id);
}
