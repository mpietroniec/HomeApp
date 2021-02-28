package com.p.homeapp.views.archives;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.p.homeapp.R;
import com.p.homeapp.adapters.archivesAdapters.TaskArchivesAdapter;
import com.p.homeapp.entities.Task;
import com.p.homeapp.helpers.DateParser;

import java.util.ArrayList;
import java.util.Date;

public class TaskArchivesActivity extends AppCompatActivity {
    // UI components
    private RecyclerView rvTaskArchives;

    //vars
    private ArrayList<Task> mTasks = new ArrayList<>();
    private TaskArchivesAdapter taskArchivesAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_archives);

        rvTaskArchives = findViewById(R.id.rv_task_archives);
        taskArchivesAdapter = new TaskArchivesAdapter(mTasks);
        insertRecyclerView();
        insertFakeTasks();
    }

    private void insertRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvTaskArchives.setLayoutManager(linearLayoutManager);

        taskArchivesAdapter = new TaskArchivesAdapter(mTasks);
        rvTaskArchives.setAdapter(taskArchivesAdapter);
    }

    private void insertFakeTasks() {
        for (int i = 1; i < 50; i++) {
            Task task = new Task();
            task.setTaskName("Task" + i);
            task.setGroupName("Grupa" + i);
            mTasks.add(task);
        }
        taskArchivesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_archives_menu, menu);
        return true;
    }
}