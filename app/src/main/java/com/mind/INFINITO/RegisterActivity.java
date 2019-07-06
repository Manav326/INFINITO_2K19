package com.mind.INFINITO;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;

import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;
    private Button signup;
    private  EditText uEmail, uUserName, uPasswd, uRePasswd;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signup =(Button) findViewById(R.id.signup);
        rlayout = findViewById(R.id.rlayout);
        mAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("initialLogIn", MODE_PRIVATE);

        uEmail = findViewById(R.id.uEmail);
        uUserName = findViewById(R.id.uUserName);
        uPasswd = findViewById(R.id.uPasswd);
        uRePasswd = findViewById(R.id.uRePasswd);


        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User....");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 String Email = uEmail.getText().toString().trim();
                String User = uUserName.getText().toString().trim();
                String Password = uPasswd.getText().toString().trim();
                String RePassword = uRePasswd.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    uEmail.setError("Enter Valid Email Adress");
                    uEmail.setFocusable(true);
                    }
                else if ((Password.length()<6) || (RePassword.length()<6)){

                    uPasswd.setError("Password Length atleast 6");
                    uPasswd.setFocusable(true);
                }

               else if (!(Password.equals(RePassword))){

                    uRePasswd.setError("Password Didn't Match");
                    uRePasswd.setFocusable(true);
                }
                else{

                    registerUser(Email,Password);
                }

               /* Intent intent = new Intent(getApplicationContext(), dashboard.class);
                startActivity(intent);*/
            }
        });
    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sp.edit().putBoolean("initialLogIn",true).apply();
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email =user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("phone","");
                            hashMap.put("fav","");
                            hashMap.put("image","");

                            FirebaseDatabase database =FirebaseDatabase.getInstance();

                            DatabaseReference reference = database.getReference("users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this,"Registered.."+user.getEmail(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), dashboard.class);
                            startActivity(intent);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
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
