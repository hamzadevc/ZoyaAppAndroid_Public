package com.grappetite.zoya.covid;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.MainActivity;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.receivers.FinishActivityReceiver;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mumayank.com.airlocationlibrary.AirLocation;

public class CovidSymptomsActivity extends CustomActivity implements ConnectionInfoView.Listener, CovidSymptomAdapter.ItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ll_main)
    View ll_main;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView v_info;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;

    @BindView(R.id.find_nearest_covid)
    ImageView findNearestCovid;

    private AirLocation airLocation;


    @BindView(R.id.btn_next)
    Button btn_next;
    private FinishActivityReceiver finishActivityReceiver;
    CovidSymptomAdapter adapter;
    private DialogUtils.CityDialogListener listener;


    @Override
    protected int getContentViewId() {
        return R.layout.covid_symptoms;
    }

    @Override
    protected void init() {
        finishActivityReceiver = new FinishActivityReceiver(this);
        ButterKnife.bind(this);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDatabase.getReference("CovidSymptom");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> symptomName = new ArrayList<>();

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    Log.v("" + childDataSnapshot.getKey(), "" + childDataSnapshot.getValue()); //displays the key for the node
                    symptomName.add(String.valueOf(childDataSnapshot.getValue()));

                }
                Log.v("ClickShow", "");

                RecyclerView recyclerView = findViewById(R.id.rv);
                recyclerView.setLayoutManager(new LinearLayoutManager(CovidSymptomsActivity.this));
                adapter = new CovidSymptomAdapter(CovidSymptomsActivity.this, symptomName);
                adapter.setClickListener(CovidSymptomsActivity.this::onItemClick);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findNearestCovid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                airLocation = new AirLocation(CovidSymptomsActivity.this, true, true, new AirLocation.Callbacks() {
                    @Override
                    public void onSuccess(@NotNull Location location) {
                        Geocoder geoCoder = new Geocoder(CovidSymptomsActivity.this, Locale.getDefault()); //it is Geocoder
                        StringBuilder builder = new StringBuilder();
                        try {
                            List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            String finalAddress = address.get(0).getLocality();
                            Log.v("CityName", "" + finalAddress);
                            if (finalAddress != null) {
                                Intent i = new Intent(CovidSymptomsActivity.this, CovidCentreActivity.class);
                                i.putExtra("city_name", finalAddress);
                                v.getContext().startActivity(i);
                                if (v.getContext() instanceof Activity)
                                    ((Activity) v.getContext()).overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                            } else {
                                Toast.makeText(v.getContext(), "Please select city", Toast.LENGTH_SHORT).show();

                            }
                        } catch (IOException e) {
                        } catch (NullPointerException e) {
                        }
                    }

                    @Override
                    public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {
                    }
                });


            }
        });


    }


    @OnClick({R.id.find_nearest_covid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_nearest_covid:
                Log.v("City", ":clicked");
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29) {
            String selectedCity = LocalStoragePreferences.getSelectedCity();
            Log.v("City", "" + selectedCity);

//            tv_city.setText(selectedCity);
//            tv_city.setTag(selectedCity);
        }
    }


    public void onItemClick(View view, int position) {

        Intent i = new Intent(view.getContext(), CovidSymptomDetailActivity.class);
        i.putExtra("row_value", adapter.getItem(position));
        i.putExtra("row_id", String.valueOf(position + 1));
        Log.v("Row#", String.valueOf(position + 1));
        view.getContext().startActivity(i);
        if (view.getContext() instanceof Activity)
            ((Activity) view.getContext()).overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("چیک کرو");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu_symptom_detail, menu);
        menu.findItem(R.id.action_done).setTitle("HOME");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done: {
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(i);
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

