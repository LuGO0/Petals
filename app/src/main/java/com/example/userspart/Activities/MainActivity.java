package com.example.userspart.Activities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userspart.Adapters.EventAdapter;
import com.example.userspart.CustomTypes.Event;
import com.example.userspart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private ActionBar mActionBar;
    private static final String TAG = "MainActivity";

    //// Firebase instance variables
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView usernameView;
    private Button mAlertButton;

    //database read write oprations
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mActionBar = getSupportActionBar();



        // Initialize Firebase components
        mUsername = ANONYMOUS;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .setLogo(R.mipmap.ic_launcher_foreground)
                                    .setTheme(R.style.Theme_MaterialComponents_NoActionBar_Bridge)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };




        setupDrawerLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.about_us)
        {
            //will do this when we will do our work on the credits activity
            // Intent intent=new Intent(MainActivity.this, CreditsActivity.class);
            // startActivity(intent);
            // return true;
        }

        if(mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }



    private void onSignedInInitialize(String username) {
        mUsername = username;
        //attachDatabaseReadListener();
        // Log.i(mUsername,"helllo");
        mFirebaseUser=FirebaseAuth.getInstance().getCurrentUser();
//        if(mFirebaseUser.isEmailVerified())
//        {
        //writeNewUser();
//
//        }
//        else
//        {
//
       // mFirebaseUser.sendEmailVerification();
//        }
        //attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        //mMessageAdapter.clear();
        //detachDatabaseReadListener();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
//                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        //        mMessageAdapter.clear();
        //  detachDatabaseReadListener();
    }

    // this fuction sets up drawer layout for the app which is the primary feature for the
    // navigation drawer
    void setupDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);


        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerStateChanged(int newState) {

                super.onDrawerStateChanged(newState);
                boolean isOpened = mDrawerLayout.isDrawerOpen(mNavigationView);
                boolean isVisible = mDrawerLayout.isDrawerVisible(mNavigationView);

                if (!isOpened && !isVisible) {

                    if (newState == DrawerLayout.STATE_IDLE) {
                        restoreActionBar();
                    } else {
                        overrideActionBar();
                    }
                }
            }

            private void restoreActionBar() {
                getSupportActionBar().setTitle("Petals");
                mActionBar.show();
                supportInvalidateOptionsMenu();
            }

            private void overrideActionBar() {
                mActionBar.hide();
                supportInvalidateOptionsMenu();
            }
        };


        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mActionBar.setDisplayHomeAsUpEnabled(true);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_drawer_menu);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_item:
                        Toast.makeText(MainActivity.this, "NAVIGATION", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this, LocomotionActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.events_item:
                        Toast.makeText(MainActivity.this, "EVENTS", Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent(MainActivity.this, EventsActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.signOut_item:
                        AuthUI.getInstance().signOut(MainActivity.this);
                        break;

                    default:
                        return true;
                }

                return true;
            }
        });
    }

    //simple Db read write
    private void writeNewUser() {

        mDatabase.child("users").child(mFirebaseUser.getDisplayName()).setValue(mFirebaseUser.getUid());
    }
}






