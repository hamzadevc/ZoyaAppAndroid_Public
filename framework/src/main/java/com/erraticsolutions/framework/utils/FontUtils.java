package com.erraticsolutions.framework.utils;


import android.content.Context;
import android.graphics.Typeface;

public class FontUtils{
    public static Typeface getFont(Context context,String fontAssetPath)
    {
        return Typeface.createFromAsset(context.getAssets(),fontAssetPath);
    }
}
