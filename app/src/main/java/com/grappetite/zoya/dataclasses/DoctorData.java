package com.grappetite.zoya.dataclasses;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class DoctorData implements Parcelable {
    @SerializedName("doctor_id")
    private long id;
    @SerializedName("doctor")
    private String name;
    @SerializedName("specialization")
    private String specialization;
    @SerializedName("place_name")
    private String placeName;
    @SerializedName("type")
    private String type;
    @SerializedName("place")
    private String address;
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

    public String getSpecialization() {
        return specialization;
    }

    public String getPlaceName() {
        return placeName;
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
        dest.writeString(this.specialization);
        dest.writeString(this.placeName);
        dest.writeString(this.type);
        dest.writeString(this.address);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.alternatePhoneNumber);
        dest.writeString(this.city);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.rating);
    }

    public DoctorData() {
    }

    protected DoctorData(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.specialization = in.readString();
        this.placeName = in.readString();
        this.type = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.alternatePhoneNumber = in.readString();
        this.city = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.rating = in.readString();
    }

    public static final Parcelable.Creator<DoctorData> CREATOR = new Parcelable.Creator<DoctorData>() {
        @Override
        public DoctorData createFromParcel(Parcel source) {
            return new DoctorData(source);
        }

        @Override
        public DoctorData[] newArray(int size) {
            return new DoctorData[size];
        }
    };
}
