package com.example.easyexchangeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    // firebase Auth
    FirebaseAuth firebaseAuth;


    // views
    //TextView mProfileTextView;

    ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ActionBar and its title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");


        firebaseAuth = FirebaseAuth.getInstance();

        // init view
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        // home frequent transactions (default on start)
        actionBar.setTitle("Home"); // change actionbar title

        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();


        //mProfileTextView = findViewById(R.id.profile);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                {
                    // handle item clicks
                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home:
                            // home frequent transactions
                            actionBar.setTitle("Home"); // change actionbar title

                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,fragment1,"");
                            ft1.commit();

                            return true;
                        case R.id.nav_profile:
                            // profile frequent transactions
                            // home frequent transactions
                            actionBar.setTitle("Profile"); // change actionbar title

                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,fragment2,"");
                            ft2.commit();
                            return true;
                        case R.id.nav_user:
                            // home frequent transactions
                            actionBar.setTitle("User"); // change actionbar title

                            UserFragment fragment3 = new UserFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,fragment3,"");
                            ft3.commit();
                            //user frequent transactions
                            return true;

                    }


                    return false;
                }
            };

    private void checkUserStatus()
    {
        // get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null)
        {
            //user is signed in , stay here
            // set email of logged in user

            actionBar.setTitle("AAA");


            //mProfileTextView.setText(user.getEmail());


        }
        else
        {
            // user not signed in , go to main activity
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        // check the start of app
        checkUserStatus();
        super.onStart();
    }
    /*inflate options menu*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // infiltrate menu
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    /*handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get item id
        int id = item.getItemId();

        if(id == R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkUserStatus();
        }


        return super.onOptionsItemSelected(item);
    }
}