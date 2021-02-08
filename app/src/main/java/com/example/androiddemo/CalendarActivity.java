package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.navigation.NavigationView;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        databaseHelper = new DatabaseHelper(CalendarActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        //side menu
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        blurView = findViewById(R.id.view_blurbackground3);
        blurView.setVisibility(View.INVISIBLE);

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                blurView.setVisibility(View.VISIBLE);
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                blurView.setVisibility(View.INVISIBLE);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        Animation blurAnimation =
                                AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.fade_in);
                        blurView.startAnimation(blurAnimation);
                    } else {
                        Animation blurAnimation =
                                AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.fade_out);
                        blurView.startAnimation(blurAnimation);
                    }
                }
                super.onDrawerStateChanged(newState);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_home: {
                Intent profileIntent = new Intent(CalendarActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }

            case R.id.nav_todo: {
                Intent todoIntent = new Intent(CalendarActivity.this, ToDoActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(todoIntent);
                break;
            }
            case R.id.nav_meetings: {
                Intent notesIntent = new Intent(CalendarActivity.this, MeetingsActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(notesIntent);
                break;
            }
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(CalendarActivity.this, ProfileActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_logout: {
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = preferences.edit();
                editor.remove("isLoggedIn");
                editor.apply();
                editor.clear();
                editor.apply();
                Intent loginIntent = new Intent(CalendarActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
            case R.id.nav_settings: {
                Intent settingsIntent = new Intent(CalendarActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}