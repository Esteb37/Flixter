package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Headers;

public class ActivityMovie extends AppCompatActivity {
    Context context;
    String videoKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


        TextView tvYear = findViewById(R.id.tvYear);
        TextView tvGenre = findViewById(R.id.tvGenre);
        TextView tvOverview = findViewById(R.id.tvOverview2);
        TextView tvTitle = findViewById(R.id.tvTitle2);
        TextView tvTime = findViewById(R.id.tvTime);
        ImageView ivPoster = findViewById(R.id.ivPoster2);
        RatingBar rbRating = findViewById(R.id.rbRating);
        String TAG = "MainActivity";
        ImageView[] similarPosters = new ImageView[]{findViewById(R.id.similar1), findViewById(R.id.similar2), findViewById(R.id.similar3)};


        AsyncHttpClient client = new AsyncHttpClient();

        int id = getIntent().getExtras().getInt("id");

        final String API_KEY = getString(R.string.api_key);

        final String MOVIE_URL = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s&language=en-US", id, API_KEY);
        final String SIMILAR_URL = String.format("https://api.themoviedb.org/3/movie/%s/similar?api_key=%s&language=en-US&page=1", id, API_KEY);
        final String VIDEOS_URL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s&language=en-US&page=1", id, API_KEY);


        //Create request for the movie catalogue
        client.get(MOVIE_URL, new JsonHttpResponseHandler() {

            //If the request is successfully
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Get movie request as JSON object;
                Movie movie = null;

                try {
                    movie = new Movie(json.jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                assert movie != null;
                tvTitle.setText(movie.getTitle());
                Objects.requireNonNull(getSupportActionBar()).setTitle(movie.getTitle());
                tvOverview.setText(movie.getOverview());
                tvYear.setText(movie.getRelease().split("-")[0]);
                tvTime.setText(String.format("%d mins", movie.getRuntime()));
                tvGenre.setText(movie.getGenre());
                rbRating.setRating((float) movie.getRating(5));

                String imageURL;
                context = getApplicationContext();
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageURL = movie.getBackdropPath();
                } else {
                    imageURL = movie.getPosterPath();
                }
                Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s", imageURL))
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.placeholder)
                        .into(ivPoster);

            }


            //If the request is not successfully
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });


        client.get(SIMILAR_URL, new JsonHttpResponseHandler() {

            //If the request is successfully
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Get movie request as JSON object;
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray similarMovies = jsonObject.getJSONArray("results");
                    JSONObject jsonSimilar;
                    for (int j = 0; j < 3; j++) {
                        jsonSimilar = (JSONObject) similarMovies.get(j);
                        Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s", jsonSimilar.getString("poster_path")))
                                .placeholder(R.mipmap.placeholder)
                                .error(R.mipmap.placeholder)
                                .into(similarPosters[j]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            //If the request is not successfully
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        client.get(VIDEOS_URL, new JsonHttpResponseHandler() {
            //If the request is successfully
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Get movie request as JSON object;
                JSONObject jsonObject = json.jsonObject;
                JSONObject jsonVideo;
                try {
                    JSONArray videoList = jsonObject.getJSONArray("results");
                    for (int j = 0; j < videoList.length(); j++) {
                        jsonVideo = (JSONObject) videoList.get(j);
                        if (jsonVideo.getString("type").equals("Trailer")) {
                            videoKey = jsonVideo.getString("key");
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            //If the request is not successfully
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
        ivPoster.setOnClickListener(v -> {
            Intent i = new Intent(ActivityMovie.this, TrailerActivity.class);
            i.putExtra("videoKey", videoKey);
            startActivity(i);
        });
    }


}