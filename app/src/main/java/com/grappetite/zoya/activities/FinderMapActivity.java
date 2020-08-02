package com.grappetite.zoya.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.dataclasses.DoctorData;
import com.grappetite.zoya.dataclasses.PlaceData;
import com.grappetite.zoya.utils.AppPermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinderMapActivity extends CustomActivity implements OnMapReadyCallback {
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

    @Nullable
    private DoctorData doctorData;
    @Nullable
    private PlaceData placeData;
    private SupportMapFragment mapFragment;
    private Bitmap marketBitmap;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_finder_map;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        doctorData = this.getIntent().getParcelableExtra(CommonConstants.DATA_DOCTOR);
        placeData = this.getIntent().getParcelableExtra(CommonConstants.DATA_PLACE);
        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.frag_map);
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
        if (doctorData != null) {
            tv_name.setText(doctorData.getName());
            tv_specilization.setText(doctorData.getSpecialization());
            tv_hospital.setText(doctorData.getPlaceName());
            tv_address.setText(doctorData.getAddress());

            if (doctorData.getPhoneNumber()==null || doctorData.getPhoneNumber().isEmpty())
                tv_phoneNumber.setText(doctorData.getAlternatePhoneNumber());
            else
                tv_phoneNumber.setText(String.format("%s\n%s", doctorData.getPhoneNumber(), doctorData.getAlternatePhoneNumber()));
            tv_city.setText(this.getIntent().getStringExtra(CommonConstants.EXTRA_SELECTED_CITY));
        } else if (placeData != null) {

            tv_name.setText(placeData.getName());
            tv_specilization.setText(placeData.getType());
            tv_hospital.setVisibility(View.GONE);
            tv_address.setText(placeData.getAddress());
            if (placeData.getPhoneNumber()==null || placeData.getPhoneNumber().isEmpty())
                tv_phoneNumber.setText(placeData.getAlternatePhoneNumber());
            else
                tv_phoneNumber.setText(String.format("%s\n%s", placeData.getPhoneNumber(), placeData.getAlternatePhoneNumber()));
            tv_city.setText(this.getIntent().getStringExtra(CommonConstants.EXTRA_SELECTED_CITY));

        }

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
        if (doctorData != null)
            return new LatLng(doctorData.getLatitude(), doctorData.getLongitude());
        else if (placeData != null)
            return new LatLng(placeData.getLatitude(), placeData.getLongitude());
        else
            return null;
    }

    private String getPlaceName() {
        if (doctorData != null)
            return doctorData.getName();
        else if (placeData != null)
            return placeData.getName();
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
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
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
        LatLng latLng = getLatLng();
        String placeName = getPlaceName();

        if (latLng != null && placeName != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(placeName)
                    .icon(BitmapDescriptorFactory.fromBitmap(marketBitmap)));
        }
        if (latLng != null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
    }
}
