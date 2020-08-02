package com.grappetite.zoya.viewholders;

import androidx.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.ReviewData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewViewHolder extends CustomViewHolder {

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_review)
    TextView tv_review;
    @BindView(R.id.rating)
    RatingBar ratingBar;

    public ReviewViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public void bind(ReviewData data){

        tv_name.setText(data.getUserName());
        tv_review.setText(data.getReview());
        ratingBar.setRating(Float.parseFloat(data.getRating()));
    }
}
