package com.erraticsolutions.framework.customclasses;


import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CustomViewHolder extends RecyclerView.ViewHolder {
    public CustomViewHolder(View itemView) {
        super(itemView);
        init(itemView);
    }
    public CustomViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutRes,parent,false));
        init(itemView);
    }

    protected abstract void init(View view);
}
