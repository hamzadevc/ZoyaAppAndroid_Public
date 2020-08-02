package com.grappetite.zoya.helpers;


import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.CitiesParser;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.SessionUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityPostBoyHelper implements View.OnClickListener , Callback<String> {
    private final Activity activity;
    private PostBoy postBoy;
    private ArrayList<String> citiesList;
    private TextView tv_city;
    private DialogUtils.CityDialogListener listener;
//    private ProgressDialog pd;

    private ZoyaAPI zoyaAPI;

    public CityPostBoyHelper(Activity activity, TextView tv_city, DialogUtils.CityDialogListener listener) {
        this.activity = activity;
        this.tv_city = tv_city;
        this.listener = listener;
        citiesList = new ArrayList<>();

        zoyaAPI = RetrofitInstance.getRetrofitInstance(activity.getApplicationContext()).create(ZoyaAPI.class);

//        postBoy = new PostBoy.Builder(activity, RequestType.GET, WebUrls.BASE_URL+WebUrls.METHOD_CITIES).create();
//        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
//        postBoy.setListener(new CitiesListener());
        tv_city.setOnClickListener(this);
    }

    public ArrayList<String> getCities() {
        return citiesList;
    }

    public void setCities(ArrayList<String> cities) {
        citiesList.clear();
        citiesList.addAll(cities);
    }

    @Override
    public void onClick(View view) {
        if (citiesList.size() == 0) {
            zoyaAPI.getCities(LocalStoragePreferences.getAuthToken(), null).enqueue(this);
//            pd = DialogUtils.getLoadingDialog(activity, "Loading cities...");
//            pd.show();
//            postBoy.call();
        } else {
            DialogUtils.showCities(activity, citiesList, tv_city, listener);
        }
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

//        DialogUtils.dismiss(pd);

        CitiesParser parser = new CitiesParser(response.body());
        switch (parser.getResponseCode()) {
            case 200:
                citiesList.clear();
                citiesList.addAll(parser.getCities());
//                DialogUtils.showCities(activity, citiesList, tv_city, listener);
                break;
            case 204:
                Toast.makeText(activity, "No city found", Toast.LENGTH_SHORT).show();
                break;
            case 401:
                SessionUtils.logout(activity, true);
                break;
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
//        DialogUtils.dismiss(pd);
        Toast.makeText(activity, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
    }

//    private class CitiesListener implements PostBoyListener {
//        private ProgressDialog pd;
//
//        @Override
//        public void onPostBoyConnecting() throws PostBoyException {
//            pd = DialogUtils.getLoadingDialog(activity, "Loading cities...");
//            pd.show();
//        }
//
//        @Override
//        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
//            CitiesParser parser = new CitiesParser(json);
//            if (parser.getResponseCode() == 200) {
//                citiesList.clear();
//                citiesList.addAll(parser.getCities());
//            }
//        }
//
//        @Override
//        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
//            DialogUtils.dismiss(pd);
//            Parser parser = new Parser(json);
//            switch (parser.getResponseCode()) {
//                case 200:
//                    DialogUtils.showCities(activity, citiesList, tv_city, listener);
//                    break;
//                case 204:
//                    Toast.makeText(activity, "No city found", Toast.LENGTH_SHORT).show();
//                    break;
//                case 401:
//                    SessionUtils.logout(activity, true);
//                    break;
//            }
//        }
//
//        @Override
//        public void onPostBoyConnectionFailure() throws PostBoyException {
//            DialogUtils.dismiss(pd);
//            Toast.makeText(activity, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onPostBoyError(PostBoyException e) {
//
//        }
//    }

}
