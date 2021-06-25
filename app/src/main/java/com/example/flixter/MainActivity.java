package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.adapters.MovieAdapter;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    //URl for the movie catalogue


    //Tag for logging
    public static final String TAG = "MainActivity";

    //Local movie catalogue
    List<Movie> movies;
    //Client for http requests
    AsyncHttpClient client = new AsyncHttpClient();

    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String API_KEY = getString(R.string.api_key);
        final String PLAYING_URL = String.format("https://api.themoviedb.org/3/movie/top_rated?api_key=%s&language=en-US&page=1", API_KEY);

        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        movies = new ArrayList<>();

        MovieAdapter.OnClickListener onClickListener = position -> {
            Movie selectMovie = movies.get(position);
            Intent i = new Intent(MainActivity.this, ActivityMovie.class);
            i.putExtra("id", selectMovie.getId());
            startActivity(i);
        };

        MovieAdapter.OnScrollListener onScrollListener = position ->{

        };

        //Create the adapter
        movieAdapter = new MovieAdapter(this, movies, onClickListener,onScrollListener);

        //Set adapter on recyclerview
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager for the recyclerview
        rvMovies.setLayoutManager((new LinearLayoutManager(this)));



        //Create request for the movie catalogue

    }

    public void loadMovies(int page, String URL){
        client.get(URL, new JsonHttpResponseHandler() {

            //If the request is successfully
            @Override
            public void onSuccess(int i, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.d(TAG, "onSuccess");

                //Get movie request as JSON object;
                JSONObject jsonObject = json.jsonObject;
                try {

                    //Get movie catalogue as array of json objects;
                    JSONArray results = jsonObject.getJSONArray("results");

                    //Create array of movie objects from the movie catalogue
                    movies.addAll(Movie.fromJSONArray(results));
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d(TAG, "HIt json Exception", e);
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