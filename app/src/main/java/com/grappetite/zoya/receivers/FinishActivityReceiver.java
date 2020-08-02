package com.grappetite.zoya.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.erraticsolutions.framework.activities.CustomActivity;

public class FinishActivityReceiver extends BroadcastReceiver{
    private CustomActivity customActivity;

    public FinishActivityReceiver(CustomActivity customActivity) {
        this.customActivity = customActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (customActivity!=null)
            customActivity.onBackPressed();
    }
}
