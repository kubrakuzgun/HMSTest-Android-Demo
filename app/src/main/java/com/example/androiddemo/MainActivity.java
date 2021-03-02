//Training 5-Main
package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ToDoListAdapter.OnItemClickListener, MeetingListAdapter.OnItemClickListener {

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
    RecyclerView todaysTodoList;
    RecyclerView todaysMeetingList;
    TextView textNotodo;
    TextView textNoMeeting;
    String formattedDate;
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
        todaysTodoList = findViewById(R.id.todaysTodoListView);
        textNotodo = findViewById(R.id.text_notodo);
        textNoMeeting = findViewById(R.id.text_nomeeting);
        todaysMeetingList = findViewById(R.id.todaysMeetingListView);

        ///construct an object for DatabaseHelper class. (set the current activity as context argument)
        databaseHelper = new DatabaseHelper(MainActivity.this);
        ///
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
        formattedDate = sdf.format(currentDateTime.getTime());
        Log.d("formatted date", formattedDate);

        //List To do
        mTodos = new ArrayList<>();
        mTodos.clear();
        mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), formattedDate));
        mAdapter = new ToDoListAdapter(MainActivity.this, mTodos);
        mAdapter.setOnItemClickListener(MainActivity.this);
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
        meetingAdapter.setOnItemClickListener(MainActivity.this);
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
                Intent todoIntent = new Intent(MainActivity.this, MainActivity.class);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("Do you want to log out?");
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alert.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor = preferences.edit();
                        editor.remove("isLoggedIn");
                        editor.clear();
                        editor.apply();
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(loginIntent);
                    }
                });

                alert.show();
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
    public void todoRefreshRecylerView(){
        mAdapter.notifyDataSetChanged();
        mTodos.clear();
        mTodos.addAll(databaseHelper.getTodoByDate(databaseHelper.getIDfromUsername(user), formattedDate));
        //list all to-do
        mAdapter = new ToDoListAdapter(MainActivity.this, mTodos);
        mAdapter.setOnItemClickListener(MainActivity.this);
        todaysTodoList.setAdapter(mAdapter);
    }


    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void onEditClick(int position) {
        //TODO add edit to-do func

        //get selected to-do's position and key
        final ToDo selectedItem = mTodos.get(position);
        Log.d("position", ": " + mTodos.get(position));
        Log.d("todo", ": " + selectedItem.getTodoID());


        //create alert to confirm delete
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        LinearLayout ll = new LinearLayout(MainActivity.this);
        final EditText newTitle = new EditText(MainActivity.this);
        newTitle.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newTitle.setHint("Title");
        newTitle.setText(selectedItem.getTodoTitle());

        final EditText newDesc = new EditText(MainActivity.this);
        newDesc.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newDesc.setHint("Description");
        newDesc.setText(selectedItem.getTodoDesc());

        final EditText newDate = new EditText(MainActivity.this);
        newDate.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newDate.setHint("Date");
        newDate.setFocusable(false);
        newDate.setClickable(true);
        newDate.setText(selectedItem.getTodoDate());

        final Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener editdatedialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                newCalendar.set(Calendar.YEAR, year);
                newCalendar.set(Calendar.MONTH, monthOfYear);
                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                newDate.setText(sdf.format(newCalendar.getTime()));
            }

        };

        newDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, editdatedialog, newCalendar
                        .get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(newTitle);
        ll.addView(newDesc);
        ll.addView(newDate);
        alert.setTitle("Edit TODO");
        alert.setView(ll);


        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //update to-do
                selectedItem.setTodoTitle(newTitle.getText().toString().trim());
                selectedItem.setTodoDesc(newDesc.getText().toString().trim());
                selectedItem.setTodoDate(newDate.getText().toString().trim());
                databaseHelper.updateToDo(selectedItem);

                todoRefreshRecylerView();
                if(mAdapter.getItemCount()==0){
                    textNotodo= findViewById(R.id.text_notodo);
                    todaysTodoList.setVisibility(View.GONE);
                    textNotodo.setVisibility(View.VISIBLE);
                }

            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();

    }


    @Override
    public void onDeleteClick(int position) {
        //get selected to-do's position and key
        final ToDo selectedItem = mTodos.get(position);
        Log.d("position", ": " + mTodos.get(position));
        Log.d("todo", ": " + selectedItem.getTodoID());


        //create alert to confirm delete
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Do you want to delete this TODO?");

        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //delete to-do
                databaseHelper.deleteToDo(selectedItem);

                todoRefreshRecylerView();

                if(mAdapter.getItemCount()==0){
                    textNotodo= findViewById(R.id.text_notodo);
                    todaysTodoList.setVisibility(View.GONE);
                    textNotodo.setVisibility(View.VISIBLE);
                }
            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();

    }
    public void meetingRefreshRecylerView(){
        meetingAdapter.notifyDataSetChanged();
        mMeetings.clear();
        mMeetings.addAll(databaseHelper.getMeetingByDate(databaseHelper.getIDfromUsername(user), formattedDate));
        //list all meeting
        meetingAdapter = new MeetingListAdapter(MainActivity.this, mMeetings);
        meetingAdapter.setOnItemClickListener(MainActivity.this);
        todaysMeetingList.setAdapter(meetingAdapter);
    }


    @Override
    public void meetingOnItemClick(int position) {

    }


    @Override
    public void meetingOnEditClick(int position) {
        //Meeting add edit meeting func

        //get selected meeting's position and key
        final Meeting meetingSelectedItem = mMeetings.get(position);
        Log.d("position", ": " + mMeetings.get(position));
        Log.d("meeting", ": " + meetingSelectedItem.getMeetingID());


        //create alert to confirm delete
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        LinearLayout ll = new LinearLayout(MainActivity.this);
        final EditText newMeetingTitle = new EditText(MainActivity.this);
        newMeetingTitle.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newMeetingTitle.setHint("Meeting Title");
        newMeetingTitle.setText(meetingSelectedItem.getMeetingTitle());

        final EditText newMeetingDate = new EditText(MainActivity.this);
        newMeetingDate.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newMeetingDate.setHint("Meeting Date");
        newMeetingDate.setFocusable(false);
        newMeetingDate.setClickable(true);
        newMeetingDate.setText(meetingSelectedItem.getMeetingDate());
        final Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener editdatedialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                newCalendar.set(Calendar.YEAR, year);
                newCalendar.set(Calendar.MONTH, monthOfYear);
                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                newMeetingDate.setText(sdf.format(newCalendar.getTime()));
            }

        };
        newMeetingDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, editdatedialog, newCalendar
                        .get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText newMeetingStart = new EditText(MainActivity.this);
        newMeetingStart.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newMeetingStart.setHint("Start Time");
        newMeetingStart.setFocusable(false);
        newMeetingStart.setClickable(true);
        newMeetingStart.setText(meetingSelectedItem.getMeetingStart());

        newMeetingStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        newMeetingStart.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                timePicker.setTitle("Start Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

                timePicker.show();
            }
        });


        final EditText newMeetingEnd = new EditText(MainActivity.this);
        newMeetingEnd.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newMeetingEnd.setHint("End Time");
        newMeetingEnd.setFocusable(false);
        newMeetingEnd.setClickable(true);
        newMeetingEnd.setText(meetingSelectedItem.getMeetingEnd());

        newMeetingEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        newMeetingEnd.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                timePicker.setTitle("Start Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

                timePicker.show();
            }
        });



        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(newMeetingTitle);
        ll.addView(newMeetingDate);
        ll.addView(newMeetingStart);
        ll.addView(newMeetingEnd);
        alert.setTitle("Edit Meeting");
        alert.setView(ll);


        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //update to-do
                meetingSelectedItem.setMeetingTitle(newMeetingTitle.getText().toString().trim());
                meetingSelectedItem.setMeetingDate(newMeetingDate.getText().toString().trim());
                meetingSelectedItem.setMeetingStart(newMeetingStart.getText().toString().trim());
                meetingSelectedItem.setMeetingEnd(newMeetingEnd.getText().toString().trim());
                databaseHelper.updateMeeting(meetingSelectedItem);

                meetingRefreshRecylerView();
                if(meetingAdapter.getItemCount()==0){
                    textNoMeeting= findViewById(R.id.text_nomeeting);
                    todaysMeetingList.setVisibility(View.GONE);
                    textNoMeeting.setVisibility(View.VISIBLE);
                }

            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();

    }


    @Override
    public void meetingOnDeleteClick(int position) {
        //get selected to-do's position and key
        final Meeting meetingSelectedItem = mMeetings.get(position);
        Log.d("position", ": " + mMeetings.get(position));
        Log.d("meeting", ": " + meetingSelectedItem.getMeetingID());


        //create alert to confirm delete
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Do you want to delete this Meeting?");

        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //delete meeting
                databaseHelper.deleteMeeting(meetingSelectedItem);

                meetingRefreshRecylerView();

                if(meetingAdapter.getItemCount()==0){
                    textNoMeeting= findViewById(R.id.text_nomeeting);
                    todaysMeetingList.setVisibility(View.GONE);
                    textNoMeeting.setVisibility(View.VISIBLE);
                }
            }

        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();

    }

}