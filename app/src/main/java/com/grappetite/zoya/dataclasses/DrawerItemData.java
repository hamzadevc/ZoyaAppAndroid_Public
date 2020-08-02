package com.grappetite.zoya.dataclasses;

import androidx.annotation.DrawableRes;

public class DrawerItemData {
    @DrawableRes
    private int iconRes;
    private String title;

    public DrawerItemData(String title, int iconRes) {
        this.iconRes = iconRes;
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getTitle() {
        return title;
    }
}
