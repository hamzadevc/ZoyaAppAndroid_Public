package com.grappetite.zoya.dataclasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemediesData implements Parcelable {
    @SerializedName("remedy_id")
    private long id;
    @SerializedName("remedy")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("ingredients")
    private List<String> ingredients;


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeStringList(this.ingredients);
    }

    public RemediesData() {
    }

    protected RemediesData(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.ingredients = in.createStringArrayList();
    }

    public static final Parcelable.Creator<RemediesData> CREATOR = new Parcelable.Creator<RemediesData>() {
        @Override
        public RemediesData createFromParcel(Parcel source) {
            return new RemediesData(source);
        }

        @Override
        public RemediesData[] newArray(int size) {
            return new RemediesData[size];
        }
    };
}
