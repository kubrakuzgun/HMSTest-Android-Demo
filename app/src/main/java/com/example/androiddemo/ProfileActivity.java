package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;
    String user;
    CircleImageView profilePicture;
    TextView usernameText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseHelper = new DatabaseHelper(ProfileActivity.this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        user = extras.getString("username");


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


        blurView = findViewById(R.id.view_blurbackground2);
        blurView.setVisibility(View.INVISIBLE);

        profilePicture = findViewById(R.id.image_pp);
        usernameText = findViewById(R.id.text_username);

        usernameText.setText(user);

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

        if(preferences.contains("profilePicture"))
        {

            String encodedImage = preferences.getString("profilePicture",null);

            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);

            Bitmap bitmapImage = BitmapFactory.decodeByteArray(b, 0, b.length);

            profilePicture.setImageBitmap(bitmapImage);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_home: {
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                homeIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(homeIntent);
                break;
            }

/*            case R.id.nav_calendar: {
                Intent calendarIntent = new Intent(ProfileActivity.this, CalendarActivity.class);
                calendarIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(calendarIntent);
                break;
            }*/

            case R.id.nav_todo: {
                Intent todoIntent = new Intent(getApplicationContext(), CreateToDoActivity.class);
                todoIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(todoIntent);
                break;
            }
/*            case R.id.nav_meetings: {
                Intent meetingsIntent = new Intent(getApplicationContext(), CreateMeetingsActivity.class);
                meetingsIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(meetingsIntent);
                break;
            }*/
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
/*            case R.id.nav_settings: {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                settingsIntent.putExtra("username", user);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(settingsIntent);
                break;
            }*/

        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    //select picture from device
    public void uploadPicture(View view){
        Intent intent=new Intent();
        //set file type to image
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //when picture is choosen, go to result
        startActivityForResult(intent,1);
    }

    //check picture choose result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if picture is successfully choosen (correct format)
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri selectedImage = data.getData();

            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                profilePicture.setImageBitmap(bmp);

                // store Bitmap as base64 string in SharedPreferences
                editor = preferences.edit();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedString = Base64.encodeToString(b, Base64.DEFAULT);
                editor.putString("profilePicture",encodedString);
                editor.apply();
                recreate();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProfileActivity.this, "File not found", Toast.LENGTH_LONG).show();
            }

        }
        else {
            Toast.makeText(ProfileActivity.this, "You haven't selected an image",Toast.LENGTH_LONG).show();
        }
    }
}