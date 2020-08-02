package com.grappetite.zoya.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.NewsFeedCommentRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.CommentData;
import com.grappetite.zoya.dataclasses.NewsFeedData;
import com.grappetite.zoya.restapis.parsers.NewsFeedCommentParser;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.restapis.webmaps.PostMaps;
import com.grappetite.zoya.utils.DialogUtils;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewFeedCommentsActivity extends CustomActivity implements NewsFeedCommentRecyclerAdapter.Listener, ConnectionInfoView.Listener {

    private static final String TAG = "NewFeedCommentsActivity";

    @BindView(R.id.rv)  RecyclerView rv;
    @BindView(R.id.v_connection_info)
    ConnectionInfoView v_info;
    @BindView(R.id.et_comment)  EditText    et_comment;

    private NewsFeedCommentRecyclerAdapter adb;
    private LinearLayoutManager layoutManager;
    private ArrayList<CommentData>  list;
    private PostBoy postBoy, postCommentPostBoy, flagCommentPostBoy;
    private NewsFeedData newsFeedData;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_feed_comments;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        newsFeedData = this.getIntent().getParcelableExtra(CommonConstants.DATA_NEWS_FEED);
        list = new ArrayList<>();
        adb = new NewsFeedCommentRecyclerAdapter();
        adb.setListener(this);
        postBoy = new PostBoy.Builder(this, RequestType.GET,WebUrls.BASE_URL+WebUrls.METHOD_NEWS_FEED_COMMENTS).create();
        postCommentPostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA,WebUrls.BASE_URL+WebUrls.METHOD_POST_COMMENT).create();
        flagCommentPostBoy = new PostBoy.Builder(this, RequestType.POST_FORM_DATA,WebUrls.BASE_URL+WebUrls.METHOD_FLAG_COMMENT).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("COMMENTS");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));

        v_info.setListener(this);

        adb.setList(list);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adb);

        postBoy.setListener(new CommentsListener());
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.addGETValue("newsfeed_id",String.valueOf(newsFeedData.getId()));
        postBoy.call();

        postCommentPostBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postCommentPostBoy.setListener(new PostCommentListener());
        flagCommentPostBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
    }

    private boolean isValid() {
        return !et_comment.getText().toString().isEmpty();
    }

    @OnClick({R.id.iv_post_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_post_comment:  {
                if (isValid()) {
                    postCommentPostBoy.setPOSTValues(PostMaps.postComment(newsFeedData.getId(),et_comment.getText().toString()));
                    postCommentPostBoy.call();
                }
            }
                break;
        }
    }

    @Override
    public void onFlagIconClicked(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Flag comment")
                .setMessage("Are you sure you want to flag this comment as an inappropriate content?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    CommentData cd = list.get(position);
                    flagCommentPostBoy.setListener(new FlagCommentListener(position,cd));
                    flagCommentPostBoy.setPOSTValues(PostMaps.flagComment(list.get(position).getId(),!cd.isFlagged()));
                    flagCommentPostBoy.call();
                })
                .setNegativeButton("No",null)
                .show();
    }

    @Override
    public void onRetry() {
        postBoy.call();
    }

    private class FlagCommentListener implements PostBoyListener {
        private final CommentData commentData;
        private final int position;

        FlagCommentListener(int position, CommentData commentData) {
            this.position = position;
            this.commentData = commentData;
        }

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            commentData.setIsFlagged(!commentData.isFlagged());
            adb.notifyItemChanged(position);
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            NewsFeedCommentParser parser = new NewsFeedCommentParser(json);
            switch (parser.getResponseCode()) {
                case 200:
                {
                    CommentData commentData = parser.getCommentData();
                    for (int i=0 ; i<list.size() ; i++) {
                        if (commentData.getId() == list.get(i).getId())
                        {
                            list.remove(i);
                            list.add(i,commentData);
                        }
                    }
                }
                    break;
                case 401:
                    SessionUtils.logout(NewFeedCommentsActivity.this,true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            Toast.makeText(NewFeedCommentsActivity.this, R.string.internet_connection_fail, Toast.LENGTH_SHORT).show();
            commentData.setIsFlagged(!commentData.isFlagged());
            adb.notifyItemChanged(position);
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }

    private class PostCommentListener implements PostBoyListener {
        private ProgressDialog pd;
        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            pd = DialogUtils.getLoadingDialog(NewFeedCommentsActivity.this,"Posting comment...");
            pd.show();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            NewsFeedCommentParser parser = new NewsFeedCommentParser(json);
            if (parser.getResponseCode()==200)
                list.add(0,parser.getCommentData());
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            DialogUtils.dismiss(pd);
            Parser parser = new NewsFeedCommentParser(json);
            switch (parser.getResponseCode()) {
                case 200:
                {
                    adb.notifyItemInserted(0);
                    if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        layoutManager.scrollToPosition(0);
                    }
                    et_comment.setText(null);
                    v_info.hideAll();
                }
                    break;
                case 401:
                    SessionUtils.logout(NewFeedCommentsActivity.this,true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            DialogUtils.dismiss(pd);
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }

    private class CommentsListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            v_info.showLoader();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            NewsFeedCommentParser parser = new NewsFeedCommentParser(json);
            if (parser.getResponseCode()==200)
                list.addAll(parser.getComments());
        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            v_info.hideAll();
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    adb.notifyDataSetChanged();
                    break;
                case 204:
                    v_info.showNothingFound();
                    break;
                case 401:
                    SessionUtils.logout(NewFeedCommentsActivity.this,true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            v_info.showInternetConnectionFail();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {

        }
    }
}
