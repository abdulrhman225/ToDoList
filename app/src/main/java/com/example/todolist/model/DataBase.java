package com.example.todolist.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    public  SQLiteOpenHelper openHelper;
    public SQLiteDatabase database;
    public static final String DataBaseName = "TaskDataBase";
    public static final String ID_CLN_NAME = "ID";
    public static final String TIME_CLN_NAME = "TIME";
    public static final String TASK_CLN_NAME = "TASK";
    public static final int version  = 9;
    public DataBase(@Nullable Context context) {
        super(context, DataBaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table " +DataBaseName +"("+ID_CLN_NAME +" INTEGER PRIMARY KEY AUTOINCREMENT ," +TIME_CLN_NAME+" TEXT , " +TASK_CLN_NAME + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseName);
        onCreate(sqLiteDatabase);
    }



    public boolean insert (task task){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_CLN_NAME , task.getTask());
        values.put(TIME_CLN_NAME , task.getTime());
        long result = db.insert(DataBaseName , null , values);

        return result != -1;
    }

    public ArrayList<task> getAllTasks(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<task> tasks = new ArrayList<>();

        Cursor cursor = db.rawQuery(" SELECT * FROM "+DataBaseName ,null);

        if (cursor.moveToFirst()){
            do{
                int taskId = cursor.getInt(0);
                String time = cursor.getString(1);
                String task = cursor.getString(2);

                task tas = new task(time , task ,taskId);
                tasks.add(tas);

            }while(cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }


    public task getTask(int TaskID){
        SQLiteDatabase db = getReadableDatabase();

        String[] args = {String.valueOf(TaskID)};
        Cursor cursor = db.rawQuery(" SELECT * FROM "+DataBaseName +" WHERE " +ID_CLN_NAME +" =? ",args );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String time = cursor.getString(1);
            String task = cursor.getString(2);

            task tas = new task(time, task ,id);

            cursor.close();
            return tas;
        }
        return null;
    }

    public boolean delete (task task){
        SQLiteDatabase db = getWritableDatabase()   ;


        return   db.delete(DataBaseName ,   ID_CLN_NAME +" =? "
                , new String[]{String.valueOf(task.getId())}) > 0;

    }

    public boolean update (task task){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TIME_CLN_NAME , task.getTime());
        values.put(TASK_CLN_NAME , task.getTask());

        String[] args = {String.valueOf(task.getId())};

        int result = db.update(DataBaseName , values , ID_CLN_NAME + " =? " ,args   );

        return result > 0 ;
    }

}