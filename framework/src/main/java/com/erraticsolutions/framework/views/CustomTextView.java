package com.erraticsolutions.framework.views;


import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.erraticsolutions.framework.R;
import com.erraticsolutions.framework.utils.FontUtils;

import static com.erraticsolutions.framework.R.styleable.View_cfont;

public class CustomTextView extends AppCompatTextView
{

    public CustomTextView(Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs)
    {
        if (attrs!=null)
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.View);
            if(a.hasValue(View_cfont))
                this.setTypeface(FontUtils.getFont(this.getContext(),a.getString(View_cfont)));
            a.recycle();
        }
    }
}
