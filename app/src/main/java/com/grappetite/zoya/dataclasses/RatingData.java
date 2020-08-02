package com.grappetite.zoya.dataclasses;

import com.google.gson.annotations.SerializedName;

public class RatingData {

    @SerializedName("rating")
    private String rating;

    @SerializedName("totalRating")
    private String total;

    @SerializedName("star_1")
    private String star1;

    @SerializedName("star_2")
    private String star2;

    @SerializedName("star_3")
    private String star3;

    @SerializedName("star_4")
    private String star4;

    @SerializedName("star_5")
    private String star5;


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStar1() {
        return star1;
    }

    public void setStar1(String star1) {
        this.star1 = star1;
    }

    public String getStar2() {
        return star2;
    }

    public void setStar2(String star2) {
        this.star2 = star2;
    }

    public String getStar3() {
        return star3;
    }

    public void setStar3(String star3) {
        this.star3 = star3;
    }

    public String getStar4() {
        return star4;
    }

    public void setStar4(String star4) {
        this.star4 = star4;
    }

    public String getStar5() {
        return star5;
    }

    public void setStar5(String star5) {
        this.star5 = star5;
    }
}
