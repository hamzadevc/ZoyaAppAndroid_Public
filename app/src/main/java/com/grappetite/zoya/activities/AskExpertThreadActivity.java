package com.grappetite.zoya.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.AskExpertThreadRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ExpertData;
import com.grappetite.zoya.dataclasses.MessageData;
import com.grappetite.zoya.helpers.ChatHelper;
import com.grappetite.zoya.views.ConnectionInfoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AskExpertThreadActivity extends CustomActivity implements ConnectionInfoView.Listener {

    private static final String TAG = "AskExpertThreadActivity";
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv_profile_pic)
    ImageView iv_profilePic;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_specialization)
    TextView tv_specialization;
    @BindView(R.id.et_msg)
    TextView et_msg;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView v_info;
//    @BindView(R.id.s_show_history)
//    SwitchCompat sShowHistory;
    private ExpertData expertData;
    private ArrayList<MessageData> list;
    private AskExpertThreadRecyclerAdapter adb;
    private LinearLayoutManager layoutManager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_ask_expert_thread;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        expertData = this.getIntent().getParcelableExtra(CommonConstants.DATA_EXPERT);
        list = new ArrayList<>();
        adb = new AskExpertThreadRecyclerAdapter();
        layoutManager = new LinearLayoutManager(this);
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("ASK AN EXPERT");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));

        adb.setList(list);
        layoutManager.setStackFromEnd(true);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adb);
        v_info.setListener(this);
    }

    @Override
    protected void populate() {
        Picasso.get()
                .load(expertData.getImageUrl())
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.profile_pic_placeholder_expert)
                .into(iv_profilePic);
        tv_name.setText(expertData.getName());
        tv_specialization.setText(expertData.getSpecialization());
        if (ChatHelper.isInitialized()) {
            ChatHelper.getInstance().setListener(new SocketListener());
            requestChatHistoryWithPossibleError();
        }

        if (LocalStoragePreferences.showChatHistoryDisclaimer())
            showDisclaimer();
    }


    private boolean isValid() {
        return !et_msg.getText().toString().isEmpty();
    }

    private void scrollToBottom() {
        if (list.size() > 2 && layoutManager.findLastCompletelyVisibleItemPosition() == list.size() - 2) {
            layoutManager.scrollToPosition(list.size() - 1);
        }
    }

    private Runnable showConnectionFailureRunnable = new Runnable() {
        @Override
        public void run() {
            v_info.showInternetConnectionFail();
        }
    };
    private void requestChatHistoryWithPossibleError() {
        ChatHelper.getInstance().requestChatHistory(expertData.getId());
        v_info.postDelayed(showConnectionFailureRunnable, 5000);
        v_info.showLoader();
    }

    @OnClick({R.id.iv_post_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_post_msg:
                if (isValid()) {
                    boolean isSent = ChatHelper.getInstance().sendMessage(expertData.getId(), et_msg.getText().toString());
                    if (isSent) {
                        list.add(new MessageData(expertData.getId(), et_msg.getText().toString()));
                        adb.notifyItemInserted(list.size() - 1);
                        et_msg.setText(null);
                        scrollToBottom();
                    } else
                        Toast.makeText(this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onRetry() {
        if (ChatHelper.isInitialized()) {
            if (ChatHelper.getInstance().isConnected())
                requestChatHistoryWithPossibleError();
            else
                ChatHelper.getInstance().reconnectToWebSocket();
            v_info.showLoader();
        }
    }

    @OnCheckedChanged({R.id.s_show_history})
    public void onCheckChanged(CompoundButton btn, boolean isChecked) {
        switch (btn.getId()) {
            case R.id.s_show_history:
                if (isChecked && LocalStoragePreferences.showChatHistoryDisclaimer()){
                }
                break;
        }
    }

    private void showDisclaimer() {
        View view = LayoutInflater.from(this).inflate(R.layout.include_chat_history_disclaimer,null,false);
        AlertDialog ad = new AlertDialog.Builder(this)
                .setView(view)
                .setOnCancelListener(dialog -> LocalStoragePreferences.setShowChatHistoryDisclaimer(false))
                .show();
        if (ad.getWindow()!=null)
            ad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        view.findViewById(R.id.btn_yes).setOnClickListener(v -> {
            ad.dismiss();
            LocalStoragePreferences.setShowChatHistoryDisclaimer(false);
        });
        view.findViewById(R.id.btn_no).setOnClickListener(v -> {
            LocalStoragePreferences.setShowChatHistoryDisclaimer(false);
//                        sShowHistory.setChecked(false);
            ad.dismiss();
        });
    }

    private class SocketListener implements ChatHelper.Listener {
        @Override
        public void onSocketConnected() {
            runOnUiThread(() -> {
                if (ChatHelper.isInitialized()) {
                    requestChatHistoryWithPossibleError();
                }
            });
        }

        @Override
        public void onSocketConnectionFail() {
            runOnUiThread(() -> {
                try {
                    v_info.showInternetConnectionFail();
                    list.clear();
                    adb.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "onSocketConnectionFail: " + e.toString());
                }
            });
        }

        @Override
        public void onHistoryReceived(ArrayList<MessageData> messageHistory) {
            list.clear();
            list.addAll(messageHistory);
            runOnUiThread(() -> {
                v_info.hideAll();
                v_info.removeCallbacks(showConnectionFailureRunnable);
                adb.notifyDataSetChanged();
            });
        }

        @Override
        public void onMessageReceived(MessageData message) {
            if (message.getExpertId() == expertData.getId()) {
                list.add(message);
                runOnUiThread(() -> {
                    adb.notifyItemInserted(list.size() - 1);
                    scrollToBottom();
                });
            }
        }
    }

}
