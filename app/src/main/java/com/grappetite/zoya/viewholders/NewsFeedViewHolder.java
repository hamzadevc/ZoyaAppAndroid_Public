package com.grappetite.zoya.viewholders;

import android.media.MediaPlayer;
import android.net.Uri;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.erraticsolutions.framework.customclasses.CustomViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.NewsFeedRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonMethods;
import com.grappetite.zoya.dataclasses.NewsFeedData;
import com.grappetite.zoya.dataclasses.TagData;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedViewHolder extends CustomViewHolder {

    @Nullable
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @Nullable
    @BindView(R.id.vv_cover)
    VideoView vv_cover ;
    @Nullable
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Nullable
    @BindView(R.id.flex_tags)
    FlexboxLayout flex_tags;
    @Nullable
    @BindView(R.id.iv_news_feed_type)
    ImageView iv_newsFeedType;
    @Nullable
    @BindView(R.id.fl_bottom)
    View v_bottom;

    public NewsFeedViewHolder(View itemView) {
        super(itemView);
    }

    public NewsFeedViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
        super(parent, layoutRes);
    }

    @Override
    protected void init(View view) {
        ButterKnife.bind(this, view);
    }

    public void bind(NewsFeedData data, NewsFeedRecyclerAdapter.Listener listener) {
        if (tv_title != null)
            tv_title.setText(data.getTitle());
        if (iv_cover != null)
        {
                Picasso.get()
                        .load(CommonMethods.getOptimisedImageUrl(itemView.getContext(), data.getImageUrl()))
                        .placeholder(R.drawable.ic_news_feed_placeholder)
                        .into(iv_cover);

            if (data.getType() == NewsFeedData.Type.VIDEO && data.getYoutubeEnglishVideoUrl().contains("zoyaapp")){
                MediaController mediaController = new MediaController(itemView.getContext());
                vv_cover.setMediaController(mediaController);
                vv_cover.setVideoURI(Uri.parse(data.getYoutubeEnglishVideoUrl()));
              //  vv_cover.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                vv_cover.setOnPreparedListener(mp -> iv_cover.setVisibility(View.GONE));
            }

        }
        if (iv_newsFeedType != null) {
            if (data.getType() == NewsFeedData.Type.VIDEO)
                iv_newsFeedType.setImageResource(R.drawable.play);
            else if (data.getType() == NewsFeedData.Type.ARTICAL && data.getLanguagesList().size()>0)
                iv_newsFeedType.setImageResource(R.drawable.audio);
            else
                iv_newsFeedType.setImageBitmap(null);
        }
        if (flex_tags != null) {
            flex_tags.removeAllViews();
            for (TagData tg : data.getTags()) {
                View view = LayoutInflater.from(this.itemView.getContext()).inflate(R.layout.view_tag_news_feed, flex_tags, false);
                TextView tvTag = view.findViewById(R.id.tv_tag);
                tvTag.setText(tg.getTitle());
                flex_tags.addView(view, FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                view.setOnClickListener(v -> {
                    if (listener != null)
                        listener.onRecyclerItemTagClicked(tg);
                });
                if (flex_tags.getChildCount() == 3)
                    break;
            }
        }
        if (v_bottom != null)
        {
            v_bottom.setBackgroundResource(data.isSheRocks() ? R.color.pink_a80 : R.color.blue_a80);
            v_bottom.setVisibility(data.getType()!= NewsFeedData.Type.AD?View.VISIBLE:View.GONE);
        }
    }
}
