package com.p.homeapp.views.groupView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.GroupAdapter;
import com.p.homeapp.controllers.GroupController;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.groupView.createGroup.CreateGroupActivity;
import com.p.homeapp.views.mainView.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private FloatingActionButton floatingBtnAddGroup, floatingBtnCreateGroup, floatingBtnJoinToGroup;

    private RecyclerView rvGroup;
    private List<Group> mGroups;
    private GroupAdapter groupAdapter;
    private GroupController groupController;

    private FirebaseUser fUser;

    boolean flagIsClicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mGroups = new ArrayList<>();
        rvGroup = findViewById(R.id.rv_group);
        groupAdapter = new GroupAdapter(getBaseContext(), mGroups, this);
        groupController = new GroupController(mGroups, getApplicationContext(), groupAdapter);

        floatingBtnAddGroup = findViewById(R.id.btn_add_group);
        floatingBtnCreateGroup = findViewById(R.id.btn_create_group);
        floatingBtnJoinToGroup = findViewById(R.id.btn_join_to_group);

        rvGroup.setHasFixedSize(true);
        rvGroup.setLayoutManager(new LinearLayoutManager(this));

        rvGroup.setAdapter(groupAdapter);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        groupController.getUserGroups();

        flagIsClicked = false;

        floatingBtnAddGroup.setOnClickListener(view -> {
            flagIsClicked = !flagIsClicked;
            if (flagIsClicked) {
                floatingBtnCreateGroup.setVisibility(View.VISIBLE);
                floatingBtnJoinToGroup.setVisibility(View.VISIBLE);
            } else {
                floatingBtnCreateGroup.setVisibility(View.INVISIBLE);
                floatingBtnJoinToGroup.setVisibility(View.INVISIBLE);
            }
        });

        floatingBtnCreateGroup.setOnClickListener(view -> {
            startCreatingGroup();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GroupActivity.this, FragmentActivity.class);
        startActivity(intent);
        finish();
    }

    private void startCreatingGroup() {
        Intent intent = new Intent(GroupActivity.this, CreateGroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}