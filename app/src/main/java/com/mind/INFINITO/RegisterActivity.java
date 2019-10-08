package com.mind.INFINITO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;

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
    private  EditText uEmail, uUserName, uPasswd, fvGame,phonNo;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    SharedPreferences sp;
    String User,favGame,phone;
    byte[] pngData;
    String QRId;

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
         phonNo=findViewById(R.id.phonNo);
        uEmail = findViewById(R.id.uEmail);
        uUserName = findViewById(R.id.uUserName);
        uPasswd = findViewById(R.id.uPasswd);
        fvGame = findViewById(R.id.fvGame);


        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User....");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  phone=phonNo.getText().toString().trim();
                 String Email = uEmail.getText().toString().trim();
                 User = uUserName.getText().toString().trim();
                String Password = uPasswd.getText().toString().trim();
                 favGame = fvGame.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    uEmail.setError("Enter Valid Email Adress");
                    uEmail.setFocusable(true);
                    }
               else if (Password.length()<6){

                    uPasswd.setError("Password Length atleast 6");
                    uPasswd.setFocusable(true);
                }

              else if ((phonNo.length()<10) || (phonNo.length()>10)){

                    phonNo.setError("Enter Valid Phone Number");
                    phonNo.setFocusable(true);
                }
                else{

                    registerUser(Email,Password);
                }
            }
        });
    }

   /* private   byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        pngData = pngOutputStream.toByteArray();
        return pngData;
    }*/

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           sp.edit().putBoolean("initialLogIn",true).apply();
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email =user.getEmail();
                            String uid = user.getUid();
                            String phonef="+91-".concat(phone);
                        /*    QRId="INF19".concat(uid.substring(0,10));
                            try {
                                getQRCodeImage("This is my first QR Code", 350, 350);
                            } catch (WriterException e) {
                                System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
                            } catch (IOException e) {
                                System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
                            }
                            String pngDataString=pngData.toString();*/

                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",User);
                            hashMap.put("phone",phonef);
                            hashMap.put("fav",favGame);
                            hashMap.put("image","No Image");
                         //   hashMap.put("QRCode",pngDataString);
                            FirebaseDatabase database =FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("users");
                            reference.child(uid).setValue(hashMap);
                            Toast.makeText(RegisterActivity.this,"Registered.."+user.getEmail(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

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
