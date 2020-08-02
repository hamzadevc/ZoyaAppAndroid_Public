package com.grappetite.zoya.dataclasses;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TagData implements Parcelable {
    @SerializedName("tag_id")
    private long id;
    @SerializedName("tag")
    private String title;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public TagData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
    }

    protected TagData(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
    }

    public static final Creator<TagData> CREATOR = new Creator<TagData>() {
        @Override
        public TagData createFromParcel(Parcel source) {
            return new TagData(source);
        }

        @Override
        public TagData[] newArray(int size) {
            return new TagData[size];
        }
    };
}
