package com.grappetite.zoya.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.AskExpertRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ExpertData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.parsers.ExpertParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AskExpertActivity extends CustomActivity implements RecyclerItemClickListener, ConnectionInfoView.Listener {

    @BindView(R.id.rv)  RecyclerView rv;
    @BindView(R.id.v_connection_info)  ConnectionInfoView  v_info;
    private ArrayList<ExpertData>   list;
    private AskExpertRecyclerAdapter adb;

    private PostBoy postBoy;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ask_expert;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adb = new AskExpertRecyclerAdapter();
        postBoy = new PostBoy.Builder(this, RequestType.GET, WebUrls.BASE_URL+WebUrls.METHOD_EXPERTS).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("ASK AN EXPERT");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));
        adb.setList(list);
        adb.setListener(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adb);
        v_info.setListener(this);

        postBoy.setListener(new ExpertsListener());
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.addGETValue("limit","500");
        postBoy.call();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up,R.anim.right_exit);
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        Intent i = new Intent(this, AskExpertThreadActivity.class);
        i.putExtra(CommonConstants.DATA_EXPERT,list.get(position));
        this.startActivity(i);
    }

    @Override
    public void onRetry() {
        postBoy.call();
    }


    private class ExpertsListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            v_info.showLoader();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            ExpertParser parser = new ExpertParser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    list.addAll(parser.getExperts());
                    break;
            }
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            v_info.hideAll();
            ExpertParser parser = new ExpertParser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    adb.customNotifyDataSetChanged();
                    break;
                case 204:
                    v_info.showNothingFound();
                    break;
                case 401:
                    SessionUtils.logout(AskExpertActivity.this,true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            v_info.showInternetConnectionFail();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
