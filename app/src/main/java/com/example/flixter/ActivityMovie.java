package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.Rating;
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

import okhttp3.Headers;

public class ActivityMovie extends AppCompatActivity {
    Context context;

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
        ImageView similar1 = findViewById((R.id.similar1));
        ImageView similar2 = findViewById((R.id.similar2));
        ImageView similar3 = findViewById((R.id.similar3));

        AsyncHttpClient client = new AsyncHttpClient();

        int id = getIntent().getExtras().getInt("id");

        final String MOVIE_URL = String.format("https://api.themoviedb.org/3/movie/%s?api_key=9ace9cb9774ff085d83b43b0724903f6&language=en-US", id);
        final String SIMILAR_URL = String.format("https://api.themoviedb.org/3/movie/%s/similar?api_key=9ace9cb9774ff085d83b43b0724903f6&language=en-US&page=1", id);

        //Create request for the movie catalogue
        client.get(MOVIE_URL, new JsonHttpResponseHandler() {

            //If the request is successfully
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Get movie request as JSON object;
                JSONObject movie = json.jsonObject;


                try {
                    tvTitle.setText(movie.getString("title"));
                    getSupportActionBar().setTitle(movie.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    tvOverview.setText(movie.getString("overview"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    tvYear.setText(movie.getString("release_date"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    tvTime.setText(movie.getString("runtime")+" mins");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject genre = (JSONObject) movie.getJSONArray("genres").get(0);
                    tvGenre.setText((String) genre.get("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                double rating = 0;
                try {
                    rating = movie.getDouble("vote_average")/2;
                    rbRating.setRating((float) rating);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                context = getApplicationContext();
                try {
                    Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s",movie.getString("poster_path")))
                            .placeholder(R.mipmap.placeholder)
                            .error(R.mipmap.placeholder)
                            .into(ivPoster);
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


        client.get(SIMILAR_URL, new JsonHttpResponseHandler() {

            //If the request is successfully
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Get movie request as JSON object;
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray similar = jsonObject.getJSONArray("results");


                    try {
                        JSONObject jsonSimilar1 = (JSONObject) similar.get(0);
                        Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s",jsonSimilar1.getString("poster_path")))
                                .placeholder(R.mipmap.placeholder)
                                .error(R.mipmap.placeholder)
                                .into(similar1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonSimilar2 = (JSONObject) similar.get(1);
                        Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s",jsonSimilar2.getString("poster_path")))
                                .placeholder(R.mipmap.placeholder)
                                .error(R.mipmap.placeholder)
                                .into(similar2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonSimilar3 = (JSONObject) similar.get(2);
                        Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s",jsonSimilar3.getString("poster_path")))
                                .placeholder(R.mipmap.placeholder)
                                .error(R.mipmap.placeholder)
                                .into(similar3);
                    } catch (JSONException e) {
                        e.printStackTrace();
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


    }
}