package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getIntent().getExtras().getString("username","defaultuser");
        Log.d("main username", user);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TextView textDD = findViewById(R.id.text_dd);
        TextView textMonth = findViewById(R.id.text_month);
        TextView textDay = findViewById(R.id.text_day);


        //display current date

        Calendar currentDateTime = Calendar.getInstance();
        String dd_string = String.valueOf(currentDateTime.get(Calendar.DAY_OF_MONTH));

        String month_string = currentDateTime.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day_string = currentDateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        textDD.setText(dd_string);
        textMonth.setText(month_string);
        textDay.setText(day_string);


        //side menu
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        View headerView = navView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.header_username);
        TextView navUserDetail = headerView.findViewById(R.id.header_userdetail);

        navUsername.setText(user);
        navUserDetail.setText("Level 1");


        blurView = findViewById(R.id.view_blurbackground);
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

            case R.id.nav_calendar: {
                Intent calendarIntent = new Intent(MainActivity.this, CalendarActivity.class);
                calendarIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(calendarIntent);
                break;
            }
            case R.id.nav_todo: {
                Intent todoIntent = new Intent(MainActivity.this, ToDoActivity.class);
                todoIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(todoIntent);
                break;
            }
            case R.id.nav_meetings: {
                Intent meetingsIntent = new Intent(MainActivity.this, MeetingsActivity.class);
                meetingsIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(meetingsIntent);
                break;
            }
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_logout: {
                editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
            case R.id.nav_settings: {
                editor.clear();
                editor.apply();
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            finishAffinity();
        }
    }

}