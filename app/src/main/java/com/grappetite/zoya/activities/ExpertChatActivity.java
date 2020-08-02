package com.grappetite.zoya.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.grappetite.zoya.R;
import com.grappetite.zoya.dataclasses.Expert;
import com.grappetite.zoya.viewholders.ExpertViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExpertChatActivity extends CustomActivity {

    @BindView(R.id.rv)  RecyclerView rv;

    Query query;
    DatabaseReference expDB;
    String uID;

    FirebaseRecyclerAdapter<Expert, ExpertViewHolder> adapter;
    FirebaseRecyclerOptions<Expert> options;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_expert_chat;
    }

    @Override
    protected void init() {

        ButterKnife.bind(this);
        expDB = FirebaseDatabase.getInstance().getReference().child("Experts");

    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {

        this.showBackButton(true);
        this.setSupportActionbarTitle("ASK AN EXPERT");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));

        rv.setLayoutManager( new LinearLayoutManager(this));

        expDB.keepSynced(true);
//        userDB.keepSynced(true);

        query = expDB;

        options = new FirebaseRecyclerOptions.Builder<Expert>()
                .setQuery(query,Expert.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Expert, ExpertViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ExpertViewHolder holder, int position, @NonNull Expert model) {
                holder.setExpName(model.getExpName());
                holder.setExpSpecial(model.getExpSpecial());
                holder.setIsOnline(model.getIsOnline());
                holder.setExpImage(model.getExpImage());

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ExpertChatThreadActivity.class);
                        intent.putExtra("expID", getRef(position).getKey());
                        intent.putExtra("expName", model.getExpName());
                        intent.putExtra("expSpecial", model.getExpSpecial());
                        intent.putExtra("expImage",model.getExpImage());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ExpertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ExpertViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate((R.layout.chat_display), parent, false));
            }
        };

        rv.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

//        userDB.child("isOnline").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
//        userDB.child("isOnline").setValue("false");
    }
}









//    Toolbar toolbar;
//    RecyclerView recyclerView;
//
//    Query query;
//    DatabaseReference db;
//    DatabaseReference userDB;
//    FirebaseAuth firebaseAuth;
//
//    FirebaseRecyclerAdapter<Expert, ExpertViewHolder> firebaseRecyclerAdapter;
//    FirebaseRecyclerOptions<Expert> options;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_expert_chat);
//
//        toolbar = findViewById(R.id.chatToolbar);
//        recyclerView = findViewById(R.id.chatsList);
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        setSupportActionBar(toolbar);
//        this.setSupportActionbarTitle("ASK AN EXPERT");
//        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
////        setSupportActionbarTitle("ASK AN EXPERT");
////        setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        db = FirebaseDatabase.getInstance().getReference();
//        db.keepSynced(true);
//
//        userDB = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
//        userDB.keepSynced(true);
//        userDB.child("isOnline").setValue("true");
//
//
//        query = db.child("Experts");
//
//        options = new FirebaseRecyclerOptions.Builder<Expert>()
//                .setQuery(query, Expert.class)
//                .build();
//
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Expert, ExpertViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ExpertViewHolder holder, final int position, @NonNull Expert model) {
//
//                String expName = model.getExpName();
//                String expSpecial = model.getExpSpecial();
//
//                holder.setExpName(model.getExpName());
//                holder.setExpSpecial(model.getExpSpecial());
//                holder.setIsOnline(model.getIsOnline());
//
//
////                holder.setUserThumbImage(model.getUserThumbImage());
//
//                holder.view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent intent = new Intent(getApplicationContext(), ExpertChatThreadActivity.class);
//                        intent.putExtra("expID", getRef(position).getKey());
//                        intent.putExtra("expName", expName);
//                        intent.putExtra("expSpecial", expSpecial);
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public ExpertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//                return new ExpertViewHolder(LayoutInflater.from(viewGroup.getContext())
//                        .inflate((R.layout.chat_display), viewGroup, false));
//            }
//        };
//
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.overridePendingTransition(R.anim.scale_up, R.anim.right_exit);
//    }
//
//    public final void setSupportActionbarFont(String assetPath) {
//        if (toolbar != null) {
//            View tbTitle = toolbar.findViewById(com.erraticsolutions.framework.R.id.tb_title);
//            if (tbTitle != null && tbTitle instanceof TextView) {
//                ((TextView) tbTitle).setTypeface(FontUtils.getFont(this, assetPath));
//            } else{
//                try {
//                    Field field = toolbar.getClass().getDeclaredField("mTitleTextView");
//                    field.setAccessible(true);
//                    TextView tv = (TextView) field.get(toolbar);
//                    tv.setTypeface(FontUtils.getFont(this, assetPath));
//                } catch (NoSuchFieldException e) {
//                    Log.e("TAG", "setSupportActionbarFont: ", e);
//                } catch (IllegalAccessException e) {
//                    Log.e("TAG", "setSupportActionbarFont: ", e);
//                }
//            }
//
//        }
//    }
//
//    public final void setSupportActionbarTitle(CharSequence title) {
//        if (this.getSupportActionBar() != null) {
//            View tbTitle = toolbar.findViewById(com.erraticsolutions.framework.R.id.tb_title);
//            if (tbTitle != null && tbTitle instanceof TextView) {
//                ((TextView) tbTitle).setText(title);
//            } else {
//                this.getSupportActionBar().setTitle(title);
//            }
//        }
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        userDB.child("isOnline").setValue("true");
//        firebaseRecyclerAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        userDB.child("isOnline").setValue("false");
//        firebaseRecyclerAdapter.stopListening();
//    }
//
//    public static class ExpertViewHolder extends RecyclerView.ViewHolder {
//
//        View view;
//
//        public ExpertViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            view = itemView;
//        }
//
//        public void setExpName(String expName) {
//            CustomTextView name = view.findViewById(R.id.expName);
//            name.setText(expName);
//        }
//
//        public void setExpSpecial(String expSpecial) {
//            CustomTextView status = view.findViewById(R.id.expSpecial);
//            status.setText(expSpecial);
//        }
//
//        public void setIsOnline(String isOnline) {
//            ImageView imageView = view.findViewById(R.id.isOnline);
//
//            if (isOnline.equals("true")) {
//                imageView.setImageResource(R.drawable.s_circle_green_online);
//            } else {
//                imageView.setImageResource(R.drawable.s_circle_gray_offline);
//            }
//        }
//
////        public void setUserImage(String userImage) {
////            CircleImageView image = view.findViewById(R.id.allUserProfileImage);
////            Picasso.get().load(userImage).into(image);
////
////        }
//
////        public void setUserThumbImage(final String userThumbImage) {
////            final CircleImageView image = view.findViewById(R.id.allUserProfileImage);
////
////            Picasso.get()
////                    .load(userThumbImage)
////                    .networkPolicy(NetworkPolicy.OFFLINE)
////                    .placeholder(R.drawable.default_profile)
////                    .resize(100, 100)
////                    .into(image, new Callback() {
////                        @Override
////                        public void onSuccess() {
////
////                        }
////
////                        @Override
////                        public void onError(Exception e) {
////
////                            Picasso.get()
////                                    .load(userThumbImage)
////                                    .placeholder(R.drawable.default_profile)
////                                    .resize(100, 100)
////                                    .into(image);
////
////                        }
////                    });
////
////        }
//    }
//
//}
