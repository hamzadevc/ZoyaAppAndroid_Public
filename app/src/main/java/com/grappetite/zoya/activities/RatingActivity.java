package com.grappetite.zoya.activities;

import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erraticsolutions.framework.activities.CustomActivity;
import com.erraticsolutions.framework.customclasses.ContextCustom;
import com.google.gson.JsonElement;
import com.grappetite.zoya.R;
import com.grappetite.zoya.adapters.ReviewRecyclerAdapter;
import com.grappetite.zoya.customclasses.CommonConstants;
import com.grappetite.zoya.customclasses.LocalStoragePreferences;
import com.grappetite.zoya.dataclasses.ReviewData;
import com.grappetite.zoya.postboy.PostBoy;
import com.grappetite.zoya.postboy.PostBoyException;
import com.grappetite.zoya.postboy.PostBoyListener;
import com.grappetite.zoya.postboy.RequestType;
import com.grappetite.zoya.restapis.parsers.Parser;
import com.grappetite.zoya.restapis.parsers.ReviewParser;
import com.grappetite.zoya.restapis.urls.WebUrls;
import com.grappetite.zoya.utils.SessionUtils;
import com.grappetite.zoya.views.ConnectionInfoView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RatingActivity extends CustomActivity implements ConnectionInfoView.Listener {

    @BindView(R.id.tv_total_rating)
    TextView tv_total_rating;
    @BindView(R.id.tv_total_number)
    TextView tv_total_number;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.civ)
    ConnectionInfoView civ;

    @BindView(R.id.bar_1)
    LinearLayout bar_1;
    @BindView(R.id.bar_2)
    LinearLayout bar_2;
    @BindView(R.id.bar_3)
    LinearLayout bar_3;
    @BindView(R.id.bar_4)
    LinearLayout bar_4;
    @BindView(R.id.bar_5)
    LinearLayout bar_5;
    @BindView(R.id.c_1)
    ConstraintLayout c_1;
    @BindView(R.id.rv)
    RecyclerView rv;

    ArrayList<ReviewData> list;
    ReviewRecyclerAdapter adb;
    private PostBoy postBoy;
    private PostBoy postBoyRating;

    String place_id;

    // TODO:: NEEDS REFACTORING

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rating;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        list = new ArrayList<>();
        adb = new ReviewRecyclerAdapter();
        place_id = getIntent().getStringExtra("place_id");
        postBoy = new PostBoy.Builder(this, RequestType.GET,WebUrls.BASE_URL+WebUrls.METHOD_REVIEW).create();
        postBoyRating = new PostBoy.Builder(this, RequestType.GET,WebUrls.BASE_URL+WebUrls.METHOD_GET_RATING).create();
    }

    @Override
    protected void assign(@Nullable Bundle savedInstanceState) {
        this.showBackButton(true);
        this.setSupportActionbarTitle("Ratings");
        this.setSupportActionbarFont(ContextCustom.getString(this,R.string.font_semiBold));

        civ.setListener(this);
        adb.setList(list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adb);

        postBoy.setListener(new ReviewListener());
        postBoy.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoy.addGETValue("place_id", String.valueOf(place_id));
        postBoy.call();

        postBoyRating.setListener(new RatingListener());
        postBoyRating.addHeader(CommonConstants.HEADER_AUTH_TOKEN, LocalStoragePreferences.getAuthToken());
        postBoyRating.addGETValue("place_id", String.valueOf(place_id));
        postBoyRating.call();


    }

    @Override
    public void onRetry() {
        postBoy.call();
    }


    private class ReviewListener implements PostBoyListener {
        @Override
        public void onPostBoyConnecting() throws PostBoyException {
            list.clear();
            adb.notifyDataSetChanged();
            civ.showLoader();
        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {
            ReviewParser review = new ReviewParser(json);
            if(review.getResponseCode()==200){
                list.clear();
                list.addAll(review.getReview());
            }

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            civ.hideAll();
            Parser parser = new Parser(json);
            switch (parser.getResponseCode()) {
                case 200:
                    adb.notifyDataSetChanged();
                    break;
                case 204:
                    Toast.makeText(getApplicationContext(),"204",Toast.LENGTH_LONG).show();
                    civ.showNothingFound();
                    break;
                case 401:
                    SessionUtils.logout(RatingActivity.this, true);
                    break;
            }
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {
            civ.showInternetConnectionFail();
        }

        @Override
        public void onPostBoyError(PostBoyException e) {
            Log.e("rating", "PostByError: " + e.getMessage());
        }
    }

    private class RatingListener implements PostBoyListener {

        @Override
        public void onPostBoyConnecting() throws PostBoyException {

        }

        @Override
        public void onPostBoyAsyncConnected(String json, int responseCode) throws PostBoyException {

        }

        @Override
        public void onPostBoyConnected(String json, int responseCode) throws PostBoyException {
            Parser parser = new Parser(json);
            JsonElement rating = parser.getResponse().getAsJsonObject().get("rating");

            String ratingVal = String.valueOf(Float.parseFloat(rating.getAsJsonObject().get("rating").getAsString()));
            String totalRating = rating.getAsJsonObject().get("totalRating").getAsString();
            String star_1 = rating.getAsJsonObject().get("star_1").getAsString();
            String star_2 = rating.getAsJsonObject().get("star_2").getAsString();
            String star_3 = rating.getAsJsonObject().get("star_3").getAsString();
            String star_4 = rating.getAsJsonObject().get("star_4").getAsString();
            String star_5 = rating.getAsJsonObject().get("star_5").getAsString();

            SetRatingCard(ratingVal,totalRating,star_1,star_2,star_3,star_4,star_5);
        }

        @Override
        public void onPostBoyConnectionFailure() throws PostBoyException {


        }

        @Override
        public void onPostBoyError(PostBoyException e) {
            Log.e("rating", "PostByError: " + e.getMessage());

        }
    }


    public void SetRatingCard(String rating, String totalRating, String star1, String star2, String star3, String star4, String star5) {

        int totalAllVoters = Integer.parseInt(totalRating);
        int totalRateStar1 = Integer.parseInt(star1);
        int totalRateStar2 = Integer.parseInt(star2);
        int totalRateStar3 = Integer.parseInt(star3);
        int totalRateStar4 = Integer.parseInt(star4);
        int totalRateStar5 = Integer.parseInt(star5);

        double votersInDouble = Double.parseDouble(totalRating);


        c_1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                c_1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int widthView = c_1.getWidth();

                //RATING STAR 1
                double star1 = totalRateStar1;
                double sum1 = (star1 / votersInDouble);
                int rating1 = (int) (sum1 * widthView);

                ConstraintLayout.LayoutParams layoutParams1 = new ConstraintLayout.LayoutParams(rating1, ConstraintLayout.LayoutParams.MATCH_PARENT);
                layoutParams1.setMargins(0, 5, 0, 5);
                bar_1.setBackgroundColor(Color.parseColor("#ff6f31"));
                bar_1.setLayoutParams(layoutParams1);

                //RATING STAR 2
                double star2 = totalRateStar2;
                double sum2 = (star2 / votersInDouble);
                int rating2 = (int) (sum2 * widthView);
                ConstraintLayout.LayoutParams layoutParams2 = new ConstraintLayout.LayoutParams(rating2, ConstraintLayout.LayoutParams.MATCH_PARENT);
                layoutParams2.setMargins(0, 5, 0, 5);
                bar_2.setBackgroundColor(Color.parseColor("#ff9f02"));
                bar_2.setLayoutParams(layoutParams2);

                //RATING STAR 3
                double star3 = totalRateStar3;
                double sum3 = (star3 / votersInDouble);
                int rating3 = (int) (sum3 * widthView);
                ConstraintLayout.LayoutParams layoutParams3 = new ConstraintLayout.LayoutParams(rating3, ConstraintLayout.LayoutParams.MATCH_PARENT);
                layoutParams3.setMargins(0, 5, 0, 5);
                bar_3.setBackgroundColor(Color.parseColor("#ffcf02"));
                bar_3.setLayoutParams(layoutParams3);

                //RATING STAR 4
                double star4 = totalRateStar4;
                double sum4 = (star4 / votersInDouble);
                int rating4 = (int) (sum4 * widthView);
                ConstraintLayout.LayoutParams layoutParams4 = new ConstraintLayout.LayoutParams(rating4, ConstraintLayout.LayoutParams.MATCH_PARENT);
                layoutParams4.setMargins(0, 5, 0, 5);
                bar_4.setBackgroundColor(Color.parseColor("#9ace6a"));
                bar_4.setLayoutParams(layoutParams4);

                //RATING STAR 5
                double star5 = totalRateStar5;
                double sum5 = (star5 / votersInDouble);
                int rating5 = (int) (sum5 * widthView);
                ConstraintLayout.LayoutParams layoutParams5 = new ConstraintLayout.LayoutParams(rating5, ConstraintLayout.LayoutParams.MATCH_PARENT);
                layoutParams5.setMargins(0, 5, 0, 5);
                bar_5.setBackgroundColor(Color.parseColor("#57bb8a"));
                bar_5.setLayoutParams(layoutParams5);


            }
        });

        tv_total_rating.setText(String.valueOf(Float.parseFloat(rating)));
        ratingBar.setRating(Float.parseFloat(rating));
        tv_total_number.setText(totalAllVoters + " ratings");

    }



}
