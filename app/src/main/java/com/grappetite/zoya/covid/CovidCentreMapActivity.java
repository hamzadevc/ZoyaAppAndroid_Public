package com.grappetite.zoya.covid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grappetite.zoya.R;
import com.grappetite.zoya.utils.AppPermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CovidCentreMapActivity extends CustomActivity implements OnMapReadyCallback {
    private static final String TAG = "FinderMapActivity";
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_specilization)
    TextView tv_specilization;
    @BindView(R.id.tv_hospital)
    TextView tv_hospital;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_phone_number)
    TextView tv_phoneNumber;

    private SupportMapFragment mapFragment;
    private Bitmap marketBitmap;

    String cityName, rowID;
    private DatabaseReference getUserDatabaseReference;

    String lat_, lng_, placeName_;
    boolean loaded = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_finder_map;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        cityName = getIntent().getStringExtra("city_name");
        rowID = getIntent().getStringExtra("row_id");
        Log.v("CheckFinal", "" + cityName + ":" + rowID);

        tv_city.setText(cityName);
        tv_specilization.setText("Covid Centres");
        tv_hospital.setVisibility(View.GONE);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_map);

        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("CovidCentre").child(cityName).child(rowID);
        getUserDatabaseReference.keepSynced(true);
        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    placeName_ = dataSnapshot.child("Place Name").getValue().toString();
                    Log.v("Place Name", dataSnapshot.child("Place Name").getValue().toString());
                    tv_name.setText(dataSnapshot.child("Place Name").getValue().toString());
                    tv_address.setText(dataSnapshot.child("Address").getValue().toString());
                    tv_phoneNumber.setText(dataSnapshot.child("Landline").getValue().toString());
                    lat_ = (dataSnapshot.child("Location").child("Latitude").getValue().toString());
                    lng_ = (dataSnapshot.child("Location").child("Longitude").getValue().toString());
                    loaded = true;

                } catch (Exception e) {

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("تلاش کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
    }

    @Override
    protected void asyncPopulate() {
        marketBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.google_map_marker);
    }

    @Override
    protected void populate() {

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 299) {
            if (AppPermissionUtils.hasLocationPermission(this))
                mapFragment.getMapAsync(this);
        }
    }

    private LatLng getLatLng() {
        if (lat_ != null)
            return new LatLng(Double.parseDouble(lat_), Double.parseDouble(lng_));
        else
            return null;
    }

    private String getPlaceName() {
        if (placeName_ != null)
            return placeName_;

        else
            return null;
    }

    @OnClick({
            R.id.btn_directions,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_directions: {
                LatLng latLng = getLatLng();

                if (latLng != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/dir/?api=1&&destination=" + latLng.latitude + "," + latLng.longitude));
                    startActivity(intent);
                }
            }
            break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (loaded) {
            LatLng latLng = getLatLng();
            String placeName = getPlaceName();

            if (latLng != null && placeName != null) {
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(placeName_)
                        .icon(BitmapDescriptorFactory.fromBitmap(marketBitmap)));
            }
            if (latLng != null)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
        }
    }
}
