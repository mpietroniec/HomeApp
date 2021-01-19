package com.p.homeapp.views.addingSettlementViews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p.homeapp.R;
import com.p.homeapp.adapters.FragmentAddAdapter;
import com.p.homeapp.entities.User;

import java.util.ArrayList;


public class FragmentAddSettlement extends Fragment {
    // UI components
    private RecyclerView mRecyclerView;

    // vars
    private ArrayList<User> mUsers = new ArrayList<>();
    private FragmentAddAdapter mTaskOwnerAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_group_members, container, false);
        mRecyclerView = view.findViewById(R.id.id_group_members_recyclerView);
        initRecyclerView();
        insertFakeOwners();
        return view;
    }

    private void insertFakeOwners() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setLogin("Login: " + i);
            mUsers.add(user);
        }
        mTaskOwnerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mTaskOwnerAdapter = new FragmentAddAdapter(mUsers);
        mRecyclerView.setAdapter(mTaskOwnerAdapter);
    }
}
