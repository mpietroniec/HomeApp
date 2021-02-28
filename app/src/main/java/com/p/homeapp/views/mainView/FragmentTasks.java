
package com.p.homeapp.views.mainView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.TaskAdapter;
import com.p.homeapp.entities.Task;
import com.p.homeapp.helpers.DateParser;
import com.p.homeapp.views.addingTasksViews.AddTaskActivity;
import com.p.homeapp.views.archives.TaskArchivesActivity;

import java.util.ArrayList;

public class FragmentTasks extends Fragment implements ItemClickListener {
    //UI components
    private RecyclerView mRecyclerView;
    private FloatingActionButton addTaskButton;

    //vars
    private ArrayList<Task> mTasks = new ArrayList<>();

    private TaskAdapter mTaskAdapter;
    private Context context;
    private static final String TAG = "FragmentTask";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.activity_fragment_review, container, false);
        View view = inflater.inflate(R.layout.activity_fragment_tasks_review, container, false);
        mRecyclerView = view.findViewById(R.id.tasks_recyclerView);
        addTaskButton = view.findViewById(R.id.btn_add_task);
        addTaskButton.setOnClickListener(v -> {
            // UWAGA UWAGA UWAGA zamienione klasy
            Intent intent = new Intent(getContext(), AddTaskActivity.class);
            startActivity(intent);
        });
        initRecyclerView();
        insertFakeTasks();
        return view;
    }

    private void insertFakeTasks() {
        for (int i = 1; i <= 50; i++) {
            Task task = new Task();
            task.setTaskName("Name: " + i);
            if(i%2==0){
                task.setDeadline(DateParser.stringToDateParser("2000-01-01"));
                task.setDrawable(R.drawable.ic_baseline_shopping_cart_26);
            } else {

                task.setDrawable(R.drawable.ic_baseline_home_work_26);
            }
            mTasks.add(task);
        }
        mTaskAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mTaskAdapter = new TaskAdapter(mTasks,this);
        mRecyclerView.setAdapter(mTaskAdapter);
    }



    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onNoteClick: clicked." + position);
    }
}