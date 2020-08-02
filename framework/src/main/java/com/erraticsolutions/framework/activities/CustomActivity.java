package com.erraticsolutions.framework.activities;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.erraticsolutions.framework.R;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.erraticsolutions.framework.utils.FontUtils;

import java.lang.reflect.Field;

public abstract class CustomActivity extends AppCompatActivity {
    private static final String TAG = "CustomActivity";
    private Handler handler = new Handler();
    private Toolbar toolbar;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setOrientation())
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setContentView(this.getContentViewId());

        View tb = this.findViewById(R.id.tb);
        if (tb != null && tb instanceof Toolbar) {
            toolbar = (Toolbar) tb;
            this.setSupportActionBar((Toolbar) tb);
            //noinspection ConstantConditions
            this.getSupportActionBar().setTitle(null);
        }

        init();
        assign(savedInstanceState);
        repopulate();
    }

    protected boolean setOrientation() {
        return true;
    }

    protected final void showBackButton(boolean show) {
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            this.getSupportActionBar().setDisplayShowHomeEnabled(show);
        }
    }

    protected abstract int getContentViewId();

    protected abstract void init();

    protected abstract void assign(@Nullable Bundle savedInstanceState);

    @WorkerThread
    protected void asyncPopulate() {
    }

    @UiThread
    protected void populate() {
    }

    protected void preload() {
    }

    public final void repopulate() {
        preload();
        new Thread(new Runnable() {
            @Override
            public void run() {
                asyncPopulate();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        populate();
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public final void setSupportActionbarTitle(CharSequence title) {
        if (this.getSupportActionBar() != null) {
            View tbTitle = toolbar.findViewById(R.id.tb_title);
            if (tbTitle != null && tbTitle instanceof TextView) {
                ((TextView) tbTitle).setText(title);
            } else
                this.getSupportActionBar().setTitle(title);
        }
    }


    public final void setSupportActionbarTitle(@StringRes int stringRes) {
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().setTitle(ContextCustom.getString(this, stringRes));
    }

    public final LocalBroadcastManager getLocalBroadcastManager() {
        return LocalBroadcastManager.getInstance(this);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public final void setSupportActionbarFont(String assetPath) {
        if (toolbar != null) {
            View tbTitle = toolbar.findViewById(R.id.tb_title);
            if (tbTitle != null && tbTitle instanceof TextView) {
                ((TextView) tbTitle).setTypeface(FontUtils.getFont(this,assetPath));
            } else
                try {
                    Field field = toolbar.getClass().getDeclaredField("mTitleTextView");
                    field.setAccessible(true);
                    TextView tv = (TextView) field.get(toolbar);
                    tv.setTypeface(FontUtils.getFont(this, assetPath));
                } catch (NoSuchFieldException e) {
                    Log.e(TAG, "setSupportActionbarFont: ", e);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "setSupportActionbarFont: ", e);
                }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivities(Intent[] intents) {
        super.startActivities(intents);
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
