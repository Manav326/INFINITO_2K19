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

public class dashboard extends AppCompatActivity implements View.OnClickListener {
    private AppCompatImageView team, map, schedule, live, sconnect, share;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        live = (AppCompatImageView) findViewById(R.id.live);
        team = (AppCompatImageView) findViewById(R.id.team);
        map = (AppCompatImageView) findViewById(R.id.map);
        schedule = (AppCompatImageView) findViewById(R.id.schedule);
        sconnect = (AppCompatImageView) findViewById(R.id.sconnect);
        share = (AppCompatImageView) findViewById(R.id.share);


        live.setOnClickListener(this);
        team.setOnClickListener(this);
        map.setOnClickListener(this);
        sconnect.setOnClickListener(this);
        schedule.setOnClickListener(this);
        share.setOnClickListener(this);
        /*live.setOnClickListener((View.OnClickListener) this) ;*/

        /*live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),livematches.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live:
                Intent intent = new Intent(getApplicationContext(), livematches.class);
                startActivity(intent);
                break;

            case R.id.team:
                Intent intent1 = new Intent(getApplicationContext(), TeamMembers.class);
                startActivity(intent1);
                break;

            case R.id.map:
                Intent intent2 = new Intent(getApplicationContext(), LocalMap.class);
                startActivity(intent2);
                break;

            case R.id.schedule:
                Intent intent3 = new Intent(getApplicationContext(), Schedule.class);
                startActivity(intent3);
                break;

            case R.id.sconnect:
                Intent intent4 = new Intent(getApplicationContext(), SocialConnect.class);
                startActivity(intent4);
                break;

            case R.id.share:
                Intent intent5 = new Intent(getApplicationContext(), Share.class);
                startActivity(intent5);
                break;


        }
    }
}



