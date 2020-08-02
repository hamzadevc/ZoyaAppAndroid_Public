package com.grappetite.zoya.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.NewsFeedData;
import com.grappetite.zoya.dataclasses.TagData;
import com.grappetite.zoya.viewholders.NewsFeedViewHolder;
import com.grappetite.zoya.views.NewsFeedSliderView;

import java.util.ArrayList;

public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<NewsFeedViewHolder> implements View.OnClickListener {

    private Listener listener;
    private RecyclerView rv;
    private ArrayList<NewsFeedData> list;
    private ArrayList<NewsFeedData> featuredList;

    public NewsFeedRecyclerAdapter setList(ArrayList<NewsFeedData> list) {
        this.list = list;
        return this;
    }

    public void setFeaturedList(ArrayList<NewsFeedData> featuredList) {
        this.featuredList = featuredList;
    }

    public NewsFeedRecyclerAdapter setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.rv = recyclerView;
    }

    @Override
    public NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0)
            return new NewsFeedViewHolder(new NewsFeedSliderView(parent.getContext()).setListener(listener));
        else
            return new NewsFeedViewHolder(parent, R.layout.eachview_news_feed);
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (position==0 && hasFeatureList())
            return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(NewsFeedViewHolder vh, int position) {
        if (vh.getItemViewType()==1) {
            vh.itemView.setOnClickListener(this);
            vh.bind(list.get(hasFeatureList()?position-1:position),listener);
        }
        else if (vh.getItemViewType()==0 && featuredList!=null) {
            NewsFeedSliderView newsFeedSliderView = (NewsFeedSliderView) vh.itemView;
            newsFeedSliderView.setFeaturedNewsFeed(featuredList);
        }
    }

    @Override
    public int getItemCount() {
        int sc= list==null?0:list.size();
        int fc = hasFeatureList()?1:0;
        return sc+fc;
    }

    private boolean hasFeatureList() {
        return featuredList!=null && featuredList.size()>0;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null)
            listener.onRecyclerItemClick(v,hasFeatureList()?rv.getChildAdapterPosition(v)-1:rv.getChildAdapterPosition(v));
    }

    public interface Listener {
        void onRecyclerItemClick(View view, int position);
        void onRecyclerItemTagClicked(TagData tagData);
    }
}
