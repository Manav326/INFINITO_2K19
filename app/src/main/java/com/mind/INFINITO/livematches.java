package com.mind.INFINITO;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mind.INFINITO.playerLive.AthletcsLive;
import com.mind.INFINITO.playerLive.BasketballLive;
import com.mind.INFINITO.playerLive.CricketLive;
import com.mind.INFINITO.playerLive.FootballLive;
import com.mind.INFINITO.playerLive.LawntennisLive;
import com.mind.INFINITO.playerLive.TabletennisLive;
import com.mind.INFINITO.playerLive.VolleyballLive;

public class livematches extends AppCompatActivity implements View.OnClickListener {

    private Button athletics, cricket, volleyball, basketball, tabletennis, lawntennis,football;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livematches);
        athletics = findViewById(R.id.athletics);
        cricket = findViewById(R.id.cricket);
        volleyball = findViewById(R.id.volleyball);
        basketball = findViewById(R.id.basketball);
        tabletennis = findViewById(R.id.tabletennis);
        lawntennis = findViewById(R.id.lawntennis);
        football = findViewById(R.id.football);
        athletics.setOnClickListener(this) ;
        cricket.setOnClickListener(this) ;
        volleyball.setOnClickListener(this) ;
        basketball.setOnClickListener(this) ;
        tabletennis.setOnClickListener(this) ;
        lawntennis.setOnClickListener(this) ;
        football.setOnClickListener(this) ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.athletics:
                Intent intent = new Intent(getApplicationContext(),AthletcsLive.class);
                startActivity(intent);
             break;

            case R.id.cricket:
                Intent intent1 = new Intent(getApplicationContext(),CricketLive.class);
                startActivity(intent1);
                break;

            case R.id.volleyball:
                Intent intent2 = new Intent(getApplicationContext(),VolleyballLive.class);
                startActivity(intent2);
                break;

            case R.id.basketball:
                Intent intent3 = new Intent(getApplicationContext(), BasketballLive.class);
                startActivity(intent3);
                break;

            case R.id.tabletennis:
                Intent intent4 = new Intent(getApplicationContext(),TabletennisLive.class);
                startActivity(intent4);
                break;

            case R.id.lawntennis:
                Intent intent5 = new Intent(getApplicationContext(),LawntennisLive.class);
                startActivity(intent5);
                break;
            case R.id.football:
                Intent intent6 = new Intent(getApplicationContext(), FootballLive.class);
                startActivity(intent6);
                break;
        }
    }
}




