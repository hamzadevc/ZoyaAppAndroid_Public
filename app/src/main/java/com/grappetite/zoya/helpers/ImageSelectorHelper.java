package com.grappetite.zoya.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.BuildConfig;
import com.grappetite.zoya.R;
import com.grappetite.zoya.utils.AppPermissionUtils;
import com.grappetite.zoya.utils.DialogUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;


public class ImageSelectorHelper {

    private static final String TAG = "ImageSelectorHelper";
    private static final int PERMISSION_REQUEST_CODE_STORAGE = 2929;
    private static final int PERMISSION_REQUEST_CODE_CAMERA = 3029;
    private static final int ACTIVITY_RESULT_CODE_CAMERA = 3129;

    private Activity activity;
    private Fragment fragment;
    private File destDir;
    private Listener listener;
    private File cameraFile;

    public ImageSelectorHelper(Activity activity) {
        this.activity = activity;
        init();
    }

    public ImageSelectorHelper(Fragment fragment) {
        this.fragment = fragment;
        init();
    }

    private void init() {
        Context ctx = activity != null ? activity : fragment.getActivity();
        File folder = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (folder != null && !folder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            folder.mkdir();
        }
        cameraFile = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "img.jpg");
        destDir = new File(ctx.getFilesDir().getAbsoluteFile() + "/pictures");
        if (!destDir.exists())
            //noinspection ResultOfMethodCallIgnored
            destDir.mkdir();
    }


    private boolean showRemove;

    public void getImage(final boolean showRemove) {
        this.showRemove = showRemove;

        DialogUtils.showList((activity != null) ? activity : fragment.getActivity(), null, showRemove ? new String[]{"Capture Photo", "Gallery", "Remove", "Cancel"} : new String[]{"Capture Photo", "Gallery", "Cancel"}
                , (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openCamera();
                            break;

                        case 1:
                            openGallery();
                            break;

                        case 2:
                            if (showRemove)
                                removeImage();
                            break;
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (requestCode == ACTIVITY_RESULT_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                File outFile = File.createTempFile("camera", ".jpg", destDir);
                startCrop(UCrop.of(Uri.fromFile(cameraFile), Uri.fromFile(outFile)));
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        } else if (requestCode == PickConfig.PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> pick = i.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            if (pick.size() > 0) {
                File galleryFile = new File(pick.get(0));
                try {
                    File outFile = File.createTempFile("gallery", galleryFile.getName(), destDir);
                    startCrop(UCrop.of(Uri.fromFile(galleryFile), Uri.fromFile(outFile)));
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            if (cameraFile.exists())                //noinspection ResultOfMethodCallIgnored
                cameraFile.delete();
            if (listener != null)                     //noinspection ConstantConditions
                listener.onImageSelected(new File(UCrop.getOutput(i).getPath()));
        }
    }

    private void openCamera() {
        if (AppPermissionUtils.hasCameraPermission(activity != null ? activity : fragment.getActivity())) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                i.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity != null ? activity : fragment.getActivity(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        cameraFile));
            else
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));

            if (activity != null)
                activity.startActivityForResult(i, ACTIVITY_RESULT_CODE_CAMERA);
            else
                fragment.startActivityForResult(i, ACTIVITY_RESULT_CODE_CAMERA);
        } else {
            if (activity != null)
                AppPermissionUtils.requestCameraPermission(activity, PERMISSION_REQUEST_CODE_CAMERA);
            else
                AppPermissionUtils.requestCameraPermission(fragment, PERMISSION_REQUEST_CODE_CAMERA);
        }
    }

    private void openGallery() {
        if (AppPermissionUtils.hasExternalStoragePermission(activity!=null?activity:fragment.getActivity())) {
            PickConfig.Builder bld;
            if (activity != null)
                bld = new PickConfig.Builder(activity);
            else
                bld = new PickConfig.Builder(fragment);

            bld.pickMode(PickConfig.MODE_SINGLE_PICK)
                    .maxPickSize(30)
                    .spanCount(3)
                    .toolbarColor(R.color.colorPrimary)
                    .build();
        }
        else
        {
            if (activity!=null)
                AppPermissionUtils.requestExternalStoragePermission(activity,PERMISSION_REQUEST_CODE_STORAGE);
            else
                AppPermissionUtils.requestExternalStoragePermission(fragment,PERMISSION_REQUEST_CODE_STORAGE);
        }
    }

    public void removeImage() {

        new AlertDialog.Builder((activity != null) ? activity : fragment.getActivity())
                .setTitle("Remove Image")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null)
                        listener.onImageRemoved();
                })
                .setNegativeButton("No",null)
                .show();
    }

    private void startCrop(UCrop uCrop) {
        UCrop.Options opt = new UCrop.Options();
        opt.setFreeStyleCropEnabled(false);
        opt.withMaxResultSize(512, 512);
        opt.withAspectRatio(1,1);
        opt.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        opt.setToolbarColor(ContextCustom.getColor((activity != null) ? activity : fragment.getActivity(), R.color.colorPrimary));
        opt.setStatusBarColor(ContextCustom.getColor((activity != null) ? activity : fragment.getActivity(), R.color.colorPrimaryDark));
        opt.setActiveWidgetColor(ContextCustom.getColor((activity != null) ? activity : fragment.getActivity(), R.color.colorPrimary));
        uCrop.withOptions(opt);
        if (activity != null)
            uCrop.start(activity);
        else
            uCrop.start(fragment.getActivity(), fragment);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_STORAGE && AppPermissionUtils.hasExternalStoragePermission(activity != null ? activity : fragment.getActivity()))
            openGallery();
        else if (requestCode == PERMISSION_REQUEST_CODE_STORAGE)
            AppPermissionUtils.showExternalStorageRational(activity != null ? activity : fragment.getActivity());
        else if ((requestCode == PERMISSION_REQUEST_CODE_CAMERA) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
            openCamera();
        else if (requestCode == PERMISSION_REQUEST_CODE_CAMERA)
            AppPermissionUtils.showCameraRational(activity != null ? activity : fragment.getActivity());
    }


    public boolean deleteAllFiles() {
        return destDir.exists() && destDir.delete();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public interface Listener {
        void onImageSelected(File file);

        void onImageRemoved();
    }
}
