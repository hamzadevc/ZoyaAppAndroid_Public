package com.erraticsolutions.framework.views;


import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;

import com.erraticsolutions.framework.R;
import com.erraticsolutions.framework.utils.FontUtils;

public class CustomButton extends AppCompatButton
{

    public CustomButton(Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
