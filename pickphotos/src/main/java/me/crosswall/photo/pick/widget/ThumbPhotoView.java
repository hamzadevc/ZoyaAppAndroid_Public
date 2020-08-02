package me.crosswall.photo.pick.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import me.crosswall.photo.pick.PickConfig;
import me.crosswall.photo.pick.R;
import me.crosswall.photo.pick.util.UriUtil;


/**
 * Created by yuweichen on 15/12/9.
 */
public class ThumbPhotoView extends RelativeLayout{


    ImageView photo_thumbview;

    ImageView photo_thumbview_selected;

    TextView photo_thumbview_position;

    public ThumbPhotoView(Context context) {
        super(context);
        initView(context);
    }

    public ThumbPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ThumbPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.item_pickphoto_view,this);
        photo_thumbview = view.findViewById(R.id.photo_thumbview);
        photo_thumbview_selected = view.findViewById(R.id.photo_thumbview_selected);
        photo_thumbview_position = view.findViewById(R.id.photo_thumbview_position);
    }


    public void loadData(String folderPath,int pickMode){
        Uri uri = UriUtil.generatorUri(folderPath,UriUtil.LOCAL_FILE_SCHEME);
        Glide.with(getContext()).load(uri).placeholder(R.drawable.default_error).thumbnail(0.3f).error(R.drawable.default_error).into(photo_thumbview);
        if(pickMode == PickConfig.MODE_MULTIP_PICK){
            photo_thumbview_selected.setVisibility(VISIBLE);
        }else{
            photo_thumbview_selected.setVisibility(GONE);
        }
    }

    public void showSelected(boolean showSelected){
        if(showSelected){
            photo_thumbview_selected.setBackgroundResource(R.drawable.photo_selected);
        }else{
            photo_thumbview_selected.setBackgroundResource(R.drawable.photo_unselected);
        }
    }

    public void toggleSelect(int queuePosition){
       // Logger.d("queuePosition:"+queuePosition);
        if(queuePosition==0){
            photo_thumbview_position.setText("");
            photo_thumbview_position.setVisibility(GONE);
        }else{
            photo_thumbview_position.setVisibility(VISIBLE);
            photo_thumbview_position.setText(String.valueOf(queuePosition));
        }
    }


}
