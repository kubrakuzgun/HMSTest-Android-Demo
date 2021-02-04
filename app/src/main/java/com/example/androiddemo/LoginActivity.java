package com.example.androiddemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private DatabaseHelper databaseHelper;
    private String user;

    public void setUser(String currentuser){
        user = currentuser;
    }

    public void goToMainActivity(){
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.putExtra("username", user);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        startActivity(mainIntent);
    }

    //check empty inputs
    public boolean checkIfFieldsEmpty(EditText field){
        if(field.length()==0)
        {
            field.requestFocus();
            field.setError("This field cannot be blank!");
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(LoginActivity.this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        if (preferences.contains("isLoggedIn")){   //go to main activity if user already logged in
            setUser(preferences.getString("username", ""));
            Log.d("username", user);
            goToMainActivity();
        }
        else{   //continue login activity if user not logged in
            EditText username = findViewById(R.id.edittext_email);
            EditText password = findViewById(R.id.edittext_psw);
            Button btn_login = findViewById(R.id.button_logout);
            TextView register = findViewById(R.id.clickabletext_login);


            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!checkIfFieldsEmpty(username) && !checkIfFieldsEmpty(password)){
                        if(databaseHelper.checkCredentials(username.getText().toString().trim(),password.getText().toString())){
                            setUser(username.getText().toString().trim());
                            editor.putBoolean("isLoggedIn",true);
                            editor.putString("username", username.getText().toString().trim());
                            editor.apply();
                            goToMainActivity();

                        }
                        else{
                            CustomErrorDialog alert = new CustomErrorDialog();
                            alert.showDialog(LoginActivity.this, "Incorrect username or password!");

                        }
                    }
                }
            });

        }

    }

    //close app when navigation back pressed
    @Override
    public void onBackPressed(){
        finishAffinity();
    }

    public void goToRegister(View v){
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        startActivity(registerIntent);
    }

}