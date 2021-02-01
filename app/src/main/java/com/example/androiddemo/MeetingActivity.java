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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MeetingActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    View blurView;
    EditText edt_end, edt_start, edt_title, edt_date;
    ImageButton btn_StartEdit, btn_EndEdit, btn_DateEdit;
    Button btn_OK, btn_Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        definitions();
        timeEditClicks();
        OkCancelClicks();
    }

    public void definitions() {
        btn_StartEdit = (ImageButton) findViewById(R.id.btn_meetingStartEdit);
        btn_EndEdit = (ImageButton) findViewById(R.id.btn_meetingEndEdit);
        btn_DateEdit = (ImageButton)findViewById(R.id.btn_meetingDateEdit);

        edt_start = (EditText) findViewById(R.id.edtxt_meetingStart);
        edt_end = (EditText) findViewById(R.id.edtxt_meetingEnd);
        edt_date = (EditText)findViewById(R.id.edtxt_meetingDate);
        edt_title = (EditText) findViewById(R.id.edtxt_meetingTitle);

        btn_OK = (Button) findViewById(R.id.btn_meetingOK);
        btn_Cancel = (Button) findViewById(R.id.btn_meetingCancel);
    }

    public void timeEditClicks() {
        //Edit Date
        btn_DateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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

        //Edit Start Time
        btn_StartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_start.setText(selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Start Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

                timePicker.show();

            }
        });
        //Edit End Time
        btn_EndEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();//
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                int minute = mcurrentTime.get(Calendar.MINUTE);//
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edt_end.setText(selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("End Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);

                timePicker.show();

            }
        });
    }

    public void OkCancelClicks() {

        try {
            final SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("BusinessCalendar", MODE_PRIVATE, null); //open or create (böyle bir database varsa onu açar yoksa create eder)
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Meetings(title VARCHAR, starTime VARCHAR, endTime VARCHAR)");
            btn_OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqLiteDatabase.execSQL("INSERT INTO Hikayeler(yazarisim, kitapismi, cikisyil) VALUES('"+
                            edt_title.getText().toString()+"','"+
                            edt_start.getText().toString()+"',"+
                            edt_start.getText().toString());
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
