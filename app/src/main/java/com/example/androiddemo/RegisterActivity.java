package com.example.androiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private User user;

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
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        user = new User();


        EditText username = findViewById(R.id.edittext_username);
        EditText email = findViewById(R.id.edittext_email);
        EditText password = findViewById(R.id.edittext_psw);
        EditText confirmPassword = findViewById(R.id.edittext_pswconfirm);
        Button btn_register = findViewById(R.id.button_logout);
        TextView login = findViewById(R.id.clickabletext_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check empty inputs
                if ( !checkIfFieldsEmpty(username) && !checkIfFieldsEmpty(email) && !checkIfFieldsEmpty(password) && !checkIfFieldsEmpty(confirmPassword) ){
                    //check password length
                    if(password.getText().toString().trim().length()<6){
                        password.setError("Şifre en az 6 karakter olmalıdır.");
                    }
                    else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                        confirmPassword.setError("Şifreler uyuşmuyor.");
                    }
                    else {
                        //create user
                        if (!databaseHelper.checkUserEmail(email.getText().toString().trim())) {
                            if(!databaseHelper.checkUsername(username.getText().toString().trim())){
                                user.setUsername(username.getText().toString().trim());
                                user.setEmail(email.getText().toString().trim());
                                user.setPassword(password.getText().toString().trim());
                                databaseHelper.addUser(user);
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                startActivity(mainIntent);

                            }
                            else
                            {
                                CustomErrorDialog alert = new CustomErrorDialog();
                                alert.showDialog(RegisterActivity.this, "This username is already in use!");
                            }

                        } else {
                            CustomErrorDialog alert = new CustomErrorDialog();
                            alert.showDialog(RegisterActivity.this, "This e-mail is already in use!");
                        }
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(loginIntent);
            }
        });

    }

    //close app when navigation back pressed
    @Override
    public void onBackPressed(){
        finishAffinity();
    }

}