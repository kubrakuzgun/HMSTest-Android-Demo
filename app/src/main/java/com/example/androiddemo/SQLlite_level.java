package com.example.androiddemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLlite_level extends SQLiteOpenHelper {
    public  SQLlite_level(Context c){
       super(c,"bussinessCalendar",null,1);//Context, DatabaseName, null, upgrade versionu(1)
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Add queries in onCreate
        //not null, primary key autoincrement
String sql ="create table ToDo (task VARCHAR not null, date VARCHAR not null, starTime VARCHAR, endTime VARCHAR)";
        db.execSQL(sql); //sql stringini Database de sorgu olarak çalıştırma komutu
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Old version , new version parametreleri :
        //Upgrade classı sürekli versiyon takibi yapmalı.
        // Her yeni veri eklendiğinde versiyon güncellenmeli.
        // Yeni verilen versiyon daha önceki versiyonlarla karşılaştırılır. Daha mı önce daha mı sonra?
        //Önceki bi versiyonsa güncelleme var, current version ise güncelleme yok
        db.execSQL("drop table if exists ToDo"); //ToDo tablosunu sil
    }
}
