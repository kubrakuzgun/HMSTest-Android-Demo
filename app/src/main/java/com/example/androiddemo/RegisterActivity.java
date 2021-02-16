package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.edittext_username);
        EditText email = findViewById(R.id.edittext_email);
        EditText password = findViewById(R.id.edittext_psw);
        EditText confirmPassword = findViewById(R.id.edittext_pswconfirm);
        Button btn_register = findViewById(R.id.button_logout);

        // register button onClick


    }

    //close app when navigation back pressed
    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    public void goToLogin(View v){
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        startActivity(loginIntent);
    }


    //func. to check empty inputs


}