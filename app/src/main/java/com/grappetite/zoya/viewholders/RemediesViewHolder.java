package com.grappetite.zoya.viewholders;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.RemediesData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemediesViewHolder extends CustomViewHolder {

    @BindView(R.id.tv_title)    TextView tv_title;

    public RemediesViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public void bind(RemediesData data) {
        tv_title.setText(data.getTitle());
    }
}
