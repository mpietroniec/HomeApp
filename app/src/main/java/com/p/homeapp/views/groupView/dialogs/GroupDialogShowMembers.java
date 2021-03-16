package com.p.homeapp.views.groupView.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.userAdapters.UserAdapter;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.User;

import java.util.ArrayList;
import java.util.List;

public class GroupDialogShowMembers extends AppCompatDialogFragment {

    private String groupId;
    private Context context;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> membersList;

    public GroupDialogShowMembers(String groupId, Context context) {
        this.groupId = groupId;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_group_dialog_show_members, null);

        recyclerView = view.findViewById(R.id.rv_members_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        membersList = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), membersList);
        recyclerView.setAdapter(userAdapter);

        getAllMembers(groupId);

        builder.setView(view).setTitle("All Members")
                .setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }

    private void getAllMembers(String groupId) {
        membersList.clear();
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Group currentGroup = task.getResult().getValue(Group.class);

                FirebaseDatabase.getInstance().getReference().child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        for (String memberId : currentGroup.getMembersId()) {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                User member = dataSnapshot.getValue(User.class);
                                if (member.getId().equals(memberId)) {
                                    membersList.add(member);
                                }
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}