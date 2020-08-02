package com.grappetite.zoya.dataclasses;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class PlaceData implements Parcelable {
    @SerializedName("place_id")
    private long id;
    @SerializedName("place_name")
    private String name;
    @SerializedName("place")
    private String address;
    @SerializedName("type")
    private String type;
    @SerializedName("phone")
    private String phoneNumber;
    @SerializedName("phone2")
    private String alternatePhoneNumber;
    @SerializedName("city")
    private String city;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("rating")
    private String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAtLeastOnePhoneNumber() {
        return TextUtils.isEmpty(phoneNumber)?alternatePhoneNumber:phoneNumber;
    }

    public String getAlternatePhoneNumber() {
        return alternatePhoneNumber;
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.alternatePhoneNumber);
        dest.writeString(this.city);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.rating);
    }

    public PlaceData() {
    }

    protected PlaceData(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.type = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.alternatePhoneNumber = in.readString();
        this.city = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.rating = in.readString();
    }

    public static final Creator<PlaceData> CREATOR = new Creator<PlaceData>() {
        @Override
        public PlaceData createFromParcel(Parcel source) {
            return new PlaceData(source);
        }

        @Override
        public PlaceData[] newArray(int size) {
            return new PlaceData[size];
        }
    };
}
