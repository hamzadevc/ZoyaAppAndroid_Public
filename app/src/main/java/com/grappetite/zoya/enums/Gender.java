package com.grappetite.zoya.enums;


public enum  Gender {
    MALE,FEMALE;

    public static Gender fromString(String gender) {
        switch (gender.trim().toLowerCase())
        {
            case "male":
                return MALE;
            case "female":
                return FEMALE;
            default:
                return null;
        }
    }

    public Gender invert() {
        if (this.equals(MALE))
            return FEMALE;
        else
            return MALE;
    }

    public String getTextForButton() {
        switch (this) {
            case MALE:
                return "Female";
            case FEMALE:
                return "Male";
            default:
                return super.toString();
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case MALE:
                return "male";
            case FEMALE:
                return "female";
            default:
                return super.toString();
        }
    }
}
