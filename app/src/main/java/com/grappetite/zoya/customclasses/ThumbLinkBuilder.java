package com.grappetite.zoya.customclasses;


import com.grappetite.zoya.restapis.urls.WebUrls;

public class ThumbLinkBuilder {

    public static int WRAP_CONTENT = -1;
    private String link;
    private int width = WRAP_CONTENT, height = WRAP_CONTENT;

    public ThumbLinkBuilder(String link) {
        this.link = link;
    }

    public ThumbLinkBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    public ThumbLinkBuilder setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public ThumbLinkBuilder setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public String create()
    {
        return WebUrls.URL + "/thumb.php?src="
                + link
                + (width == WRAP_CONTENT ? "" : "&w=" + width)
                + (height == WRAP_CONTENT ? "" : "&h=" + height);
    }
}
