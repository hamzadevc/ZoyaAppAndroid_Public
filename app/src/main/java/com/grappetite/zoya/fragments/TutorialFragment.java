package com.grappetite.zoya.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.erraticsolutions.framework.fragments.CustomFragment;
import com.erraticsolutions.framework.utils.UIUtils;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialFragment extends CustomFragment {
    @BindView(R.id.iv)
    ImageView iv;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_tutorial;
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        if (this.getArguments() != null) {
            int imageRes = this.getArguments().getInt(CommonConstants.EXTRA_TUTORIAL_IMAGE);
            int sw = UIUtils.getScreenWidth(getContext());
            Picasso.get()
                    .load(imageRes)
                    .resize(sw, (int) (sw * 1.78f))
                    .into(iv);
        }
    }
}
