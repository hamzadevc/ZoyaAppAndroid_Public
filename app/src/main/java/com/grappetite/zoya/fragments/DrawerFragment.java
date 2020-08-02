package com.grappetite.zoya.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.erraticsolutions.framework.fragments.CustomFragment;
import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.AskExpertActivity;
import com.grappetite.zoya.activities.FinderActivity;
import com.grappetite.zoya.activities.NewsFeedActivity;
import com.grappetite.zoya.activities.ProfileActivity;
import com.grappetite.zoya.activities.SymptomsStep1Activity;
import com.grappetite.zoya.adapters.DrawerItemsRecyclerAdapter;
import com.grappetite.zoya.dataclasses.DrawerItemData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrawerFragment extends CustomFragment implements RecyclerItemClickListener {

    private static final String ITEM_NEWS_FEED = "Newsfeed";
    private static final String ITEM_SYMPTOMS = "Symptoms";
    private static final String ITEM_PERIOD_TRACKER = "Period Tracker";
    private static final String ITEM_HOME_REMEDIES = "Home Remendies";
    private static final String ITEM_CHAT_WITH_EXPERT = "Chat with Experts";
    private static final String ITEM_FINDER = "Finder";
    private static final String ITEM_CONTACT = "Contact";
    private static final String ITEM_SETTINGS = "Settings";

    @BindView(R.id.rv)  RecyclerView rv;
    private ArrayList<DrawerItemData>   list;
    private DrawerItemsRecyclerAdapter adb;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_drawer;
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this, view);
        list = new ArrayList<>();
        adb = new DrawerItemsRecyclerAdapter();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        adb.setList(list);
        adb.setListener(this);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setAdapter(adb);
    }

    @Override
    protected void asyncPopulate() {
        list.add(new DrawerItemData(ITEM_NEWS_FEED,R.drawable.news_feed));
        list.add(new DrawerItemData(ITEM_SYMPTOMS,R.drawable.symptoms));
        list.add(new DrawerItemData(ITEM_PERIOD_TRACKER,R.drawable.period_tracker));
        list.add(new DrawerItemData(ITEM_HOME_REMEDIES,R.drawable.home_remedies));
        list.add(new DrawerItemData(ITEM_CHAT_WITH_EXPERT,R.drawable.chat_with_expert));
        list.add(new DrawerItemData(ITEM_FINDER,R.drawable.finder));
        list.add(new DrawerItemData(ITEM_CONTACT,R.drawable.contact));
        list.add(new DrawerItemData(ITEM_SETTINGS,R.drawable.settings));
    }

    @Override
    protected void populate() {
        adb.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_profile_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_profile_pic:
            {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                this.startActivity(i);
                this.getActivity().overridePendingTransition(R.anim.right_enter,R.anim.scale_down);
            }
                break;
        }
    }

    @Override
    public void onRecyclerItemClick(View view, int position) {
        Intent i = null;
        switch (list.get(position).getTitle()) {
            case ITEM_NEWS_FEED:
                i = new Intent(this.getActivity(), NewsFeedActivity.class);
            break;
            case ITEM_FINDER:
                i = new Intent(this.getActivity(), FinderActivity.class);
            break;
            case ITEM_SYMPTOMS:
                i = new Intent(this.getActivity(), SymptomsStep1Activity.class);
            break;
            case ITEM_CHAT_WITH_EXPERT:
                i = new Intent(this.getActivity(), AskExpertActivity.class);
            break;
        }
        if (i!=null) {
            this.startActivity(i);
            this.getActivity().overridePendingTransition(R.anim.right_enter,R.anim.scale_down);
        }
    }
}
