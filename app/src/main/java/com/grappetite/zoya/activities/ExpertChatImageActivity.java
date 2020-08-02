package com.grappetite.zoya.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.grappetite.zoya.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ExpertChatImageActivity extends AppCompatActivity {

    ZoomageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_chat_image);

        imageView = findViewById(R.id.iv_zoom_image);

        Picasso.get()
                .load(getIntent().getStringExtra("URI"))
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .into(imageView);

    }
}
