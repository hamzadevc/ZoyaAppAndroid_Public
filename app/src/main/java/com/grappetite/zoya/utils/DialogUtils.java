package com.grappetite.zoya.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.grappetite.zoya.customclasses.LocalStoragePreferences;

import java.util.ArrayList;

public class DialogUtils {
    public static ProgressDialog getLoadingDialog(Context context, String msg) {
        ProgressDialog pd = new ProgressDialog(context);
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(false);
        pd.setMessage(msg);
        pd.setTitle(null);
        return pd;
    }

    public static void dismiss(ProgressDialog pd) {
        if (pd != null && pd.isShowing())
            pd.dismiss();
    }

    public static void showRationalDialog(final Context context, @StringRes int message) {
        new AlertDialog.Builder(context)
                .setTitle("Permission Required")
                .setMessage(message)
                .setPositiveButton("Settings", (dialog, which) -> {
                    Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    i.setData(uri);
                    if (context instanceof Activity)
                        ((Activity) context).startActivityForResult(i, 0);
                    else
                        context.startActivity(i);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static void showList(Context context, String title, String[] list, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context)
                .setItems(list, onClickListener)
                .setTitle(title)
                .show();
    }

    public static void showCities(Context context, ArrayList<String> list, TextView tv_city, CityDialogListener listener) {
        new AlertDialog.Builder(context)
                .setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list), (dialogInterface, position) -> {
                    String city = list.get(position);
                    LocalStoragePreferences.setSelectedCity(city);
                    if (tv_city != null) {
                        tv_city.setText(city);
                        tv_city.setTag(city);
                    }
                    if (listener != null)
                        listener.onCitySelected(city);
                })
                .show();
    }

    public interface CityDialogListener {
        void onCitySelected(String city);
    }
}
