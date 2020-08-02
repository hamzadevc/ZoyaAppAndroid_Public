package com.erraticsolutions.framework.views;


import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.erraticsolutions.framework.R;
import com.erraticsolutions.framework.utils.FontUtils;

public class CustomEditText extends AppCompatEditText
{

    public CustomEditText(Context context)
    {
        super(context);
        init(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @Override
    public void setError(CharSequence error) {
        this.requestFocus();
        super.setError(error);
    }
}
