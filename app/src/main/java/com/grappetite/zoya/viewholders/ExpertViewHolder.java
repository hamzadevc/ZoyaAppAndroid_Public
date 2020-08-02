package com.grappetite.zoya.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;

import com.erraticsolutions.framework.views.CustomTextView;
import com.grappetite.zoya.R;

public class ExpertViewHolder extends RecyclerView.ViewHolder {

    public View view;

    public ExpertViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }

    public void setExpName(String expName) {
        CustomTextView name = view.findViewById(R.id.expName);
        name.setText(expName);
    }

    public void setExpSpecial(String expSpecial) {
        CustomTextView status = view.findViewById(R.id.expSpecial);
        status.setText(expSpecial);
    }

    public void setIsOnline(String isOnline) {
        ImageView imageView = view.findViewById(R.id.isOnline);

        if (isOnline.equals("true")) {
            imageView.setImageResource(R.drawable.s_circle_green_online);
        } else {
            imageView.setImageResource(R.drawable.s_circle_gray_offline);
        }
    }

    public void setExpImage(String expImage) {
        ImageView imageView = view.findViewById(R.id.iv_profile_pic);

//        if(expImage != null || !expImage.equals("")){
//            Picasso.get()
//                    .load(expImage)
//                    .transform(new CropCircleTransformation())
//                    .placeholder(R.drawable.profile_pic_placeholder_expert)
//                    .resize(100,100)
//                    .into(imageView);
//        }

    }


}
