package com.p.homeapp.views.groupView.createGroup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.controllers.GroupController;
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
    private GroupController groupController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        eTxtGroupName = findViewById(R.id.etxt_group_name);
        eTxtGroupDescription = findViewById(R.id.etxt_group_description);
        btSaveGroup = findViewById(R.id.btn_save_group);

        mRootRef = FirebaseDatabase.getInstance().getReference("groups");
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        groupController = new GroupController(getApplicationContext());

        btSaveGroup.setOnClickListener(view -> {
        Group group = getGroupData();
        saveGroup(group, fUser.getUid());
        });

    }

    private Group getGroupData() {
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
        group.setTasksId(tasksIds);

        return group;
    }

    private void saveGroup(Group group, String userId) {
        groupController.createGroup(group, userId);
        changeLayoutToGroupActivity();
    }

    private void changeLayoutToGroupActivity(){
        FirebaseDatabase.getInstance().getReference("userGroups").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = new Intent(CreateGroupActivity.this, GroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

