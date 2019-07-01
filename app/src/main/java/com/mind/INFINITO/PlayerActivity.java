package com.mind.INFINITO;

import android.os.Bundle;
import android.os.PersistableBundle;


import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mind.INFINITO.R;


public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    YouTubePlayer player;
    Button fullscreen;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yt_player);
        final YouTubePlayerView playerView =  findViewById(R.id.youTubePlayerView);
        playerView.initialize(DeveloperKey.DEVELOPER_KEY,this);
        fullscreen = findViewById(R.id.fscreen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setFullscreen(true);
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        this.player = player;
        if (!b){
            player.loadVideo("ktMdrKXAVxk");
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
