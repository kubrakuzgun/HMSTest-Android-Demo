package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class CreateMeetingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;
    EditText edt_end, edt_start, edt_title, edt_date;
    ImageButton btn_StartEdit, btn_EndEdit, btn_DateEdit;
    Button btn_OK, btn_Cancel;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meetings);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");
        Log.d("Meeting username", user);

        databaseHelper = new DatabaseHelper(CreateMeetingsActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();


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

        blurView = findViewById(R.id.view_blurbackground5);
        blurView.setVisibility(View.INVISIBLE);


        definitions();
        timeEditClicks();
        Meeting meeting = new Meeting();

        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add to-do
                meeting.setMeetingTitle(edt_title.getText().toString().trim());
                meeting.setMeetingDate(edt_date.getText().toString().trim());
                meeting.setMeetingStart(edt_start.getText().toString().trim());
                meeting.setMeetingEnd(edt_end.getText().toString().trim());

                Bundle extras = getIntent().getExtras();
                user = extras.getString("username");

                meeting.setMeeetingUserID(databaseHelper.getIDfromUsername(user));
                databaseHelper.addMeeting(meeting);
            }
        });

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
                Intent profileIntent = new Intent(CreateMeetingsActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_calendar: {
                Intent calendarIntent = new Intent(CreateMeetingsActivity.this, CalendarActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(calendarIntent);
                break;
            }
            case R.id.nav_todo: {
                Intent todoIntent = new Intent(CreateMeetingsActivity.this, ToDoActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(todoIntent);
                break;
            }

            case R.id.nav_profile: {
                Intent profileIntent = new Intent(CreateMeetingsActivity.this, ProfileActivity.class);
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
                Intent loginIntent = new Intent(CreateMeetingsActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
            case R.id.nav_settings: {
                Intent settingsIntent = new Intent(CreateMeetingsActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void definitions() {
        btn_StartEdit = (ImageButton) findViewById(R.id.btn_meetingStartEdit);
        btn_EndEdit = (ImageButton) findViewById(R.id.btn_meetingEndEdit);
        btn_DateEdit = (ImageButton) findViewById(R.id.btn_meetingDateEdit);

        edt_start = (EditText) findViewById(R.id.edtxt_meetingStart);
        edt_end = (EditText) findViewById(R.id.edtxt_meetingEnd);
        edt_date = (EditText) findViewById(R.id.edtxt_meetingDate);
        edt_title = (EditText) findViewById(R.id.edtxt_meetingTitle);

        btn_OK = (Button) findViewById(R.id.btn_meetingOK);
        btn_Cancel = (Button) findViewById(R.id.btn_meetingCancel);
    }

    public void timeEditClicks() {
        //Edit Date
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener datedialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                edt_date.setText(sdf.format(myCalendar.getTime()));
            }

        };

        btn_DateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateMeetingsActivity.this, datedialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Edit Start Time
        btn_StartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//
                TimePickerDialog timePicker;

                timePicker = new TimePickerDialog(CreateMeetingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_start.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                timePicker.setTitle("Start Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

                timePicker.show();

            }
        });
        //Edit End Time
        btn_EndEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(CreateMeetingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_end.setText(String.format("%02d:%02d", selectedHour, selectedMinute));//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("End Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

                timePicker.show();

            }
        });
    }

}


