package com.example.flixter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.flixter.databinding.ActivityTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TrailerActivity extends YouTubeBaseActivity {

    //Tag for logging
    public static final String TAG = "TrailerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //YouTube API key
        final String YT_API = getString(R.string.yt_api);

        //View Binding to reduce boilerplate
        ActivityTrailerBinding binding = ActivityTrailerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Get Trailer video key
        final String videoKey = getIntent().getStringExtra("videoKey");

        // Get the YouTube player view
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(YT_API, new YouTubePlayer.OnInitializedListener() {
            @Override

            //On successful video initialization
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                //Play video
                youTubePlayer.cueVideo(videoKey);

                //Set video in fullscreen
                youTubePlayer.setFullscreen(true);
            }

            //On unsuccessful video initialization
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                //Log the error
                Log.e(TAG, "Error initializing YouTube player");
            }
        });
    }
}
