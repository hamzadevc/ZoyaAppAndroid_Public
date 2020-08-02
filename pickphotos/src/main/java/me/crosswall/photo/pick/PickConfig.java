package me.crosswall.photo.pick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

/**
 * Created by yuweichen on 15/12/10.
 */
public class PickConfig {

    public static int DEFAULT_SPANCOUNT = 3;

    public static int DEFAULT_PICKSIZE  = 1;

    public static int MODE_SINGLE_PICK  = 1;

    public static int MODE_MULTIP_PICK  = 2;

    public static int DEFALUT_TOOLBAR_COLOR = R.color.colorPrimary;

    public static boolean DEFALUT_SHOW_GIF = false;

    public static boolean DEFALUT_USE_CURSORLOADER = true;
    public static boolean DEFALUT_CHECK_IMAGE = false;

    public final static int PICK_REQUEST_CODE = 10607;

    public final static String EXTRA_STRING_ARRAYLIST = "extra_string_array_list";

    public final static String EXTRA_PICK_BUNDLE = "extra_pick_bundle";
    public final static String EXTRA_SPAN_COUNT  = "extra_span_count";
    public final static String EXTRA_PICK_MODE   = "extra_pick_mode";
    public final static String EXTRA_MAX_SIZE    = "extra_max_size";
    public final static String EXTRA_TOOLBAR_COLOR = "extra_toolbar_color";
    public final static String EXTRA_SHOW_GIF      = "extra_show_gif";
    public final static String EXTRA_CURSOR_LOADER = "extra_cursor_loader";
    public final static String EXTRA_CHECK_IMAGE   = "extra_check_image";

    private int spanCount;
    private int pickMode;
    private int maxPickSize;
    private int toolbarColor;
    private boolean showGif;
    private boolean useCursorLoader;
    private boolean checkImage;

    private PickConfig(Activity activity,PickConfig.Builder builder){
        startPick(activity,null,init(builder));
    }

    private PickConfig(Fragment fragment,PickConfig.Builder builder){
        startPick(null,fragment,init(builder));
    }

    private Bundle init(PickConfig.Builder builder) {
        this.spanCount = builder.spanCount;
        this.pickMode  = builder.pickMode;
        this.maxPickSize  = builder.maxPickSize;
        this.toolbarColor = builder.toolbarColor;
        this.showGif   = builder.showGif;
        this.useCursorLoader = builder.useCursorLoader;
        this.checkImage = builder.checkImage;
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SPAN_COUNT,this.spanCount);
        bundle.putInt(EXTRA_PICK_MODE,this.pickMode);
        bundle.putInt(EXTRA_MAX_SIZE,this.maxPickSize);
        bundle.putInt(EXTRA_TOOLBAR_COLOR,this.toolbarColor);
        bundle.putBoolean(EXTRA_SHOW_GIF,this.showGif);
        bundle.putBoolean(EXTRA_CURSOR_LOADER,this.useCursorLoader);
        bundle.putBoolean(EXTRA_CHECK_IMAGE,this.checkImage);
        return bundle;
    }

    private void startPick(Activity activity,Fragment fragment, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PICK_BUNDLE,bundle);
        intent.setClass(activity!=null?activity:fragment.getActivity(),PickPhotosActiviy.class);
        if (activity!=null)
            activity.startActivityForResult(intent,PICK_REQUEST_CODE);
        else
            fragment.startActivityForResult(intent,PICK_REQUEST_CODE);
    }


    public static class Builder{

        private Activity activity;
        private Fragment fragment;
        private int spanCount = DEFAULT_SPANCOUNT;
        private int pickMode  = MODE_SINGLE_PICK;
        private int maxPickSize  = DEFAULT_PICKSIZE;
        private int toolbarColor = DEFALUT_TOOLBAR_COLOR;
        private boolean showGif  = DEFALUT_SHOW_GIF;
        private boolean useCursorLoader = DEFALUT_USE_CURSORLOADER;
        private boolean checkImage = DEFALUT_CHECK_IMAGE;
        public Builder(Activity activity){
            if(activity == null) {
                throw new IllegalArgumentException("A non-null Activity or Fragment must be provided");
            }
            this.activity = activity;
        }
        public Builder(Fragment fragment){
            if(fragment == null) {
                throw new IllegalArgumentException("A non-null Activity or Fragment must be provided");
            }
            this.fragment = fragment;
        }

        public PickConfig.Builder spanCount(int spanCount){
            this.spanCount = spanCount;
            if(this.spanCount==0){
                this.spanCount = DEFAULT_SPANCOUNT;
            }
            return this;
        }

        public PickConfig.Builder pickMode(int pickMode){
            this.pickMode = pickMode;
            if(this.pickMode==0){
                this.pickMode = MODE_SINGLE_PICK;
            }
            return this;
        }

        public PickConfig.Builder maxPickSize(int maxPickSize){
            this.maxPickSize = maxPickSize;
            if(this.maxPickSize==0){
                this.maxPickSize = DEFAULT_PICKSIZE;
            }
            return this;
        }

        public PickConfig.Builder toolbarColor(@ColorRes int toolbarColor){
            this.toolbarColor = toolbarColor;
            if(this.toolbarColor==0){
                this.toolbarColor = DEFALUT_TOOLBAR_COLOR;
            }
            return this;
        }

        public PickConfig.Builder showGif(boolean showGif){
            this.showGif = showGif;
            return this;
        }

        public PickConfig.Builder checkImage(boolean checkImage){
            this.checkImage = checkImage;
            return this;
        }

        public PickConfig.Builder useCursorLoader(boolean useCursorLoader){
            this.useCursorLoader = useCursorLoader;
            return this;
        }

        public PickConfig build(){
            if (activity!=null)
                return new PickConfig(activity,this);
            else
                return new PickConfig(fragment,this);
        }
    }

}
