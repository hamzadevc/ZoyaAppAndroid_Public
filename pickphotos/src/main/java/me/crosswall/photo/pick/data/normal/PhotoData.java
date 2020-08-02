package me.crosswall.photo.pick.data.normal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.List;

import me.crosswall.photo.pick.data.Data;
import me.crosswall.photo.pick.model.PhotoDirectory;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

/**
 * Created by yuweichen on 15/12/8.
 */
public class PhotoData {

    private static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };

    private final static String IMAGE_JPEG = "image/jpeg";
    private final static String IMAGE_PNG = "image/png";
    private final static String IMAGE_GIF = "image/gif";
    private final static String SORT = MediaStore.Images.Media.DATE_ADDED + " DESC";
    private final static Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


    public static List<PhotoDirectory> getPhotos(Context context, boolean checkImageStatus, boolean showGif) {

        ContentResolver resolver = context.getContentResolver();

        String selection = MIME_TYPE + "=? or " + MIME_TYPE + "=? " + (showGif ? ("or " + MIME_TYPE + "=?") : "");
        String[] selectionArgs;
        if (showGif) {
            selectionArgs = new String[]{IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF};
        } else {
            selectionArgs = new String[]{IMAGE_JPEG, IMAGE_PNG};
        }


        Cursor data = resolver.query(IMAGE_URI, IMAGE_PROJECTION,
                selection, selectionArgs, SORT);

        if (data == null) return null;

        List<PhotoDirectory> directories = Data.getDataFromCursor(context,data,checkImageStatus);
        data.close();

        return directories;
    }
}

