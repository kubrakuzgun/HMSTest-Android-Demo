package com.example.androiddemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class dataSource {
    //SQLlite katmanıyla iletişim kurup uygulamayla iletişimini sağlayan katman burası
    //Select, delete, insert  queryleri eklenecek bu classa
    SQLiteDatabase db; //databasedeki verilere erişmek için kullandığımız yapı
    SQLlite_level db2;

    //constructor içinde db2 bağlantı noktasını initialize et
    public dataSource(Context c) {
        db2 = new SQLlite_level(c);
    }

    //veri kaynağı uygulama ile database arasında bağlantı açıp kapatacak. İşim olmadığında db kapalı olmalı.
    public void open() {
        db = db2.getWritableDatabase(); //yazılabilir bir db olsun (sadece readable olmasın)
    }

    public void close() {
        db2.close(); //bağlantıyı kapat
    }

    public void createTask(ToDo t) { //for to do
        //String task = "No Task";
        //String date = "31.01.2021"
        //String starTime = "12:00";
        //String endTime = "13:00";
        //ToDo t = new ToDo();
        //t.setTask(task);
        //t.setTaskDate(date);
        //t.setTaskStart(starTime);
        //t.setTaskEnd(endTime);
        ContentValues val = new ContentValues();//querye parametre geçirmemi sağlayacak olan obje
        val.put("task", t.getTask());
        val.put("date", t.getTaskDate());
        val.put("starTime", t.getTaskStart());
        val.put("endTime", t.getTaskEnd());
//db ye eklenecek verileri objeye put ettik.
        db.insert("ToDo", null, val);
    }

    public void deleteTask(ToDo t) {
       // String date=t.getTaskDate();
        //String startTime=t.getTaskStart();
        //String endTime=t.getTaskEnd();
        String task= t.getTask()
        db.delete("ToDo", "task="+task,null);

    }

    public List<ToDo> list() { //db nin select queryle döndüğü tabloyu listeye oradan da arayüze ekleyeceğiz
        String columns[] = {"task", "date", "starTime", "endTime"};
        List<ToDo> taskList = new ArrayList<ToDo>();
        String[] s;
        Cursor c = db.query("ToDo", columns, null, null, null, null, null);
        c.moveToFirst(); //ilk elemanın başına git
        while (!c.isAfterLast()) {
            String task = c.getString(0);
            String date = c.getString(1);
            String starTime = c.getString(2);
            String endTime = c.getString(3);
            String element = task + " " + date + " " + starTime + " " + endTime;
            ToDo t = new ToDo("", "", "", "");
            t.setTask(task);
            t.setTaskDate(date);
            t.setTaskStart(starTime);
            t.setTaskEnd(endTime);
            taskList.add(t);
            c.moveToNext();
        }
        c.close();
        return taskList;
    }
}

