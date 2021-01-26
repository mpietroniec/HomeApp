package com.p.homeapp.views.createGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.groupView.GroupActivity;

public class CreateGroupActivity extends AppCompatActivity {

    EditText eTxtGroupName, eTxtGroupDescription;
    Button btSaveGroup;

    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        eTxtGroupName = findViewById(R.id.group_name_etxt);
        eTxtGroupDescription = findViewById(R.id.group_description_etxt);
        btSaveGroup = findViewById(R.id.save_group_btn);

        mRootRef = FirebaseDatabase.getInstance().getReference("groups");

        btSaveGroup.setOnClickListener(view -> {
            String groupName = eTxtGroupName.getText().toString();
            String groupDescription = eTxtGroupDescription.getText().toString();
            String groupId = mRootRef.push().getKey();

            Group group = new Group();
            group.setName(groupName);
            group.setDescription(groupDescription);
            group.setId(groupId);

            saveGroup(group);
        });
    }

    private void saveGroup(Group group) {
        FirebaseDatabase.getInstance().getReference("groups").child(group.getId()).setValue(group);
        Toast.makeText(CreateGroupActivity.this, "Group was created", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateGroupActivity.this, GroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}