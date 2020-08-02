package com.grappetite.zoya.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.erraticsolutions.framework.fragments.CustomFragment;
import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.ContactActivity;
import com.grappetite.zoya.activities.FinderPlacesTypesActivity;
import com.grappetite.zoya.activities.NewPeriodTrackerActivity;
import com.grappetite.zoya.activities.NewsFeedActivity;
import com.grappetite.zoya.activities.RemediesActivity;
import com.grappetite.zoya.activities.SettingsActivity;
import com.grappetite.zoya.activities.SymptomsStep1Activity;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.helpers.ChatHelper;
import com.grappetite.zoya.utils.PeriodUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends CustomFragment {
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        PeriodUtils.setPeriodAlarm(getActivity());
        ChatHelper.initialize();
        if (this.getActivity() != null) {
            if (this.getActivity().getIntent().hasExtra(CommonConstants.EXTRA_SCREEN_TO_OPEN))
                switch (this.getActivity().getIntent().getStringExtra(CommonConstants.EXTRA_SCREEN_TO_OPEN)) {
                    case CommonConstants.VALUE_SCREEN_PERIOD_TRACKER:
                        startPeriodTracker();
                        break;
                }
        }
    }

    private void startPeriodTracker() {
        Intent i = new Intent(this.getActivity(), NewPeriodTrackerActivity.class);
        this.startActivity(i);
        if (this.getActivity() != null)
            this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
    }

    private void showItsUnderDevelopment() {
        if (getActivity() != null)
            new AlertDialog.Builder(getActivity())
                    .setTitle("Coming Soon!")
                    .setMessage("We're almost ready and will release this feature shortly")
                    .setPositiveButton("Ok", null)
                    .show();
    }

    @OnClick({
         //  R.id.ll_period_tracker,
            R.id.ll_news_feed,
            R.id.ll_symptoms,
            R.id.ll_remedies,
        //  R.id.ll_ask_expert,
            R.id.ll_finder,
            R.id.ll_contact,
            R.id.ll_settings,
    })
    public void onClick(View view) {
        if (this.getActivity() != null)
            switch (view.getId()) {
                case R.id.ll_news_feed: {
                    Intent i = new Intent(this.getActivity(), NewsFeedActivity.class);
                    this.startActivity(i);
                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
                }
                break;
                case R.id.ll_symptoms: {
                    Intent i = new Intent(this.getActivity(), SymptomsStep1Activity.class);
                    this.startActivity(i);
                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
                }
                break;
//                case R.id.ll_period_tracker: {
//
//                    Intent i = new Intent(this.getActivity(), ComingSoon.class);
//                    this.startActivity(i);
//                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
//                    Intent i;
//
//                    if(LocalStoragePreferences.getProfileData().getPeriodLast() != 0){
//                        i =  new Intent(this.getActivity(),NewPeriodTrackerActivity.class);
//                    }else {
//                        i =  new Intent(this.getActivity(), NewPeriodEditActivity.class);
//                    }
//
//                    this.startActivity(i);
//                    this.getActivity().overridePendingTransition(R.anim.right_enter,R.anim.scale_down);
//
//                }
//                break;
                case R.id.ll_remedies: {
                    Intent i = new Intent(this.getActivity(), RemediesActivity.class);
                    this.startActivity(i);
                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
                }
                break;
//                case R.id.ll_ask_expert: {
////                    showItsUnderDevelopment();
////                    Intent i = new Intent(this.getActivity(), ExpertChatActivity.class);
//                    Intent i = new Intent(this.getActivity(), ComingSoon.class);
//                    this.startActivity(i);
//                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
//                }
//                break;
                case R.id.ll_finder: {
                    Intent i = new Intent(this.getActivity(), FinderPlacesTypesActivity.class);
                    this.startActivity(i);
                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
                }
                break;
                case R.id.ll_contact: {
                    Intent i = new Intent(this.getActivity(), ContactActivity.class);
                    this.startActivity(i);
                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
                }
                break;
                case R.id.ll_settings: {
                    Intent i = new Intent(this.getActivity(), SettingsActivity.class);
                    this.startActivity(i);
                    this.getActivity().overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
                }
                break;
            }
    }
}
