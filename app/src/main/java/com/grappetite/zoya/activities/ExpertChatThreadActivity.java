package com.grappetite.zoya.activities;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.MessageAdapter;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.Message;
import com.grappetite.zoya.restapis.firebasemaps.FirebaseMaps;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ExpertChatThreadActivity extends CustomActivity implements View.OnClickListener, ConnectionInfoView.Listener {

    private static final int GALLERY_PICK = 1;
    private static final String TYPE_MESSAGE = "TYPE_MESSAGE";
    private static final String TYPE_IMAGE = "TYPE_MESSAGE";
    private static final String SENDER = "USER";

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_name)
    TextView expName;
    @BindView(R.id.tv_specialization)
    TextView expSpecial;
    @BindView(R.id.iv_profile_pic)
    ImageView expImage;
    @BindView(R.id.et_msg)
    EditText et_msg;
    @BindView(R.id.iv_post_image)
    ImageView iv_post_image;
    @BindView(R.id.s_check_anonymous)
    SwitchCompat switchCompat;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView v_info;


    String uID;
    String expID;
    FirebaseAuth auth;
    DatabaseReference db;
    DatabaseReference n_db;

    List<Message> messageList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    MessageAdapter adapter;

    StorageReference storeRef;
    String senderRef, receiverRef;


    @Override
    protected int getContentViewId() {
        return (R.layout.activity_expert_chat_thread);
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        uID = auth.getCurrentUser().getUid();
        n_db = FirebaseDatabase.getInstance().getReference().child("Notifications");
        db = FirebaseDatabase.getInstance().getReference();
        storeRef = FirebaseStorage.getInstance().getReference().child("MessageImages");
        expName.setText(getIntent().getStringExtra("expName"));
        expSpecial.setText(getIntent().getStringExtra("expSpecial"));
        expID = getIntent().getStringExtra("expID");
        senderRef = "Messages/" + uID + "/" + expID;
        receiverRef = "Messages/" + expID + "/" + uID;

        switchCompat.setChecked(LocalStoragePreferences.getIsAnonymous());

//        if(getIntent().getStringExtra("expImage").equals("")){
//            expImage.setImageResource(R.drawable.profile_pic_placeholder);
//        }else {
//            Picasso.get()
//                    .load(getIntent().getStringExtra("expImage"))
//                    .transform(new CropCircleTransformation())
//                    .resize(100,100)
//                    .placeholder(R.drawable.profile_pic_placeholder)
//                    .into(expImage);
//        }

    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {

        this.showBackButton(true);
        this.setSupportActionbarTitle("ASK AN EXPERT");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));


        adapter = new MessageAdapter(messageList, this);
        v_info.setListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
        rv.scrollToPosition(adapter.getItemCount() - 1);

        FetchMessages();
    }

    private void FetchMessages() {
        db.child("Messages/"+uID+"/"+expID).addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Message messages = dataSnapshot.getValue(Message.class);
                        messageList.add(messages);
                        adapter.notifyDataSetChanged();
                        rv.scrollToPosition(adapter.getItemCount()-1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
    }

    @OnClick({R.id.iv_post_msg,R.id.iv_post_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_post_msg:
                SendMessage(et_msg.getText().toString());
                et_msg.getText().clear();
                break;
            case R.id.iv_post_image:
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select Image"), GALLERY_PICK);
                break;
        }
    }

    @OnCheckedChanged({R.id.s_check_anonymous})
    public void onCheckChanged(CompoundButton btn, boolean isChecked) {
        switch (btn.getId()) {
            case R.id.s_check_anonymous:
                if (isChecked){
                    showDisclaimer();
                    LocalStoragePreferences.setIsAnonymous(true);
                    db.child("Users/"+uID).child("isAnonymous").setValue("true");
                }else {
                    db.child("Users/"+uID).child("isAnonymous").setValue("false");
                    LocalStoragePreferences.setIsAnonymous(false);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {


            v_info.showLoader();
            et_msg.setEnabled(false);
            iv_post_image.setEnabled(false);


            Uri uri = data.getData();

            final String key = db.child("Messages/"+uID+"/"+expID).push().getKey();

            int imageID = (int) System.currentTimeMillis();
            storeRef = storeRef.child(imageID + ".jpg");

            UploadTask uploadTask = storeRef.putFile(uri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storeRef.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        SendMessageImage(task.getResult().toString(),key);
                        v_info.hideAll();
                        et_msg.setEnabled(true);
                        iv_post_image.setEnabled(true);

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        v_info.hideAll();
                        et_msg.setEnabled(true);
                        iv_post_image.setEnabled(true);

                    }

                }
            });

        }


    }

    private void SendMessageImage(String message, String key) {

        String sendRef = senderRef + "/" +key;
        String recieveRef = receiverRef + "/" +key;

        Map<String, Object> details = FirebaseMaps.SendImageMessage(message,uID,sendRef,recieveRef);


        db.updateChildren(details).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                Map<String, Object> params = FirebaseMaps.Notification(uID,TYPE_IMAGE,SENDER);

                n_db.child(expID).push().setValue(params)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    rv.scrollToPosition(adapter.getItemCount() - 1);

                                }
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void SendMessage(String s) {
        if (!TextUtils.isEmpty(s)) {

            final String key = db.child("Messages/"+uID+"/"+expID).push().getKey();
            String sendRef = senderRef + "/" +key;
            String recieveRef = receiverRef + "/" +key;

            Map<String, Object> details = FirebaseMaps.SendTextMessage(et_msg.getText().toString(),uID,sendRef,recieveRef);

            db.updateChildren(details).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    Map<String, Object> params = FirebaseMaps.Notification(uID,TYPE_MESSAGE,SENDER);
                    n_db.child(expID).push().setValue(params)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        rv.scrollToPosition(adapter.getItemCount()-1);
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void showDisclaimer() {
        View view = LayoutInflater.from(this).inflate(R.layout.include_chat_history_disclaimer,null,false);
        AlertDialog ad = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        if (ad.getWindow()!=null)
            ad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    @Override
    public void onRetry() {

    }
}


//public class ExpertChatThreadActivity extends AppCompatActivity {
//
//    private static final int GALLERY_PICK = 1 ;
//    Toolbar chatThreadToolbar;
//    ImageView expProfilePic;
//    CustomTextView expName,expSpecial;
//    RecyclerView rv;
//
//    String expID,userID;
//    FirebaseAuth firebaseAuth;
//    DatabaseReference db;
//    DatabaseReference notificationDb;
//
//    CustomEditText messageText;
//    ImageView btn_send_text;
//    ImageView btn_send_image;
//
//    List<Message> messageList = new ArrayList<>();
//    LinearLayoutManager linearLayoutManager;
//    MessageAdapter messageAdapter;
//
//    StorageReference storageReference;
//    String senderRef, receiverRef;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_expert_chat_thread);
//
//
//
//        chatThreadToolbar = findViewById(R.id.chatThreadToolbar);
//        expProfilePic = findViewById(R.id.iv_profile_pic);
//        expName = findViewById(R.id.tv_name);
//        expSpecial = findViewById(R.id.tv_specialization);
//        rv = findViewById(R.id.rv);
//        messageText = findViewById(R.id.et_msg);
//        btn_send_text = findViewById(R.id.iv_post_msg);
//        btn_send_image = findViewById(R.id.iv_post_image);
//
//        expID = getIntent().getStringExtra("expID");
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        userID = firebaseAuth.getCurrentUser().getUid();
//        db = FirebaseDatabase.getInstance().getReference();
//        notificationDb = FirebaseDatabase.getInstance().getReference().child("Notifications");
//        storageReference = FirebaseStorage.getInstance().getReference().child("MessageImages");
//
//
//
//        messageAdapter = new MessageAdapter(messageList,this);
//        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(linearLayoutManager);
//        rv.setAdapter(messageAdapter);
//
//        rv.scrollToPosition(messageAdapter.getItemCount()-1);
//
//
//        setSupportActionBar(chatThreadToolbar);
//        getSupportActionBar().setTitle("Ask an Expert");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        expName.setText(getIntent().getStringExtra("expName"));
//        expSpecial.setText(getIntent().getStringExtra("expSpecial"));
//
//        senderRef = "Messages/" + userID + "/" + expID;
//        receiverRef = "Messages/" + expID + "/" + userID;
//
//        FetchMessages();
//
//        btn_send_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String message = messageText.getText().toString();
//                SendMessage(message);
//                messageText.setText("");
//            }
//        });
//
//
//        btn_send_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent();
//                in.setType("image/*");
//                in.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(in, "Select Image"), GALLERY_PICK);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
//
//            Uri uri = data.getData();
//
//            final String key = db.child("Messages").child(userID).child(expID).push().getKey();
//
//            int imageID = (int) System.currentTimeMillis();
//            storageReference = storageReference.child(imageID + ".jpg");
//
//            UploadTask uploadTask = storageReference.putFile(uri);
//            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//                    return storageReference.getDownloadUrl();
//                }
//
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//
//                        SendMessageImage(task.getResult().toString(),key);
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
//                    }
//
//                }
//            });
//
//        }
//
//
//    }
//
//    private void SendMessageImage(String message, String key){
//        Map messageTextBody = new HashMap();
//        messageTextBody.put("message", message);
//        messageTextBody.put("seen", false);
//        messageTextBody.put("type", "image");
//        messageTextBody.put("time", ServerValue.TIMESTAMP);
//        messageTextBody.put("from", expID);
//
//        Map messageBodyDetails = new HashMap();
//
//        messageBodyDetails.put(senderRef + "/" + key, messageTextBody);
//        messageBodyDetails.put(receiverRef + "/" + key, messageTextBody);
//
//        db.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//
//                if (task.isSuccessful()) {
//
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put("from", userID);
//                    params.put("type", "TYPE_MESSAGE");
//                    params.put("sender", "EXPERT");
//
//                    notificationDb.child(expID).push().setValue(params)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        rv.scrollToPosition(messageAdapter.getItemCount() - 1);
//
//                                    }
//                                }
//                            });
//                } else {
//                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.overridePendingTransition(R.anim.scale_up,R.anim.right_exit);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home){
//            Intent intent = new Intent(this,ExpertChatActivity.class);
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//

//
//
//
//    private void FetchMessages() {
//        db.child("Messages")
//                .child(userID).child(expID)
//                .addChildEventListener(new ChildEventListener() {
//
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        Message messages = dataSnapshot.getValue(Message.class);
//                        messageList.add(messages);
//                        messageAdapter.notifyDataSetChanged();
//                        rv.scrollToPosition(messageAdapter.getItemCount()-1);
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//    }
//}
