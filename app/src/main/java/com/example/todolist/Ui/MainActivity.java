package com.example.todolist.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.model.CustomDoList;
import com.example.todolist.model.DataBase;
import com.example.todolist.R;
import com.example.todolist.model.onItemClick;
import com.example.todolist.model.task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView Recycle;
    FloatingActionButton fab_newTask;
    DataBase db = new DataBase(this);
    CustomDoList customDoList;
    ArrayList<task> tasks = new ArrayList<>();
    public static final String ChannelID = "Channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Recycle = findViewById(R.id.main_RecycleView);
        fab_newTask = findViewById(R.id.floatingActionButton);

        tasks = db.getAllTasks();
        customDoList = new CustomDoList(tasks, new onItemClick() {
            @Override
            public void onItemClick(int taskId) {


                Intent intent = new Intent(getApplicationContext() , editActivity.class);
                intent.putExtra("taskId" , taskId);
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);

        Recycle.setAdapter(customDoList);
        Recycle.setLayoutManager(manager);
        Recycle.setHasFixedSize(true);







        fab_newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreatingListActivity.class);
                startActivity(intent);

            }
        });








    }
}
