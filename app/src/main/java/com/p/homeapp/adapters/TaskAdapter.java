package com.p.homeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private ArrayList<Task> mTasks = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public TaskAdapter(ArrayList<Task> tasks, ItemClickListener itemClickListener) {
        this.mTasks = tasks;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_review_row, parent, false);
        return new TaskViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.taskName.setText(mTasks.get(position).getTaskName());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView taskName;
        ItemClickListener itemClickListener;

        public TaskViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            taskName = itemView.findViewById(R.id.id_task_name_txt);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(getAdapterPosition());
        }
    }
}
