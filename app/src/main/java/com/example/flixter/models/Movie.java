package com.example.flixter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
    Class for representing the information of a Movie
 */
public class Movie {


    //Movie attributes
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    String release;
    String genre;
    int runtime;
    double rating;

    //Movie id
    int id;

    /*
        Movie constructor from JSON object

        @param JSON Object jsonObject: the JSON with the movie information

     */
    public Movie(JSONObject jsonObject) throws JSONException {

        //Parse the movie information from the JSON into the object's attributes
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        id = jsonObject.getInt("id");

        //These attributes are only available through the "Movie Details" request
        //sent by ActivityMovie, so the MainActivity will not be able to access them
        try {
            rating = jsonObject.getDouble("vote_average");
            runtime = jsonObject.getInt("runtime");
            release = jsonObject.getString("release_date");

            //Get only the first genre of the list
            JSONObject genre = (JSONObject) jsonObject.getJSONArray("genres").get(0);
            this.genre = (String) genre.get("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
        Transforms an array of JSON objects into an array of Movie Objects

        @param JSONArray movieJsonArray : The array with the Movie Information

        @return List<Movie> : Array of movie objects with parameters set
     */
    public static List<Movie> fromJSONArray(JSONArray movieJsonArray) throws JSONException {

        //Initialize the movie array as empty list
        List<Movie> movies = new ArrayList<>();

        //Transform all json objects into movie objects and add to the movie array
        for (int i = 0; i < movieJsonArray.length(); i++) {

            //Construct Movie Object from JSONObject
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    //Formatted image path getters
    public String getPosterPath() { return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath); }
    public String getBackdropPath() { return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath); }

    //Attribute getters
    public String getOverview() { return overview;}
    public String getRelease() { return release; }
    public double getRating(double stars) { return rating * (stars / 10); }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getRuntime() { return runtime; }
    public int getId() { return id; }

}
