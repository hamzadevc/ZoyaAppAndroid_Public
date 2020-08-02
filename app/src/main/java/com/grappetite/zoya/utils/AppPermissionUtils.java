package com.grappetite.zoya.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.grappetite.zoya.R;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AppPermissionUtils {

    private static boolean hasPermission(Context context,String permission) {
        return !(context == null || permission == null) && ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestPermission(Activity activity, String permission,int requestCode) {
        if (activity==null || permission == null)
            return;
        ActivityCompat.requestPermissions(activity,new String[]{permission},requestCode);
    }

    private static void requestPermission(Fragment fragment, String permission, int requestCode) {
        if (fragment==null || permission == null)
            return;
        fragment.requestPermissions(new String[]{permission},requestCode);

    }

    private static void showRational(Activity activity, String permission, @StringRes int message) {
        if (activity==null || permission == null)
            return;
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,permission))
            DialogUtils.showRationalDialog(activity,message);
    }

    //----------------------------------------STORAGE-------------------------------------//
    public static boolean hasExternalStoragePermission(Context context) {
        return hasPermission(context, WRITE_EXTERNAL_STORAGE);
    }

    public static void requestExternalStoragePermission(Activity activity, int requestCode) {
        requestPermission(activity,WRITE_EXTERNAL_STORAGE, requestCode);
    }


    public static void requestExternalStoragePermission(Fragment fragment, int requestCode) {
        requestPermission(fragment,WRITE_EXTERNAL_STORAGE, requestCode);
    }

    public static void showExternalStorageRational(Activity activity) {
        showRational(activity,WRITE_EXTERNAL_STORAGE, R.string.permission_storage_rational);
    }


    //----------------------------------------CAMERA-------------------------------------//
    public static boolean hasCameraPermission(Context context) {
        return hasPermission(context,CAMERA);
    }

    public static void requestCameraPermission(Activity activity, int requestCode) {
        requestPermission(activity,CAMERA, requestCode);
    }

    public static void requestCameraPermission(Fragment fragment, int requestCode) {
        requestPermission(fragment,CAMERA, requestCode);
    }

    public static void showCameraRational(Activity activity) {
        showRational(activity,CAMERA, R.string.permission_camera_rational);
    }

    //----------------------------------------LOCATION-------------------------------------//
    public static boolean hasLocationPermission(Context context) {
        return hasPermission(context,ACCESS_FINE_LOCATION);
    }

    public static void requestLocationPermission(Activity activity, int requestCode) {
        requestPermission(activity,ACCESS_FINE_LOCATION, requestCode);
    }

    public static void requestLocationPermission(Fragment fragment, int requestCode) {
        requestPermission(fragment,ACCESS_FINE_LOCATION, requestCode);
    }

    public static void showLocationRational(Activity activity) {
        showRational(activity,ACCESS_FINE_LOCATION, R.string.permission_camera_rational);
    }
}


