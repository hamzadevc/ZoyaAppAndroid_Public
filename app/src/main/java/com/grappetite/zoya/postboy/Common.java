package com.grappetite.zoya.postboy;


class Common {
    static final String FORM_DATA = "_FORM_DATA";
    static final String X_WWW_FORM_URLENCODED = "_X_WWW_FORM_URLENCODED";

    public static String getFirstPartRequestType(RequestType requestType) {
        String[] s = requestType.toString().split("_");
        if (s.length>0)
            return s[0];
        else
            return requestType.toString();
    }
}
