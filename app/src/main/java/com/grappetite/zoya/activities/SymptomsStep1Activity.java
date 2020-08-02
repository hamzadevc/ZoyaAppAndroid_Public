package com.grappetite.zoya.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.covid.CovidSymptomsActivity;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.enums.Gender;
import com.grappetite.zoya.enums.ViewType;
import com.grappetite.zoya.helpers.SymptomPicassoHelper;
import com.grappetite.zoya.receivers.FinishActivityReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SymptomsStep1Activity extends CustomActivity {

    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.iv_torso)
    ImageView iv_torso;
    @BindView(R.id.iv_left_arm)
    ImageView iv_leftArm;
    @BindView(R.id.iv_right_arm)
    ImageView iv_rightArm;
    @BindView(R.id.iv_right_hand)
    ImageView iv_rightHand;
    @BindView(R.id.iv_left_hand)
    ImageView iv_leftHand;
    @BindView(R.id.iv_pelvis)
    ImageView iv_pelvis;
    @BindView(R.id.iv_legs)
    ImageView iv_legs;
    @BindView(R.id.covid_banner)
    ImageView covidBtnClick;
    @BindView(R.id.iv_right_foot)
    ImageView iv_rightFoot;
    @BindView(R.id.iv_left_foot)
    ImageView iv_leftFoot;
    @BindView(R.id.btn_view_type)
    Button btn_viewType;
    @BindView(R.id.btn_gender)
    Button btn_gender;
    private SymptomPicassoHelper symptomPicassoHelper;
    private FinishActivityReceiver finishActivityReceiver;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_symptoms_step_1;
    }

    @Override
    protected void init() {
        finishActivityReceiver = new FinishActivityReceiver(this);
        ButterKnife.bind(this);
        symptomPicassoHelper = new SymptomPicassoHelper(this);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("چیک کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        this.getLocalBroadcastManager().registerReceiver(finishActivityReceiver, new IntentFilter(CommonConstants.ACTION_MOVE_TO_SYMPTOMS_SCREEN_ONE));
    }

    @Override
    protected void populate() {
        btn_gender.setTag(R.string.gender, Gender.FEMALE);
        btn_viewType.setTag(R.string.view_type, ViewType.FRONT);
        setBodyImages(Gender.FEMALE, ViewType.FRONT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (finishActivityReceiver != null)
            this.getLocalBroadcastManager().unregisterReceiver(finishActivityReceiver);
    }

    private void setBodyImages(Gender gender, ViewType viewType) {
        String path = "step_one/" + (gender == Gender.MALE ? "male/" : "female/");
        switch (viewType) {
            case FRONT:
                path += "front/";
                break;
            case BACK:
                path += "back/";
                break;
            case LEFT:
                path += "left/";
                break;
            case RIGHT:
                path += "right/";
                break;
        }
        boolean isFront = viewType == ViewType.FRONT;
        symptomPicassoHelper.load(iv_head, path + "head.png");
        symptomPicassoHelper.load(iv_torso, path + "torso.png");
        symptomPicassoHelper.load(iv_leftArm, path + (isFront ? "left_arm.png" : "right_arm.png"));
        symptomPicassoHelper.load(iv_rightArm, path + (isFront ? "right_arm.png" : "left_arm.png"));
        symptomPicassoHelper.load(iv_leftHand, path + (isFront ? "left_hand.png" : "right_hand.png"));
        symptomPicassoHelper.load(iv_rightHand, path + (isFront ? "right_hand.png" : "left_hand.png"));
        symptomPicassoHelper.load(iv_pelvis, path + "pelvis.png");
        symptomPicassoHelper.load(iv_legs, path + "legs.png");
        symptomPicassoHelper.load(iv_leftFoot, path + (isFront ? "left_foot.png" : "right_foot.png"));
        symptomPicassoHelper.load(iv_rightFoot, path + (isFront ? "right_foot.png" : "left_foot.png"));
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


    @OnClick({R.id.btn_gender, R.id.btn_view_type, R.id.covid_banner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gender: {
                Gender gender = ((Gender) btn_gender.getTag(R.string.gender)).invert();
                ViewType viewType = (ViewType) btn_viewType.getTag(R.string.view_type);
                btn_gender.setText(gender.getTextForButton());
                btn_gender.setTag(R.string.gender, gender);
                setBodyImages(gender, viewType);
            }
            break;
            case R.id.btn_view_type: {
                Gender gender = (Gender) btn_gender.getTag(R.string.gender);
                ViewType viewType = ((ViewType) btn_viewType.getTag(R.string.view_type)).invert();
                btn_viewType.setText(viewType.invert().toString());
                btn_viewType.setTag(R.string.view_type, viewType);
                setBodyImages(gender, viewType);
                Log.v("ClickedView", "" + "hello");
            }
            break;
            case R.id.covid_banner: {
                startActivity(new Intent(SymptomsStep1Activity.this, CovidSymptomsActivity.class));
                Log.v("ClickedView", "" + "hello");
            }
            break;
        }
    }


    @OnClick({
            R.id.iv_head,
            R.id.iv_torso,
            R.id.iv_left_arm,
            R.id.iv_right_arm,
            R.id.iv_right_hand,
            R.id.iv_left_hand,
            R.id.iv_pelvis,
            R.id.iv_legs,
            R.id.iv_right_foot,
            R.id.iv_left_foot,
    })
    public void onBodyPartsClicked(View view) {
        Gender gender = (Gender) btn_gender.getTag(R.string.gender);
        ViewType viewType = (ViewType) btn_viewType.getTag(R.string.view_type);
        Intent i = new Intent(this, SymptomsStep2Activity.class);
        i.putExtra(CommonConstants.EXTRA_GENDER, gender);
        i.putExtra(CommonConstants.EXTRA_VIEW_TYPE, viewType);

        boolean isFront = viewType == ViewType.FRONT;
        switch (view.getId()) {
            case R.id.iv_head:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, CommonConstants.VALUE_BODY_PART_HEAD);
                break;
            case R.id.iv_torso:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, CommonConstants.VALUE_BODY_PART_TORSO);
                break;
            case R.id.iv_left_arm:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, isFront ? CommonConstants.VALUE_BODY_PART_LEFT_ARM : CommonConstants.VALUE_BODY_PART_RIGHT_ARM);
                break;
            case R.id.iv_right_arm:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, isFront ? CommonConstants.VALUE_BODY_PART_RIGHT_ARM : CommonConstants.VALUE_BODY_PART_LEFT_ARM);
                break;
            case R.id.iv_right_hand:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, isFront ? CommonConstants.VALUE_BODY_PART_RIGHT_HAND : CommonConstants.VALUE_BODY_PART_LEFT_HAND);
                break;
            case R.id.iv_left_hand:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, isFront ? CommonConstants.VALUE_BODY_PART_LEFT_HAND : CommonConstants.VALUE_BODY_PART_RIGHT_HAND);
                break;
            case R.id.iv_pelvis:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, CommonConstants.VALUE_BODY_PART_PELVIS);
                break;
            case R.id.iv_legs:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, CommonConstants.VALUE_BODY_PART_LEGS);
                break;
            case R.id.iv_right_foot:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, isFront ? CommonConstants.VALUE_BODY_PART_RIGHT_FOOT : CommonConstants.VALUE_BODY_PART_LEFT_FOOT);
                break;
            case R.id.iv_left_foot:
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART, isFront ? CommonConstants.VALUE_BODY_PART_LEFT_FOOT : CommonConstants.VALUE_BODY_PART_RIGHT_FOOT);
                break;
        }

        this.startActivity(i);
        this.overridePendingTransition(R.anim.right_enter, R.anim.left_exit);
    }
}
