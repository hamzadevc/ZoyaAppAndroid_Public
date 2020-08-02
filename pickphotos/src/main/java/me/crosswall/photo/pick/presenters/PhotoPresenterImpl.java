package me.crosswall.photo.pick.presenters;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import me.crosswall.photo.pick.PickConfig;
import me.crosswall.photo.pick.data.loader.MediaStoreHelper;
import me.crosswall.photo.pick.data.normal.PhotoObserver;
import me.crosswall.photo.pick.model.PhotoDirectory;
import me.crosswall.photo.pick.views.PhotoView;
import rx.Subscriber;


/**
 * Created by yuweichen on 15/12/9.
 */
public class PhotoPresenterImpl extends SafePresenter<PhotoView> {

    public AppCompatActivity context;
    public PhotoPresenterImpl(AppCompatActivity context, PhotoView photoView) {
        super(photoView);
        this.context = context;
    }

    @Override
    public void initialized(Object... objects) {

        boolean useCursorLoader = (boolean) objects[0];
        Bundle bundle = (Bundle) objects[1];

        if(useCursorLoader){
            getPhotosByLoader(bundle);
        }else{
            boolean checkImage = bundle.getBoolean(PickConfig.EXTRA_CHECK_IMAGE,PickConfig.DEFALUT_CHECK_IMAGE);
            boolean showGif = bundle.getBoolean(PickConfig.EXTRA_SHOW_GIF,PickConfig.DEFALUT_SHOW_GIF);
            PhotoObserver.getPhotos(context,checkImage,showGif).subscribe(safeSubscriber(albumSubcriber));
        }
    }

    public void getPhotosByLoader(Bundle args){
        MediaStoreHelper.getPhotoDirs(context, args, new MediaStoreHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<PhotoDirectory> directories) {
                PhotoView photoView = getView();
                if(photoView!=null){
                    photoView.showPhotosView(directories);
                }
            }
        });
    }


    Subscriber<List<PhotoDirectory>> albumSubcriber = new Subscriber<List<PhotoDirectory>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            PhotoView photoView = getView();
            if(photoView!=null){
                photoView.showException(e.getMessage());
            }
        }

        @Override
        public void onNext(List<PhotoDirectory> albumInfos) {
           PhotoView photoView = getView();
           if(photoView!=null){
               photoView.showPhotosView(albumInfos);
           }
        }
    };

}
