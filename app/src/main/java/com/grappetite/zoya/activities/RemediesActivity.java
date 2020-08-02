package com.grappetite.zoya.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.grappetite.zoya.covid.CovidRemediesActivity;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.RemediesRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.RemediesData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.RemediesParser;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemediesActivity extends CustomActivity implements RecyclerItemClickListener, ConnectionInfoView.Listener, Callback<String> {

    @BindView(R.id.rv)
    IndexFastScrollRecyclerView rv;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView civ;
    private RemediesRecyclerAdapter adb;
    private ArrayList<RemediesData> list;

    private ZoyaAPI zoyaAPI;
    ImageView covid_remedies;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_remedies;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        adb = new RemediesRecyclerAdapter();
        list = new ArrayList<>();

        covid_remedies = findViewById(R.id.covid_remedies);
        covid_remedies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RemediesActivity.this, CovidRemediesActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
            }
        });

        zoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);


    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.setSupportActionbarTitle("دادی کے ٹوٹکے");
        this.showBackButton(true);
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
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

        zoyaAPI.getRemedies(LocalStoragePreferences.getAuthToken()).enqueue(this);

        civ.setListener(this);
        civ.showLoader();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        Intent i = new Intent(this, RemedyDetailActivity.class);
        i.putExtra(CommonConstants.DATA_REMEDIES, list.get(position));
        this.startActivity(i);
        this.overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
    }

    @Override
    public void onRetry() {
        zoyaAPI.getRemedies(LocalStoragePreferences.getAuthToken()).enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        RemediesParser parser = new RemediesParser(response.body());
        switch (parser.getResponseCode()) {
            case 200:
                civ.hideAll();
                list.addAll(parser.getRemedies());
                adb.notifyDataSetChanged();
                break;
            case 204:
                civ.showNothingFound();
                break;
            case 401:
                SessionUtils.logout(RemediesActivity.this, true);
                break;
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        civ.showInternetConnectionFail();
    }

}
