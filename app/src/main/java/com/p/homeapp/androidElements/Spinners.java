package com.p.homeapp.androidElements;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;

import java.util.ArrayList;
import java.util.List;

public class Spinners {

    FirebaseUser fUser;

    private List<Group> userGroupList;

    public Spinners() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public ArrayAdapter<Group> groupSpinnerFacade(Context context){
        return groupSpinner(context);
    }

    private ArrayAdapter<Group> groupSpinner(Context context) {

        userGroupList = new ArrayList<>();

        ArrayAdapter<Group> arrayAdapter = new ArrayAdapter<>(context,
                R.layout.activity_add_task_spinner_item, userGroupList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        FirebaseDatabase.getInstance().getReference().child("groups").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            userGroupList.clear();
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                Group group = snapshot.getValue(Group.class);
                                if (group.getMembersId().contains(fUser.getUid())) {
                                    userGroupList.add(group);
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                });
        return arrayAdapter;
    }

}
