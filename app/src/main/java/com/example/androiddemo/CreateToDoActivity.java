package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateToDoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private DatabaseHelper databaseHelper;
    String user;

    ToDoListAdapter mAdapter;
    private List<ToDo> mTodos;

    TextView notodo;
    RecyclerView todoListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        EditText todoTitle = findViewById(R.id.editText_Title);
        EditText todoDesc = findViewById(R.id.editText_Desc);
        EditText todoDate = findViewById(R.id.editText_Date);
        Button addButton = findViewById(R.id.button_Add);
        notodo = findViewById(R.id.text_notodo);
        todoListView = findViewById(R.id.todoListView);
        todoListView.setLayoutManager(new LinearLayoutManager(this));

        //get username from extras
        Bundle extras = getIntent().getExtras();
        user= extras.getString("username");

        //side menu & nav header
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.header_username);
        TextView navUserDetail = headerView.findViewById(R.id.header_userdetail);
        navUsername.setText(user);
        navUserDetail.setText("Hello!");

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        databaseHelper = new DatabaseHelper(CreateToDoActivity.this);

        // create an object of To Do Class

        // create ArrayList to store to do objects


        // get all to do from database and add to ArrayList


        // create Adapter to display all to do items in the recycler view


        // if there are previously added todos, add to recycler view


        // date picker dialog

        // date editText on click


        // add button on click

    }


    // override ToDoListAdapter's methods
    // item click

    // edit

    // delete




    // nav menu method override
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_home: {
                Intent homeIntent = new Intent(CreateToDoActivity.this, MainActivity.class);
                homeIntent.putExtra("username", user);
                startActivity(homeIntent);
                break;
            }
/*            case R.id.nav_calendar: {
                Intent calendarIntent = new Intent(CreateToDoActivity.this, CalendarActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(calendarIntent);
                break;
            }
            case R.id.nav_meetings: {
                Intent notesIntent = new Intent(CreateToDoActivity.this, CreateMeetingsActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(notesIntent);
                break;
            }*/
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(CreateToDoActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_logout: {
                editor = preferences.edit();
                editor.remove("isLoggedIn");
                editor.clear();
                editor.apply();
                Intent loginIntent = new Intent(CreateToDoActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            }
/*            case R.id.nav_settings: {
                Intent settingsIntent = new Intent(CreateToDoActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }*/
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}