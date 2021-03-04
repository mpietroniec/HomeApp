package com.p.homeapp.views.groupView.createGroup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.groupView.GroupActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends AppCompatActivity {

    EditText eTxtGroupName, eTxtGroupDescription;
    Button btSaveGroup;

    private DatabaseReference mRootRef;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        eTxtGroupName = findViewById(R.id.etxt_group_name);
        eTxtGroupDescription = findViewById(R.id.etxt_group_description);
        btSaveGroup = findViewById(R.id.btn_save_group);

        mRootRef = FirebaseDatabase.getInstance().getReference("groups");
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        btSaveGroup.setOnClickListener(view -> {

            getGroupData();

        });
    }

    private void getGroupData() {
        String groupName = eTxtGroupName.getText().toString();
        String groupDescription = eTxtGroupDescription.getText().toString();
        String groupId = mRootRef.push().getKey();
        String creatorUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<String> membersIds = new ArrayList<>();
        membersIds.add(creatorUserId);
        List<String> tasksIds = new ArrayList<>();
        tasksIds.add("0");

        Group group = new Group();

        group.setName(groupName);
        group.setDescription(groupDescription);
        group.setId(groupId);
        group.setCreatorUserId(creatorUserId);
        group.setMembersId(membersIds);
        group.setTasksId(tasksIds);

        saveGroup(group);
    }

    private void saveGroup(Group group) {
        FirebaseDatabase.getInstance().getReference("groups").child(group.getId()).setValue(group);
        addGroupIdToUser(group.getId());

        Intent intent = new Intent(CreateGroupActivity.this, GroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void addGroupIdToUser(String groupId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(fUser.getUid());
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user = task.getResult().getValue(User.class);
                List<String> userGroupsId = user.getUserGroupsId();
                if(userGroupsId == null){
                    userGroupsId = new ArrayList<>();
                }
                userGroupsId.add(groupId);
                user.setUserGroupsId(userGroupsId);
                databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateGroupActivity.this, "Group was created", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
    }
}

