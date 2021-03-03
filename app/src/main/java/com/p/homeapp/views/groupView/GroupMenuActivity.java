package com.p.homeapp.views.groupView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.addingTasksViews.AddTaskActivity;
import com.p.homeapp.views.groupView.dialogs.GroupDialogShowMembers;
import com.p.homeapp.views.groupView.dialogs.GroupMenuDialog;

public class GroupMenuActivity extends AppCompatActivity {

    private TextView txtGroupName, txtGroupDescription;
    private Button btnEditGroup, btnInviteUsers, btnShowMembers, btnLeaveGroup;
    private FloatingActionButton btnAddTaskFromGroupMenu;

    private String groupId;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_menu);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        txtGroupName = findViewById(R.id.txt_group_name);
        txtGroupDescription = findViewById(R.id.txt_group_description);
        btnEditGroup = findViewById(R.id.btn_edit_group);
        btnInviteUsers = findViewById(R.id.btn_invite_users);
        btnShowMembers = findViewById(R.id.btn_show_members);
        btnAddTaskFromGroupMenu = findViewById(R.id.btn_add_task_from_group_menu);
        btnLeaveGroup = findViewById(R.id.btn_leave_group);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        getGroupData();

        btnInviteUsers.setOnClickListener(view -> openInvitationDialog());

        btnShowMembers.setOnClickListener(view -> openShowMembersDialog());

        btnEditGroup.setOnClickListener(view -> {
            Intent intentEditGroup = new Intent(GroupMenuActivity.this, EditGroupActivity.class);
            intentEditGroup.putExtra("groupId", groupId);
            startActivity(intentEditGroup);
        });

        btnLeaveGroup.setOnClickListener(view -> createLeavingDialog());

        btnAddTaskFromGroupMenu.setOnClickListener(v -> {
            Intent intentAddTask = new Intent(GroupMenuActivity.this, AddTaskActivity.class);
            intentAddTask.putExtra("groupId", groupId);
            startActivity(intentAddTask);
        });
    }

    private void getGroupData() {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Group group = snapshot.getValue(Group.class);
                        txtGroupName.setText(snapshot.getValue(Group.class).getName());
                        txtGroupDescription.setText(snapshot.getValue(Group.class).getDescription());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void createLeavingDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Are you sure to leave this group?");
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                leaveGroup();
                Toast.makeText(GroupMenuActivity.this, "You left the group", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void leaveGroup() {
        DatabaseReference groupReference = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId);
        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);
                group.getMembersId().remove(firebaseUser.getUid());
                groupReference.child("membersId").setValue(group.getMembersId());
                Intent intent = new Intent(GroupMenuActivity.this, GroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openShowMembersDialog() {
        GroupDialogShowMembers groupDialogShowMembers = new GroupDialogShowMembers(groupId, getApplicationContext());
        groupDialogShowMembers.show(getSupportFragmentManager(), "Group dialog Show Members");
    }

    public void openInvitationDialog() {
        GroupMenuDialog groupMenuDialog = new GroupMenuDialog(groupId, getApplicationContext());
        groupMenuDialog.show(getSupportFragmentManager(), "Group dialog");
    }
}