
package com.p.homeapp.mainView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.TaskAdapter;
import com.p.homeapp.entities.Task;

import java.util.ArrayList;

public class FragmentTasks extends Fragment implements ItemClickListener {
    //UI components
    private RecyclerView mRecyclerView;

    //vars
    private ArrayList<Task> mTasks = new ArrayList<>();
    private TaskAdapter mTaskAdapter;
    private Context context;
    private static final String TAG = "FragmentTask";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.activity_fragment_review, container, false);
        View view = inflater.inflate(R.layout.activity_fragment_review, container, false);
        mRecyclerView = view.findViewById(R.id.id_task_RecyclerView);
        initRecyclerView();
        insertFakeTasks();
        return view;
    }

    private void insertFakeTasks() {
        for (int i = 0; i < 50; i++) {
            Task task = new Task();
            task.setTaskName("Name: " + i);
            mTasks.add(task);
        }
        mTaskAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mTaskAdapter = new TaskAdapter(mTasks, this);
        mRecyclerView.setAdapter(mTaskAdapter);
    }

    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onNoteClick: clicked." + position);
    }
}