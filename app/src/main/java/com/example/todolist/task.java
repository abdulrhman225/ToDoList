package com.example.todolist;

public class task {
    String time;
    String task;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public task(String time, String task, int id) {
        this.time = time;
        this.task = task;
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public task(String time, String task) {
        this.time = time;
        this.task = task;
    }
}
