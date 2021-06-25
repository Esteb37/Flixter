package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.databinding.ActivityMovieBinding;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Headers;

public class ActivityMovie extends AppCompatActivity {

    //Tag for logging
    public static final String TAG = "ActivityMovie";

    //Current context
    Context context;

    //The key of the Movie's Trailer video
    String videoKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View binding to reduce boilerplate
        ActivityMovieBinding binding = ActivityMovieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //Get current context
        context = getApplicationContext();

        //Get the layout elements
        TextView tvYear = (TextView) findViewById(R.id.tvYear);
        TextView tvGenre = (TextView) findViewById(R.id.tvGenre);
        TextView tvOverview = (TextView) findViewById(R.id.tvOverview2);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle2);
        TextView tvTime = (TextView) findViewById(R.id.tvTime);
        ImageView ivPoster = (ImageView) findViewById(R.id.ivPoster2);
        RatingBar rbRating = (RatingBar) findViewById(R.id.rbRating);

        //Get
        ImageView[] similarPosters = (ImageView[]) new ImageView[]{findViewById(R.id.similar1), findViewById(R.id.similar2), findViewById(R.id.similar3)};
        ImageView[] bSimilarPosters = (ImageView[]) new ImageView[]{findViewById(R.id.bsimilar1), findViewById(R.id.bsimilar2), findViewById(R.id.bsimilar3)};

        //Client for HTTP Requests
        AsyncHttpClient client = new AsyncHttpClient();

        //Get the current movie's ID
        int id = getIntent().getExtras().getInt("id");

        //Get the API Key for HTTP requests
        final String API_KEY = getString(R.string.api_key);

        //URLs for all the movie details requests to be made
        final String MOVIE_URL = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s&language=en-US", id, API_KEY);
        final String SIMILAR_URL = String.format("https://api.themoviedb.org/3/movie/%s/similar?api_key=%s&language=en-US&page=1", id, API_KEY);
        final String VIDEOS_URL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s&language=en-US&page=1", id, API_KEY);

        //Create the request for the Movie Details
        client.get(MOVIE_URL, new JsonHttpResponseHandler() {

            //If the request is successful
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Selected movie object
                Movie movie = null;

                try {
                    //Initialize movie object from the JSON information
                    movie = new Movie(json.jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Make sure the movie object is not null
                assert movie != null;

                //Set title of activity to Movie Title
                Objects.requireNonNull(getSupportActionBar()).setTitle(movie.getTitle());

                //Set movie information
                tvTitle.setText(movie.getTitle());
                tvOverview.setText(movie.getOverview());
                tvYear.setText(movie.getRelease().split("-")[0]); //Select only the year
                tvTime.setText(String.format("%d mins", movie.getRuntime()));
                tvGenre.setText(movie.getGenre());
                rbRating.setRating((float) movie.getRating(5));

                //Get the path to the movie's backdrop
                String imageURL = movie.getBackdropPath();

                //Set the backdrop image with rounded corners and a placeholder
                Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s", imageURL))
                        .placeholder(R.mipmap.placeholder)
                        .transform(new FitCenter(), new RoundedCorners(30))
                        .error(R.mipmap.placeholder)
                        .into(ivPoster);

            }


            //If the request is not successful
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        //Create the request for the Similar Movies list
        client.get(SIMILAR_URL, new JsonHttpResponseHandler() {

            //If the request is successful
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Get movies request as JSON object;
                JSONObject jsonObject = json.jsonObject;
                try {

                    //Get the list of similar movies from the fetched JSON object
                    JSONArray similarMovies = jsonObject.getJSONArray("results");
                    JSONObject jsonSimilar;

                    //For each of the first three similar movies
                    for (int j = 0; j < 3; j++) {

                        //Get the similar movie details JSON
                        jsonSimilar = (JSONObject) similarMovies.get(j);

                        //Set the front image of jth movie poster with rounded corners and a placeholder
                        Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s", jsonSimilar.getString("poster_path")))
                                .placeholder(R.mipmap.placeholder)
                                .transform(new FitCenter(), new RoundedCorners(30))
                                .error(R.mipmap.placeholder)
                                .into(similarPosters[j]);

                        //Set the background semitransparent image of the jth movie poster
                        Glide.with(context).load(String.format("https://image.tmdb.org/t/p/w342/%s", jsonSimilar.getString("poster_path")))
                                .placeholder(R.mipmap.placeholder)
                                .error(R.mipmap.placeholder)
                                .into(bSimilarPosters[j]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            //If the request is not successful
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        //Create the request for the Trailer Videos list
        client.get(VIDEOS_URL, new JsonHttpResponseHandler() {

            //If the request is successful
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                //Get videos request as JSON object;
                JSONObject jsonObject = json.jsonObject;

                try {
                    //Get the list of videos
                    JSONArray videoList = jsonObject.getJSONArray("results");
                    JSONObject jsonVideo;

                    //Parse the video list in search for the first with type "Trailer"
                    for (int j = 0; j < videoList.length(); j++) {

                        //Get the jth video information
                        jsonVideo = (JSONObject) videoList.get(j);

                        //If the jth video is of type "Trailer"
                        if (jsonVideo.getString("type").equals("Trailer")) {

                            //Set the videoKey to this video's key
                            videoKey = jsonVideo.getString("key");

                            //Exit the parse
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //If the request is not successful
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        //Onclick listener for the poster to send the user to the trailer
        ivPoster.setOnClickListener(v -> {

            //Set an intent to open the trailer activity
            Intent i = new Intent(ActivityMovie.this, TrailerActivity.class);

            //Pass the video key to the intent
            i.putExtra("videoKey", videoKey);

            //Start the trailer activity
            startActivity(i);
        });
    }
}