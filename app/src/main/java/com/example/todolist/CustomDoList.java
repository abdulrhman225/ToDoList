package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomDoList extends RecyclerView.Adapter<CustomDoList.DoListPlaceHolder> {

    ArrayList<task> tasks ;

    public void setTasks(ArrayList<task> tasks) {
        this.tasks = tasks;
    }

    onItemClick onItemClick;


    public CustomDoList (ArrayList<task> tasks , onItemClick onItemClick ){
        this.tasks = tasks;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public DoListPlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_taske , parent ,false);
        DoListPlaceHolder holder = new DoListPlaceHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoListPlaceHolder holder, int position) {
        task task = tasks.get(position);

        holder.tv_time.setText(task.getTime());
        holder.checkBox_task.setText(task.getTask());
        holder.tv_time.setTag(task.getId());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class DoListPlaceHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        CheckBox checkBox_task;
        public DoListPlaceHolder(@NonNull View itemView) {
            super(itemView);

            tv_time = itemView.findViewById(R.id.CustomTextView);
            checkBox_task = itemView.findViewById(R.id.custom_CheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = (int) tv_time.getTag();

                    onItemClick.onItemClick(id);
                }
            });







        }
    }
}