package com.grappetite.zoya.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.erraticsolutions.framework.views.CustomTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.grappetite.zoya.R;
import com.grappetite.zoya.activities.ExpertChatImageActivity;
import com.grappetite.zoya.dataclasses.Message;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private List<Message> userMessageList;
    private FirebaseAuth firebaseAuth;
    Context context;

    public MessageAdapter(List<Message> userMessageList, Context context) {
        this.userMessageList = userMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.message_layout, viewGroup, false);

        firebaseAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {

        String onlineUserId = firebaseAuth.getCurrentUser().getUid();

        Message messages = userMessageList.get(i);

        String fromUserId = messages.getFrom();
        String messageType = messages.getType();

        messageViewHolder.image.setVisibility(View.GONE);
        messageViewHolder.text.setVisibility(View.GONE);

        if (fromUserId.equals(onlineUserId)) {
            messageViewHolder.layout.setGravity(Gravity.LEFT);


            if (messageType.equals("text")) {
                messageViewHolder.text.setVisibility(View.VISIBLE);
                messageViewHolder.text.setBackgroundResource(R.drawable.s_round_10_solid_gray);
                messageViewHolder.text.setGravity(Gravity.START);
                messageViewHolder.text.setText(messages.getMessage());
            } else {
                messageViewHolder.image.setVisibility(View.VISIBLE);
                messageViewHolder.image.setBackgroundResource(R.drawable.s_round_10_solid_gray);
                Picasso.get()
                        .load(messages.getMessage())
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(messageViewHolder.image);

                messageViewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ExpertChatImageActivity.class);
                        intent.putExtra("URI", messages.getMessage());
                        context.startActivity(intent);
                    }
                });

            }


        } else {
            messageViewHolder.layout.setGravity(Gravity.RIGHT);

            if (messageType.equals("text")) {
                messageViewHolder.text.setVisibility(View.VISIBLE);
                messageViewHolder.text.setBackgroundResource(R.drawable.s_round_10_solid_purple_white);
                messageViewHolder.text.setGravity(Gravity.END);
                messageViewHolder.text.setText(messages.getMessage());
            } else {
                messageViewHolder.image.setVisibility(View.VISIBLE);
                messageViewHolder.image.setBackgroundResource(R.drawable.s_round_10_solid_purple_white);
                Picasso.get()
                        .load(messages.getMessage())
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(messageViewHolder.image);

                messageViewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ExpertChatImageActivity.class);
                        intent.putExtra("URI", messages.getMessage());
                        context.startActivity(intent);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public CustomTextView text;
        public RelativeLayout layout;
        public ImageView image;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.tv_message);
            image = itemView.findViewById(R.id.iv_message_image);
            layout = itemView.findViewById(R.id.layout_message);
        }

    }
}

