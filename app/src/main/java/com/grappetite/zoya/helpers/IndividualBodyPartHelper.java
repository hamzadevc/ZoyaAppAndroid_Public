package com.grappetite.zoya.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.SymptomsStep3Activity;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.enums.Gender;
import com.grappetite.zoya.enums.ViewType;
import com.grappetite.zoya.views.HotspotView;

public class IndividualBodyPartHelper {
    private Context context;
    private ViewGroup parent;
    private SymptomPicassoHelper symptomPicassoHelper;

    public IndividualBodyPartHelper(Context context, ViewGroup parent) {
        this.context = context;
        this.parent = parent;
        symptomPicassoHelper = new SymptomPicassoHelper(context);
    }

    public View getBodyPartView(Gender gender, ViewType viewType, String bodyPart) {
        boolean isMale = gender == Gender.MALE;
        String path= "step_two/"+(isMale?"male/":"female/");
        switch (viewType) {
            case FRONT:
                path+="front/";
                break;
            case BACK:
                path+="back/";
                break;
            case LEFT:
                path+="left/";
                break;
            case RIGHT:
                path+="right/";
                break;
        }
        View view=null;
        if (viewType==ViewType.FRONT) {
            //-----------------------------FRONT VIEW---------------------------------------//
            switch (bodyPart) {
                case CommonConstants.VALUE_BODY_PART_HEAD:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_head_front:R.layout.view_body_part_female_head_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"head.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEFT_ARM:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_left_arm_front:R.layout.view_body_part_female_left_arm_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"left_arm.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_RIGHT_ARM:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_right_arm_front:R.layout.view_body_part_female_right_arm_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"right_arm.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEFT_HAND:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_left_hand_front:R.layout.view_body_part_female_left_hand_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"left_hand.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_RIGHT_HAND:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_right_hand_front:R.layout.view_body_part_female_right_hand_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"right_hand.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_TORSO:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_torso_front:R.layout.view_body_part_female_torso_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"torso.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_PELVIS:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_pelvis_front:R.layout.view_body_part_female_pelvis_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"pelvis.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEGS:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_legs_front:R.layout.view_body_part_female_legs_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"legs.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEFT_FOOT:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_left_foot_front:R.layout.view_body_part_female_left_foot_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"left_foot.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_RIGHT_FOOT:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_right_foot_front:R.layout.view_body_part_female_right_foot_front,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"right_foot.png");
                    break;
            }
        }
        else if (viewType==ViewType.BACK){
            //-----------------------------BACK VIEW---------------------------------------//
            switch (bodyPart) {
                case CommonConstants.VALUE_BODY_PART_HEAD:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_head_back:R.layout.view_body_part_female_head_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"head.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEFT_ARM:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_left_arm_back:R.layout.view_body_part_female_left_arm_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"left_arm.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_RIGHT_ARM:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_right_arm_back:R.layout.view_body_part_female_right_arm_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"right_arm.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEFT_HAND:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_hand_back:R.layout.view_body_part_hand_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"left_hand.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_RIGHT_HAND:
                    view = LayoutInflater.from(context).inflate(R.layout.view_body_part_hand_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"right_hand.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_TORSO:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_torso_back:R.layout.view_body_part_female_torso_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"torso.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_PELVIS:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_pelvis_back:R.layout.view_body_part_female_pelvis_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"pelvis.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEGS:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_legs_back:R.layout.view_body_part_female_legs_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"legs.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_LEFT_FOOT:
                    view = LayoutInflater.from(context).inflate(R.layout.view_body_part_male_left_foot_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"left_foot.png");
                    break;
                case CommonConstants.VALUE_BODY_PART_RIGHT_FOOT:
                    view = LayoutInflater.from(context).inflate(R.layout.view_body_part_male_left_foot_back,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"right_foot.png");
                    break;
            }
        }
        else if(viewType==ViewType.RIGHT) {
            switch (bodyPart) {
                case CommonConstants.VALUE_BODY_PART_TORSO:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_torso_right:R.layout.view_body_part_male_torso_right,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"torso.png");
                    break;
            }
        }
        else if(viewType==ViewType.LEFT) {
            switch (bodyPart) {
                case CommonConstants.VALUE_BODY_PART_TORSO:
                    view = LayoutInflater.from(context).inflate(isMale?R.layout.view_body_part_male_torso_right:R.layout.view_body_part_male_torso_right,parent,false);
                    symptomPicassoHelper.load(view.findViewById(R.id.iv_body_part),path+"torso.png");
                    break;
            }
        }
        if (view!=null)
        {
            ViewGroup rl = (ViewGroup) ((ViewGroup)view).getChildAt(0);
            for (int i =0 ; i<rl.getChildCount() ; i++) {
                rl.getChildAt(i).setOnClickListener(new IndividualBodyPartClickListener(gender));
            }
        }
        return view;
    }


    private class IndividualBodyPartClickListener  implements View.OnClickListener{

        private Gender gender;

        private IndividualBodyPartClickListener(Gender gender) {
            this.gender = gender;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof HotspotView) {
                HotspotView rv = (HotspotView) view;
                Intent i = new Intent(view.getContext(), SymptomsStep3Activity.class);
                String mainBodyPart = rv.getMainBodyPartName();
                String subBodyPart = rv.getSubBodyPartName();
                i.putExtra(CommonConstants.EXTRA_MAIN_BODY_PART,mainBodyPart);
                i.putExtra(CommonConstants.EXTRA_SUB_BODY_PART,subBodyPart);
                i.putExtra(CommonConstants.EXTRA_GENDER,gender);
                view.getContext().startActivity(i);
                if (view.getContext() instanceof Activity)
                    ((Activity)view.getContext()).overridePendingTransition(R.anim.right_enter,R.anim.left_exit);
            }

        }
    }
}
