package com.mind.INFINITO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
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
import com.mind.INFINITO.ui.main.*;

public class dashboard extends AppCompatActivity implements View.OnClickListener {
     CircularImageView myProfilePic;
     Button Logout;
    TextView myName, myEmail, myfav;
    LinearLayout team, map, schedule, live, sconnect, share;
    ImageView infsite,infinsta,inffb;
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
         ImageLoaderConfiguration config1 = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        live =  findViewById(R.id.live);
        team =  findViewById(R.id.team);
        map =  findViewById(R.id.map);
        Logout=  findViewById(R.id.buttonLogout);
        schedule =  findViewById(R.id.schedule);
        inffb =  findViewById(R.id.inffb);
        infinsta =  findViewById(R.id.infinsta);
        infsite =  findViewById(R.id.infsite);

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

        infinsta.setOnClickListener(this);
        inffb.setOnClickListener(this);
        infsite.setOnClickListener(this);
        live.setOnClickListener(this);
        team.setOnClickListener(this);
        map.setOnClickListener(this);
        //sconnect.setOnClickListener(this);
        schedule.setOnClickListener(this);
        share.setOnClickListener(this);
        live.setOnClickListener((View.OnClickListener) this) ;
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent6 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent6);
                finish();

            }
        });

       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot ds : dataSnapshot.getChildren()){
                   String name = "" + ds.child("name").getValue() ;
                   String email = "" + ds.child("email").getValue() ;
                   String fav = "" + ds.child("fav").getValue() ;
                   String image = "" + ds.child("image").getValue() ;
                   String temp = email.substring(0,13);
                   String emailToShow =temp.concat("...");
                   myName.setText(name);
                   myEmail.setText(emailToShow);
                   myfav.setText(fav);

                 try{
                       if(image!=null ){
                      // Picasso.get.load(image).into(myProfilePic);
                       ImageLoader.getInstance().displayImage(image, myProfilePic);}
                       else{ImageLoader.getInstance().displayImage(image, myProfilePic);
                           ImageLoader.getInstance().displayImage("drawable://"+R.drawable.contacts_profile_account, myProfilePic);
                       }
                   }
                   catch (Exception e){
                      // picasso.get().load(R.drawable.teamwork__team__woman__account__profile).into(myProfilePic);
                       ImageLoader.getInstance().displayImage("drawable://"+R.drawable.contacts_profile_account, myProfilePic);
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


            /*case R.id.sconnect:
                Intent intent4 = new Intent(getApplicationContext(), FbWebView.class);
                startActivity(intent4);
                break;*/
            case R.id.inffb:
                Intent intentfb = new Intent(getApplicationContext(), Fragment_Fb.class);
                startActivity(intentfb);
                break;
            case R.id.infsite:
                Intent intentsite = new Intent(getApplicationContext(), Fragment_Site.class);
                startActivity(intentsite);
                break;
            case R.id.infinsta:
                Intent intentinsta = new Intent(getApplicationContext(), Fragment_Insta.class);
                startActivity(intentinsta);
                break;

            case R.id.share:
                Intent intent5 = new Intent(Intent.ACTION_SEND);
                intent5.setType("text/plain");
                String shareBody = "Website : http://www.infinito.org.in/#home \n\n Facebook : https://www.facebook.com/pg/InfinitoIITPatna/ \n\n " +
                        "Instagram : https://www.instagram.com/infinito_iitp/ \n\n Android App : ****************************************";
                String shareSubject = "A Warm Welcome From INFINITO-19";
                intent5.putExtra(Intent.EXTRA_TEXT,shareBody);
                intent5.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                startActivity(Intent.createChooser(intent5, "Share Using"));
                break;
        }
    }
}


