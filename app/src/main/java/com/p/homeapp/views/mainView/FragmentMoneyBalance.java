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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.ExpenditureAdapter;
import com.p.homeapp.entities.Expenditure;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.addingSettlementViews.AddSettlementActivity;

import java.util.ArrayList;

public class FragmentMoneyBalance extends Fragment implements ItemClickListener {
    // UI components
    private RecyclerView mRecyclerView;
    private FloatingActionButton addExpenditureButton;

    //vars
    private ArrayList<Expenditure> mExpenditures = new ArrayList<>();
    private ArrayList<Group> mGroups = new ArrayList<>();

    private static final String TAG = "FragmentMoneyBalance";
    private ExpenditureAdapter mExpenditureAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_money_balance, container, false);
        mRecyclerView = view.findViewById(R.id.rv_money_balance);
        addExpenditureButton = view.findViewById(R.id.btn_add_task);
        addExpenditureButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddSettlementActivity.class);
            startActivity(intent);
        });
        intiRecyclerView();
        readUserGroups();
        return view;
    }

    private void intiRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mExpenditureAdapter = new ExpenditureAdapter(mGroups,getContext());
        mRecyclerView.setAdapter(mExpenditureAdapter);
    }

    private void readUserGroups(){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("groups");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroups.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Group group = dataSnapshot.getValue(Group.class);
/*                    if(group.getMembersId().contains(currentUserId))
                        mGroups.add(group);*/
                }
                mExpenditureAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onNoteClick: clicked." + position);
    }
}
