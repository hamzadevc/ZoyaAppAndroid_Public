package me.crosswall.photo.pick.util;

import android.graphics.BitmapFactory;

/**
 * Created by yuweichen on 15/12/12.
 */
public class BitmapUtil {
    /**
     * 检查文件是否损坏
     * Check if the file is corrupted
     * @param filePath
     * @return
     */
    public static boolean checkImgCorrupted(String filePath) {
        BitmapFactory.Options options = null;
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);
        return options.mCancel || options.outWidth == -1
                || options.outHeight == -1;
    }
}
