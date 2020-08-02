package com.grappetite.zoya.dataclasses;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class CommentData {
    @SerializedName("comment_id")
    private long id;

    @SerializedName("message")
    private String comment;

    @SerializedName("fullname")
    private String commentedByName;

    @SerializedName("image_url")
    private String commentedByImageUrl;

    @SerializedName("has_flagged")
    private int isFlagged;


    public long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentedByName() {
        return commentedByName;
    }

    public String getCommentedByImageUrl() {
        return TextUtils.isEmpty(commentedByImageUrl)?null:commentedByImageUrl;
    }

    public boolean isFlagged() {
        return isFlagged==1;
    }

    public void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged?1:0;
    }
}
