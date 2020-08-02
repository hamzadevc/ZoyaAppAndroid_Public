package me.crosswall.photo.pick.data.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import me.crosswall.photo.pick.PickConfig;
import me.crosswall.photo.pick.data.Data;
import me.crosswall.photo.pick.model.PhotoDirectory;


/**
 * see https://github.com/donglua/PhotoPicker/blob/master/PhotoPicker/src/main/java/me/iwf/photopicker/utils/MediaStoreHelper.java
 */
public class MediaStoreHelper {


    public static void getPhotoDirs(AppCompatActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, args.getBoolean(PickConfig.EXTRA_CHECK_IMAGE), resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;
        private boolean checkImageStatus;

        public PhotoDirLoaderCallbacks(Context context, boolean checkImageStatus, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
            this.checkImageStatus = checkImageStatus;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context, args.getBoolean(PickConfig.EXTRA_SHOW_GIF, false));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;

            List<PhotoDirectory> directories = Data.getDataFromCursor(context,data,checkImageStatus);
            data.close();

            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }

}
