package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.Calendar;

public class editActivity extends AppCompatActivity {

    EditText et_task;
    NumberPicker hours , minutes , state;
    Button btn_update;
    DataBase db  ;
    task task;
    String tasks;
    String time;
    int id;
    boolean res ;
    task ta;

    Calendar calendar;
    AlarmManager alarmManger;

    PendingIntent penIn;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        CreateNotification();

        et_task = findViewById(R.id.newText);
        hours = findViewById(R.id.hour);
        minutes = findViewById(R.id.minute);
        state = findViewById(R.id.states);
        btn_update = findViewById(R.id.update);

        db = new DataBase(editActivity.this);

        Intent intent = getIntent();

        int taskId = intent.getIntExtra("taskId",-1);
        ta = db.getTask(taskId);

        tasks = ta.getTask();
        time = ta.getTime();
        id = ta.getId();



        int ho = time.indexOf(":");
        String h = time.substring(0 , ho-1);
        int hou = Integer.parseInt(h);


        hours.setMinValue(0 );
        hours.setMaxValue(12);
        hours.setValue(hou);
        hours.setEnabled(false);
        hours.setBackgroundColor(getResources().getColor(R.color.teal_200));



        int mi = time.indexOf(":" , ho+1);
        String min = time.substring(ho+1  , mi-1);
        int minu = Integer.parseInt(min);

        minutes.setMaxValue(59);
        minutes.setMinValue(0);
        minutes.setValue(minu);
        minutes.setEnabled(false);
        minutes.setBackgroundColor(getResources().getColor(R.color.teal_200));


        String sta = time.substring(mi+1);
        if (sta.equals("am")){
            state.setValue(0);
        }else{
            state.setValue(1);
        }

        String[] states = {"am", "pm"};
        state.setMinValue(0);
        state.setMaxValue(1);
        state.setDisplayedValues(states);
        state.setEnabled(false);
        state.setBackgroundColor(getResources().getColor(R.color.teal_200));


        et_task.setText(tasks);


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = hours.getValue()+ " :" +minutes.getValue()+" :" + states[state.getValue()];
                String task = et_task.getText().toString();

                task tas = new task(time ,task , id);
                db.update(tas);

                Intent in = new Intent(editActivity.this , MainActivity.class);
                startActivity(in);
                cancelAlarm();
                startAlarm();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete:
                task = new task(null , null ,  id);
                res = db.delete(task);
                if (res ) {
                    Intent intent = new Intent(editActivity.this , MainActivity.class);
                    startActivity(intent);
                    cancelAlarm();
                }

                return true;

            case R.id.edit:
                et_task.setEnabled(true);
                btn_update.setVisibility(View.VISIBLE);
                hours.setEnabled(true);
                minutes.setEnabled(true);
                state.setEnabled(true);




                return true;
        }

        return false;
    }

    public void startAlarm(){



        calendar = Calendar.getInstance();
        if (state.getValue() == 0){
            calendar.set(Calendar.HOUR_OF_DAY , hours.getValue());
        }else {
            calendar.set(Calendar.HOUR_OF_DAY , hours.getValue()+12);

        }

        calendar.set(Calendar.MINUTE , minutes.getValue());
        calendar.set(Calendar.SECOND , 0);
        calendar.set(Calendar.MILLISECOND , 0);

        alarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this , AlarmService.class);
        penIn = PendingIntent.getBroadcast(this , 0 , intent , 0);

        alarmManger.setInexactRepeating(AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis() , AlarmManager.INTERVAL_DAY , penIn);

    }

    public void cancelAlarm (){


        Intent intent = new Intent(this , AlarmService.class);
        penIn = PendingIntent.getBroadcast(this , 0 , intent , 0);

        if (alarmManger != null){
            alarmManger.cancel(penIn);
        }
    }
    public void CreateNotification () {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Task is Ready Task";

            NotificationChannel channel = new NotificationChannel(MainActivity.ChannelID, "ChanelName", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);

        }
    }

}