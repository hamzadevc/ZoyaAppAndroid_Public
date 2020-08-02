package com.grappetite.zoya.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.DrawerItemData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.viewholders.DrawerItemViewHolder;

import java.util.ArrayList;

public class DrawerItemsRecyclerAdapter extends RecyclerView.Adapter<DrawerItemViewHolder> implements View.OnClickListener {
    private ArrayList<DrawerItemData>   list;
    private RecyclerView rv;
    private RecyclerItemClickListener listener;

    public DrawerItemsRecyclerAdapter setListener(RecyclerItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.rv = recyclerView;
    }

    public DrawerItemsRecyclerAdapter setList(ArrayList<DrawerItemData> list) {
        this.list = list;
        return this;
    }

    @Override
    public DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DrawerItemViewHolder(parent, R.layout.eachview_drawer_item);
    }

    @Override
    public void onBindViewHolder(DrawerItemViewHolder vh, int position) {
        DrawerItemData data = list.get(position);
        vh.getIv().setImageResource(data.getIconRes());
        vh.getTv().setText(data.getTitle());
        vh.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null)
            listener.onRecyclerItemClick(v,rv.getChildAdapterPosition(v));
    }
}
