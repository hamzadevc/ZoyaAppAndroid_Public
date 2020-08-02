package com.grappetite.zoya.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.SymptomsRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.SymptomData;
import com.grappetite.zoya.enums.Gender;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.receivers.FinishActivityReceiver;
import com.grappetite.zoya.restapis.parsers.SymptomsParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.GetMaps;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SymptomsStep3Activity extends CustomActivity implements ConnectionInfoView.Listener {

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
    @BindView(R.id.btn_next)
    Button btn_next;

    private ArrayList<SymptomData> list;
    private SymptomsRecyclerAdapter adb;
    private PostBoy postBoy;
    private String mainBodyPart, subBodyPart;
    private Gender gender;
    private FinishActivityReceiver finishActivityReceiver;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_symptoms_step_3;
    }

    @Override
    protected void init() {
        finishActivityReceiver = new FinishActivityReceiver(this);
        ButterKnife.bind(this);
        mainBodyPart = this.getIntent().getStringExtra(CommonConstants.EXTRA_MAIN_BODY_PART);
        subBodyPart = this.getIntent().getStringExtra(CommonConstants.EXTRA_SUB_BODY_PART);
        gender = (Gender) this.getIntent().getSerializableExtra(CommonConstants.EXTRA_GENDER);

        list = new ArrayList<>();
        adb = new SymptomsRecyclerAdapter();
        postBoy = new PostBoy.Builder(this, RequestType.GET, WebUrls.BASE_URL + WebUrls.METHOD_SYMPTOMS).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("چیک کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));

        adb.setList(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adb);
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.setGETValues(GetMaps.symptoms(mainBodyPart, subBodyPart, gender));
        postBoy.setListener(new SymptomsListener());
        postBoy.call();
        v_info.setListener(this);
        this.getLocalBroadcastManager().registerReceiver(finishActivityReceiver,new IntentFilter(CommonConstants.ACTION_MOVE_TO_SYMPTOMS_SCREEN_ONE));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (finishActivityReceiver!=null)
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
    @OnClick({R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next: {
                ArrayList<SymptomData> selectedSymptoms =  adb.getSelectedSymptoms();
                if (selectedSymptoms.size()>0) {
                    Intent i = new Intent(this, SymptomsStep4Activity.class);
                    i.putExtra(CommonConstants.EXTRA_SYMPTOMS,selectedSymptoms);
                    i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART,mainBodyPart);
                    i.putExtra(CommonConstants.EXTRA_SUB_BODY_PART,subBodyPart);
                    i.putExtra(CommonConstants.EXTRA_GENDER,gender);
                    this.startActivity(i);
                    this.overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
                }
                else
                    Toast.makeText(this, "Please select at least one symptom", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
    }

    @Override
    public void onRetry() {
        postBoy.call();
    }


    private class SymptomsListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            v_info.showLoader();
            ll_main.setVisibility(View.INVISIBLE);
            btn_next.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            SymptomsParser parser = new SymptomsParser(json);
            if (parser.getResponseCode() == 200) {
                list.addAll(parser.getSymptoms());
            }
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            v_info.hideAll();
            SymptomsParser parser = new SymptomsParser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    ll_main.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.VISIBLE);
                    adb.notifyDataSetChanged();
                    SymptomData.SymptomCoverData coverData = parser.getSymptomCoverData();
                    tv_title.setText(String.format(Locale.getDefault(), "%s Symptoms", coverData.getTitle()));
                    Picasso.get()
                            .load(R.drawable.temp_female_head_front)
                            .placeholder(R.color.blue_symotoms_cover)
                            .into(iv_cover);
                    break;
                case 204:
                    v_info.showNothingFound();
                    break;
                case 401:
                    SessionUtils.logout(SymptomsStep3Activity.this, true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            v_info.showInternetConnectionFail();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
