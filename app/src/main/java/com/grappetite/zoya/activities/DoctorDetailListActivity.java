package com.grappetite.zoya.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.DoctorDetailRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.DoctorData;
import com.grappetite.zoya.dataclasses.PlaceData;
import com.grappetite.zoya.helpers.CityPostBoyHelper;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.DoctorTypesParser;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetailListActivity extends CustomActivity implements RecyclerItemClickListener, ConnectionInfoView.Listener, Callback<String> {

    @BindView(R.id.rv)
    IndexFastScrollRecyclerView rv;
    @BindView(R.id.civ) ConnectionInfoView  civ;
    @BindView(R.id.tv_city) TextView tv_city;

    private DoctorDetailRecyclerAdapter adb;
    private ArrayList<DoctorData> list;
    private ZoyaAPI zoyaAPI;
    private String specialization;
    private String placeName;
    private boolean isPlace;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_doctor_detail_list;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adb = new DoctorDetailRecyclerAdapter();
        adb.setListener(this);

        zoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);

        if(this.getIntent().hasExtra(CommonConstants.EXTRA_SPECIALIZATION)){
            specialization = this.getIntent().getStringExtra(CommonConstants.EXTRA_SPECIALIZATION);
            isPlace =false;
        }else if (this.getIntent().hasExtra(CommonConstants.DATA_PLACE)){
            placeName = ((PlaceData)this.getIntent().getParcelableExtra(CommonConstants.DATA_PLACE)).getName();
            isPlace =true;
        }

        CityPostBoyHelper cityPostBoyHelper = new CityPostBoyHelper(this, tv_city, city -> {
            if(isPlace){
                zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),city,null,null,"asc",placeName,null).enqueue(this);
            }else {
                zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),city,"doctor",null,"asc",null,specialization).enqueue(this);
            }

            list.clear();
            adb.notifyDataSetChanged();
            civ.showLoader();
        });
        cityPostBoyHelper.setCities(this.getIntent().getStringArrayListExtra(CommonConstants.DATA_CITIES));
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("FINDER");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));

        adb.setList(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adb);
        rv.setIndexBarTextColor("#53afbc");
        rv.setIndexBarColor("#FFFFFF");
        rv.setIndexBarTransparentValue(1f);
        rv.setIndexbarMargin(0);
        rv.setIndexBarHighLateTextVisibility(false);
        rv.setIndexBarCornerRadius(0);

        civ.setListener(this);
        String selectedCity = this.getIntent().getStringExtra(CommonConstants.EXTRA_SELECTED_CITY);
        tv_city.setText(selectedCity);
        tv_city.setTag(selectedCity);


        if(isPlace){
            zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),selectedCity,null,null,"asc",placeName,null).enqueue(this);
        }else {
            zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),selectedCity,"doctor",null,"asc",null,specialization).enqueue(this);
        }

        list.clear();
        adb.notifyDataSetChanged();
        civ.showLoader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==29){

            if(isPlace){

                zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),
                        tv_city.getText().toString(),
                        null,
                        null,
                        "asc", placeName, null)
                        .enqueue(this);

            }else {

                zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),
                        tv_city.getText().toString(),
                        "doctor",
                        null,
                        "asc", null, specialization)
                        .enqueue(this);

            }

            list.clear();
            adb.notifyDataSetChanged();
            civ.showLoader();

        }
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        Intent i = new Intent(this,FinderMapActivity.class);
        i.putExtra(CommonConstants.EXTRA_SELECTED_CITY,tv_city.getTag().toString());
        i.putExtra(CommonConstants.DATA_DOCTOR,list.get(position));
        this.startActivityForResult(i,29);
    }

    @Override
    public void onRetry() {
        if(isPlace){

            zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),
                    tv_city.getText().toString(),
                    null,
                    null,
                    "asc", placeName, null)
                    .enqueue(this);

        }else {

            zoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),
                    tv_city.getText().toString(),
                    "doctor",
                    null,
                    "asc", null, specialization)
                    .enqueue(this);

        }


        list.clear();
        adb.notifyDataSetChanged();
        civ.showLoader();
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        civ.hideAll();
        DoctorTypesParser parser = new DoctorTypesParser(response.body());
        switch (parser.getResponseCode()) {
            case 200:
                list.clear();
                list.addAll(parser.getDoctorTypes());
                adb.notifyDataSetChanged();
                break;
            case 204:
                civ.showNothingFound();
                break;
            case 401:
                SessionUtils.logout(DoctorDetailListActivity.this,true);
                break;
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        civ.showInternetConnectionFail();
    }

}
