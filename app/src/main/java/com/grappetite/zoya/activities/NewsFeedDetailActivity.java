package com.grappetite.zoya.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.grappetite.zoya.R;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.NewsFeedData;
import com.grappetite.zoya.dataclasses.TagData;
import com.grappetite.zoya.interfaces.ZoyaAPI;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.postboy.RetrofitInstance;
import com.grappetite.zoya.restapis.parsers.NewsFeedParser;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.utils.YoutubeUtils;
import com.grappetite.zoya.views.ConnectionInfoView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedDetailActivity extends CustomActivity implements YouTubePlayer.OnInitializedListener, Callback<String> {

    private static final String TAG = "NewsFeedDetailActivity";

    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_description)
    TextView tv_description;
    @BindView(R.id.tv_author_name)
    TextView tv_authorName;
    @BindView(R.id.tv_author_about)
    TextView tv_authorAbout;
    @BindView(R.id.iv_author_profile_pic)
    ImageView iv_authorImageUrl;

    @BindView(R.id.iv_like)
    ImageView iv_like;
    @BindView(R.id.tv_like)
    TextView tv_like;
    @BindView(R.id.flex_tags)
    FlexboxLayout flex_tags;
    @BindView(R.id.frag_youtube_player)
    View v_youtubePlayer;
    @BindView(R.id.sp_languages)
    Spinner sp_languages;
    @BindView(R.id.ll_lang)
    View ll_languages;

    @BindView(R.id.fl_help)
    View fl_help;

    @BindView(R.id.v_connection_info)
    ConnectionInfoView civ;
    String language_value;

    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private NewsFeedData newsFeedData;
    private PostBoy likePostBoy;
    private ArrayAdapter<NewsFeedData.Languages> languageAdb;
    private List<NewsFeedData.Languages> languagesList;
    private NewsFeedDataChangedReceiver newsFeedDataChangedReceiver;
    private FinishReceiver finishReceiver;
    private String youtubeUrl;
    private YouTubePlayer youTubePlayer;

    private ZoyaAPI zoyaAPI;
    private boolean isLoaded = false;

    private FirebaseAuth mAuth;
    private DatabaseReference getUserDatabaseReference;
    String user_id;
    boolean runOnce = true;
    boolean runOnceVideo = true;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_feed_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        zoyaAPI = RetrofitInstance.getRetrofitInstance(getApplicationContext()).create(ZoyaAPI.class);

