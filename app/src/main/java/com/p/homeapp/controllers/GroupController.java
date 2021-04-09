package com.p.homeapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.adapters.GroupAdapter;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.groupView.GroupActivity;
import com.p.homeapp.views.groupView.createGroup.CreateGroupActivity;

import java.util.List;

public class GroupController {

    private FirebaseUser fUser;
    private List<Group> mGroups;
    private Context context;
    private GroupAdapter groupAdapter;

    public GroupController(Context context) {
        this.context = context;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public GroupController(List<Group> mGroups, Context context, GroupAdapter groupAdapter) {
        this.mGroups = mGroups;
        this.context = context;
        this.groupAdapter = groupAdapter;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void getUserGroups(){
        redUserGroups();
    }

    public void createGroup(Group group, String userId){
        addDataToGroupTable(group, userId);
    }

    public void joinToGroup(String groupId, String userId){
        addDataToGroupUsersTable(groupId, userId);
        addDataToUserGroupsTable(groupId, userId);
    }

    private void addDataToGroupTable(Group group, String userId){
        FirebaseDatabase.getInstance().getReference("groups").child(group.getId()).setValue(group)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            addDataToGroupUsersTable(group.getId(), userId);
                            addDataToUserGroupsTable(group.getId(), userId);
                            Toast.makeText(context, "Group was created successfully ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Group creation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void redUserGroups(){
        String currentUserId = fUser.getUid();
        FirebaseDatabase.getInstance().getReference().child("userGroups").child(currentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        String groupId = snapshot.getKey();
                        getGroup(groupId);
                    }
                }
            }
        });
    }

    private void getGroup(String groupId){
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Group group = task.getResult().getValue(Group.class);
                        mGroups.add(group);
                        groupAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void addDataToGroupUsersTable(String groupId, String userId){
        FirebaseDatabase.getInstance().getReference("groupUsers").child(groupId).child(userId)
                .setValue(true);
    }

    private void addDataToUserGroupsTable(String groupId, String userId){
        FirebaseDatabase.getInstance().getReference("userGroups").child(userId).
                child(groupId).setValue(true);
    }

}

