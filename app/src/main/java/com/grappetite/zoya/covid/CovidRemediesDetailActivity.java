package com.grappetite.zoya.covid;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class CovidRemediesDetailActivity extends CustomActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rm1)
    TextView tv_rm1;

    @BindView(R.id.rm2)
    TextView tv_rm2;

    @BindView(R.id.rm3)
    TextView tv_rm3;

    @BindView(R.id.rm4)
    TextView tv_rm4;


    String rowID, rowValue;
    private DatabaseReference getUserDatabaseReference;

    String rm1, rm2, rm3, rm4;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_covid_remedy_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        rowID = getIntent().getStringExtra("row_id");
        rowValue = getIntent().getStringExtra("row_value");
        Log.v("Values", "" + rowID + ":" + rowValue);

        tv_title.setText(rowValue);

//        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("CovidRemedy").child("1").child("Headache");
//        getUserDatabaseReference.keepSynced(true);
//        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                try {
//                    rm1 = dataSnapshot.child("Remedy1").getValue().toString();
//                    rm2 = dataSnapshot.child("Remedy2").getValue().toString();
//                    rm3 = dataSnapshot.child("Remedy3").getValue().toString();
//                    rm4 = dataSnapshot.child("Remedy4").getValue().toString();
//                    Log.v("Detail", "" + rm1 + ":" + rm2 + ":" + rm3);
//
//                    tv_rm1.setText("Remedy 1: " + rm1);
//                    tv_rm2.setText("Remedy 2: " + rm2);
//                    tv_rm3.setText("Remedy 3: " + rm3);
//                    if (!rm4.isEmpty()) {
//                        tv_rm4.setText("Remedy 4: " + rm4);
//                    } else {
//                        tv_rm4.setVisibility(View.GONE);
//                    }
//
//
//                } catch (Exception e) {
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("CovidRemedy").child(rowID).child(rowValue);
        getUserDatabaseReference.keepSynced(true);
        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    rm1 = dataSnapshot.child("Remedy1").getValue().toString();
                    rm2 = dataSnapshot.child("Remedy2").getValue().toString();
                    rm3 = dataSnapshot.child("Remedy3").getValue().toString();
                    tv_rm1.setText("Remedy 1: " + rm1);
                    tv_rm2.setText("Remedy 2: " + rm2);
                    tv_rm3.setText("Remedy 3: " + rm3);
                    tv_rm4.setVisibility(View.GONE);
                    Log.v("Detail", "" + dataSnapshot.child("Remedy1").getValue().toString());

                } catch (Exception e) {

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
        this.setSupportActionbarTitle("دادی کے ٹوٹکے");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
    }

}
