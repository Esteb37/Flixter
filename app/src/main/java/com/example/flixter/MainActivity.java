package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.adapters.MovieAdapter;
import com.example.flixter.databinding.ActivityMainBinding;
import com.example.flixter.models.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    int page = 1;
    String API_KEY;
    String catalogue = "now_playing";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        API_KEY = getString(R.string.api_key);

        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        FloatingActionButton btnUp = findViewById(R.id.btnUp);

        movies = new ArrayList<>();


        MovieAdapter.OnClickListener onClickListener = position -> {
            Movie selectMovie = movies.get(position);
            Intent i = new Intent(MainActivity.this, ActivityMovie.class);
            i.putExtra("id", selectMovie.getId());
            startActivity(i);
        };

        MovieAdapter.OnScrollListener onScrollListener = position ->{
            if(position>=movies.size()-1){
                page+=1;
                loadMovies(page);
            }
        };


        //Create the adapter
        movieAdapter = new MovieAdapter(this, movies, onClickListener,onScrollListener);

        //Set adapter on recyclerview
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager for the recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMovies.setLayoutManager(layoutManager);

        //Create request for the movie catalogue

        loadMovies(page);

        btnUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) rvMovies
                        .getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
    }

    public void loadMovies(int page){
        final String URL = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s&language=en-US&page=%s", catalogue,API_KEY, page);
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

    public void getCatalogue(View view){
        catalogue = view.getTag().toString();
        movies.clear();
        movieAdapter.notifyDataSetChanged();
        loadMovies(1);
    }

}