package com.grappetite.zoya.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ConditionData;
import com.grappetite.zoya.dataclasses.SymptomData;
import com.grappetite.zoya.enums.Gender;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.receivers.FinishActivityReceiver;
import com.grappetite.zoya.restapis.parsers.ConditionsParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.GetMaps;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SymptomsStep4Activity extends CustomActivity implements ConnectionInfoView.Listener {

    @BindView(R.id.ll_your_choices)
    LinearLayout ll_yourChoices;
    @BindView(R.id.ll_your_possible_conditions)
    LinearLayout ll_possibleConditions;
    @BindView(R.id.ll_main)
    View ll_main;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView v_connectionInfo;

    private String mainBodyPart, subBodyPart;
    private Gender gender;

    private ArrayList<SymptomData> selectedSymptoms;
    private PostBoy postBoy;
    private FinishActivityReceiver finishActivityReceiver;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_symptoms_step_4;
    }

    @Override
    protected void init() {
        finishActivityReceiver = new FinishActivityReceiver(this);
        ButterKnife.bind(this);

        selectedSymptoms = this.getIntent().getParcelableArrayListExtra(CommonConstants.EXTRA_SYMPTOMS);
        mainBodyPart = this.getIntent().getStringExtra(CommonConstants.EXTRA_MAIN_BODY_PART);
        subBodyPart = this.getIntent().getStringExtra(CommonConstants.EXTRA_SUB_BODY_PART);
        gender = (Gender) this.getIntent().getSerializableExtra(CommonConstants.EXTRA_GENDER);

        postBoy = new PostBoy.Builder(this, RequestType.GET, WebUrls.BASE_URL + WebUrls.METHOD_CONDITIONS).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("چیک کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));

        postBoy.setGETValues(GetMaps.conditions(selectedSymptoms,mainBodyPart,subBodyPart,gender));
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.setListener(new ConditionListeners(this));
        postBoy.call();
        v_connectionInfo.setListener(this);
        this.getLocalBroadcastManager().registerReceiver(finishActivityReceiver,new IntentFilter(CommonConstants.ACTION_MOVE_TO_SYMPTOMS_SCREEN_ONE));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (finishActivityReceiver!=null)
            this.getLocalBroadcastManager().unregisterReceiver(finishActivityReceiver);
    }

    @Override
    public void onRetry() {
        postBoy.call();
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
            case R.id.action_done:
            {
                Intent i = new Intent(this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(i);
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class ConditionListeners implements PostBoyListener, View.OnClickListener {
        private ArrayList<ConditionData> list = new ArrayList<>();
        private LayoutInflater layoutInflater;

        private ConditionListeners(Context context) {
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            ll_main.setVisibility(View.INVISIBLE);
            v_connectionInfo.showLoader();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            ConditionsParser parser = new ConditionsParser(json);
            list.clear();
            if (parser.getResponseCode() == 200)
                list.addAll(parser.getConditions());
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            v_connectionInfo.hideAll();
            ConditionsParser parser = new ConditionsParser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    ll_main.setVisibility(View.VISIBLE);
                    ll_possibleConditions.removeAllViews();
                    ll_yourChoices.removeAllViews();
                    for (ConditionData data : list) {
                        View v = layoutInflater.inflate(R.layout.include_tv_condition, ll_possibleConditions, false);
                        ((TextView) v.findViewById(R.id.tv_title)).setText(data.getName());
                        v.setTag(data);
                        v.setOnClickListener(this);
                        ll_possibleConditions.addView(v);
                    }
                    for (SymptomData data : selectedSymptoms) {
                        View v = layoutInflater.inflate(R.layout.include_tv_condition, ll_possibleConditions, false);
                        TextView tv = v.findViewById(R.id.tv_title);
                        tv.setText(data.getName());
                        ll_yourChoices.addView(v);
                    }
                    break;
                case 204:
                    v_connectionInfo.showNothingFound();
                    break;
                case 401:
                    SessionUtils.logout(SymptomsStep4Activity.this, true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            v_connectionInfo.showInternetConnectionFail();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }



        @Override
        public void onClick(View view) {
            ConditionData data = (ConditionData) view.getTag();
            Intent i = new Intent(view.getContext(), SymptomDetailActivity.class);
            i.putExtra(CommonConstants.EXTRA_CONDITION_ID,data.getId());
            view.getContext().startActivity(i);
            if (view.getContext() instanceof Activity)
                ((Activity) view.getContext()).overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
        }
    }
}
