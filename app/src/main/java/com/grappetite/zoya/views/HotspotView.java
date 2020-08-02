package com.grappetite.zoya.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.grappetite.zoya.R;


public class HotspotView extends View {

    private String subBodyPartName, mainBodyPartName;

    public HotspotView(Context context) {
        super(context);
        init(null);
    }

    public HotspotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HotspotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HotspotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (!this.isInEditMode())
            if (attrs != null) {
                TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.HotspotView);
                subBodyPartName = a.getString(R.styleable.HotspotView_sub_body_part_name);
                mainBodyPartName = a.getString(R.styleable.HotspotView_main_body_part_name);
//                if (TextUtils.isEmpty(subBodyPartName))
//                    throw new IllegalArgumentException("Sub-Body part name cannot be null or empty");
//                if (TextUtils.isEmpty(mainBodyPartName))
//                    throw new IllegalArgumentException("Main-Body part name cannot be null or empty");
                a.recycle();
            }
    }

    @NonNull
    public String getSubBodyPartName() {
        return subBodyPartName;
    }

    public String getMainBodyPartName() {
        return mainBodyPartName;
    }
}
