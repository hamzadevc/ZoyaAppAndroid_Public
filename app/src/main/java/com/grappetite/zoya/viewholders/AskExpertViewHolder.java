package com.grappetite.zoya.viewholders;


import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.ExpertData;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AskExpertViewHolder extends CustomViewHolder {
    @BindView(R.id.tv_header)   TextView    tvHeader;
    @BindView(R.id.iv_profile_pic)  ImageView iv_profilePic;
    @BindView(R.id.tv_name) TextView tv_name;
    @BindView(R.id.tv_specialization) TextView tv_specialization;
    @BindView(R.id.iv_online)   ImageView iv_online;

    public AskExpertViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public void bind(ExpertData data) {
        Picasso.get()
                .load(data.getImageUrl())
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.profile_pic_placeholder_expert)
                .into(iv_profilePic);
        tv_name.setText(data.getName());
        tv_specialization.setText(data.getSpecialization());
        iv_online.setImageResource(data.getIsOnline()?R.drawable.s_circle_green_online:R.drawable.s_circle_gray_offline);
    }

    public void showHeader(boolean isOnline,int users) {
        tvHeader.setVisibility(View.VISIBLE);
        tvHeader.setTextColor(ContextCustom.getColor(itemView.getContext(),isOnline?R.color.green_txt:R.color.gray_txt));
        tvHeader.setText(String.format(Locale.getDefault(),"%d %s",users,isOnline?"Online":"Offline"));
    }

    public void hideHeader() {
        tvHeader.setVisibility(View.GONE);
    }

}
