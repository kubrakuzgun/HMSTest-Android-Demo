package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
    String user;
    private DatabaseHelper databaseHelper;
    ToDoListAdapter mAdapter;
    //List Of records
    private List<ToDo> mTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");

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
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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

        //List today's to do
/*        mTodos = new ArrayList<>();
        mTodos.clear();
        mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), formattedDate));
        mAdapter = new ToDoListAdapter(MainActivity.this, mTodos);
        if (mAdapter.getItemCount() > 0) {
            textNotodo.setVisibility(View.INVISIBLE);
            mTodos.clear();
            mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), formattedDate));
            //attach adapter to activity's view (add card to recycler view)
            todaysTodoList.setAdapter(mAdapter);
        }*/


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
        navUserDetail.setText("Hello!");

        if(preferences.contains("profilePicture"))
        {

            String encodedImage = preferences.getString("profilePicture",null);

            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);

            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);

            navProfilepicture.setImageBitmap(bitmapImage);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

/*            case R.id.nav_calendar: {
                Intent calendarIntent = new Intent(MainActivity.this, CalendarActivity.class);
                calendarIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(calendarIntent);
                break;
            }*/
            case R.id.nav_todo: {
                Intent todoIntent = new Intent(MainActivity.this, CreateToDoActivity.class);
                todoIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(todoIntent);
                break;
            }
/*            case R.id.nav_meetings: {
                Intent meetingsIntent = new Intent(MainActivity.this, CreateMeetingsActivity.class);
                meetingsIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(meetingsIntent);
                break;
            }*/
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_logout: {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("Do you want to log out?");
                alert.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor = preferences.edit();

                        //remove isLoggedIn key, delete other values (not keys)


                        //go to login activity
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(loginIntent);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.show();
                break;
            }
/*
            case R.id.nav_settings: {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }
*/

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
        startActivity(todoIntent);
    }


}