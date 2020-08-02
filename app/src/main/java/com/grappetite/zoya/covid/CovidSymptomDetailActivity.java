package com.grappetite.zoya.covid;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.MainActivity;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.utils.ActivityUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CovidSymptomDetailActivity extends CustomActivity {

    @BindView(R.id.tv_title_symptoms)
    TextView tv_titleSymptoms;
    @BindView(R.id.tv_desc_symptoms)
    TextView tv_descSymptoms;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView v_connectionInfo;
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    @BindView(R.id.sv)
    ScrollView sv;
    CovidSymptomAdapter adapter;
    String rowID, rowValue;
    private DatabaseReference getUserDatabaseReference;

    String howCommon, overview, questionToAskDoctor, RiskFactor, SelfCare, Treatment, WhatToExpect, WhenToSeeDoctor;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_symptom_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        rowID = getIntent().getStringExtra("row_id");
        rowValue = getIntent().getStringExtra("row_value");
        Log.v("Values", "" + rowID + ":" + rowValue);

        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("CovidSymptomDetail").child(rowID);
        getUserDatabaseReference.keepSynced(true);
        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    howCommon = dataSnapshot.child("How Common").getValue().toString();
                    overview = dataSnapshot.child("Overview").getValue().toString();
                    questionToAskDoctor = dataSnapshot.child("Question To Ask Doctor").getValue().toString();
                    RiskFactor = dataSnapshot.child("Risk Factor").getValue().toString();
                    SelfCare = dataSnapshot.child("Self Care").getValue().toString();
                    Treatment = dataSnapshot.child("Treatment").getValue().toString();
                    WhatToExpect = dataSnapshot.child("What To Expect").getValue().toString();
                    WhenToSeeDoctor = dataSnapshot.child("When To See Doctor").getValue().toString();
                    Log.v("Detail", "" + howCommon);

                } catch (Exception e) {

                }
                if (howCommon != null) {
                    tv_titleSymptoms.setVisibility(!TextUtils.isEmpty(rowValue) ? View.VISIBLE : View.GONE);
                    tv_descSymptoms.setVisibility(!TextUtils.isEmpty(rowValue) ? View.VISIBLE : View.GONE);

                    tv_titleSymptoms.setText(String.format(Locale.getDefault(), "%s Symptoms", "Coronavirus (COVID-19)"));
                    tv_descSymptoms.setText(rowValue);

                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("Overview");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(overview);
                        ll_content.addView(view);
                    }
                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("Treatment");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(Treatment);
                        ll_content.addView(view);
                    }
                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("How Common");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(howCommon);
                        ll_content.addView(view);
                    }
                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("Question To Ask Doctor");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(questionToAskDoctor);
                        ll_content.addView(view);
                    }
                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("Risk Factor");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(RiskFactor);
                        ll_content.addView(view);
                    }
                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("Self Care");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(SelfCare);
                        ll_content.addView(view);
                    }
                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("What To Expect");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(WhatToExpect);
                        ll_content.addView(view);
                    }
                    if (!TextUtils.isEmpty(rowValue)) {
                        View view = getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                        ((TextView) view.findViewById(R.id.tv_title)).setText("When To See Doctor");
                        ((TextView) view.findViewById(R.id.tv_desc)).setText(WhenToSeeDoctor);
                        ll_content.addView(view);
                    }
                    sv.setVisibility(View.VISIBLE);
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
        this.setSupportActionbarTitle("چیک کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));


        if (LocalStoragePreferences.showSymptomsDisclaimer()) {
            View view = LayoutInflater.from(this).inflate(R.layout.include_symptoms_disclaimer, null, false);
            view.findViewById(R.id.tv_terms_and_conditions).setOnClickListener(v -> {
                ActivityUtils.openTermsAndConditions(this);
            });
            AlertDialog ad = new AlertDialog.Builder(this)
                    .setView(view)
                    .setOnCancelListener(dialog -> {
                        LocalStoragePreferences.setShowSymptomsDisclaimer(false);
                    })
                    .show();
            if (ad.getWindow() != null)
                ad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

    }

    @Override
    protected void populate() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.menu_symptom_detail, menu);
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

    @OnClick({R.id.tv_terms_and_conditions})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_terms_and_conditions: {
                ActivityUtils.openTermsAndConditions(this);
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
    }

}
