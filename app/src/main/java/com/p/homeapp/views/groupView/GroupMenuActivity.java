package com.p.homeapp.views.groupView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;

public class GroupMenuActivity extends AppCompatActivity {

    private TextView txtGroupName, txtGroupDescription;
    private Button btEditGroup, btInviteUsers;

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

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        getGroup(groupId);



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

    public void openDialog() {
        GroupMenuDialog groupMenuDialog = new GroupMenuDialog();
        groupMenuDialog.show(getSupportFragmentManager(), "Group dialog");
    }
}