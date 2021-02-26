package com.p.homeapp.adapters.archivesAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p.homeapp.R;
import com.p.homeapp.entities.Task;
import com.p.homeapp.helpers.DateParser;

import java.util.ArrayList;

public class TaskArchivesAdapter extends RecyclerView.Adapter<TaskArchivesAdapter.TaskArchivesViewHolder> {
    private ArrayList<Task> mTasks;

    public TaskArchivesAdapter(ArrayList<Task> mTasks) {
        this.mTasks = mTasks;
    }

    @NonNull
    @Override
    public TaskArchivesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_archives_row, parent, false);
        return new TaskArchivesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskArchivesViewHolder holder, int position) {
        holder.archivesTaskName.setText(mTasks.get(position).getTaskName());
        holder.archivesTaskGroupName.setText(mTasks.get(position).getGroupName());
//        holder.archivesTaskUserName.setText(mTasks.get(position).getUser().getLogin());
//        holder.archivesTaskDoneDate.setText(DateParser.dateToStringParser(mTasks.get(position).getDeadline()));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskArchivesViewHolder extends RecyclerView.ViewHolder {
        private TextView archivesTaskName, archivesTaskGroupName, archivesTaskUserName, archivesTaskDoneDate;

        public TaskArchivesViewHolder(@NonNull View itemView) {
            super(itemView);
            archivesTaskName = itemView.findViewById(R.id.txt_archives_task_name);
            archivesTaskGroupName = itemView.findViewById(R.id.txt_archives_task_group_name);
            archivesTaskUserName = itemView.findViewById(R.id.txt_archives_task_user_name);
            archivesTaskDoneDate = itemView.findViewById(R.id.txt_archives_task_done_date);
        }
    }
}
