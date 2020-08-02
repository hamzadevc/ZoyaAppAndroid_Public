package com.grappetite.zoya.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.erraticsolutions.framework.views.CustomButton;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.FinderPlacesTypeRecyclerAdapter;
import com.grappetite.zoya.covid.CovidCentreActivity;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.PlacesTypeData;
import com.grappetite.zoya.helpers.CityPostBoyHelper;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.PlacesTypesParser;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinderPlacesTypesActivity extends CustomActivity implements RecyclerItemClickListener, ConnectionInfoView.Listener, Callback<String> {

    @BindView(R.id.tv_city)
    TextView tv_city;

    @BindView(R.id.covid_center_btn)
    CustomButton covidCenterBtn;


    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.civ)
    ConnectionInfoView civ;

    private ArrayList<PlacesTypeData> list;
    private FinderPlacesTypeRecyclerAdapter adb;
    private CityPostBoyHelper cityPostBoyHelper;
    private ArrayList<String> citiesList;

    private ZoyaAPI ZoyaAPI;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_finder_places_types;
    }



    @Override
    protected void init() {
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adb = new FinderPlacesTypeRecyclerAdapter();
        ZoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);
        cityPostBoyHelper = new CityPostBoyHelper(this, tv_city, null);

        covidCenterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_city.getTag() != null) {
                    Intent i = new Intent(FinderPlacesTypesActivity.this, CovidCentreActivity.class);
                    i.putExtra("city_name", tv_city.getTag().toString());
                    v.getContext().startActivity(i);
                    if (v.getContext() instanceof Activity)
                        ((Activity) v.getContext()).overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                } else {
                    Toast.makeText(v.getContext(), "Please select city", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29) {
            String selectedCity = LocalStoragePreferences.getSelectedCity();
            tv_city.setText(selectedCity);
            tv_city.setTag(selectedCity);
        }
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("تلاش کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        adb.setList(list);
        adb.setListener(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adb);
        civ.setListener(this);


        {
            String selectedCity = LocalStoragePreferences.getSelectedCity();
            if (selectedCity != null) {
                tv_city.setText(selectedCity);
                tv_city.setTag(selectedCity);
            }
        }

        ZoyaAPI.getPlacesTypes(LocalStoragePreferences.getAuthToken()).enqueue(this);
        civ.showLoader();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }

    @OnClick({R.id.tv_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                Log.v("City",":clicked");
                break;
        }
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        if (tv_city.getTag() != null) {
            Intent i;
            if (list.get(position).getTitle().toLowerCase().trim().equals("doctors"))
                i = new Intent(this, DoctorTypesActivity.class);

            else
                i = new Intent(this, FinderPlacesByTypesActivity.class);
            i.putExtra(CommonConstants.EXTRA_SELECTED_CITY, tv_city.getTag().toString());
            i.putExtra(CommonConstants.DATA_CITIES, cityPostBoyHelper.getCities());
            i.putExtra(CommonConstants.DATA_PLACE_TYPE, list.get(position));
            this.startActivityForResult(i, 29);
            Log.v("Clicked",""+"Contact");

        }
        else {
            Toast.makeText(this, "Please select city", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onRetry() {
        ZoyaAPI.getPlacesTypes(LocalStoragePreferences.getAuthToken()).enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        PlacesTypesParser parser = new PlacesTypesParser(response.body());

        switch (parser.getResponseCode()) {
            case 200:
                civ.hideAll();
                list.clear();
                list.addAll(parser.getPlacesTypes());
                adb.notifyDataSetChanged();
                break;
            case 204:
                civ.showNothingFound();
                break;
            case 401:
                SessionUtils.logout(FinderPlacesTypesActivity.this, true);
                break;
        }


    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        civ.showInternetConnectionFail();
    }

}
