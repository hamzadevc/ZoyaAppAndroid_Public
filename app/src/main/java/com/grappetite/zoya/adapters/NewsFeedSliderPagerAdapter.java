package com.grappetite.zoya.adapters;


import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.NewsFeedDetailActivity;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.dataclasses.NewsFeedData;
import com.grappetite.zoya.viewholders.NewsFeedViewHolder;

import java.util.ArrayList;

public class NewsFeedSliderPagerAdapter extends PagerAdapter {

    private ArrayList<NewsFeedData> list;
    private NewsFeedRecyclerAdapter.Listener listener;

    public NewsFeedSliderPagerAdapter setListener(NewsFeedRecyclerAdapter.Listener listener) {
        this.listener = listener;
        return this;
    }

    public NewsFeedSliderPagerAdapter(ArrayList<NewsFeedData> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view,@NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        NewsFeedViewHolder vh = new NewsFeedViewHolder(container,R.layout.eachview_news_feed);
        vh.bind(list.get(position),listener);
        vh.itemView.setOnClickListener(v -> {
            Intent i = new Intent(container.getContext(),NewsFeedDetailActivity.class);
            i.putExtra(CommonConstants.DATA_NEWS_FEED,list.get(position));
            container.getContext().startActivity(i);
        });
        container.addView(vh.itemView);
        return vh.itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
