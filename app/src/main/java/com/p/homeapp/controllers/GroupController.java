package com.p.homeapp.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.adapters.GroupAdapter;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.groupView.GroupActivity;
import com.p.homeapp.views.groupView.GroupMenuActivity;

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

    public void removeGroup(String groupId) {
        deleteGroupIdFromUsers(groupId);
    }

    public void getUserGroups() {
        readUserGroups();
    }

    private void deleteGroupIdFromUsers(String groupId) {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> membersIdList;
                    membersIdList = task.getResult().getValue(Group.class).getMembersId();
                    for (String memberId : membersIdList) {
                        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference()
                                .child("users").child(memberId);
                        userDatabaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                User user = task.getResult().getValue(User.class);
                                deleteGroupIdFromSingleUser(user, groupId, userDatabaseReference);
                            }
                        });
                    }
                    deleteGroupFromDb(groupId);

                    Intent intent = new Intent(context, GroupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

    }

    private void deleteGroupIdFromSingleUser(User user, String groupId, DatabaseReference databaseReference) {
        user.getUserGroupsId().remove(groupId);
        databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }
        });

    }

    private void readUserGroups() {
        String currentUserId = fUser.getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        getUserGroupsId(currentUserId);
                    }
                });
    }

    private void getUserGroupsId(String currentUserId) {
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> userGroupsIdList = task.getResult().getValue(User.class).getUserGroupsId();
                            if (userGroupsIdList != null) {
                                for (String groupId : userGroupsIdList) {
                                    getGroup(groupId);
                                }
                            }
                        }
                    }
                });
    }

    private void getGroup(String groupId) {
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

    private void deleteGroupFromDb(String groupId) {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(context, "Group was deleted", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}

