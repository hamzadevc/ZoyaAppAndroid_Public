package com.grappetite.zoya.viewholders;


import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerItemViewHolder extends CustomViewHolder {
    @BindView(R.id.iv)  ImageView   iv;
    @BindView(R.id.tv)  TextView    tv;
    public DrawerItemViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public ImageView getIv() {
        return iv;
    }

    public TextView getTv() {
        return tv;
    }
}
