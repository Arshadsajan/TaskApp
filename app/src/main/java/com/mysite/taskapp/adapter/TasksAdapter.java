package com.mysite.taskapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysite.taskapp.R;
import com.mysite.taskapp.interfaces.TaskInterface;
import com.mysite.taskapp.model.TasksModel;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {
    Context context;
    List<TasksModel> listTasks;
    TaskInterface listener;

    public TasksAdapter(Context context, List<TasksModel> listTasks, TaskInterface listener) {
        this.context = context;
        this.listTasks = listTasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_row, parent, false);
        TasksViewHolder viewHolder = new TasksViewHolder(view);
        view.setOnClickListener(v -> {
            listener.onItemClicked(listTasks.get(viewHolder.getAdapterPosition()));
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        TasksModel task = listTasks.get(position);
        holder.taskIndex.setText(String.valueOf(position + 1));
        holder.taskName.setText(task.getTaskName());
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }

    static class TasksViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskIndex;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.txtTask);
            taskIndex = itemView.findViewById(R.id.txtTaskIndex);
        }
    }
}
