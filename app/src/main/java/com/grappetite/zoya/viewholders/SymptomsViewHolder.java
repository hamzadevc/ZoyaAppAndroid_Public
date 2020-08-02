package com.grappetite.zoya.viewholders;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.SymptomData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SymptomsViewHolder extends CustomViewHolder{
    @BindView(R.id.tv_title)    TextView tv_title;
    @BindView(R.id.iv_icon)     ImageView iv_icon;

    public SymptomsViewHolder(ViewGroup parent, int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public void bind(SymptomData data) {
        iv_icon.setImageResource(data.isChecked()?R.drawable.symptoms_subtract:R.drawable.symptoms_add);
        tv_title.setText(data.getName());
    }
}
