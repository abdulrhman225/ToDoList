package com.example.todolist.Ui;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.todolist.AlarmService;
import com.example.todolist.model.DataBase;
import com.example.todolist.R;
import com.example.todolist.model.task;

import java.util.Calendar;

public class CreatingListActivity extends AppCompatActivity {
    NumberPicker np_hours, np_minutes, np_state;
    EditText et_mission_place;
    Button btn_save;
    DataBase db = new DataBase(this);
    task mission;
    AlarmManager alarmManger;
    PendingIntent penIn;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_list);

        CreateNotification();

        np_hours = findViewById(R.id.hours);
        np_minutes = findViewById(R.id.minutes);
        np_state = findViewById(R.id.state);
        et_mission_place = findViewById(R.id.missionPlace);
        btn_save = findViewById(R.id.btn_save);

        np_hours.setMaxValue(12);
        np_hours.setMinValue(0);


        np_minutes.setMaxValue(59);
        np_minutes.setMinValue(0);


        String[] states = {"am", "pm"};
        np_state.setMaxValue(1);
        np_state.setMinValue(0);
        np_state.setDisplayedValues(states);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hours = String.valueOf(np_hours.getValue());
                String minutes = String.valueOf(np_minutes.getValue());
                String state = states[np_state.getValue()];


                String time = hours + " :" + minutes + " :" + state;

                if (!time.equals("0 :0 :am")) {

                    String task = et_mission_place.getText().toString();


                    mission = new task(time, task);

                    db.insert(mission);

                    Intent intent = new Intent(CreatingListActivity.this, MainActivity.class);
                    startActivity(intent);

                    Calendar calendar = Calendar.getInstance();
                    if (np_state.getValue() == 0) {
                        calendar.set(Calendar.HOUR_OF_DAY, np_hours.getValue());
                    } else {
                        calendar.set(Calendar.HOUR_OF_DAY, np_hours.getValue() + 12);

                    }

                    calendar.set(Calendar.MINUTE, np_minutes.getValue());
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    startAlarm(calendar);

                } else {
                    Toast.makeText(getApplicationContext(), "please enter the Time", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void startAlarm(Calendar c) {


        alarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmService.class);
        penIn = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManger.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), penIn);
    }

    public void CreateNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "Task is Ready Task";

            NotificationChannel channel = new NotificationChannel(MainActivity.ChannelID, "ChanelName", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);

        }
    }

}