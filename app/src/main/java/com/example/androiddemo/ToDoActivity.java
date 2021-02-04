package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ToDoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ToDoListAdapter.OnItemClickListener {
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;
    ToDoListAdapter mAdapter;
    String user;

    private List<ToDo> mTodos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        user = getIntent().getExtras().getString("username","defaultuser");

        databaseHelper = new DatabaseHelper(ToDoActivity.this);
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

        blurView = findViewById(R.id.view_blurbackground4);
        blurView.setVisibility(View.INVISIBLE);

        ToDo todo = new ToDo();
        EditText todoTitle = findViewById(R.id.editText_Title);
        EditText todoDesc = findViewById(R.id.editText_Desc);
        EditText todoDate = findViewById(R.id.editText_Date);
        TextView notodo = findViewById(R.id.text_notodo);
        Button addButton = findViewById(R.id.button_Add);
        RecyclerView todoListView = findViewById(R.id.todoListView);

        todoListView.setLayoutManager(new LinearLayoutManager(this));


        mTodos = new ArrayList<>();
        mTodos.clear();
        mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
        mAdapter = new ToDoListAdapter(ToDoActivity.this, mTodos);
        if(mAdapter.getItemCount()>0){
            notodo.setVisibility(View.INVISIBLE);
            mTodos.clear();
            mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
            //attach adapter to activity's view (add card to recycler view)
            todoListView.setAdapter(mAdapter);
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add to-do
                todo.setTodoTitle(todoTitle.getText().toString().trim());
                todo.setTodoDesc(todoDesc.getText().toString().trim());
                todo.setTodoDate(todoDate.getText().toString().trim());
                todo.setTodoUserID(databaseHelper.getIDfromUsername(user));
                databaseHelper.addToDo(todo);
                mAdapter.notifyDataSetChanged();
                mTodos.clear();
                mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
                //list all to-do
                notodo.setVisibility(View.GONE);
                mAdapter = new ToDoListAdapter(ToDoActivity.this, mTodos);
                todoListView.setAdapter(mAdapter);

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
                drawerLayout.closeDrawer(GravityCompat.START);
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
                Intent profileIntent = new Intent(ToDoActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_calendar: {
                Intent calendarIntent = new Intent(ToDoActivity.this, CalendarActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(calendarIntent);
                break;
            }
            case R.id.nav_meetings: {
                Intent notesIntent = new Intent(ToDoActivity.this, MeetingsActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(notesIntent);
                break;
            }
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(ToDoActivity.this, ProfileActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(profileIntent);
                break;
            }
            case R.id.nav_logout: {
                editor.clear();
                editor.apply();
                Intent loginIntent = new Intent(ToDoActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
            case R.id.nav_settings: {
                editor.clear();
                editor.apply();
                Intent loginIntent = new Intent(ToDoActivity.this, SettingsActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onEditClick(int position) {
        //TODO add edit to-do func
    }

    @Override
    public void onDeleteClick(int position) {
        //get selected memory's position and key
        final ToDo selectedItem = mTodos.get(position);

        //create alert to confirm delete
        AlertDialog.Builder alert = new AlertDialog.Builder(ToDoActivity.this);
        alert.setMessage("Do you want to delete this TODO?");

        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //delete to-do
                databaseHelper.deleteToDo(selectedItem);
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