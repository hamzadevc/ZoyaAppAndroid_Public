package com.grappetite.zoya.dataclasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PlacesTypeData implements Parcelable {
    @SerializedName("image_url")
    private String iconUrl;
    @SerializedName("name")
    private String title;

    public String getIconUrl() {
        return iconUrl;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iconUrl);
        dest.writeString(this.title);
    }

    public PlacesTypeData() {
    }

    protected PlacesTypeData(Parcel in) {
        this.iconUrl = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<PlacesTypeData> CREATOR = new Parcelable.Creator<PlacesTypeData>() {
        @Override
        public PlacesTypeData createFromParcel(Parcel source) {
            return new PlacesTypeData(source);
        }

        @Override
        public PlacesTypeData[] newArray(int size) {
            return new PlacesTypeData[size];
        }
    };
}
