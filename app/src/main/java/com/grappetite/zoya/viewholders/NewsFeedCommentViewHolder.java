package com.grappetite.zoya.viewholders;


import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.CommentData;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class NewsFeedCommentViewHolder extends CustomViewHolder {
    @BindView(R.id.iv_profile_pic)  ImageView   iv_profilePic;
    @BindView(R.id.iv_flag)  ImageView   iv_flag;
    @BindView(R.id.tv_profile_name) TextView    tv_profileName;
    @BindView(R.id.tv_comment) TextView    tv_comment;


    public NewsFeedCommentViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public void setOnFlagClickListener(View.OnClickListener listener) {
        iv_flag.setOnClickListener(listener);
    }

    public void bind(CommentData commentData) {
        Picasso.get()
                .load(commentData.getCommentedByImageUrl())
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.profile_pic_placeholder)
                .into(iv_profilePic);
        tv_profileName.setText(commentData.getCommentedByName());
        tv_comment.setText(commentData.getComment());
        iv_flag.setImageResource(commentData.isFlagged()?R.drawable.ic_flag_active:R.drawable.ic_flag_inactive);
    }
}
