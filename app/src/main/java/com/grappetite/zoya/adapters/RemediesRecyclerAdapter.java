package com.grappetite.zoya.adapters;


import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.RemediesData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.viewholders.RemediesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RemediesRecyclerAdapter extends RecyclerView.Adapter<RemediesViewHolder> implements SectionIndexer {

    private static final String TAG = "RemediesRecyclerAdapter";
    private ArrayList<RemediesData> list;
    private RecyclerItemClickListener listener;

    public RemediesRecyclerAdapter setList(ArrayList<RemediesData> list) {
        this.list = list;
        return this;
    }

    public RemediesRecyclerAdapter setListener(RecyclerItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public RemediesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RemediesViewHolder(parent, R.layout.eachview_remedies);
    }

    @Override
    public void onBindViewHolder(RemediesViewHolder vh, int position) {
        vh.bind(list.get(position));
        vh.itemView.setOnClickListener((view)->{if (listener!=null) listener.onRecyclerItemClick(vh.itemView,position);});
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>();
        for (char i = 'a' ; i<='z' ; i++) {
            sections.add(String.valueOf(i));
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0 ; i<list.size() ; i++) {
            RemediesData data = list.get(i);
            String title =data.getTitle();
            char c = (char) (sectionIndex+97);
            if (TextUtils.isEmpty(title.toLowerCase()))
                return 0;
            else if (title.toLowerCase().charAt(0) == c)
                return i;
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}
