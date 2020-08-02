package com.grappetite.zoya.postboy;


class ResponseData {
    private int code;
    private String string;

    ResponseData(int code, String string) {
        this.code = code;
        this.string = string;
    }

    int getCode() {
        return code;
    }

    String getString() {
        return string;
    }
}
