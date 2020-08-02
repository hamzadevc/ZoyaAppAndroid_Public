package com.grappetite.zoya.activities;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.grappetite.zoya.R;

public class ExpertRequestActivity extends AppCompatActivity {

    Button btn_sendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_request);

        btn_sendRequest = findViewById(R.id.btn_sendRequest);

        String expID = getIntent().getStringExtra("expID");

        btn_sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_sendRequest.setEnabled(false);
                btn_sendRequest.setTextColor(Color.RED);

                Toast.makeText(getApplicationContext(),expID,Toast.LENGTH_LONG).show();

            }
        });

    }
}

