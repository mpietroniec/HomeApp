package com.p.homeapp.views.groupView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.userAdapters.UserAdapterWithThrowAway;
import com.p.homeapp.controllers.GroupController;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.mainView.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditGroupActivity extends AppCompatActivity {

    private EditText eTxtGroupName, eTxtGroupDescription;
    private Button btnSaveGroup;
    private RecyclerView rvMembersName;
    private UserAdapterWithThrowAway userAdapterWithThrowAway;
    private List<User> membersList;
    private GroupController groupController;

    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        eTxtGroupName = findViewById(R.id.etxt_group_name);
        eTxtGroupDescription = findViewById(R.id.etxt_group_description);
        btnSaveGroup = findViewById(R.id.btn_save_group);
        rvMembersName = findViewById(R.id.rv_members_name);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        membersList = new ArrayList<>();
        userAdapterWithThrowAway = new UserAdapterWithThrowAway(this, membersList, groupId);
        rvMembersName.setHasFixedSize(true);
        rvMembersName.setLayoutManager(new LinearLayoutManager(this));
        rvMembersName.setAdapter(userAdapterWithThrowAway);

        groupController = new GroupController(membersList, getApplicationContext(), userAdapterWithThrowAway);

        getGroupData();

        btnSaveGroup.setOnClickListener(view -> updateGroup(groupId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    public void startMainView(MenuItem item) {
        Intent intent = new Intent(EditGroupActivity.this, FragmentActivity.class);
        startActivity(intent);
    }

    private void getGroupData() {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Group group = snapshot.getValue(Group.class);
                        eTxtGroupName.setText(group.getName());
                        eTxtGroupDescription.setText(group.getDescription());
                        forceRefreshMembersList(groupId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateGroup(String groupId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", eTxtGroupName.getText().toString());
        map.put("description", eTxtGroupDescription.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId)
                .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditGroupActivity.this, "Group successfully updated",
                        Toast.LENGTH_SHORT).show();
                changeLayoutToGroupActivity();
            }
        });
    }

    private void changeLayoutToGroupActivity() {
        Intent intent = new Intent(EditGroupActivity.this, GroupActivity.class);
        startActivity(intent);
        finish();
    }

    private void forceRefreshMembersList(String groupId){
        FirebaseDatabase.getInstance().getReference("groupUsers")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupController.getAllGroupMembers(groupId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}