package com.grappetite.zoya.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.PlacesRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.CommonMethods;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.PlaceData;
import com.grappetite.zoya.dataclasses.PlacesTypeData;
import com.grappetite.zoya.helpers.CityPostBoyHelper;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.parsers.PlaceParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class FinderPlacesByTypesActivity extends CustomActivity implements ConnectionInfoView.Listener, RecyclerItemClickListener {

    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.rv)
    IndexFastScrollRecyclerView rv;
    @BindView(R.id.civ)
    ConnectionInfoView civ;

    private PlacesTypeData placesTypeData;
    private ArrayList<PlaceData> list;
    private PlacesRecyclerAdapter adb;
    private PostBoy postBoy;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_doctor_detail_list;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        placesTypeData = this.getIntent().getParcelableExtra(CommonConstants.DATA_PLACE_TYPE);
        list = new ArrayList<>();
        adb = new PlacesRecyclerAdapter();
        CityPostBoyHelper cityPostBoyHelper = new CityPostBoyHelper(this, tv_city, city -> {
            postBoy.addGETValue("city", city);
            postBoy.call();
        });
        cityPostBoyHelper.setCities(this.getIntent().getStringArrayListExtra(CommonConstants.DATA_CITIES));
        postBoy = new PostBoy.Builder(this, RequestType.GET, WebUrls.BASE_URL + WebUrls.METHOD_PLACES).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("تلاش کرو");
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
        postBoy.setListener(new DoctorTypesListener());
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.addGETValue("city", selectedCity);
        postBoy.addGETValue("type",placesTypeData.getTitle());
        postBoy.call();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==29)
            CommonMethods.refreshByCity(tv_city,postBoy);
    }

    @Override
    public void onRetry() {
        postBoy.call();
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        Log.v("Clicked",""+"ContactMAP");
        Intent i = new Intent(this, FinderMapActivity.class);
        i.putExtra(CommonConstants.EXTRA_SELECTED_CITY, tv_city.getTag().toString());
        i.putExtra(CommonConstants.DATA_PLACE, list.get(position));
        this.startActivityForResult(i,29);
    }

    private class DoctorTypesListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            list.clear();
            adb.notifyDataSetChanged();
            civ.showLoader();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            PlaceParser parser = new PlaceParser(json);
            if (parser.getResponseCode() == 200) {
                list.clear();
                list.addAll(parser.getDoctorTypes());
            }
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            civ.hideAll();
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    adb.notifyDataSetChanged();
                    break;
                case 204:
                    civ.showNothingFound();
                    break;
                case 401:
                    SessionUtils.logout(FinderPlacesByTypesActivity.this, true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            civ.showInternetConnectionFail();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
