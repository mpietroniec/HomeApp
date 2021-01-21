package com.p.homeapp.views.mainView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.views.addingSettlementViews.AddSettlementActivity;

public class FragmentMoneyBalance extends Fragment implements ItemClickListener {
    // UI components
    private RecyclerView mRecyclerView;
    private FloatingActionButton addExpenditureButton;

    //vars
    private static final String TAG = "FragmentMoneyBalance";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.activity_fragment_review, container, false);
        View view = inflater.inflate(R.layout.activity_fragment_tasks_review, container, false);
        mRecyclerView = view.findViewById(R.id.id_recyclerView);
        addExpenditureButton = view.findViewById(R.id.id_add_button);
        addExpenditureButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddSettlementActivity.class);
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onNoteClick: clicked." + position);
    }
}
