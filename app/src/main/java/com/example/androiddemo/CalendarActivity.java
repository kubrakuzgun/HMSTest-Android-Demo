package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;
    CalendarView calendarView;
    ToDoListAdapter mAdapter;
    MeetingListAdapter meetingAdapter;
    private List<ToDo> mTodos;
    private List<Meeting> mMeetings;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");
        Log.d("main username", user);

        calendarView = findViewById(R.id.calendarView);
        RecyclerView todaysTodoList = findViewById(R.id.currentTodoListView);
        RecyclerView todaysMeetingList = findViewById(R.id.currentMeetingListView);
        TextView textNotodo, textNoMeeting;
        textNotodo = findViewById(R.id.text_calNotodo);
        textNoMeeting = findViewById(R.id.text_calNoMeeting);

        ///
        databaseHelper = new DatabaseHelper(CalendarActivity.this);
        ///
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        ///
        todaysTodoList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        todaysMeetingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        Calendar currentDateTime = Calendar.getInstance();
        String dd_string = String.valueOf(currentDateTime.get(Calendar.DAY_OF_MONTH));
        String month_string = currentDateTime.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day_string = currentDateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        String currentDate = sdf.format(currentDateTime.getTime());
        Log.d("formatted date", currentDate);

        //List To do
        mTodos = new ArrayList<>();
        mTodos.clear();
        mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), currentDate));
        mAdapter = new ToDoListAdapter(CalendarActivity.this, mTodos);
        if (mAdapter.getItemCount() > 0) {
            textNotodo.setVisibility(View.INVISIBLE);
            mTodos.clear();
            mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), currentDate));
            //attach adapter to activity's view (add card to recycler view)
            todaysTodoList.setAdapter(mAdapter);
        }

        //List Meeting
        mMeetings = new ArrayList<>();
        mMeetings.clear();
        mMeetings.addAll(databaseHelper.getMeetingByDate(databaseHelper.getIDfromUsername(user), currentDate));
        meetingAdapter = new MeetingListAdapter(CalendarActivity.this, mMeetings);
        if (meetingAdapter.getItemCount() > 0) {
            textNoMeeting.setVisibility((View.INVISIBLE));
            mMeetings.clear();
            mMeetings.addAll(databaseHelper.getMeetingByDate(databaseHelper.getIDfromUsername(user),currentDate));
            todaysMeetingList.setAdapter(meetingAdapter);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                textNotodo.setVisibility(View.VISIBLE);
                textNoMeeting.setVisibility(View.VISIBLE);
                todaysTodoList.setAdapter(null);
                todaysMeetingList.setAdapter(null);
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                String formattedDate = sdf.format(calendar.getTime());
                Log.d("formatDate", "sDate formatted: " + formattedDate);
                //List To do
                mTodos = new ArrayList<>();
                mTodos.clear();
                mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), formattedDate));
                mAdapter = new ToDoListAdapter(CalendarActivity.this, mTodos);
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
                if (mMeetings != null) {
                    meetingAdapter = new MeetingListAdapter(CalendarActivity.this, mMeetings);
                    if (meetingAdapter.getItemCount() > 0) {
                        textNoMeeting.setVisibility((View.INVISIBLE));
                        mMeetings.clear();
                        mMeetings.addAll(databaseHelper.getMeetingByDate(databaseHelper.getIDfromUsername(user), formattedDate));
                        todaysMeetingList.setAdapter(meetingAdapter);
                    }
                }
            }
        });


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
                Intent profileIntent = new Intent(getApplicationContext(), MainActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }

            case R.id.nav_todo: {
                Intent todoIntent = new Intent(getApplicationContext(), CreateToDoActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(todoIntent);
                break;
            }
            case R.id.nav_meetings: {
                Intent notesIntent = new Intent(getApplicationContext(), CreateMeetingsActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(notesIntent);
                break;
            }
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
            case R.id.nav_settings: {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}