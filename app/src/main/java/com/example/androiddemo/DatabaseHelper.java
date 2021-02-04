package com.example.androiddemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 10;
    // Database Name
    private static final String DATABASE_NAME = "DemoDatabase.db";
    // User table name
    private static final String TABLE_USER = "user";
    // To-do table name
    private static final String TABLE_TODO = "todo";
    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_USERNAME = "user_usernamename";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    // To-do Table Columns names
    private static final String COLUMN_TODO_ID = "todo_id";
    private static final String COLUMN_CURRENT_USER_ID = "current_user_id";
    private static final String COLUMN_TODO_TITLE = "todo_title";
    private static final String COLUMN_TODO_DESC = "todo_desc";
    private static final String COLUMN_TODO_DATE = "todo_date";
    private static final String COLUMN_TODO_STATUS = "todo_status";
    // create table sql query
    private String CREATE_USER_TABLE= "CREATE TABLE " + TABLE_USER+ "("
            + COLUMN_USER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_USERNAME+ " TEXT UNIQUE,"
            + COLUMN_USER_EMAIL+ " TEXT UNIQUE," + COLUMN_USER_PASSWORD+ " TEXT" + ")";
    // drop table sql query
    private String DROP_USER_TABLE= "DROP TABLE IF EXISTS " + TABLE_USER;

    // create table sql query
    private String CREATE_TODO_TABLE= "CREATE TABLE " + TABLE_TODO+ "("
            + COLUMN_TODO_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TODO_TITLE+ " TEXT," + COLUMN_CURRENT_USER_ID+ " INTEGER," + COLUMN_TODO_DESC+ " TEXT,"
            + COLUMN_TODO_STATUS+ " TEXT," + COLUMN_TODO_DATE+ " TEXT" + ")";
    // drop table sql query
    private String DROP_TODO_TABLE= "DROP TABLE IF EXISTS " + TABLE_USER;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.enableWriteAheadLogging();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.enableWriteAheadLogging();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        Log.d("user table created", "true");
        db.execSQL(CREATE_TODO_TABLE);
        Log.d("todo table created", "true");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_TODO_TABLE);
        // Create tables again
        onCreate(db);
    }


    //create user record
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        Log.d("user table", "user added");
        db.close();
    }

     // fetch all user and return the list of user records
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_USERNAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_USERNAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }

    // update user record
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

     // delete user record
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // to check user exist or not
    public boolean checkUserEmail(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    // to check user already exist or not
    public boolean checkUsername(String username) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {username};

        // query user table with condition
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //check user credentials and auth user for login
    public boolean checkCredentials(String username, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {username, password};

        // query user table with conditions
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    //create user record
    public void addToDo(ToDo todo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_TITLE, todo.getTodoTitle());
        values.put(COLUMN_TODO_DESC, todo.getTodoDesc());
        values.put(COLUMN_TODO_DATE, todo.getTodoDate());
        values.put(COLUMN_CURRENT_USER_ID, todo.getTodoUserID());
        values.put(COLUMN_TODO_STATUS, "Incomplete");
        // Inserting Row
        db.insert(TABLE_TODO, null, values);
        Log.d("todo table", "todo item added");
        db.close();
    }

    // update user record
    public void updateToDo(ToDo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_TITLE, todo.getTodoTitle());
        values.put(COLUMN_TODO_DESC, todo.getTodoDesc());
        values.put(COLUMN_TODO_DATE, todo.getTodoDate());
        values.put(COLUMN_TODO_STATUS, todo.getTodoStatus());
        // updating row
        db.update(TABLE_TODO, values, COLUMN_TODO_ID + " = ?",
                new String[]{String.valueOf(todo.getTodoID())});
        db.close();
    }

    // delete user record
    public void deleteToDo(ToDo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_TODO, COLUMN_TODO_ID + " = ?",
                new String[]{String.valueOf(todo.getTodoID())});
        db.close();
    }

    public Integer getIDfromUsername(String currentuser){
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_USERNAME + " = ?";

        // selection argument
        String[] selectionArgs = {currentuser};

        // query user table with condition
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int id = cursor.getColumnIndex(COLUMN_USER_ID);
        cursor.close();
        db.close();
        return id;
    }


    // fetch all user and return the list of user records
    public List<ToDo> getAllToDo(Integer currentuser_id) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_TODO_ID,
                COLUMN_CURRENT_USER_ID,
                COLUMN_TODO_TITLE,
                COLUMN_TODO_DESC,
                COLUMN_TODO_DATE,
                COLUMN_TODO_STATUS
        };
        // sorting orders
        String sortOrder =
                COLUMN_TODO_ID + " ASC";
        List<ToDo> todoList = new ArrayList<ToDo>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_CURRENT_USER_ID + " = ?";

        // selection arguments
        Integer[] selectionArgs = {currentuser_id};

        // query the to-do table
        Cursor cursor = db.query(TABLE_TODO, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ToDo todo = new ToDo();
                todo.setTodoID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_ID))));
                todo.setTodoUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CURRENT_USER_ID))));
                todo.setTodoTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_TITLE)));
                todo.setTodoDesc(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_DESC)));
                todo.setTodoDate(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_DATE)));
                todo.setTodoStatus(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_STATUS)));
                // Adding user record to list
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return todoList;
    }

}

