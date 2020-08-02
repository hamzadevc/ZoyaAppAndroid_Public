package com.grappetite.zoya.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.ExpertData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.viewholders.AskExpertViewHolder;

import java.util.ArrayList;

public class AskExpertRecyclerAdapter extends RecyclerView.Adapter<AskExpertViewHolder> implements View.OnClickListener {
    private ArrayList<ExpertData> list;
    private RecyclerItemClickListener listener;
    private RecyclerView rv;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.rv = recyclerView;
    }

    public AskExpertRecyclerAdapter setListener(RecyclerItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public AskExpertRecyclerAdapter setList(ArrayList<ExpertData> list) {
        this.list = list;
        return this;
    }

    @Override
    public AskExpertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AskExpertViewHolder(parent, R.layout.eachview_ask_expert);
    }

    @Override
    public void onBindViewHolder(AskExpertViewHolder vh, int position) {
        ExpertData data = list.get(position);
        if (position==0 || data.getIsOnline()!=list.get(position-1).getIsOnline())
            vh.showHeader(data.getIsOnline(),data.getIsOnline()?onlineUsers:offlineUsers);
        else
            vh.hideHeader();
        vh.bind(list.get(position));
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

    private int onlineUsers, offlineUsers;
    public void customNotifyDataSetChanged() {
        onlineUsers=0;
        offlineUsers=0;
        for (ExpertData data : list) {
            if (data.getIsOnline())
                onlineUsers++;
            else
                offlineUsers++;
        }
        this.notifyDataSetChanged();
    }
}
