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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_editToDo, btn_addToDo, btn_editMeeting, btn_addMeeting, btn_editBirth, btn_addBirth;
    CheckBox chk_ToDo;
    Button btn_Today, btn_Calendar, btn_Settings, btn_Me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonDefinition();
        //editButtonsClick();
        addButtonsClick();
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


    }

    public void buttonDefinition() {
        btn_addToDo = (ImageButton) findViewById(R.id.btn_mainAddToDo);
        btn_editToDo = (ImageButton) findViewById(R.id.btn_mainEditTodo);
        btn_addMeeting = (ImageButton) findViewById(R.id.btn_mainAddMeeting);
        btn_editMeeting = (ImageButton) findViewById(R.id.btn_mainEditMeeting);
        btn_addBirth = (ImageButton) findViewById(R.id.btn_mainAddBirth);
        btn_editBirth = (ImageButton) findViewById(R.id.btn_mainEditBirth);
        btn_Today = (Button) findViewById(R.id.btn_todayTab);
        btn_Calendar = (Button) findViewById(R.id.btn_CalendarTab);
        btn_Settings = (Button) findViewById(R.id.btn_SettingsTab);
        btn_Me = (Button) findViewById(R.id.btn_MeTab);
    }

    public void addButtonsClick(){
        //add to do
        btn_addToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ToDoActivity.class);
                startActivity(intent);
            }
        });
        //add meeting
        btn_addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetingActivity.class);
                startActivity(intent);

            }
        });
        // add birthday
        btn_addBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Birthday.class);
                startActivity(intent);
            }
        });
    }

}