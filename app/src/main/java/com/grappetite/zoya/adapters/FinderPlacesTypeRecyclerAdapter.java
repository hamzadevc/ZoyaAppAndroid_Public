package com.grappetite.zoya.adapters;


import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erraticsolutions.framework.views.CustomButton;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.PlacesTypeData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.viewholders.FinderPlacesTypeViewHolder;

import java.util.List;

import butterknife.BindView;

public class FinderPlacesTypeRecyclerAdapter extends RecyclerView.Adapter<FinderPlacesTypeViewHolder> {

    private List<PlacesTypeData> list;
    private RecyclerItemClickListener listener;



    public FinderPlacesTypeRecyclerAdapter setListener(RecyclerItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public FinderPlacesTypeRecyclerAdapter setList(List<PlacesTypeData> list) {
        this.list = list;
        return this;
    }

    @Override
    public FinderPlacesTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FinderPlacesTypeViewHolder(parent, R.layout.eachview_finder_places_type);
    }

    @Override
    public void onBindViewHolder(FinderPlacesTypeViewHolder vh, int position) {

        vh.bind(list.get(position));
        vh.itemView.setOnClickListener(view -> {
            if (listener!=null)
                listener.onRecyclerItemClick(vh.itemView,position);
            Log.v("Clicked",""+"ContactCLICK");
        });


}





    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }
}
