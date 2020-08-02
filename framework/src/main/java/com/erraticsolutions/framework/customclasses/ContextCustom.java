package com.erraticsolutions.framework.customclasses;

import android.content.Context;
import androidx.annotation.ArrayRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class ContextCustom extends ContextCompat {
    public static String getString(Context context, @StringRes int stringRes) {
        return context.getString(stringRes);
    }

    public static String[] getStringArray(Context context, @ArrayRes int id) {
        return context.getResources().getStringArray(id);
    }
}
