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
import com.grappetite.zoya.adapters.DoctorTypesRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.DoctorData;
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

public class DoctorTypesActivity extends CustomActivity implements ConnectionInfoView.Listener, RecyclerItemClickListener, Callback<String> {

    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.rv)
    IndexFastScrollRecyclerView rv;
    @BindView(R.id.civ)
    ConnectionInfoView civ;

    private ArrayList<DoctorData> list;
    private DoctorTypesRecyclerAdapter adb;
    private CityPostBoyHelper cityPostBoyHelper;
    private ZoyaAPI ZoyaAPI;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_doctor_types;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adb = new DoctorTypesRecyclerAdapter();
        ZoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);

        cityPostBoyHelper = new CityPostBoyHelper(this, tv_city, city -> {

            ZoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(), city, "specialization", "specialization", "asc", null, null).enqueue(this);
            Connecting();

        });
        cityPostBoyHelper.setCities(this.getIntent().getStringArrayListExtra(CommonConstants.DATA_CITIES));
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("FINDER");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));

        civ.setListener(this);
        adb.setList(list);
        adb.setListener(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adb);
        rv.setIndexBarTextColor("#53afbc");
        rv.setIndexBarColor("#FFFFFF");
        rv.setIndexBarTransparentValue(1f);
        rv.setIndexbarMargin(0);
        rv.setIndexBarHighLateTextVisibility(false);
        rv.setIndexBarCornerRadius(0);
        String selectedCity = this.getIntent().getStringExtra(CommonConstants.EXTRA_SELECTED_CITY);
        tv_city.setText(selectedCity);
        tv_city.setTag(selectedCity);

        ZoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(), selectedCity, "specialization", "specialization", "asc", null, null).enqueue(this);
        Connecting();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29) {
            String selectedCity = LocalStoragePreferences.getSelectedCity();
            if (!selectedCity.equals(tv_city.getText())) {
                tv_city.setText(selectedCity);
                tv_city.setTag(selectedCity);
                ZoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),
                        LocalStoragePreferences.getSelectedCity(),
                        "specialization",
                        "specialization",
                        "asc", null, null)
                        .enqueue(this);
                Connecting();
            }
        }
    }

    @Override
    public void onRetry() {
        ZoyaAPI.getDoctors(LocalStoragePreferences.getAuthToken(),
                tv_city.getText().toString(),
                "specialization",
                "specialization",
                "asc", null, null)
                .enqueue(this);
        Connecting();
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        Intent i = new Intent(this, DoctorDetailListActivity.class);
        i.putExtra(CommonConstants.EXTRA_SELECTED_CITY, tv_city.getTag().toString());
        i.putExtra(CommonConstants.EXTRA_SPECIALIZATION, list.get(position).getSpecialization());
        i.putExtra(CommonConstants.DATA_CITIES, cityPostBoyHelper.getCities());
        this.startActivityForResult(i, 29);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        DoctorTypesParser parser = new DoctorTypesParser(response.body());

        switch (parser.getResponseCode()) {
            case 200:
                civ.hideAll();
                list.clear();
                list.addAll(parser.getDoctorTypes());
                adb.notifyDataSetChanged();
                break;
            case 204:
                civ.showNothingFound();
                break;
            case 401:
                SessionUtils.logout(DoctorTypesActivity.this, true);
                break;
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        civ.showInternetConnectionFail();
    }

    void Connecting() {
        list.clear();
        adb.notifyDataSetChanged();
        civ.showLoader();
    }
}
