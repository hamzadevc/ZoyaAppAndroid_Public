package com.grappetite.zoya.viewholders;

import androidx.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.DoctorData;
import com.grappetite.zoya.dataclasses.PlaceData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorDetailViewHolder extends CustomViewHolder {

    @BindView(R.id.tv_name)             TextView tv_name;
    @BindView(R.id.tv_specilization)    TextView tv_specilization;
    @BindView(R.id.tv_hospital)         TextView tv_hospital;
    @BindView(R.id.tv_address)          TextView tv_address;
    @BindView(R.id.tv_phone_number)     TextView tv_phoneNumber;
//    @BindView(R.id.tv_rating)           TextView tv_rating;
//    @BindView(R.id.rb_rating)           RatingBar rb_rating;


    public DoctorDetailViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this,view);
    }

    public void bind(DoctorData data) {
        tv_name.setText(data.getName());
        tv_specilization.setText(data.getSpecialization());
        tv_hospital.setText(data.getPlaceName());
        tv_address.setText(data.getAddress());
        tv_phoneNumber.setVisibility(TextUtils.isEmpty(data.getAtLeastOnePhoneNumber())?View.GONE:View.VISIBLE);
        tv_phoneNumber.setText(data.getAtLeastOnePhoneNumber());
//        tv_rating.setVisibility(View.GONE);
//        rb_rating.setVisibility(View.GONE);
//        if(TextUtils.isEmpty(data.getRating()) || data.getRating() == null){
//            tv_rating.setText("Not rated yet");
//            rb_rating.setEnabled(false);
//        }else {
//            tv_rating.setText("Rating");
//            rb_rating.setRating(Float.valueOf(data.getRating()));
//        }

    }
    public void bind(PlaceData data) {
        tv_name.setText(data.getName());
        tv_specilization.setText(data.getType());
        tv_hospital.setText(data.getCity());
        tv_address.setText(data.getAddress());
        tv_phoneNumber.setVisibility(TextUtils.isEmpty(data.getAtLeastOnePhoneNumber())?View.GONE:View.VISIBLE);
        tv_phoneNumber.setText(data.getAtLeastOnePhoneNumber());
//        if(TextUtils.isEmpty(data.getRating()) || data.getRating() == null){
//            tv_rating.setText("Not rated yet");
//            rb_rating.setRating(0);
//        }else {
//            tv_rating.setText("Rating");
//            rb_rating.setRating(Float.valueOf(data.getRating()));
//        }
    }
}
