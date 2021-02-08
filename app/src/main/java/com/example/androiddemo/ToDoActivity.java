package com.example.androiddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class ToDoActivity extends AppCompatActivity {
    EditText edt_task, edt_date, edt_startTime, edt_endTime;
    Button btn_date, btn_start, btn_end;
    Button btn_OK, btn_Cancel;
    dataSource ds = new dataSource(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        //OnCreate : uygulama başladığında / uygulama başladığında db bağlantım açılsın:

        ds.open();

       definitions();
       btn_date.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Calendar mcurrentTime = Calendar.getInstance();
               int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
               int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
               int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

               DatePickerDialog datePicker;//Datepicker objemiz
               datePicker = new DatePickerDialog(MeetingActivity.this, new DatePickerDialog.OnDateSetListener() {

                   @Override
                   public void onDateSet(DatePicker view, int year, int monthOfYear,
                                         int dayOfMonth) {
                       // TODO Auto-generated method stub
                       edt_date.setText( dayOfMonth + "/" + monthOfYear+ "/"+year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz

                   }
               },year,month,day);//başlarken set edilcek değerlerimizi atıyoruz
               datePicker.setTitle("Date");
               datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePicker);
               datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", datePicker);

               datePicker.show();

           }
       });

       btn_start.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Calendar mcurrentTime = Calendar.getInstance();//
               int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
               int minute = mcurrentTime.get(Calendar.MINUTE);//
               TimePickerDialog timePicker;
               timePicker = new TimePickerDialog(MeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                       edt_startTime.setText(selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                   }
               }, hour, minute, true);//true 24 saatli sistem için
               timePicker.setTitle("Start Time");
               timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
               timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

               timePicker.show();
           }
       });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_endTime.setText(selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("End Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

                timePicker.show();
            }
        });

        //Cancel button
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //OK button
       btn_OK.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ToDo t =new ToDo(edt_task.getText().toString(),edt_date.getText().toString(),edt_startTime.getText().toString(), edt_endTime.getText().toString());
           ds.createTask(t);
           Intent intent= new Intent(ToDoActivity.this,MainActivity.class);
           intent.putExtra(t);
           }
       });

    }
    
    public void definitions(){
        edt_task = (EditText) findViewById(R.id.edtxt_Task);
        edt_date = (EditText) findViewById(R.id.edtxt_toDoDate);
        edt_startTime= (EditText)findViewById(R.id.edtxt_toDoStart);
        edt_endTime = (EditText) findViewById(R.id.edtxt_toDoEnd);

        btn_date = (Button)findViewById(R.id.btn_ToDoeditDate);
        btn_start = (Button)findViewById(R.id.btn_ToDoeditStart);
        btn_end = (Button)findViewById(R.id.btn_ToDoeditEnd);

        btn_OK = (Button)findViewById(R.id.btn_toDoOK);
        btn_Cancel = (Button) findViewById(R.id.btn_toDoCancel);
    }
}