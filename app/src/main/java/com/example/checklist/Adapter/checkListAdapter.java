package com.example.checklist.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checklist.AddNewTask;
import com.example.checklist.MainActivity;
import com.example.checklist.model.checkListModel;
import com.example.checklist.R;
import com.example.checklist.utils.DatabaseHandler;

import java.util.List;

public class checkListAdapter extends RecyclerView.Adapter<checkListAdapter.ViewHolder> {

    private List<checkListModel> checkList;
    private DatabaseHandler db;
    private MainActivity activity;

    public checkListAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final checkListModel item = checkList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<checkListModel> todoList) {
        this.checkList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        checkListModel item = checkList.get(position);
        db.deleteTask(item.getId());
        checkList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        checkListModel item = checkList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
