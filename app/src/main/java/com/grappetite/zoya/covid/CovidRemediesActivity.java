package com.grappetite.zoya.covid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grappetite.zoya.R;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CovidRemediesActivity extends CustomActivity implements ConnectionInfoView.Listener, CovidSymptomAdapter.ItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView civ;
    CovidRemediesAdapter adb;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_covid_remedies;
    }

    @Override
    protected void init() {

        ButterKnife.bind(this);
        civ.showLoader();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDatabase.getReference("CovidRemedy");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> remedyName = new ArrayList<>();

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childDataSnapshot1 : childDataSnapshot.getChildren()) {

                        Log.v("Key", childDataSnapshot1.getKey()); //displays the key for the node
                        remedyName.add(String.valueOf(childDataSnapshot1.getKey()));
                    }
                }
                Log.v("ClickShow", "");

                adb = new CovidRemediesAdapter(CovidRemediesActivity.this, remedyName);
                rv.setLayoutManager(new LinearLayoutManager(CovidRemediesActivity.this));
                adb.setClickListener(CovidRemediesActivity.this::onItemClick);
                rv.setAdapter(adb);
                civ.hideAll();
                civ.setListener(CovidRemediesActivity.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.setSupportActionbarTitle("دادی کے ٹوٹکے");
        this.showBackButton(true);
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }

    public void onItemClick(View view, int position) {
        Intent i = new Intent(this, CovidRemediesDetailActivity.class);
        i.putExtra("row_value", adb.getItem(position));
        i.putExtra("row_id", String.valueOf(position + 1));
        Log.v("Row#", String.valueOf(position + 1));
        view.getContext().startActivity(i);
        if (view.getContext() instanceof Activity)
            ((Activity) view.getContext()).overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
    }

    @Override
    public void onRetry() {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
