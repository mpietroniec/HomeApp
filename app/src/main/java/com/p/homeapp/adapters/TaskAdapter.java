package com.p.homeapp.adapters;

import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Task;
import com.p.homeapp.helpers.DateParser;
import com.p.homeapp.views.updateTask.TaskUpdateActivity;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Task> mTasks;
    private ArrayList<Object> items = new ArrayList<>();
    private ItemClickListener mItemClickListener;


    public TaskAdapter(ArrayList<Task> tasks, ItemClickListener itemClickListener) {
        this.mTasks = tasks;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == 1) {
            View view = layoutInflater.inflate(R.layout.activity_fragment_tasks_with_date_row, parent, false);
            return new TaskWithDateViewHolder(view, mItemClickListener);
        } else {
            View view = layoutInflater.inflate(R.layout.activity_fragment_tasks_without_date_row, parent, false);
            return new TaskWithoutDateViewHolder(view, mItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                TaskWithDateViewHolder taskWithDateViewHolder = (TaskWithDateViewHolder) holder;
                taskWithDateViewHolder.taskName.setText(mTasks.get(position).getTaskName());
                //taskWithDateViewHolder.taskDeadline.setText(String.valueOf(mTasks.get(position).getDeadline()));
                taskWithDateViewHolder.taskDeadline.setText(DateParser.dateToStringParser(mTasks.get(position).getDeadline()));
                taskWithDateViewHolder.taskType.setImageResource(mTasks.get(position).getDrawable());
                taskWithDateViewHolder.taskReviewLayout.setOnClickListener(v -> {
                    if (taskWithDateViewHolder.taskExpandableView.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(taskWithDateViewHolder.taskReviewLayout, new AutoTransition());
                        taskWithDateViewHolder.taskExpandableView.setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(taskWithDateViewHolder.taskReviewLayout, new AutoTransition());
                        taskWithDateViewHolder.taskExpandableView.setVisibility(View.GONE);
                    }
                });
                taskWithDateViewHolder.taskUpdate.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(),TaskUpdateActivity.class);
                    v.getContext().startActivity(intent);
                });
                break;
            case 2:
                TaskWithoutDateViewHolder taskWithoutDateViewHolder = (TaskWithoutDateViewHolder) holder;
                taskWithoutDateViewHolder.taskName.setText(mTasks.get(position).getTaskName());
                taskWithoutDateViewHolder.taskType.setImageResource(mTasks.get(position).getDrawable());
                taskWithoutDateViewHolder.taskReviewLayout.setOnClickListener(v -> {
                    if (taskWithoutDateViewHolder.taskExpandableView.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(taskWithoutDateViewHolder.taskReviewLayout, new AutoTransition());
                        taskWithoutDateViewHolder.taskExpandableView.setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(taskWithoutDateViewHolder.taskReviewLayout, new AutoTransition());
                        taskWithoutDateViewHolder.taskExpandableView.setVisibility(View.GONE);
                    }
                });
                taskWithoutDateViewHolder.taskUpdate.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(),TaskUpdateActivity.class);
                    v.getContext().startActivity(intent);
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mTasks.get(position).getDeadline() == null) {
            return 2;
        } else {
            return 1;
        }
    }

    public static class TaskWithDateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView taskName, taskDeadline, taskUpdate;
        private ImageView taskType;
        RelativeLayout taskExpandableView;
        CardView taskReviewLayout;
        ItemClickListener itemClickListener;

        public TaskWithDateViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            taskName = itemView.findViewById(R.id.id_task_date_name_txt);
            taskDeadline = itemView.findViewById(R.id.id_task_date_date_txt);
            taskType = itemView.findViewById(R.id.id_task_date_type_img);
            taskReviewLayout = itemView.findViewById(R.id.id_task_date_card);
            taskExpandableView = itemView.findViewById(R.id.id_task_date_expanded_row);
            taskUpdate = itemView.findViewById(R.id.id_task_with_date_update_txt_btn);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(getAdapterPosition());
        }
    }

    public static class TaskWithoutDateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView taskName, taskUpdate;
        private ImageView taskType;
        RelativeLayout taskExpandableView;
        CardView taskReviewLayout;
        ItemClickListener itemClickListener;

        public TaskWithoutDateViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            taskName = itemView.findViewById(R.id.id_task_no_date_name_txt);
            taskType = itemView.findViewById(R.id.id_task_no_date_type_img);
            taskReviewLayout = itemView.findViewById(R.id.id_task_no_date_card);
            taskExpandableView = itemView.findViewById(R.id.id_task_no_date_expanded_row);
            taskUpdate = itemView.findViewById(R.id.id_task_without_date_update_txt_btn);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(getAdapterPosition());
        }
    }
}