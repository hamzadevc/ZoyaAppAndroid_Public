package com.grappetite.zoya.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.enums.Gender;
import com.grappetite.zoya.enums.ViewType;
import com.grappetite.zoya.helpers.IndividualBodyPartHelper;
import com.grappetite.zoya.receivers.FinishActivityReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SymptomsStep2Activity extends CustomActivity {

    @BindView(R.id.btn_view_type)   Button btn_viewType;
    @BindView(R.id.btn_side_view)   Button btn_sideView;
    @BindView(R.id.btn_gender)      Button btn_gender;
    @BindView(R.id.fl_body_parts)   FrameLayout fl_bodyParts;

    private String bodyPart;
    private IndividualBodyPartHelper individualBodyPartHelper;
    private FinishActivityReceiver finishActivityReceiver;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_symptoms_step_2;
    }

    @Override
    protected void init() {
        finishActivityReceiver = new FinishActivityReceiver(this);
        ButterKnife.bind(this);
        this.bodyPart = this.getIntent().getStringExtra(CommonConstants.EXTRA_MAIN_BODY_PART);
        individualBodyPartHelper = new IndividualBodyPartHelper(this,fl_bodyParts);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("چیک کرو");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));
        this.getLocalBroadcastManager().registerReceiver(finishActivityReceiver,new IntentFilter(CommonConstants.ACTION_MOVE_TO_SYMPTOMS_SCREEN_ONE));
    }

    @Override
    protected void populate() {
        Gender gender = (Gender) this.getIntent().getSerializableExtra(CommonConstants.EXTRA_GENDER);
        ViewType viewType = (ViewType) this.getIntent().getSerializableExtra(CommonConstants.EXTRA_VIEW_TYPE);

        btn_gender.setTag(R.string.gender,gender);
        btn_gender.setText(gender.getTextForButton());

        btn_viewType.setTag(R.string.view_type,viewType);
        if (!bodyPart.contains("foot"))
            btn_viewType.setText(viewType.invert().toString());
        else
            btn_viewType.setText(viewType.invert().toStringByFoot());

        btn_sideView.setVisibility(bodyPart.contains("Torso")?View.VISIBLE:View.GONE);

        setBodyPart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter,R.anim.right_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (finishActivityReceiver!=null)
            this.getLocalBroadcastManager().unregisterReceiver(finishActivityReceiver);
    }

    private void setBodyPart() {
        Gender gender = (Gender) btn_gender.getTag(R.string.gender);
        ViewType viewType;
        if (btn_sideView.getTag(R.string.view_type)==null)
            viewType = (ViewType) btn_viewType.getTag(R.string.view_type);
        else
            viewType = (ViewType) btn_sideView.getTag(R.string.view_type);

        View view = individualBodyPartHelper.getBodyPartView(gender,viewType,bodyPart);

        fl_bodyParts.removeAllViews();
        if (view!=null)
            fl_bodyParts.addView(view);
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

    @OnClick({R.id.btn_gender, R.id.btn_view_type, R.id.btn_side_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gender:
            {
                Gender gender = ((Gender) btn_gender.getTag(R.string.gender)).invert();
                btn_gender.setText(gender.getTextForButton());
                btn_gender.setTag(R.string.gender,gender);
                setBodyPart();
            }
                break;
            case R.id.btn_view_type:
            {
                btn_sideView.setTag(R.string.view_type,null);
                ViewType viewType = ((ViewType) btn_viewType.getTag(R.string.view_type)).invert();
                if (!bodyPart.contains("foot"))
                    btn_viewType.setText(viewType.invert().toString());
                else
                    btn_viewType.setText(viewType.invert().toStringByFoot());

                btn_viewType.setTag(R.string.view_type,viewType);
                setBodyPart();
            }
                break;

            case R.id.btn_side_view:
            {
                ViewType viewType;
                if (btn_sideView.getTag(R.string.view_type)==null)
                    viewType = ViewType.LEFT;
                else
                    viewType = ((ViewType) btn_sideView.getTag(R.string.view_type)).invert();

                btn_sideView.setTag(R.string.view_type,viewType);
                setBodyPart();
            }
                break;
        }
    }
}
