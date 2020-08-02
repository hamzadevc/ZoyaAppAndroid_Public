package me.crosswall.photo.pick;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.crosswall.photo.pick.adapter.ThumbPhotoAdapter;
import me.crosswall.photo.pick.model.PhotoDirectory;
import me.crosswall.photo.pick.presenters.PhotoPresenterImpl;
import me.crosswall.photo.pick.util.PermissionUtil;
import me.crosswall.photo.pick.util.UIUtil;
import me.crosswall.photo.pick.views.PhotoView;
import me.crosswall.photo.pick.widget.AlbumPopupWindow;

/**
 * Created by yuweichen on 15/12/8.
 */
public class PickPhotosActiviy extends AppCompatActivity implements PhotoView {

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView btn_category;
    View mPopupAnchorView;

    PhotoPresenterImpl photoresenter;

    AlbumPopupWindow albumPopupWindow;

    ThumbPhotoAdapter thumbPhotoAdapter;

    private int spanCount;

    private int colorPrimary;

    private int maxPickSize;

    private int pickMode;

    private boolean showCamera;

    private boolean supportPreview;

    private boolean showGif;

    private boolean useCursorLoader;
    private Bundle bundle;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        initData();
        initView();
        loadPhoto();

    }

    private void initData() {
        bundle = getIntent().getBundleExtra(PickConfig.EXTRA_PICK_BUNDLE);
        spanCount = bundle.getInt(PickConfig.EXTRA_SPAN_COUNT,PickConfig.DEFAULT_SPANCOUNT);
        pickMode  = bundle.getInt(PickConfig.EXTRA_PICK_MODE,PickConfig.MODE_SINGLE_PICK);
        maxPickSize  = bundle.getInt(PickConfig.EXTRA_MAX_SIZE,PickConfig.DEFAULT_PICKSIZE);
        colorPrimary = bundle.getInt(PickConfig.EXTRA_TOOLBAR_COLOR,PickConfig.DEFALUT_TOOLBAR_COLOR);
        useCursorLoader = bundle.getBoolean(PickConfig.EXTRA_CURSOR_LOADER,PickConfig.DEFALUT_USE_CURSORLOADER);
    }

    private void initView() {

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        btn_category = findViewById(R.id.btn_category);
        mPopupAnchorView = findViewById(R.id.photo_footer);

        UIUtil.setTranslucentStatusColor(this,colorPrimary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundResource(colorPrimary);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorPrimary));
        }

        recyclerView.setLayoutManager(bindGridLayoutManager(spanCount));
        thumbPhotoAdapter = new ThumbPhotoAdapter(this,spanCount,maxPickSize,pickMode,toolbar);
        recyclerView.setAdapter(thumbPhotoAdapter);
        albumPopupWindow = new AlbumPopupWindow(this);
        albumPopupWindow.setAnchorView(mPopupAnchorView);
        albumPopupWindow.setOnItemClickListener(onItemClickListener);
        btn_category.setText(getString(R.string.all_photo));
        mPopupAnchorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumPopupWindow.show();
            }
        });

    }

    public GridLayoutManager bindGridLayoutManager(int spanCount){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,spanCount);
        return gridLayoutManager;
    }

    private void loadPhoto() {
        photoresenter = new PhotoPresenterImpl(PickPhotosActiviy.this,this);

        if(!PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            PermissionUtil.showPermissionDialog(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        }else{
            photoresenter.initialized(useCursorLoader,bundle);

        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            albumPopupWindow.setSelectedIndex(i);
            albumPopupWindow.getListView().smoothScrollToPosition(i);
            PhotoDirectory albumInfo = albumPopupWindow.getItem(i);
            thumbPhotoAdapter.clearAdapter();
            thumbPhotoAdapter.addData(albumInfo.getPhotos());
            btn_category.setText(albumInfo.getName());
            recyclerView.scrollToPosition(0);
            albumPopupWindow.dismiss();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            photoresenter.initialized(useCursorLoader,bundle);
        }else{
            String permission = permissions[0];
            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            if(!showRationale){
                PermissionUtil.showAppSettingDialog(this);
            }
        }
    }

    @Override
    public void showPhotosView(List<PhotoDirectory> photoDirectories) {
       // Toast.makeText(PickPhotosActiviy.this,"albumInfos size:" + photoDirectories.size(),Toast.LENGTH_SHORT).show();
        //thumbPhotoAdapter.clearAdapter();
        thumbPhotoAdapter.addData(photoDirectories.get(0).getPhotos());
        albumPopupWindow.addData(photoDirectories);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(pickMode==PickConfig.MODE_SINGLE_PICK){
            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_pick_photo, menu);
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId==android.R.id.home){
            finish();
        }else if(itemId==R.id.action_finish){
            ArrayList<String> selectedImages = thumbPhotoAdapter.getSelectedImages();
            if(selectedImages.size()==0){
                Toast.makeText(this,"请选择照片",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST,selectedImages);
                setResult(RESULT_OK,intent);
                finish();
            }

        }
        return true;
    }


    @Override
    public void showException(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}
