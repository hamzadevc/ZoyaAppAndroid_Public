package com.grappetite.zoya.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.MessageData;
import com.grappetite.zoya.viewholders.AskExpertThreadViewHolder;

import java.util.ArrayList;

public class AskExpertThreadRecyclerAdapter extends RecyclerView.Adapter<AskExpertThreadViewHolder> {
    private final static int TYPE_SENT = 0;
    private final static int TYPE_RECEIVED= 1;
    private ArrayList<MessageData> list;

    public AskExpertThreadRecyclerAdapter setList(ArrayList<MessageData> list) {
        this.list = list;
        return this;
    }

    @Override
    public AskExpertThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT)
            return new AskExpertThreadViewHolder(parent, R.layout.eachview_ask_expert_sent);
        else
            return new AskExpertThreadViewHolder(parent, R.layout.eachview_ask_expert_received);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isMessageSent()?TYPE_SENT:TYPE_RECEIVED;
    }

    @Override
    public void onBindViewHolder(AskExpertThreadViewHolder vh, int position) {
        vh.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
}
