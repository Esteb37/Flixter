package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.adapters.MovieAdapter;
import com.example.flixter.databinding.ActivityMainBinding;
import com.example.flixter.models.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    //Tag for logging
    public static final String TAG = "MainActivity";

    //Client for HTTP requests
    AsyncHttpClient client = new AsyncHttpClient();

    //Adapter for the Movie Recyclerview
    MovieAdapter movieAdapter;

    //Movie catalogue array for the recyclerview
    List<Movie> movies;

    //Page variable to handle lazy loading
    int page = 1;

    //Catalogue variable to handle the catalogue menu
    String catalogue = "now_playing";

    //TMDB API key
    String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //View binding to reduce boilerplate
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Get API key from secrets
        API_KEY = getString(R.string.api_key);

        //Get layout items
        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        FloatingActionButton btnUp = (FloatingActionButton) findViewById(R.id.btnUp);
        SearchView svSearch = (SearchView) findViewById(R.id.svSearch);

        //Initialize movie list as empty array
        movies = new ArrayList<>();

        //Listener for clicking on a movie item
        MovieAdapter.OnClickListener onClickListener = position -> {

            //Get selected movie
            Movie selectMovie = movies.get(position);

            //Create intent to open movie details
            Intent i = new Intent(MainActivity.this, ActivityMovie.class);

            //Pass the movie id to the intent
            i.putExtra("id", selectMovie.getId());

            //Start Movie Details activity
            startActivity(i);
        };

        //Listener for scrolling to handle lazy loading
        MovieAdapter.OnScrollListener onScrollListener = position ->{

            //If the user has scrolled to the end of the movie list
            if(position>=movies.size()-1){

                //Set to next page
                page+=1;

                //Get next page of movies in current catalogue and add it to the recyclerview
                getCatalogue(catalogue,page);
            }
        };

        //Create the adapter with movies array and onClick and onScroll listeners
        movieAdapter = new MovieAdapter(this, movies, onClickListener,onScrollListener);

        //Set adapter on recyclerview
        rvMovies.setAdapter(movieAdapter);

        //Set a linear layout manager for the recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMovies.setLayoutManager(layoutManager);

        //Get "Now Playing" catalogue on first page to initialize app
        getCatalogue("now_playing",1);

        //Set listener for the "Back to top" button
        btnUp.setOnClickListener(v -> {

            //Cast movies RecyclerView layout manager normal Linear Layout Manager
            LinearLayoutManager layoutManager1 = (LinearLayoutManager) rvMovies
                    .getLayoutManager();

            //Make sure the casted layout manager is not null
            assert layoutManager1 != null;

            //Scroll recyclerview to top
            layoutManager1.scrollToPositionWithOffset(0, 0);
        });

        //Set listener for searchbar input
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override

            //Whenever a new character is entered
            public boolean onQueryTextChange(String newText) {
                //API URL for movie searching
                String SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key="+API_KEY+"&language=en-US&query="+newText+"&page=1&include_adult=true";

                //Clean movies array
                movies.clear();

                //Notify the adapter that the array has been cleaned
                movieAdapter.notifyDataSetChanged();

                //If the user has written something, show queried movies
                if(newText.length()>0) loadMovies(SEARCH_URL);

                //If the user erases it, show current catalogue
                else getCatalogue(catalogue,1);

                return false;
            }
        });

    }

    /*
        Function for fetching a list of movies through an HTTP request and
        adding it to the recyclerview's movies array.

        @param String URL : The API URL for the HTTP request

        @return void
     */
    public void loadMovies(String URL){

        //Send an HTTP Request to the specified URL
        client.get(URL, new JsonHttpResponseHandler() {

            //If the request is successful
            @Override
            public void onSuccess(int i, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.d(TAG, "onSuccess");

                //Get HTTP request as JSON object;
                JSONObject jsonObject = json.jsonObject;
                try {

                    //Get movie catalogue as array of JSON objects;
                    JSONArray results = jsonObject.getJSONArray("results");

                    //Create array of movie objects from the movie catalogue
                    movies.addAll(Movie.fromJSONArray(results));

                    //Notify the adapter that the movie array has changed
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

    /*
        Receives an Onclick event from the Catalogue Menu buttons and changes
        the displayed catalogue depending on the button clicked

        @param @NotNull View view : The Button View object that was pressed

        @return void
     */
    public void getCatalogue(@NotNull View view){

        //Get the desired catalogue from the clicked button's tag|
        catalogue = view.getTag().toString();

        //Clean the movies array
        movies.clear();

        //Notify the movies adapter that the data has changed
        movieAdapter.notifyDataSetChanged();

        //Gets the first page of the selected catalogue
        getCatalogue(catalogue,1);
    }

    /*
       Sends an HTTP request for a specific movie catalogue at a
       specific page

       @param String catalogue : The desired movie catalogue to access
       @param int page : The desired page of the catalogue to access

       @return void
    */
    public void getCatalogue(String catalogue, int page){
        //Set current catalogue to the one selected
        this.catalogue = catalogue;

        //Format the API URL with the desired catalogue and page
        final String URL = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s&language=en-US&page=%s", catalogue,API_KEY, page);

        //Load the desired movies into the recyclerview
        loadMovies(URL);
    }



}