package com.grappetite.zoya.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.ReviewData;
import com.grappetite.zoya.viewholders.ReviewViewHolder;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private ArrayList<ReviewData> list;

    public ReviewRecyclerAdapter setList(ArrayList<ReviewData> list) {
        this.list = list;
        return this;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(parent, R.layout.layout_review);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder vh, int position) {
        vh.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
