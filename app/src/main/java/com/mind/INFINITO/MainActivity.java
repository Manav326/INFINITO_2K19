package com.mind.INFINITO;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

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
    private TextView tvForgot,register;
    private Button logIn;
    private EditText uName1, uPasswd1;
    private static final int RC_SIGN_IN = 100;
    private SignInButton mGoogleLogIn;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog,progressDialog2;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    SharedPreferences sp;
    byte[] pngData;
    String QRId;


    protected void onStart() {
        super.onStart();
        getDelegate().onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent intent = new Intent(this,dashboard.class);
            startActivity(intent);
            finish();
        }
    }

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
        register= findViewById(R.id.register);
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
        register.setOnClickListener(this);
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
            case R.id.register:
                Intent intent1  = new Intent(MainActivity.this,RegisterActivity.class);
                Pair[] pairs1    = new Pair[1];
                pairs1[0] = new Pair<View,String>(tvLogin,"tvLogin");
                ActivityOptions activityOptions1 = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs1);
                startActivity(intent1,activityOptions1.toBundle());
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

   /* private   byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        pngData = pngOutputStream.toByteArray();
        return pngData;
    }
*/

    private void showRecoverPasswdDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover Your Password");
        LinearLayout linearlayout = new LinearLayout(this);
        final EditText emailEt = new EditText(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
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


    private void loginUser(final String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Registered.." + email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else
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
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
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
                        }
                        else {
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
            String uri= personPhoto.toString();
            String email =user.getEmail();
            String uid = user.getUid();
            int index = email.indexOf('@');
            String name = email.substring(0,index);
            /* QRId="INF19".concat(uid.substring(0,10));
            try {
                getQRCodeImage(QRId, 350, 350);
            } catch (WriterException e) {
                System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
            }


            String pngDataString=pngData.toString();*/


                HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("email",email);
            hashMap.put("uid",uid);
            hashMap.put("name",personName);
            hashMap.put("phone","");
            hashMap.put("fav","Sports");
            hashMap.put("image",uri);
           // hashMap.put("QRCode",pngDataString);

            FirebaseDatabase database =FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("users");
            reference.child(uid).setValue(hashMap);

            Toast.makeText(MainActivity.this, "Name of User : " + personName +"\n" +"User ID : " + personId, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), dashboard.class);
            startActivity(intent);
            finish();
        }
    }




}
