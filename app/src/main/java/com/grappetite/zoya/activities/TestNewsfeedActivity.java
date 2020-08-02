package com.grappetite.zoya.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import android.view.MenuItem;
import android.view.View;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.NewsFeedRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.NewsFeedData;
import com.grappetite.zoya.dataclasses.TagData;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.NewsFeedParser;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestNewsfeedActivity extends CustomActivity implements NewsFeedRecyclerAdapter.Listener, ConnectionInfoView.Listener, Callback<String>{

    private static final String TAG = "NewsFeedActivity";

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView civ;
    private NewsFeedRecyclerAdapter adb;
    private ArrayList<NewsFeedData> list;
    private ArrayList<NewsFeedData> featuredList;
    private TagData tagData;
    private PostBoy postBoy;
    private LinearLayoutManager layoutManager;

    ZoyaAPI zoyaAPI;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_feed;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        tagData = this.getIntent().getParcelableExtra(CommonConstants.DATA_TAG);
        list = new ArrayList<>();
        featuredList = new ArrayList<>();
        adb = new NewsFeedRecyclerAdapter();

        zoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle(tagData == null ? "NEWSFEED" : tagData.getTitle().toUpperCase());
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        civ.setListener(this);
        adb.setListener(this);
        adb.setList(list);
        adb.setFeaturedList(featuredList);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new SlideInUpAnimator());
        rv.setAdapter(adb);

        zoyaAPI.getNewsfeedData(LocalStoragePreferences.getAuthToken()).enqueue(this);
        civ.showLoader();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getLocalBroadcastManager().sendBroadcast(new Intent(CommonConstants.ACTION_FINISH_NEWS_FEED));
            finish();
            this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        NewsFeedData data = list.get(position);
        if (data.getType() == NewsFeedData.Type.AD) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(data.getAdUrl()));
            this.startActivity(i);
        } else {
            Intent i = new Intent(this, NewsFeedDetailActivity.class);
            i.putExtra(CommonConstants.DATA_NEWS_FEED, data);
            this.startActivity(i);
        }
    }

    @Override
    public void onRecyclerItemTagClicked(TagData tagData) {
        Intent i = new Intent(this, NewsFeedActivity.class);
        i.putExtra(CommonConstants.DATA_TAG, tagData);
        this.startActivity(i);
    }

    @Override
    public void onRetry() {
//        postBoy.call();
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        NewsFeedParser parser = new NewsFeedParser(response.body());

        switch (parser.getResponseCode()){
            case 200:
                civ.hideAll();
                list.addAll(parser.getNewsFeeds());
//                featuredList.addAll(parser.getFeaturedNewsFeeds());
                adb.notifyDataSetChanged();
                break;
            case 204:
                civ.showNothingFound();
                break;
            case 401:
                SessionUtils.logout(TestNewsfeedActivity.this,true);
                break;
        }

    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        civ.showInternetConnectionFail();
    }

//    private class NewsFeedDataChangedReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent i) {
//            NewsFeedData data = i.getParcelableExtra(CommonConstants.DATA_NEWS_FEED);
//            for (int x = 0; x < list.size(); x++) {
//                if (data.getId() == list.get(x).getId()) {
//                    list.remove(x);
//                    list.add(x, data);
//                }
//            }
//            for (int x = 0; x < featuredList.size(); x++) {
//                if (data.getId() == list.get(x).getId()) {
//                    list.remove(x);
//                    list.add(x, data);
//                }
//            }
//        }
//    }
//
//    private class FinishReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            TestNewsfeedActivity.this.finish();
//        }
//    }
}
