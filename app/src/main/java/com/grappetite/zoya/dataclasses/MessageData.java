package com.grappetite.zoya.dataclasses;

import com.google.gson.annotations.SerializedName;

public class MessageData {
    @SerializedName("id")
    private long id;

    @SerializedName("message_to")
    private String messageTo;

    @SerializedName("user_id")
    private long userId;

    @SerializedName("expert_id")
    private long expertId;

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public long getExpertId() {
        return expertId;
    }

    public boolean isMessageSent() {
        return messageTo.equals("expert");
    }

    public MessageData(long expertId, String message) {
        this.expertId = expertId;
        this.message = message;
        messageTo = "expert";
    }
}
