package com.mind.INFINITO;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton btRegister;
    private TextView tvLogin;
    private TextView tvForgot;
    private Button logIn;
    private EditText uName1, uPasswd1;
    private static final int RC_SIGN_IN = 100;
    private SignInButton mGoogleLogIn;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog,progressDialog2;
    private FirebaseAuth mAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        MultiDex.install(this);
        sp = getSharedPreferences("initialLogIn", MODE_PRIVATE);
        if(sp.getBoolean("initialLogIn",false)){

            Intent intent = new Intent(this, dashboard.class);

        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        tvForgot= findViewById(R.id.tvForgot);
        logIn =findViewById(R.id.logIn);
        uName1 =findViewById(R.id.uName1);
        uPasswd1 =findViewById(R.id.uPasswd1);
        mGoogleLogIn =  findViewById(R.id.GoogleBt);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging User In....");
        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("Sending Recovery Email....");
        tvForgot.setOnClickListener(this);
        btRegister.setOnClickListener(this);
        logIn.setOnClickListener(this);
        mGoogleLogIn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btRegister:

                    Intent intent   = new Intent(MainActivity.this,RegisterActivity.class);
                    Pair[] pairs    = new Pair[1];
                    pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                    startActivity(intent,activityOptions.toBundle());
                break;

            case R.id.logIn:

                String Email = uName1.getText().toString().trim();
                String Password = uPasswd1.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    uName1.setError("Enter Valid Email Adress");
                    uName1.setFocusable(true);
                }
                else if ((Password.length()<6))
                {

                    uPasswd1.setError("Password Length atleast 6");
                    uPasswd1.setFocusable(true);
                }
                else {
                    loginUser(Email,Password);
                    }

                break;

            case R.id.GoogleBt:
                 signIn();
                 break;

            case R.id.tvForgot:

                showRecoverPasswdDialog();
                break;
        }
    }

    private void showRecoverPasswdDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover Your Password");

        LinearLayout linearlayout = new LinearLayout(this);
        final EditText emailEt = new EditText(this);
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);
        emailEt.setHint("Email");
        linearlayout.addView(emailEt);
        linearlayout.setPadding(10,10,10,10);
        builder.setView(linearlayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailRe = emailEt.getText().toString().trim();
                if(emailRe.length() != 0 ){
                beginRecovery(emailRe);
                }else
                    {
                    Toast.makeText(MainActivity.this, "Provide Valid Email..." , Toast.LENGTH_SHORT).show();

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();
            }
        });

         builder.create().show();

    }

    private void beginRecovery(String emailRe) {
        progressDialog2.show();
        mAuth.sendPasswordResetEmail(emailRe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog2.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Email Sent....", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Email Sending failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog2.dismiss();
                Toast.makeText(MainActivity.this, "Authentication failed." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loginUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {

                            progressDialog.dismiss();
                            sp.edit().putBoolean("initialLogIn",true).apply();
                            FirebaseUser user = mAuth.getCurrentUser();

                            //FirebaseUser user = mAuth.getCurrentUser();
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



                            Toast.makeText(MainActivity.this, "Registered.." + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), dashboard.class);
                            startActivity(intent);

                        } else
                            { progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Toast.makeText(MainActivity.this, "Authenticating With Google" , Toast.LENGTH_SHORT).show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sp.edit().putBoolean("initialLogIn",true).apply();
                                   FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {

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
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(MainActivity.this, "Authentication failed!" +task.getException(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Toast.makeText(MainActivity.this, "Name of User : " + personName +"\n" +"User ID : " + personId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), dashboard.class);
            startActivity(intent);
        }
    }
}