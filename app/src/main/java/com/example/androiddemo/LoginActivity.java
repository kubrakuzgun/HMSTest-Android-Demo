package com.example.androiddemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        EditText username = findViewById(R.id.edittext_email);
        EditText password = findViewById(R.id.edittext_psw);
        Button btn_login = findViewById(R.id.button_logout);


        //go to main activity if user already logged in


        //continue login activity if user not logged in


    }

    //override onBackPressed method: close app when pressed back


    public void goToMainActivity(){
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.putExtra("username", user);
        startActivity(mainIntent);
    }


    //func. for clickable Register text


}