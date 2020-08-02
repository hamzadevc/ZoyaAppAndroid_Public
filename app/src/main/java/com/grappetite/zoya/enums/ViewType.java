package com.grappetite.zoya.enums;


public enum ViewType {
    FRONT,BACK,LEFT,RIGHT;


    @Override
    public String toString() {
        switch (this) {
            case FRONT:
                return "Front View";
            case BACK:
                return "Back View";
            case LEFT:
                return "Left View";
            case RIGHT:
                return "Right View";
            default:
                return super.toString();

        }
    }

    public String toStringByFoot() {
        if (this == BACK)
            return "Bottom View";
        else
            return toString();
    }


    public ViewType invert() {
        switch (this) {
            case FRONT:
                return BACK;
            case BACK:
                return FRONT;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return FRONT;
        }
    }
}
