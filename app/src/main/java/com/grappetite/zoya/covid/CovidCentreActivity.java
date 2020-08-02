package com.grappetite.zoya.covid;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.receivers.FinishActivityReceiver;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CovidCentreActivity extends CustomActivity implements ConnectionInfoView.Listener, CovidCentreAdapter.ItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.civ)
    ConnectionInfoView v_info;
    @BindView(R.id.tv_city)
    TextView tv_city;
    private FinishActivityReceiver finishActivityReceiver;
    CovidCentreAdapter adapter;
    String cityName;
    //String pnumber;
    ArrayList<String> pnumber;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_covid_centre_detail_list;
    }

    @Override
    protected void init() {
        finishActivityReceiver = new FinishActivityReceiver(this);
        ButterKnife.bind(this);
        cityName = getIntent().getStringExtra("city_name");
        tv_city.setText(cityName);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDatabase.getReference("CovidCentre").child(cityName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> name = new ArrayList<>();
                ArrayList<String> city = new ArrayList<>();
                ArrayList<String> address = new ArrayList<>();
                ArrayList<String> phonenumber = new ArrayList<>();

                ArrayList<String> latitude = new ArrayList<>();
                ArrayList<String> longitude = new ArrayList<>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        Log.v("Place Name", childDataSnapshot.child("Place Name").getValue().toString());

                        name.add(childDataSnapshot.child("Place Name").getValue().toString());
                        city.add(cityName);
                        address.add(childDataSnapshot.child("Address").getValue().toString());
                        phonenumber.add(childDataSnapshot.child("Landline").getValue().toString());
                        latitude.add(childDataSnapshot.child("Location").child("Latitude").getValue().toString());
                        longitude.add(childDataSnapshot.child("Location").child("Longitude").getValue().toString());

                }

                RecyclerView recyclerView = findViewById(R.id.rv);
                recyclerView.setLayoutManager(new LinearLayoutManager(CovidCentreActivity.this));
                adapter = new CovidCentreAdapter(CovidCentreActivity.this, name, city, address, phonenumber, latitude, longitude);
                pnumber = phonenumber;
                adapter.setClickListener(CovidCentreActivity.this::onItemClick);
                recyclerView.setAdapter(adapter);
               // pnumber = String.valueOf(phonenumber);
                //pnumber = phonenumber;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onItemClick(View view, int position) {

        Intent i = new Intent(view.getContext(), CovidCentreMapActivity.class);
        i.putExtra("city_name", cityName);
        i.putExtra("row_id", String.valueOf(position + 1));
        Log.v("Check", "" + cityName + ":" + (position + 1));

        view.getContext().startActivity(i);
        if (view.getContext() instanceof Activity)
            ((Activity) view.getContext()).overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
    }
    //Hamza Tahir
//    public void ShowDialer(View view) {
//
//
//        DatabaseReference getUserDatabaseReference;
//        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("CovidCentre").child(cityName).child(rowID);
//        getUserDatabaseReference.keepSynced(true);
//        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                try {
//
//                } catch (Exception e) {
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("تلاش کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));


        v_info.setListener(this);
        this.getLocalBroadcastManager().registerReceiver(finishActivityReceiver, new IntentFilter(CommonConstants.ACTION_MOVE_TO_SYMPTOMS_SCREEN_ONE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (finishActivityReceiver != null)
            this.getLocalBroadcastManager().unregisterReceiver(finishActivityReceiver);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
    }


    @Override
    public void onRetry() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}