//        newsFeedData = this.getIntent().getParcelableExtra(CommonConstants.DATA_NEWS_FEED);

        likePostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA, WebUrls.BASE_URL + WebUrls.METHOD_LIKE_NEWS_FEED).create();
        youTubePlayerFragment = (YouTubePlayerSupportFragment) this.getSupportFragmentManager().findFragmentById(R.id.frag_youtube_player);

        newsFeedDataChangedReceiver = new NewsFeedDataChangedReceiver();
        this.getLocalBroadcastManager().registerReceiver(newsFeedDataChangedReceiver, new IntentFilter(CommonConstants.ACTION_NEWS_FEED_DATA_CHANGED));
        finishReceiver = new FinishReceiver();
        getLocalBroadcastManager().registerReceiver(finishReceiver, new IntentFilter(CommonConstants.ACTION_FINISH_NEWS_FEED));


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();


    }

    @Override
    protected boolean setOrientation() {
        return false;
    }

    private void doStepsAfterDataFetched() {
        languagesList = newsFeedData.getLanguagesList();
        languageAdb = new ArrayAdapter<>(this, R.layout.spinner_tv_blue_stroke_down_arrow, R.id.tv, languagesList);

        ll_languages.setVisibility(this.isMultiLang(0) ? View.VISIBLE : View.GONE);
        fl_help.setVisibility(this.isMultiLang(2) && LocalStoragePreferences.showNewsFeedHelp() ? View.VISIBLE : View.GONE);
        if (newsFeedData.getType() == NewsFeedData.Type.VIDEO)
            playYouTubeVideo(newsFeedData.getYoutubeEnglishVideoUrl());

        Picasso.get()
                .load(newsFeedData.getImageUrl())
                .placeholder(R.drawable.ic_news_feed_placeholder)
                .into(iv_cover);
        Picasso.get()
                .load(newsFeedData.getAuthorImageUrl())
                .transform(new CropCircleTransformation())
                .placeholder(R.drawable.profile_pic_placeholder)
                .into(iv_authorImageUrl);
        tv_title.setText(newsFeedData.getTitle());


        if (runOnce) {
            Log.v("Article Title", "" + newsFeedData.getTitle());
            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("NewsFeedArticle")
                    .child(newsFeedData.getTitle());
            getUserDatabaseReference.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long value = mutableData.getValue(Long.class);
                    if (value == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue(value + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    Log.d(TAG, "transaction:onComplete:" + databaseError);
                }
            });


            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(user_id)
                    .child("NewsFeedArticle")
                    .child(newsFeedData.getTitle());
            getUserDatabaseReference.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long value = mutableData.getValue(Long.class);
                    if (value == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue(value + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    Log.d(TAG, "transaction:onComplete:" + databaseError);
                }
            });


            runOnce = false;
        }


        tv_description.setText(newsFeedData.getDescription());
        tv_authorName.setText(newsFeedData.getAuthorName());
        tv_authorAbout.setText(newsFeedData.getAuthorAbout());
        iv_like.setImageResource(newsFeedData.isLiked() ? R.drawable.ic_like_active : R.drawable.ic_like_inactive);
        tv_like.setTextColor(ContextCustom.getColor(this, newsFeedData.isLiked() ? R.color.pink : R.color.gray_txt_dark));
        flex_tags.removeAllViews();
        for (TagData tg : newsFeedData.getTags()) {
            View view = this.getLayoutInflater().inflate(R.layout.view_tag_news_feed_detail, flex_tags, false);
            TextView tvTag = view.findViewById(R.id.tv_tag);
            tvTag.setText(tg.getTitle());
            view.setOnClickListener(view1 -> {
                Intent i = new Intent(this, NewsFeedActivity.class);
                i.putExtra(CommonConstants.DATA_TAG, tg);
                this.startActivity(i);
            });
            flex_tags.addView(view, FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        }
        languageAdb.setDropDownViewResource(R.layout.spinner_tv_blue);
        sp_languages.setAdapter(languageAdb);
    }


    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("صحت نامہ");
        this.setSupportActionbarFont(ContextCustom.getString(this, R.string.font_semiBold));
        ButterKnife.bind(this);
        likePostBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        likePostBoy.setListener(new LikeNewsFeedListener());
        fl_help.setVisibility(LocalStoragePreferences.showNewsFeedHelp() ? View.VISIBLE : View.GONE);

        showVideoView(false);

        zoyaAPI.getNewsfeedById(LocalStoragePreferences.getAuthToken(), this.getIntent().getStringExtra("id")).enqueue(this);
        civ.showLoader();


    }

    @Override
    protected void populate() {

        if (isLoaded) {
            Picasso.get()
                    .load(newsFeedData.getImageUrl())
                    .placeholder(R.drawable.ic_news_feed_placeholder)
                    .into(iv_cover);
            Picasso.get()
                    .load(newsFeedData.getAuthorImageUrl())
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.profile_pic_placeholder)
                    .into(iv_authorImageUrl);
            tv_title.setText(newsFeedData.getTitle());
            tv_description.setText(newsFeedData.getDescription());
            tv_authorName.setText(newsFeedData.getAuthorName());
            tv_authorAbout.setText(newsFeedData.getAuthorAbout());
            iv_like.setImageResource(newsFeedData.isLiked() ? R.drawable.ic_like_active : R.drawable.ic_like_inactive);
            tv_like.setTextColor(ContextCustom.getColor(this, newsFeedData.isLiked() ? R.color.pink : R.color.gray_txt_dark));
            flex_tags.removeAllViews();
            for (TagData tg : newsFeedData.getTags()) {
                View view = this.getLayoutInflater().inflate(R.layout.view_tag_news_feed_detail, flex_tags, false);
                TextView tvTag = view.findViewById(R.id.tv_tag);
                tvTag.setText(tg.getTitle());
                view.setOnClickListener(view1 -> {
                    Intent i = new Intent(this, NewsFeedActivity.class);
                    i.putExtra(CommonConstants.DATA_TAG, tg);
                    this.startActivity(i);
                });
                flex_tags.addView(view, FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (newsFeedDataChangedReceiver != null)
            this.getLocalBroadcastManager().unregisterReceiver(newsFeedDataChangedReceiver);
        if (finishReceiver != null)
            this.getLocalBroadcastManager().unregisterReceiver(finishReceiver);
    }

    @Override
    public void onBackPressed() {
        if (fl_help.getVisibility() == View.VISIBLE) {
            fl_help.setVisibility(View.GONE);
        } else
            super.onBackPressed();
    }

    private void initYoutubePlayer() {
        if (!TextUtils.isEmpty(youtubeUrl)) {
            showVideoView(true);
            youTubePlayerFragment.initialize(ContextCustom.getString(this, R.string.youtube_apikey), this);
        } else if (TextUtils.isEmpty(newsFeedData.getYoutubeEnglishVideoUrl()))
            showVideoView(false);
    }

    private void showVideoView(boolean show) {
        iv_cover.setVisibility(show ? View.GONE : View.VISIBLE);
        v_youtubePlayer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void sendBroadcastAboutNewsData() {
        getLocalBroadcastManager()
                .sendBroadcast(new Intent(CommonConstants.ACTION_NEWS_FEED_DATA_CHANGED).
                        putExtra(CommonConstants.DATA_NEWS_FEED, newsFeedData));
    }

    private boolean isMultiLang(int minSize) {
        return newsFeedData.getType() == NewsFeedData.Type.ARTICAL && languagesList.size() > minSize;
    }

    @OnClick({R.id.ll_comment, R.id.ll_like, R.id.ll_share, R.id.fl_help, R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_comment: {
                Intent i = new Intent(this, NewFeedCommentsActivity.class);
                i.putExtra(CommonConstants.DATA_NEWS_FEED, newsFeedData);
                this.startActivity(i);
            }
            break;
            case R.id.ll_like:
                likePostBoy.setPOSTValues(PostMaps.likeNewsFeed(newsFeedData.getId(), !newsFeedData.isLiked()));
                likePostBoy.call();
                break;
            case R.id.ll_share: {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.grappetite.zoya");
                i.setType("text/plain");
                startActivity(i);
            }
            break;

            case R.id.iv_close: {
                fl_help.setVisibility(View.GONE);
                LocalStoragePreferences.setShowNewsFeedHelp(false);
            }
            break;
        }
    }


    @OnItemSelected({R.id.sp_languages})
    public void onLanguageItemClicked(int position) {
        showVideoView(true);
        switch (languagesList.get(position)) {
            case English:
                youtubeUrl = newsFeedData.getYoutubeEnglishVideoUrl();
                language_value = "English";
                break;
            case Urdu:
                youtubeUrl = newsFeedData.getYoutubeUrduVideoUrl();
                language_value = "Urdu";
                break;
            case Pashto:
                youtubeUrl = newsFeedData.getYoutubePashtoVideoUrl();
                language_value = "Pashto";
                break;
            case Punjabi:
                youtubeUrl = newsFeedData.getYoutubePunjabiVideoUrl();
                language_value = "Punjabi";
                break;
            case Dari:
                youtubeUrl = newsFeedData.getYoutubeDariVideoUrl();
                language_value = "Dari";
                break;
            case Saraiki:
                youtubeUrl = newsFeedData.getYoutubeSaraikiVideoUrl();
                language_value = "Saraiki";
                break;
            case Select_Languages:
                youtubeUrl = null;
                language_value = "Default";
                break;
        }

        playYouTubeVideo(youtubeUrl);

        if (!language_value.equals("Default")) {
            Log.v("Article Title", "" + newsFeedData.getTitle());
            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("NewsFeedVideo")
                    .child(newsFeedData.getTitle())
                    .child(language_value);
            getUserDatabaseReference.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long value = mutableData.getValue(Long.class);
                    if (value == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue(value + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    Log.d(TAG, "transaction:onComplete:" + databaseError);
                }
            });


            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(user_id)
                    .child("NewsFeedVideo")
                    .child(newsFeedData.getTitle())
                    .child(language_value);
            getUserDatabaseReference.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Long value = mutableData.getValue(Long.class);
                    if (value == null) {
                        mutableData.setValue(0);
                    } else {
                        mutableData.setValue(value + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    Log.d(TAG, "transaction:onComplete:" + databaseError);
                }
            });


//            runOnce = false;
        }

    }


    private void playYouTubeVideo(String videoUrl) {
        this.youtubeUrl = videoUrl;
        if (TextUtils.isEmpty(youtubeUrl)) {
            if (youTubePlayer != null) {
                youTubePlayer.release();
                youTubePlayer = null;
            }
            showVideoView(false);
        } else {
            if (youTubePlayer == null)
                initYoutubePlayer();
            else {
                youTubePlayer.cueVideo(YoutubeUtils.getVideoId(youtubeUrl));
            }

        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.setShowFullscreenButton(true);
        String id = YoutubeUtils.getVideoId(youtubeUrl);
        if (id != null) {
            if (newsFeedData.getType() == NewsFeedData.Type.VIDEO) {
                youTubePlayer.cueVideo(id);
            } else {
                youTubePlayer.cueVideo(id);
            }
        } else {
            showVideoView(false);
        }
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        showVideoView(false);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        NewsFeedParser parser = new NewsFeedParser(response.body());

        switch (parser.getResponseCode()) {
            case 200:
                civ.hideAll();
                newsFeedData = parser.getNewsfeed();
                isLoaded = true;
                doStepsAfterDataFetched();
                break;
            case 204:
                civ.showNothingFound();
                break;
            case 401:
                SessionUtils.logout(NewsFeedDetailActivity.this, true);
                break;
        }


    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {

    }

    private class LikeNewsFeedListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            newsFeedData.setLike(!newsFeedData.isLiked());
            sendBroadcastAboutNewsData();
            repopulate();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 401:
                    SessionUtils.logout(NewsFeedDetailActivity.this, true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            newsFeedData.setLike(!newsFeedData.isLiked());
            repopulate();
            Toast.makeText(NewsFeedDetailActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
            getLocalBroadcastManager()
                    .sendBroadcast(new Intent(CommonConstants.ACTION_NEWS_FEED_DATA_CHANGED).
                            putExtra(CommonConstants.DATA_NEWS_FEED, newsFeedData));
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }

    private class NewsFeedDataChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent i) {
            NewsFeedData data = i.getParcelableExtra(CommonConstants.DATA_NEWS_FEED);
            if (data.getId() == newsFeedData.getId()) {
                newsFeedData.setLike(data.isLiked());
                repopulate();
            }
        }
    }

    private class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NewsFeedDetailActivity.this.finish();
        }
    }


}
