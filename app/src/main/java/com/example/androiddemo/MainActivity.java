package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;

    //Define string to keep bundle of username:
    String user;
    //Define DataBaseHelper:
    private DatabaseHelper databaseHelper;
    //List Adapters
    ToDoListAdapter mAdapter;
    MeetingListAdapter meetingAdapter;
    //List Of records
    private List<ToDo> mTodos;
    private List<Meeting> mMeetings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get data from Bundle object
        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");
        Log.d("main username", user);

        //Define the design tools and assigned the corresponding id to them
        TextView textDD = findViewById(R.id.text_dd);
        TextView textMonth = findViewById(R.id.text_month);
        TextView textDay = findViewById(R.id.text_day);
        RecyclerView todaysTodoList = findViewById(R.id.todaysTodoListView);
        TextView textNotodo = findViewById(R.id.text_notodo);
        TextView textNoMeeting = findViewById(R.id.text_nomeeting);
        RecyclerView todaysMeetingList = findViewById(R.id.todaysMeetingListView);

        ///construct an object for DatabaseHelper class. (set the current activity as context argument)
        databaseHelper = new DatabaseHelper(MainActivity.this);
        ///
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
        ///Locate the lists to this activity layout.
        todaysTodoList.setLayoutManager(new LinearLayoutManager(this));
        todaysMeetingList.setLayoutManager(new LinearLayoutManager(this));


        //display current date

        Calendar currentDateTime = Calendar.getInstance();
        String dd_string = String.valueOf(currentDateTime.get(Calendar.DAY_OF_MONTH));

        String month_string = currentDateTime.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day_string = currentDateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        textDD.setText(dd_string);
        textMonth.setText(month_string);
        textDay.setText(day_string);

        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        String formattedDate = sdf.format(currentDateTime.getTime());
        Log.d("formatted date", formattedDate);

        //List To do
        mTodos = new ArrayList<>();
        mTodos.clear();
        mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), formattedDate));
        mAdapter = new ToDoListAdapter(MainActivity.this, mTodos);
        if (mAdapter.getItemCount() > 0) {
            textNotodo.setVisibility(View.INVISIBLE);
            mTodos.clear();
            mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), formattedDate));
            //attach adapter to activity's view (add card to recycler view)
            todaysTodoList.setAdapter(mAdapter);
        }

        //List Meeting
        mMeetings = new ArrayList<>();
        mMeetings.clear();
        mMeetings.addAll(databaseHelper.getMeetingByDate(databaseHelper.getIDfromUsername(user), formattedDate));
        meetingAdapter = new MeetingListAdapter(MainActivity.this, mMeetings);
        if (meetingAdapter.getItemCount() > 0) {
            textNoMeeting.setVisibility((View.INVISIBLE));
            mMeetings.clear();
            mMeetings.addAll(databaseHelper.getMeetingByDate(databaseHelper.getIDfromUsername(user),formattedDate));
           todaysMeetingList.setAdapter(meetingAdapter);
        }


        //side menu
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        View headerView = navView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.header_username);
        TextView navUserDetail = headerView.findViewById(R.id.header_userdetail);
        CircleImageView navProfilepicture = headerView.findViewById(R.id.header_pp);

        navUsername.setText(user);
        navUserDetail.setText("Level 1");

        if(preferences.contains("profilePicture"))
        {

            String encodedImage = preferences.getString("profilePicture",null);

            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);

            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);

            navProfilepicture.setImageBitmap(bitmapImage);
        }



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
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(calendarIntent);
                break;
            }
            case R.id.nav_todo: {
                Intent todoIntent = new Intent(MainActivity.this, CreateToDoActivity.class);
                todoIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(todoIntent);
                break;
            }
            case R.id.nav_meetings: {
                Intent meetingsIntent = new Intent(MainActivity.this, CreateMeetingsActivity.class);
                meetingsIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(meetingsIntent);
                break;
            }
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_logout: {
                editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
            case R.id.nav_settings: {
                editor.clear();
                editor.apply();
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    public void addToDo(View view){
        Intent todoIntent = new Intent(MainActivity.this, CreateToDoActivity.class);
        todoIntent.putExtra("username", user);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(todoIntent);
    }

    public void addMeeting(View view){
        Intent meetingsIntent = new Intent(MainActivity.this, CreateMeetingsActivity.class);
        meetingsIntent.putExtra("username", user);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(meetingsIntent);
    }




}