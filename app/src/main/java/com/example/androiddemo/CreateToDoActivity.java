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

public class CreateToDoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ToDoListAdapter.OnItemClickListener {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;

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

        Bundle extras = getIntent().getExtras();
        user= extras.getString("username");
        Log.d("todo username", user);


        databaseHelper = new DatabaseHelper(CreateToDoActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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

        blurView = findViewById(R.id.view_blurbackground4);
        blurView.setVisibility(View.INVISIBLE);

        ToDo todo = new ToDo();
        EditText todoTitle = findViewById(R.id.editText_Title);
        EditText todoDesc = findViewById(R.id.editText_Desc);
        EditText todoDate = findViewById(R.id.editText_Date);
        Button addButton = findViewById(R.id.button_Add);

        notodo = findViewById(R.id.text_notodo);
        todoListView = findViewById(R.id.todoListView);

        todoListView.setLayoutManager(new LinearLayoutManager(this));


        mTodos = new ArrayList<>();
        mTodos.clear();
        mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
        mAdapter = new ToDoListAdapter(CreateToDoActivity.this, mTodos);
        mAdapter.setOnItemClickListener(CreateToDoActivity.this);
        if(mAdapter.getItemCount()>0){
            notodo.setVisibility(View.INVISIBLE);
            mTodos.clear();
            mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
            mAdapter.setOnItemClickListener(CreateToDoActivity.this);
            //attach adapter to activity's view (add card to recycler view)
            todoListView.setAdapter(mAdapter);
        }

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
                todoDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        todoDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateToDoActivity.this, datedialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add to-do
                todo.setTodoTitle(todoTitle.getText().toString().trim());
                todo.setTodoDesc(todoDesc.getText().toString().trim());
                todo.setTodoDate(todoDate.getText().toString().trim());

                Bundle extras = getIntent().getExtras();
                user= extras.getString("username");

                todo.setTodoUserID(databaseHelper.getIDfromUsername(user));

                databaseHelper.addToDo(todo);
                mAdapter.notifyDataSetChanged();
                mTodos.clear();
                mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
                //list all to-do
                notodo.setVisibility(View.GONE);
                mAdapter = new ToDoListAdapter(CreateToDoActivity.this, mTodos);
                mAdapter.setOnItemClickListener(CreateToDoActivity.this);
                todoListView.setAdapter(mAdapter);

                Toast.makeText(CreateToDoActivity.this, "TODO CREATED", Toast.LENGTH_LONG).show();

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
                Intent homeIntent = new Intent(CreateToDoActivity.this, MainActivity.class);
                homeIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(homeIntent);
                break;
            }
            case R.id.nav_calendar: {
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
            }
            case R.id.nav_profile: {
                Intent profileIntent = new Intent(CreateToDoActivity.this, ProfileActivity.class);
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
                Intent loginIntent = new Intent(CreateToDoActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(loginIntent);
                break;
            }
            case R.id.nav_settings: {
                Intent settingsIntent = new Intent(CreateToDoActivity.this, SettingsActivity.class);
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
        AlertDialog.Builder alert = new AlertDialog.Builder(CreateToDoActivity.this);
        LinearLayout ll = new LinearLayout(CreateToDoActivity.this);
        final EditText newTitle = new EditText(CreateToDoActivity.this);
        newTitle.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newTitle.setHint("Title");
        newTitle.setText(selectedItem.getTodoTitle());

        final EditText newDesc = new EditText(CreateToDoActivity.this);
        newDesc.setInputType(InputType.TYPE_CLASS_TEXT  | InputType.TYPE_TEXT_VARIATION_NORMAL);
        newDesc.setHint("Description");
        newDesc.setText(selectedItem.getTodoDesc());

        final EditText newDate = new EditText(CreateToDoActivity.this);
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
                new DatePickerDialog(CreateToDoActivity.this, editdatedialog, newCalendar
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

                mTodos = new ArrayList<>();

                mAdapter.notifyDataSetChanged();
                mTodos.clear();
                mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
                //list all to-do
                mAdapter = new ToDoListAdapter(CreateToDoActivity.this, mTodos);
                todoListView = findViewById(R.id.todoListView);
                todoListView.setLayoutManager(new LinearLayoutManager(CreateToDoActivity.this));
                todoListView.setAdapter(mAdapter);
                if(mAdapter.getItemCount()<1){
                    notodo = findViewById(R.id.text_notodo);
                    todoListView.setVisibility(View.GONE);
                    notodo.setVisibility(View.VISIBLE);
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
        AlertDialog.Builder alert = new AlertDialog.Builder(CreateToDoActivity.this);
        alert.setMessage("Do you want to delete this TODO?");

        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //delete to-do
                databaseHelper.deleteToDo(selectedItem);

                mTodos = new ArrayList<>();

                mAdapter.notifyDataSetChanged();
                mTodos.clear();
                mTodos.addAll(databaseHelper.getAllToDo(databaseHelper.getIDfromUsername(user)));
                //list all to-do
                mAdapter = new ToDoListAdapter(CreateToDoActivity.this, mTodos);
                todoListView = findViewById(R.id.todoListView);
                todoListView.setLayoutManager(new LinearLayoutManager(CreateToDoActivity.this));
                todoListView.setAdapter(mAdapter);
                if(mAdapter.getItemCount()<1){
                    notodo = findViewById(R.id.text_notodo);
                    todoListView.setVisibility(View.GONE);
                    notodo.setVisibility(View.VISIBLE);
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