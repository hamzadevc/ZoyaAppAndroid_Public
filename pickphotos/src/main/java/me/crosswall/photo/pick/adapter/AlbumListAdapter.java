package me.crosswall.photo.pick.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.crosswall.photo.pick.R;
import me.crosswall.photo.pick.model.PhotoDirectory;

/**
 * Created by yuweichen on 15/12/9.
 */
public class AlbumListAdapter extends BaseAdapter{
    private List<PhotoDirectory> photoDirectories = new ArrayList<>();
    private Context context;
    private int selected;
    private LayoutInflater inflater;
    public AlbumListAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void addData(List<PhotoDirectory> photoDirectories){
        this.photoDirectories.addAll(photoDirectories);
        notifyDataSetChanged();
    }

    public void clearAdapter(){
        this.photoDirectories.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photoDirectories.size();
    }

    @Override
    public PhotoDirectory getItem(int position) {
        return photoDirectories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeSelect(int position){
        this.selected = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view = inflater.inflate(R.layout.item_pickphoto_album,viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        PhotoDirectory albumInfo = getItem(position);
        holder.photo_album_dis_name.setText(albumInfo.getName());
        holder.photo_album_element_count.setText(albumInfo.getPhotoPaths().size()+"");
        if(this.selected==position){
            holder.photo_album_selected.setVisibility(View.VISIBLE);
        }else {
            holder.photo_album_selected.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(albumInfo.getCoverPath())){
            holder.photo_album_cover.setImageResource(R.drawable.default_error);
        }else{
           // Uri uri = UriUtil.generatorUri(albumInfo.getCoverPath(),UriUtil.LOCAL_FILE_SCHEME);
            Glide.with(context).load(albumInfo.getCoverPath()).placeholder(holder.photo_album_cover.getDrawable()).error(R.drawable.default_error).into(holder.photo_album_cover);
        }

        return view;
    }

    static class ViewHolder {

        ImageView photo_album_cover;

        ImageView photo_album_selected;

        TextView  photo_album_dis_name;

        TextView  photo_album_element_count;


        public ViewHolder(View view){
            photo_album_cover    = view.findViewById(R.id.photo_album_cover);
            photo_album_selected = view.findViewById(R.id.photo_album_selected);
            photo_album_dis_name = view.findViewById(R.id.photo_album_dis_name);
            photo_album_element_count = view.findViewById(R.id.photo_album_element_count);

        }

    }

}
