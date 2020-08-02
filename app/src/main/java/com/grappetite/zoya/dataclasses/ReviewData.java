package com.grappetite.zoya.dataclasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.grappetite.zoya.restapis.parsers.Parser;

public class ReviewData implements Parcelable {

    @SerializedName("rating_id")
    private long id;

    @SerializedName("place_id")
    private String placeID;

    @SerializedName("user_id")
    private String userID;

    @SerializedName("rating")
    private String rating;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("review")
    private String review;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.placeID);
        dest.writeString(this.userID);
        dest.writeString(this.rating);
        dest.writeString(this.userName);
        dest.writeString(this.review);
    }

    protected ReviewData(Parcel in) {
        this.id = in.readLong();
        this.placeID = in.readString();
        this.userID = in.readString();
        this.userName = in.readString();
        this.review = in.readString();
        this.rating = in.readString();

    }

    public static final Parcelable.Creator<ReviewData> CREATOR = new Parcelable.Creator<ReviewData>() {
        @Override
        public ReviewData createFromParcel(Parcel source) {
            return new ReviewData(source);
        }

        @Override
        public ReviewData[] newArray(int size) {
            return new ReviewData[size];
        }
    };
}

