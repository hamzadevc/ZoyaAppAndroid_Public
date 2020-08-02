package com.erraticsolutions.framework.views;


import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.erraticsolutions.framework.R;
import com.erraticsolutions.framework.utils.FontUtils;

public class CustomCheckBox extends AppCompatCheckBox
{

    public CustomCheckBox(Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs)
    {
        if (attrs!=null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.View);
            if(a.hasValue(R.styleable.View_cfont))
                this.setTypeface(FontUtils.getFont(this.getContext(),a.getString(R.styleable.View_cfont)));
            a.recycle();
        }
    }
}
