package com.p.homeapp.views.groupView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.R;
import com.p.homeapp.controllers.GroupController;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.addingTasksViews.AddTaskActivity;
import com.p.homeapp.views.groupView.dialogs.GroupDialogShowMembers;
import com.p.homeapp.views.groupView.dialogs.GroupDialogInviteUser;
import com.p.homeapp.views.mainView.FragmentActivity;

public class GroupMenuActivity extends AppCompatActivity {

    private TextView txtGroupName, txtGroupDescription;
    private Button btnEditGroup, btnInviteUsers, btnShowMembers, btnLeaveGroup, btnRemoveGroup;
    private FloatingActionButton btnAddTaskFromGroupMenu;

    private String groupId;

    private GroupController groupController;

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
        btnRemoveGroup = findViewById(R.id.btn_remove_group);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        groupController = new GroupController(getApplicationContext());

        getGroupData();

        btnInviteUsers.setOnClickListener(view -> openInvitationDialog());

        btnShowMembers.setOnClickListener(view -> openShowMembersDialog());

        btnEditGroup.setOnClickListener(view -> startEditGroupActivity());

        btnLeaveGroup.setOnClickListener(view -> createLeavingDialog());

        btnAddTaskFromGroupMenu.setOnClickListener(v -> startAddTaskActivity());

/*        btnRemoveGroup.setOnClickListener(view -> {
            //TODO
            startRemoveGroupDialog();
        });*/
    }

    private void startRemoveGroupDialog() {
        AlertDialog removeDialog = new AlertDialog.Builder(this).create();
        removeDialog.setTitle("Are you sure to remove this group?");
        removeDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        removeDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        removeDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    public void startMainView(MenuItem item) {
        Intent intent = new Intent(GroupMenuActivity.this, FragmentActivity.class);
        startActivity(intent);
    }

    private void startAddTaskActivity() {
        Intent intentAddTask = new Intent(GroupMenuActivity.this, AddTaskActivity.class);
        intentAddTask.putExtra("groupId", groupId);
        startActivity(intentAddTask);
    }

    private void startEditGroupActivity() {
        Intent intentEditGroup = new Intent(GroupMenuActivity.this, EditGroupActivity.class);
        intentEditGroup.putExtra("groupId", groupId);
        startActivity(intentEditGroup);
    }

    private void getGroupData() {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Group group = task.getResult().getValue(Group.class);
                txtGroupName.setText(group.getName());
                txtGroupDescription.setText(group.getDescription());
                checkUser(group);
            }
        });
    }

    private void checkUser(Group group) {
        FirebaseDatabase.getInstance().getReference("groupUsers").child(groupId)
                .child(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getValue() != null) {
                                btnLeaveGroup.setVisibility(View.VISIBLE);
                                btnEditGroup.setVisibility(View.GONE);
                                btnInviteUsers.setVisibility(View.GONE);
                                btnRemoveGroup.setVisibility(View.GONE);
                                if (firebaseUser.getUid().equals(group.getCreatorUserId())) {
                                    btnEditGroup.setVisibility(View.VISIBLE);
                                    btnInviteUsers.setVisibility(View.VISIBLE);
                                    btnRemoveGroup.setVisibility(View.VISIBLE);
                                    btnLeaveGroup.setVisibility(View.GONE);
                                }
                            } else {
                                activateDoNotPermissionAction();
                            }
                        }
                    }
                });
    }

    private void activateDoNotPermissionAction() {
        Toast.makeText(GroupMenuActivity.this,
                "You don't have permission to view this group", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(GroupMenuActivity.this, GroupActivity.class);
        startActivity(intent);
        finish();
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
                Toast.makeText(GroupMenuActivity.this, "You left the group",
                        Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void openShowMembersDialog() {
        GroupDialogShowMembers groupDialogShowMembers = new GroupDialogShowMembers(groupId, getApplicationContext());
        groupDialogShowMembers.show(getSupportFragmentManager(), "Group dialog Show Members");
    }

    public void openInvitationDialog() {
        GroupDialogInviteUser groupMenuDialog = new GroupDialogInviteUser(groupId, getApplicationContext());
        groupMenuDialog.show(getSupportFragmentManager(), "Group dialog");
    }
}