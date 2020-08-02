package me.crosswall.photo.pick.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;


public class PermissionUtil {

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 100;

    public static boolean checkPermission(Activity activity, String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int storagePermission = ActivityCompat.checkSelfPermission(activity, permission);
            return storagePermission == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    public static void showPermissionDialog(final Activity activity,String permission) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)) {

            ActivityCompat.requestPermissions(activity, new String[]{permission},
                    PermissionUtil.REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        ActivityCompat.requestPermissions(activity,new String[]{permission},
                PermissionUtil.REQUEST_CODE_ASK_PERMISSIONS);

    }


    public static void showAppSettingDialog(final Activity activity){
        new AlertDialog.Builder(activity)
                .setMessage("In order to run on Android M, your authorization is needed")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Ok",null)
                .show();
    }

}
