package com.grappetite.zoya.dataclasses;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedData implements Parcelable {

    @SerializedName("newsfeed_id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("video_url")
    private String youtubeEnglishVideoUrl;

    @SerializedName("video_urdu")
    private String youtubeUrduVideoUrl;

    @SerializedName("video_pashto")
    private String youtubePashtoVideoUrl;

    @SerializedName("video_punjabi")
    private String youtubePunjabiVideoUrl;

    @SerializedName("video_dari")
    private String youtubeDariVideoUrl;

    @SerializedName("video_saraiki")
    private String youtubeSaraikiVideoUrl;

    @SerializedName("audio")
    private String audioUrl;

    @SerializedName("author_name")
    private String authorName;

    @SerializedName("author_about")
    private String authorAbout;

    @SerializedName("author_image_url")
    private String authorImageUrl;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("has_liked_newsfeed")
    private int isLiked;

    @SerializedName("she_rocks")
    private int isSheRocks;

    @SerializedName("type")
    private String type;

    @SerializedName("tags")
    private List<TagData> tags;

    @SerializedName("multiple_video")
    private int hasMultipleVideos;

    @SerializedName("ad_url")
    private String adUrl;


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getYoutubeEnglishVideoUrl() {
        return youtubeEnglishVideoUrl;
    }

    public String getYoutubeUrduVideoUrl() {
        return youtubeUrduVideoUrl;
    }

    public String getYoutubePashtoVideoUrl() {
        return youtubePashtoVideoUrl;
    }

    public String getYoutubePunjabiVideoUrl() {
        return youtubePunjabiVideoUrl;
    }

    public String getYoutubeDariVideoUrl() {
        return youtubeDariVideoUrl;
    }

    public String getYoutubeSaraikiVideoUrl() {
        return youtubeSaraikiVideoUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorAbout() {
        return authorAbout;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public String getImageUrl() {
        return TextUtils.isEmpty(imageUrl)?null:imageUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public boolean isLiked() {
        return isLiked==1;
    }
    public boolean isSheRocks() {
        return isSheRocks==1;
    }
    public void setLike(boolean like) {
        isLiked = like?1:0;
    }

    public Type getType() {
        return Type.fromString(type);
    }

    public boolean getHasMultipleVideos() {
        return hasMultipleVideos == 1;
    }

    public List<TagData> getTags() {
        if (tags==null)
            tags = new ArrayList<>();
        return tags;
    }

    public void addTag(TagData td) {
        this.getTags().add(td);
    }

    public NewsFeedData() {
    }

    public enum Languages {
        Select_Languages,English, Urdu, Pashto, Punjabi, Dari, Saraiki;

        @Override
        public String toString() {
            if (this == Select_Languages)
                return "Select Languages";
            else
                return super.toString();
        }
    }

    public enum Type {
        VIDEO,ARTICAL,OTHER,AD;
        public static Type fromString(String type) {
            switch (type) {
                case "video":
                    return VIDEO;
                case "article":
                    return ARTICAL;
                case "ad":
                    return AD;
                default:
                    return OTHER;
            }
        }
    }


    public List<NewsFeedData.Languages> getLanguagesList() {
        ArrayList<Languages> list = new ArrayList<>();
        if (!TextUtils.isEmpty(this.getYoutubeEnglishVideoUrl()))
            list.add(NewsFeedData.Languages.English);
        if (!TextUtils.isEmpty(this.getYoutubeUrduVideoUrl()))
            list.add(NewsFeedData.Languages.Urdu);
        if (!TextUtils.isEmpty(this.getYoutubePashtoVideoUrl()))
            list.add(NewsFeedData.Languages.Pashto);
        if (!TextUtils.isEmpty(this.getYoutubePunjabiVideoUrl()))
            list.add(NewsFeedData.Languages.Punjabi);
        if (!TextUtils.isEmpty(this.getYoutubeDariVideoUrl()))
            list.add(NewsFeedData.Languages.Dari);
        if (!TextUtils.isEmpty(this.getYoutubeSaraikiVideoUrl()))
            list.add(NewsFeedData.Languages.Saraiki);
        if (list.size() > 0)
            list.add(0, NewsFeedData.Languages.Select_Languages);
        return list;
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
        dest.writeString(this.youtubeEnglishVideoUrl);
        dest.writeString(this.youtubeUrduVideoUrl);
        dest.writeString(this.youtubePashtoVideoUrl);
        dest.writeString(this.youtubePunjabiVideoUrl);
        dest.writeString(this.youtubeDariVideoUrl);
        dest.writeString(this.youtubeSaraikiVideoUrl);
        dest.writeString(this.audioUrl);
        dest.writeString(this.authorName);
        dest.writeString(this.authorAbout);
        dest.writeString(this.authorImageUrl);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.isLiked);
        dest.writeInt(this.isSheRocks);
        dest.writeString(this.type);
        dest.writeTypedList(this.tags);
        dest.writeInt(this.hasMultipleVideos);
        dest.writeString(this.adUrl);
    }

    protected NewsFeedData(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.youtubeEnglishVideoUrl = in.readString();
        this.youtubeUrduVideoUrl = in.readString();
        this.youtubePashtoVideoUrl = in.readString();
        this.youtubePunjabiVideoUrl = in.readString();
        this.youtubeDariVideoUrl = in.readString();
        this.youtubeSaraikiVideoUrl = in.readString();
        this.audioUrl = in.readString();
        this.authorName = in.readString();
        this.authorAbout = in.readString();
        this.authorImageUrl = in.readString();
        this.imageUrl = in.readString();
        this.isLiked = in.readInt();
        this.isSheRocks = in.readInt();
        this.type = in.readString();
        this.tags = in.createTypedArrayList(TagData.CREATOR);
        this.hasMultipleVideos = in.readInt();
        this.adUrl = in.readString();
    }

    public static final Creator<NewsFeedData> CREATOR = new Creator<NewsFeedData>() {
        @Override
        public NewsFeedData createFromParcel(Parcel source) {
            return new NewsFeedData(source);
        }

        @Override
        public NewsFeedData[] newArray(int size) {
            return new NewsFeedData[size];
        }
    };
}
