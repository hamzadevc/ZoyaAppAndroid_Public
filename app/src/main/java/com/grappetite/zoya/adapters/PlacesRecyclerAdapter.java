package com.grappetite.zoya.adapters;


import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.PlaceData;
import com.grappetite.zoya.interfaces.RecyclerItemClickListener;
import com.grappetite.zoya.viewholders.DoctorDetailViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<DoctorDetailViewHolder> implements SectionIndexer  {

    private static final String TAG = "PlacesRecyclerAdapter";
    private ArrayList<PlaceData> list;
    private RecyclerItemClickListener listener;

    public PlacesRecyclerAdapter setList(ArrayList<PlaceData> list) {
        this.list = list;
        return this;
    }

    public PlacesRecyclerAdapter setListener(RecyclerItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public DoctorDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoctorDetailViewHolder(parent, R.layout.eachview_doctor_detail);
    }

    @Override
    public void onBindViewHolder(DoctorDetailViewHolder vh, int position) {
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
        char c = (char) (sectionIndex+97);
        for (int i = 0 ; i<list.size() ; i++) {
            PlaceData data = list.get(i);
            String title =data.getName();
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
