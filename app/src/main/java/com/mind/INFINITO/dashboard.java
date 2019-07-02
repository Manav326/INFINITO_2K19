package com.mind.INFINITO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class dashboard extends AppCompatActivity {
    private AppCompatImageView team, map, schedule, live, sconnect,share;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        live = (AppCompatImageView) findViewById(R.id.live);
        team =  (AppCompatImageView) findViewById(R.id.team);
        map =(AppCompatImageView) findViewById(R.id.map);
        schedule = (AppCompatImageView)findViewById(R.id.schedule);
        sconnect= (AppCompatImageView)findViewById(R.id.sconnect);
        share = (AppCompatImageView)findViewById(R.id.share);
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),livematches.class);
                startActivity(intent);
            }
        });
    }
}



