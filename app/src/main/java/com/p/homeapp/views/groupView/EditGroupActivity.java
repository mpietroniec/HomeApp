package com.p.homeapp.views.groupView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.userAdapters.UserAdapterWithThrowAway;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;

import java.util.ArrayList;
import java.util.List;

public class EditGroupActivity extends AppCompatActivity {

    private EditText eTxtGroupName, eTxtGroupDescription;
    private RecyclerView rvMembersName;
    private UserAdapterWithThrowAway userAdapterWithThrowAway;
    private List<User> membersList;

    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        eTxtGroupName = findViewById(R.id.etxt_group_name);
        eTxtGroupDescription = findViewById(R.id.etxt_group_description);
        rvMembersName = findViewById(R.id.rv_members_name);

        membersList = new ArrayList<>();
        userAdapterWithThrowAway = new UserAdapterWithThrowAway(this, membersList);
        rvMembersName.setHasFixedSize(true);
        rvMembersName.setLayoutManager(new LinearLayoutManager(this));
        rvMembersName.setAdapter(userAdapterWithThrowAway);

        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");

        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                membersList.clear();
                Group group = snapshot.getValue(Group.class);
                eTxtGroupName.setText(group.getName());
                eTxtGroupDescription.setText(group.getDescription());
                FirebaseDatabase.getInstance().getReference().child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            User user = dataSnapshot.getValue(User.class);
                            if(group.getMembersId().contains(user.getId())){
                                membersList.add(user);
                            }
                        }
                        System.out.println("Użytkownicy: " + membersList.toString());
                        userAdapterWithThrowAway.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}