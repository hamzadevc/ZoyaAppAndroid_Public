package com.grappetite.zoya.views;

import android.content.Context;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.NewsFeedRecyclerAdapter;
import com.grappetite.zoya.adapters.NewsFeedSliderPagerAdapter;
import com.grappetite.zoya.dataclasses.NewsFeedData;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsFeedSliderView extends FrameLayout {
    @BindView(R.id.vp)  ViewPager vp;
    @BindView(R.id.indicator)   CirclePageIndicator indicator;
    private NewsFeedSliderPagerAdapter adb;
    private ArrayList<NewsFeedData> list;

    public NewsFeedSliderView(@NonNull Context context) {
        super(context);
        init();
    }

    public NewsFeedSliderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewsFeedSliderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NewsFeedSliderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_news_feed_slider, this, false);
        this.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adb = new NewsFeedSliderPagerAdapter(list);
        vp.setAdapter(adb);
        indicator.setViewPager(vp);
    }



    public void setFeaturedNewsFeed(ArrayList<NewsFeedData> list) {
        this.list.clear();
        this.list.addAll(list);
        vp.setAdapter(adb);
        indicator.notifyDataSetChanged();
    }

    public NewsFeedSliderView setListener(NewsFeedRecyclerAdapter.Listener listener){
        if (adb!=null)
            adb.setListener(listener);
        return this;
    }

    @OnClick({R.id.iv_left, R.id.iv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                if (vp.getCurrentItem() > 0)
                    vp.setCurrentItem(vp.getCurrentItem() - 1);
                else
                    vp.setCurrentItem(adb.getCount() - 1);
                break;
            case R.id.iv_right:
                if (vp.getCurrentItem() < adb.getCount() - 1)
                    vp.setCurrentItem(vp.getCurrentItem() + 1);
                else
                    vp.setCurrentItem(0);
                break;
        }
    }
}
