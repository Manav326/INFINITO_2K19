package com.mind.INFINITO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDex;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

public class dashboard extends AppCompatActivity implements View.OnClickListener {
    private AppCompatImageView team, map, schedule, live, sconnect, share, myProfilePic;
    TextView myName, myEmail, myfav;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
           .cacheInMemory(true).cacheOnDisk(true)
           .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
           .defaultDisplayImageOptions(defaultOptions)
           .build();
        ImageLoader.getInstance().init(config);

       /* ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);*/


        live =  findViewById(R.id.live);
        team =  findViewById(R.id.team);
        map =  findViewById(R.id.map);
        schedule =  findViewById(R.id.schedule);
        sconnect =  findViewById(R.id.sconnect);
        share =  findViewById(R.id.share);
        myProfilePic =  findViewById(R.id.myProfilePic);
        myName = findViewById(R.id.myName);
        myEmail = findViewById(R.id.myEmail);
        myfav = findViewById(R.id.myFav);

        firebaseAuth = FirebaseAuth.getInstance();
         user = firebaseAuth.getCurrentUser();
         firebaseDatabase =FirebaseDatabase.getInstance();
         databaseReference = firebaseDatabase.getReference("users");
        Query query= databaseReference.orderByChild("email").equalTo(user.getEmail());

        live.setOnClickListener(this);
        team.setOnClickListener(this);
        map.setOnClickListener(this);
        sconnect.setOnClickListener(this);
        schedule.setOnClickListener(this);
        share.setOnClickListener(this);
        live.setOnClickListener((View.OnClickListener) this) ;

        /*live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),livematches.class);
                startActivity(intent);
            }
        });*/
       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 //DataSnapshot ds = dataSnapshot.getChildren();
               for (DataSnapshot ds : dataSnapshot.getChildren()){
                   String name = "" + ds.child("uid").getValue() ;
                   String email = "" + ds.child("email").getValue() ;
                   String fav = "" + ds.child("fav").getValue() ;
                   String image = "" + ds.child("image").getValue() ;

                   myName.setText(name);
                   myEmail.setText(email);
                   myfav.setText(fav);

                   try{
                      // Picasso.get.load(image).into(myProfilePic);
                       ImageLoader.getInstance().displayImage(image, myProfilePic);

                   }
                   catch (Exception e){

                      // picasso.get().load(R.drawable.teamwork__team__woman__account__profile).into(myProfilePic);
                       ImageLoader.getInstance().displayImage("drawable://"+R.drawable.teamwork__team__woman__account__profile, myProfilePic);
                   }
               }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(dashboard.this, "Profile Pic couldn't update" , Toast.LENGTH_SHORT).show();
            }
        });
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




