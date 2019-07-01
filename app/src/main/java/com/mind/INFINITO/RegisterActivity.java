package com.mind.INFINITO;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signup =(Button) findViewById(R.id.signup);
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);
            }
        });
    }

   /* @Override
    public void onClick(View v) {
        if (v==signup){
            Intent intent   = new Intent(RegisterActivity.this,RegisterActivity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(signup,"signup");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }

    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
