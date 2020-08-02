package com.grappetite.zoya.postboy;

public enum RequestType {
    GET("GET"),

    POST_FORM_DATA("POST"+ Common.FORM_DATA),
    POST_X_WWW_FORM_URLENCODED("POST"+ Common.X_WWW_FORM_URLENCODED),

    PUT_FORM_DATA("PUT"+ Common.FORM_DATA),
    PUT_X_WWW_FORM_URLENCODED("PUT"+ Common.X_WWW_FORM_URLENCODED),

    PATCH_FORM_DATA("PATCH"+ Common.FORM_DATA),
    PATCH_X_WWW_FORM_URLENCODED("PATCH"+ Common.X_WWW_FORM_URLENCODED),

    DELETE_FORM_DATA("DELETE"+ Common.FORM_DATA),
    DELETE_X_WWW_FORM_URLENCODED("DELETE"+ Common.X_WWW_FORM_URLENCODED);

    private String name;

    RequestType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
