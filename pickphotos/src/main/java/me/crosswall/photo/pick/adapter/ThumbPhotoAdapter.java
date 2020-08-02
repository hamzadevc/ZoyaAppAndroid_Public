package me.crosswall.photo.pick.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import java.util.ArrayList;
import java.util.List;

import me.crosswall.photo.pick.PickConfig;
import me.crosswall.photo.pick.model.Photo;
import me.crosswall.photo.pick.widget.ThumbPhotoView;


/**
 * Created by yuweichen on 15/12/9.
 */
public class ThumbPhotoAdapter extends RecyclerView.Adapter<ThumbPhotoAdapter.ThumbHolder>{

    private Activity context;
    private List<Photo> photos = new ArrayList<>();
    private int width;
    private Toolbar toolbar;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private int maxPickSize;
    private int pickMode;
    public ThumbPhotoAdapter(Activity context, int spanCount, int maxPickSize, int pickMode, Toolbar toolbar){
        this.context = context;
        this.width   = context.getResources().getDisplayMetrics().widthPixels / spanCount;
        this.maxPickSize = maxPickSize;
        this.pickMode = pickMode;
        this.toolbar  = toolbar;
        if(pickMode== PickConfig.MODE_SINGLE_PICK){
            toolbar.setTitle("选择图片");
        }else{
            setTitle(selectedImages.size());
        }

    }

    public void addData(List<Photo> photos){
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    public void clearAdapter(){
        this.photos.clear();
        notifyDataSetChanged();
    }

    @Override
    public ThumbHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThumbHolder(new ThumbPhotoView(context));
    }

    @Override
    public void onBindViewHolder(ThumbHolder holder, int position) {
        holder.setData(getItem(position),position);
    }

    @Override
    public int getItemCount() {
        return this.photos.size();
    }

    public ArrayList<String> getSelectedImages(){
        return selectedImages;
    }

    public Photo getItem(int position){
        return this.photos.get(position);
    }


    class ThumbHolder extends RecyclerView.ViewHolder{

        public ThumbPhotoView thumbPhotoView;

        public ThumbHolder(View itemView) {
            super(itemView);
            thumbPhotoView = (ThumbPhotoView) itemView;
        }

        public void setData(final Photo imageInfo,final int position){

            thumbPhotoView.setLayoutParams(new FrameLayout.LayoutParams(width,width));
            thumbPhotoView.loadData(imageInfo.getPath(),pickMode);

            if(selectedImages.contains(imageInfo.getPath())){
                thumbPhotoView.showSelected(true);
            }else{
                thumbPhotoView.showSelected(false);
            }

            final String path = imageInfo.getPath();

            thumbPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(pickMode == PickConfig.MODE_SINGLE_PICK){
                        selectedImages.clear();
                        selectedImages.add(path);
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST,selectedImages);
                        context.setResult(Activity.RESULT_OK,intent);
                        context.finish();
                    }else{
                        if(selectedImages.contains(path)){
                            selectedImages.remove(path);
                            thumbPhotoView.showSelected(false);
                        }else{
                            if(selectedImages.size()==maxPickSize){
                                return;
                            }else{
                                selectedImages.add(path);
                                thumbPhotoView.showSelected(true);
                            }
                        }
                        setTitle(selectedImages.size());
                    }


                }
            });
        }
    }

    public void setTitle(int selectCount){
        toolbar.setTitle(selectCount + "/" +maxPickSize);
    }

}
