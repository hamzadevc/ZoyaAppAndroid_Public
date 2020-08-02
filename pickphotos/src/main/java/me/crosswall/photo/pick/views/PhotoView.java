package me.crosswall.photo.pick.views;
import java.util.List;
import me.crosswall.photo.pick.model.PhotoDirectory;

/**
 * Created by yuweichen on 15/12/9.
 */
public interface PhotoView extends BaseView {
    /**
     * showPhotosView
     * @param photoDirectories
     */
    void showPhotosView(List<PhotoDirectory> photoDirectories);

    /**
     * show exception message
     */
    void showException(String msg);

}
