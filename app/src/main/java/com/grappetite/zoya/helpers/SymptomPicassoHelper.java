package com.grappetite.zoya.helpers;


import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SymptomPicassoHelper {

    private final Context context;
    public SymptomPicassoHelper(Context context) {
        this.context = context;
    }

    public void load(ImageView iv, String assetPath) {
        String imagePath = "file:///android_asset/images/symptoms/"+assetPath;
        Picasso.get()
                .load(imagePath)
                .into(iv);
    }
}
