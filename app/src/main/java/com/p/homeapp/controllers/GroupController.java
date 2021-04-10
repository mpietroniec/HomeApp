package com.p.homeapp.controllers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;

import java.util.List;

public class GroupController {

    private FirebaseUser fUser;
    private List objectsList;
    private Context context;
    private RecyclerView.Adapter adapter;

    public GroupController(Context context) {
        this.context = context;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public GroupController(List objectsList, Context context, RecyclerView.Adapter adapter) {
        this.objectsList = objectsList;
        this.context = context;
        this.adapter = adapter;
        this.fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void getUserGroups() {
        readUserGroups();
    }

    public void createGroup(Group group, String userId) {
        addDataToGroupTable(group, userId);
    }

    public void getAllGroupMembers(String groupId) {
        getMembersId(groupId);
    }

    public void joinToGroup(String groupId, String userId) {
        addDataToGroupUsersTable(groupId, userId);
        addDataToUserGroupsTable(groupId, userId);
    }

    private void addDataToGroupTable(Group group, String userId) {
        FirebaseDatabase.getInstance().getReference("groups").child(group.getId()).setValue(group)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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


    private void readUserGroups() {
        String currentUserId = fUser.getUid();
        FirebaseDatabase.getInstance().getReference().child("userGroups").child(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    String groupId = dataSnapshot.getKey();
                    getGroup(groupId);
                }
            }
        });
    }


    private void getGroup(String groupId) {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Group group = task.getResult().getValue(Group.class);
                objectsList.add(group);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void addDataToGroupUsersTable(String groupId, String userId) {
        FirebaseDatabase.getInstance().getReference("groupUsers").child(groupId).child(userId)
                .setValue(true);
    }

    private void addDataToUserGroupsTable(String groupId, String userId) {
        FirebaseDatabase.getInstance().getReference("userGroups").child(userId).
                child(groupId).setValue(true);
    }

    private void getMembersId(String groupId) {
        objectsList.clear();
        FirebaseDatabase.getInstance().getReference().child("groupUsers").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String userId = snapshot.getKey();
                    getUsersForMembersList(userId);
                }
            }
        });
    }

    private void getUsersForMembersList(String userId) {
        FirebaseDatabase.getInstance().getReference("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult().getValue(User.class);
                    objectsList.add(user);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}

