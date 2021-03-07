package com.p.homeapp.views.groupView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.GroupAdapter;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.groupView.createGroup.CreateGroupActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private FloatingActionButton floatingBtnAddGroup, floatingBtnCreateGroup, floatingBtnJoinToGroup;

    private RecyclerView rvGroup;
    private List<Group> mGroups;
    private GroupAdapter groupAdapter;

    private FirebaseUser fUser;

    boolean flagIsClicked;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mGroups = new ArrayList<>();
        rvGroup = findViewById(R.id.rv_group);
        groupAdapter = new GroupAdapter(getBaseContext(), mGroups);

        floatingBtnAddGroup = findViewById(R.id.btn_add_group);
        floatingBtnCreateGroup = findViewById(R.id.btn_create_group);
        floatingBtnJoinToGroup = findViewById(R.id.btn_join_to_group);

        rvGroup.setHasFixedSize(true);
        rvGroup.setLayoutManager(new LinearLayoutManager(this));
        rvGroup.setAdapter(groupAdapter);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

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
            startCreatingGroup();
        });
    }

    private void startCreatingGroup() {
        Intent intent = new Intent(GroupActivity.this, CreateGroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void readUserGroups() {
        String currentUserId = fUser.getUid();
        getUserGroupsId(currentUserId);
        Query query = FirebaseDatabase.getInstance().getReference().child("groups");
        mGroups.clear();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

    private void getUserGroupsId(String currentUserId) {
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    List<String> userGroupsIdList = task.getResult().getValue(User.class).getUserGroupsId();
                    if (userGroupsIdList != null) {
                        for (String groupId : userGroupsIdList) {
                            getGroup(groupId);
                        }
                    }
                    groupAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getGroup(String groupId) {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Group group = task.getResult().getValue(Group.class);
                mGroups.add(group);
            }
        });
    }
}