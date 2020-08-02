package com.grappetite.zoya.viewholders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.erraticsolutions.framework.views.CustomButton;
import com.grappetite.zoya.R;
import com.grappetite.zoya.covid.CovidCentreAdapter;
import com.grappetite.zoya.dataclasses.PlacesTypeData;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinderPlacesTypeViewHolder extends CustomViewHolder {

    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;


    public FinderPlacesTypeViewHolder(ViewGroup parent, int layoutRes) {
        super(parent, layoutRes);
    }




    @Override
    protected void init(View view) {
        ButterKnife.bind(this, view);
    }

    public void bind(PlacesTypeData data) {
        tv_type.setText(data.getTitle());
        Picasso.get()
                .load(data.getIconUrl())
                .noFade()
                .into(iv_icon);
    }
}
