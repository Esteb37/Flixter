package com.example.flixter;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.flixter.databinding.ActivityMainBinding;
import com.example.flixter.databinding.ActivityTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String YT_API = getString(R.string.yt_api);

        super.onCreate(savedInstanceState);


        ActivityTrailerBinding binding = ActivityTrailerBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);


        final String videoKey = getIntent().getStringExtra("videoKey");

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(YT_API, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.cueVideo(videoKey);
                youTubePlayer.setFullscreen(true);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}
