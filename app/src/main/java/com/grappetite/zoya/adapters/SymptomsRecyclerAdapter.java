package com.grappetite.zoya.adapters;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.SymptomData;
import com.grappetite.zoya.viewholders.SymptomsViewHolder;

import java.util.ArrayList;

public class SymptomsRecyclerAdapter extends RecyclerView.Adapter<SymptomsViewHolder> implements View.OnClickListener {
    private ArrayList<SymptomData> list;
    private RecyclerView rv;

    public SymptomsRecyclerAdapter setList(ArrayList<SymptomData> list) {
        this.list = list;
        return this;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.rv = recyclerView;
    }

    @Override
    public SymptomsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SymptomsViewHolder(parent, R.layout.eachview_symptoms);
    }

    @Override
    public void onBindViewHolder(SymptomsViewHolder vh, int position) {
        vh.bind(list.get(position));
        vh.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onClick(View view) {
        int position = rv.getChildAdapterPosition(view);
        SymptomData data = list.get(position);
        data.setChecked(!data.isChecked());
        this.notifyItemChanged(position);
    }

    public ArrayList<SymptomData> getSelectedSymptoms() {
        ArrayList<SymptomData> selectedSymptoms = new ArrayList<>();
        if (list != null)
            for (SymptomData data : list) {
                if (data.isChecked())
                    selectedSymptoms.add(data);
            }
        return selectedSymptoms;
    }
}
