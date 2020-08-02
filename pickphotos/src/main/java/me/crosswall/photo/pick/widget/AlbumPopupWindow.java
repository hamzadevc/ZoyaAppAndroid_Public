package me.crosswall.photo.pick.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListPopupWindow;
import java.util.List;

import me.crosswall.photo.pick.adapter.AlbumListAdapter;
import me.crosswall.photo.pick.model.PhotoDirectory;

/**
 * Created by yuweichen on 15/12/9.
 */
public class AlbumPopupWindow extends ListPopupWindow{

    private AlbumListAdapter albumAdapter;

    public AlbumPopupWindow(Context context) {
        super(context);
        initView(context);
    }

    public AlbumPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AlbumPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.albumAdapter = new AlbumListAdapter(context);
        setAdapter(albumAdapter);
        albumAdapter.changeSelect(0);
        int sWidthPix = context.getResources().getDisplayMetrics().widthPixels;
        setContentWidth(sWidthPix);
        setHeight(sWidthPix);
        setModal(true);
    }

    public void addData(List<PhotoDirectory> photoDirectories){
        this.albumAdapter.addData(photoDirectories);
    }

    public PhotoDirectory getItem(int position){
        return this.albumAdapter.getItem(position);
    }

    public void setSelectedIndex(int position){
        this.albumAdapter.changeSelect(position);
    }
}
