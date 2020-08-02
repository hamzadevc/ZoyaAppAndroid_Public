package com.grappetite.zoya.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ConditionData;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.parsers.ConditionsParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.GetMaps;
import com.grappetite.zoya.utils.ActivityUtils;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SymptomDetailActivity extends CustomActivity {

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

    private PostBoy postBoy;
    private ConditionData conditionData;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_symptom_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        postBoy = new PostBoy.Builder(this, RequestType.GET, WebUrls.BASE_URL + WebUrls.METHOD_CONDITIONS).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("چیک کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        postBoy.setListener(new SymptomDetailListener());
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.setGETValues(GetMaps.condition(this.getIntent().getLongExtra(CommonConstants.EXTRA_CONDITION_ID, -1)));
        postBoy.call();

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
        if (conditionData != null) {
            tv_titleSymptoms.setVisibility(!TextUtils.isEmpty(conditionData.getSymptomsToString()) ? View.VISIBLE : View.GONE);
            tv_descSymptoms.setVisibility(!TextUtils.isEmpty(conditionData.getSymptomsToString()) ? View.VISIBLE : View.GONE);

            tv_titleSymptoms.setText(String.format(Locale.getDefault(), "%s Symptoms", conditionData.getName()));
            tv_descSymptoms.setText(conditionData.getSymptomsToString());

            if (!TextUtils.isEmpty(conditionData.getOverview())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("Overview");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getOverview());
                ll_content.addView(view);
            }
            if (!TextUtils.isEmpty(conditionData.getTreatment())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("Treatment");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getTreatment());
                ll_content.addView(view);
            }
            if (!TextUtils.isEmpty(conditionData.getHowCommon())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("How Common");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getHowCommon());
                ll_content.addView(view);
            }
            if (!TextUtils.isEmpty(conditionData.getQuestionsToAskDoctor())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("Question To Ask Doctor");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getQuestionsToAskDoctor());
                ll_content.addView(view);
            }
            if (!TextUtils.isEmpty(conditionData.getRiskFactor())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("Risk Factor");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getRiskFactor());
                ll_content.addView(view);
            }
            if (!TextUtils.isEmpty(conditionData.getSelfCare())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("Self Care");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getSelfCare());
                ll_content.addView(view);
            }
            if (!TextUtils.isEmpty(conditionData.getWhatToExpect())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("What To Expect");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getWhatToExpect());
                ll_content.addView(view);
            }
            if (!TextUtils.isEmpty(conditionData.getWhenToSeeDoctor())) {
                View view = this.getLayoutInflater().inflate(R.layout.include_condition_detail, ll_content, false);
                ((TextView) view.findViewById(R.id.tv_title)).setText("When To See Doctor");
                ((TextView) view.findViewById(R.id.tv_desc)).setText(conditionData.getWhenToSeeDoctor());
                ll_content.addView(view);
            }
            sv.setVisibility(View.VISIBLE);
        }
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

    @OnClick({R.id.tv_terms_and_conditions})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_ask_expert: {
//                Intent i = new Intent(this, AskExpertActivity.class);
//                this.startActivity(i);
//                this.overridePendingTransition(R.anim.right_enter, R.anim.scale_down);
//            }
//            break;
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

    private class SymptomDetailListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            v_connectionInfo.showLoader();
            sv.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            ConditionsParser parser = new ConditionsParser(json);
            if (parser.getResponseCode() == 200)
                conditionData = parser.getCondition();

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            v_connectionInfo.hideAll();
            ConditionsParser parser = new ConditionsParser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    repopulate();
                    break;
                case 204:
                    v_connectionInfo.showNothingFound();
                    break;
                case 401:
                    SessionUtils.logout(SymptomDetailActivity.this, true);
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
    }
}
