package com.grappetite.zoya.dataclasses;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConditionData implements Parcelable {
    @SerializedName("condition_id")
    private long id;
    @SerializedName("condition")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("what_to_expect")
    private String whatToExpect;

    @SerializedName("how_common")
    private String howCommon;

    @SerializedName("risk_factors")
    private String riskFactor;

    @SerializedName("treatment")
    private String treatment;

    @SerializedName("self_care")
    private String selfCare;

    @SerializedName("when_to_see_doctor")
    private String whenToSeeDoctor;

    @SerializedName("question_to_ask_doctor")
    private String questionsToAskDoctor;

    @SerializedName("symptoms")
    private List<String> symptoms;

    @SerializedName("bodyparts")
    private List<String> bodyParts;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getOverview() {
        return overview;
    }

    public String getWhatToExpect() {
        return whatToExpect;
    }

    public String getHowCommon() {
        return howCommon;
    }

    public String getRiskFactor() {
        return riskFactor;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getSelfCare() {
        return selfCare;
    }

    public String getWhenToSeeDoctor() {
        return whenToSeeDoctor;
    }

    public String getQuestionsToAskDoctor() {
        return questionsToAskDoctor;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public String getSymptomsToString() {
        String s = "";
        if (getSymptoms()!=null) {
            for (String symp : getSymptoms()) {
                if (TextUtils.isEmpty(s))
                    s=symp;
                else
                    s +="\n"+symp;
            }
        }

        return s;
    }

    public List<String> getBodyParts() {
        return bodyParts;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.overview);
        dest.writeString(this.whatToExpect);
        dest.writeString(this.howCommon);
        dest.writeString(this.riskFactor);
        dest.writeString(this.treatment);
        dest.writeString(this.selfCare);
        dest.writeString(this.whenToSeeDoctor);
        dest.writeString(this.questionsToAskDoctor);
        dest.writeStringList(this.symptoms);
        dest.writeStringList(this.bodyParts);
    }

    public ConditionData() {
    }

    protected ConditionData(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.overview = in.readString();
        this.whatToExpect = in.readString();
        this.howCommon = in.readString();
        this.riskFactor = in.readString();
        this.treatment = in.readString();
        this.selfCare = in.readString();
        this.whenToSeeDoctor = in.readString();
        this.questionsToAskDoctor = in.readString();
        this.symptoms = in.createStringArrayList();
        this.bodyParts = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ConditionData> CREATOR = new Parcelable.Creator<ConditionData>() {
        @Override
        public ConditionData createFromParcel(Parcel source) {
            return new ConditionData(source);
        }

        @Override
        public ConditionData[] newArray(int size) {
            return new ConditionData[size];
        }
    };
}

