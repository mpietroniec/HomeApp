package com.p.homeapp.views.groupView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.GroupAdapter;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.groupView.createGroup.CreateGroupActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    FloatingActionButton floatingBtnAddGroup, floatingBtnCreateGroup, floatingBtnJoinToGroup;

    RecyclerView rvGroup;
    private List<Group> mGroups;
    private GroupAdapter groupAdapter;

    boolean flagIsClicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mGroups = new ArrayList<>();
        rvGroup = findViewById(R.id.group_recyclerView);
        groupAdapter = new GroupAdapter(getBaseContext(), mGroups);

        floatingBtnAddGroup = findViewById(R.id.add_group_button);
        floatingBtnCreateGroup = findViewById(R.id.create_group);
        floatingBtnJoinToGroup = findViewById(R.id.join_to_group);

        rvGroup.setHasFixedSize(true);
        rvGroup.setLayoutManager(new LinearLayoutManager(this));
        rvGroup.setAdapter(groupAdapter);

        //readGroups();
        readUserGroups();

        flagIsClicked = false;

        floatingBtnAddGroup.setOnClickListener(view -> {
            flagIsClicked = !flagIsClicked;
            if (flagIsClicked) {
                floatingBtnCreateGroup.setVisibility(View.VISIBLE);
                floatingBtnJoinToGroup.setVisibility(View.VISIBLE);
            } else {
                floatingBtnCreateGroup.setVisibility(View.INVISIBLE);
                floatingBtnJoinToGroup.setVisibility(View.INVISIBLE);
            }
        });

        floatingBtnCreateGroup.setOnClickListener(view -> {
            Intent intent = new Intent(GroupActivity.this, CreateGroupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void readUserGroups() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference().child("groups");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroups.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Group group = dataSnapshot.getValue(Group.class);
                    if(group.getMembersId().contains(currentUserId))
                    mGroups.add(group);
                }
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readGroups() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mGroups.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Group group = dataSnapshot.getValue(Group.class);
                    mGroups.add(group);
                }
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}