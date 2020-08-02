package com.grappetite.zoya.dataclasses;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SymptomData implements Parcelable {
    @SerializedName("symptom_id")
    private long id;
    @SerializedName("name")
    private String name;

    private boolean isChecked;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public SymptomData setChecked(boolean checked) {
        isChecked = checked;
        return this;
    }




    public static class SymptomCoverData {
        @SerializedName("image_url")
        private String imageUrl;
        @SerializedName("name")
        private String title;

        public String getImageUrl() {
            return imageUrl;
        }

        public String getTitle() {
            return title;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    public SymptomData() {
    }

    protected SymptomData(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SymptomData> CREATOR = new Parcelable.Creator<SymptomData>() {
        @Override
        public SymptomData createFromParcel(Parcel source) {
            return new SymptomData(source);
        }

        @Override
        public SymptomData[] newArray(int size) {
            return new SymptomData[size];
        }
    };
}
