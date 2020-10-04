package com.example.easyexchangeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    // Views
    EditText mEmailEnt,mPasswordEnt;
    Button mRegisterBtn;
    TextView mHaveAccount;



    // Progressbar to display while registration user
    ProgressDialog progressDialog;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ActionBar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");

        // enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //init
        mEmailEnt = findViewById(R.id.emailEnt);
        mPasswordEnt = findViewById(R.id.passwordEnt);
        mRegisterBtn = findViewById(R.id.register_btn);
        mHaveAccount = findViewById(R.id.have_account);

        // In the onCreate() method , initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering user...");


        // handle register button click
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // input email,password
                String email = mEmailEnt.getText().toString().trim();
                String password = mPasswordEnt.getText().toString().trim();

                // validate email and password
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    //flag error message and focus the email address
                    mEmailEnt.setError("Invalid Email");
                    mEmailEnt.setFocusable(true);
                }
                else if(password.length() < 6)
                {
                    // flag error message and focus the password edittext
                    mPasswordEnt.setError("Password length must at least 6 characters");
                    mPasswordEnt.setFocusable(true);

                }
                else
                {
                    registerUser(email,password);// Register the user
                }


            }
        });

        // handle login textview click listener
        mHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

            }
        });


    }

    private void registerUser(String email, String password) {
        // email and password is valid , show progress dialog and start registering user
        progressDialog.show();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");

                            //Sign in success , dismiss dialog and start register activity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();


                            // Get user id and email from auth
                            String email = user.getEmail();
                            String uid = user.getUid();


                            // When user is already registered store user info in firebase database
                            // using hashmap
                            HashMap<Object,String> hashMap = new HashMap<>();
                            // Put these information in hashmap

                            hashMap.put("email",email);
                            hashMap.put("uid",uid);


                            // This is firebase database instance to store and sync data in
                            // realtime database
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            // Path to store user data named "User"
                            DatabaseReference reference = database.getReference("Users");

                            // Put data within hashmap in database
                            reference.child(uid).setValue(hashMap);

                            // REFERENCE









                            Toast.makeText(RegisterActivity.this,"Registered ...\n"+user.getEmail(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error , dismiss progress dialog and get and show the error message
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, " "+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();// go to previous activity
        return super.onSupportNavigateUp();
    }
}