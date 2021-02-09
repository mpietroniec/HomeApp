package com.p.homeapp.views.groupView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.groupView.dialogs.GroupDialogShowMembers;
import com.p.homeapp.views.groupView.dialogs.GroupMenuDialog;

public class GroupMenuActivity extends AppCompatActivity {

    private TextView txtGroupName, txtGroupDescription;
    private Button btEditGroup, btInviteUsers, btShowMembers;

    private String groupId;
    private Group group;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_menu);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        txtGroupName = findViewById(R.id.txt_group_name);
        txtGroupDescription = findViewById(R.id.txt_group_description);
        btEditGroup = findViewById(R.id.bt_edit_group);
        btInviteUsers = findViewById(R.id.bt_invite_users);
        btShowMembers = findViewById(R.id.bt_show_members);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        getGroup(groupId);

        btInviteUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInvitationDialog();
            }
        });

        btShowMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShowMembersDialog();
            }
        });


    }

    private void getGroup(String groupId) {

        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        group = snapshot.getValue(Group.class);
                        txtGroupDescription.setText(group.getDescription());
                        txtGroupName.setText(group.getName());
                        if(!group.getCreatorUserId().equals(firebaseUser.getUid())){
                            btEditGroup.setVisibility(View.INVISIBLE);
                            btInviteUsers.setVisibility(View.INVISIBLE);
                        }
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