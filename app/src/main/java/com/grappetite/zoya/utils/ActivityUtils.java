package com.grappetite.zoya.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ActivityUtils {
    public static void openTermsAndConditions(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://heyzoya.com/terms-conditions/"));
        context.startActivity(i);
    }
}
