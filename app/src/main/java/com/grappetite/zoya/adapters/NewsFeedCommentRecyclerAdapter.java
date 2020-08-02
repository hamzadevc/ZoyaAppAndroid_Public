package com.grappetite.zoya.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.CommentData;
import com.grappetite.zoya.viewholders.NewsFeedCommentViewHolder;

import java.util.ArrayList;

public class NewsFeedCommentRecyclerAdapter extends RecyclerView.Adapter<NewsFeedCommentViewHolder> {

    private ArrayList<CommentData> list;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public NewsFeedCommentRecyclerAdapter setList(ArrayList<CommentData> list) {
        this.list = list;
        return this;
    }

    @Override
    public NewsFeedCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsFeedCommentViewHolder(parent, R.layout.eachview_news_feed_comment);
    }

    @Override
    public void onBindViewHolder(NewsFeedCommentViewHolder vh, int position) {
        vh.bind(list.get(position));
        vh.setOnFlagClickListener(view -> {
            if (listener!=null)
                listener.onFlagIconClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public interface Listener {
        void onFlagIconClicked(int position);
    }
}
