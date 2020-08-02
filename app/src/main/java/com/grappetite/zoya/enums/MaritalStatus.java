package com.grappetite.zoya.enums;


public enum MaritalStatus {
    SINGLE,MARRIED;

    public static MaritalStatus fromString(String maritalStatus) {
        switch (maritalStatus.trim().toLowerCase())
        {
            case "single":
                return SINGLE;
            case "married":
                return MARRIED;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case SINGLE:
                return "single";
            case MARRIED:
                return "marriend";
            default:
                return super.toString();
        }
    }
}
