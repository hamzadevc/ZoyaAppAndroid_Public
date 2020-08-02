package com.grappetite.zoya.viewholders;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.MessageData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AskExpertThreadViewHolder extends CustomViewHolder {
    @BindView(R.id.tv_message)  TextView    tv_message;
    public AskExpertThreadViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public void bind(MessageData data) {
        tv_message.setText(data.getMessage());
    }
}
