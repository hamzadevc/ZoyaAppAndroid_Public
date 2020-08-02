package com.grappetite.zoya.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.dataclasses.RemediesData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemedyDetailActivity extends CustomActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.ll_ingredients)
    LinearLayout ll_ingredients;
    private RemediesData remediesData;

    private FirebaseAuth mAuth;
    private DatabaseReference getUserDatabaseReference;
    String user_id;
    boolean runOnce = true;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_remedy_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        remediesData = this.getIntent().getParcelableExtra(CommonConstants.DATA_REMEDIES);


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("دادی کے ٹوٹکے");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
    }

    @Override
    protected void populate() {



        if (runOnce) {
            Log.v("Remedies", "" + remediesData.getTitle());
            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Remedies")
                    .child(remediesData.getTitle());
            getUserDatabaseReference.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long value = mutableData.getValue(Long.class);
                    if (value == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue(value + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
//                    Log.d(TAG, "transaction:onComplete:" + databaseError);
                }
            });


            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(user_id)
                    .child("Remedies")
                    .child(remediesData.getTitle());
            getUserDatabaseReference.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long value = mutableData.getValue(Long.class);
                    if (value == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue(value + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
//                    Log.d(TAG, "transaction:onComplete:" + databaseError);
                }
            });


            runOnce = false;
        }

        tv_title.setText(remediesData.getTitle());
        tv_description.setText(remediesData.getDescription());
        ll_ingredients.removeAllViews();
        for (String ingredient : remediesData.getIngredients()) {
            View view = LayoutInflater.from(this).inflate(R.layout.include_remedi_ingredient, ll_ingredients, false);
            ((TextView) view.findViewById(R.id.tv_ingredient)).setText(ingredient);
            ll_ingredients.addView(view);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
    }
}
