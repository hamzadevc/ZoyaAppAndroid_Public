package com.grappetite.zoya.dataclasses;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class ExpertData implements Parcelable {
    @SerializedName("expert_id")
    private long id;

    @SerializedName("fullname")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("is_online")
    private int isOnline;

    @SerializedName("specialization")
    private String specialization;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return TextUtils.isEmpty(imageUrl)?null:imageUrl;
    }

    public boolean getIsOnline() {
        return isOnline==1;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.isOnline);
        dest.writeString(this.specialization);
    }

    public ExpertData() {
    }

    public ExpertData(long id, String name, String imageUrl , String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.isOnline = isOnline;
        this.specialization = specialization;
    }

    protected ExpertData(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.email = in.readString();
        this.imageUrl = in.readString();
        this.isOnline = in.readInt();
        this.specialization = in.readString();
    }

    public static final Parcelable.Creator<ExpertData> CREATOR = new Parcelable.Creator<ExpertData>() {
        @Override
        public ExpertData createFromParcel(Parcel source) {
            return new ExpertData(source);
        }

        @Override
        public ExpertData[] newArray(int size) {
            return new ExpertData[size];
        }
    };
}
