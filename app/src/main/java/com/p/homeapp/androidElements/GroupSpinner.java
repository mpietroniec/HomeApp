package com.p.homeapp.androidElements;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupSpinner {

    private FirebaseUser fUser;

    private List<Group> userGroupList;
    private ArrayAdapter<Group> arrayAdapter;

    public GroupSpinner() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public ArrayAdapter<Group> groupSpinnerFacade(Context context){
        return createGroupSpinner(context);
    }

    private ArrayAdapter<Group> createGroupSpinner(Context context) {

        userGroupList = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(context, R.layout.activity_add_task_spinner_item
                , userGroupList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getUserGroupsData();

        return arrayAdapter;
    }

    private void getUserGroupsData() {
        getUserGroupsId();
    }

    private void getUserGroupsId(){
        FirebaseDatabase.getInstance().getReference("userGroups").child(fUser.getUid()).get()
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

    private void getGroup(String groupId) {
        FirebaseDatabase.getInstance().getReference("groups").child(groupId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Group group = task.getResult().getValue(Group.class);
                    userGroupList.add(group);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
