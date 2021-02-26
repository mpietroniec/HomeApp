package com.p.homeapp.views.groupView.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.Invitation;
import com.p.homeapp.entities.User;

public class GroupMenuDialog extends AppCompatDialogFragment {

    private String groupId;
    private Context mContext;
    private User userToInvite;
    private String usernameToFind;


    public GroupMenuDialog(String groupId, Context mContext) {
        this.groupId = groupId;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_group_menu_dialog, null);
        EditText eTxtFindUsername = view.findViewById(R.id.etxt_find_username);
        builder.setView(view)
                .setTitle("Invite user")
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usernameToFind = eTxtFindUsername.getText().toString();
                        checkUserExist();
                    }
                });
        return builder.create();
    }


    private void checkUserExist() {
        FirebaseDatabase.getInstance().getReference().child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    boolean flag = false;
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getLogin().toLowerCase().equals(usernameToFind.toLowerCase())) {
                            flag = true;
                            userToInvite = dataSnapshot.getValue(User.class);
                            checkUserIsAlreadyGroupMember();
                        }
                    }
                    if (flag == false) {
                        Toast.makeText(mContext, "User doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void checkUserIsAlreadyGroupMember() {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            Group group = task.getResult().getValue(Group.class);
                            if (group.getMembersId().contains(userToInvite.getId())) {
                                Toast.makeText(mContext, "User is already group member", Toast.LENGTH_SHORT).show();
                            } else {
                                checkUserIsAlreadyInvited();
                            }
                        }
                    }
                });
    }

    private void checkUserIsAlreadyInvited() {
        FirebaseDatabase.getInstance().getReference().child("invitations").child(userToInvite.getId())
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {
                    boolean flag = false;
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Invitation invitation = dataSnapshot.getValue(Invitation.class);
                        if (invitation.getGroupId().equals(groupId)) {
                            flag = true;
                            Toast.makeText(mContext, "User is already invited", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (flag == false) {
                        saveInvitation();
                    }
                }
            }
        });
    }

    private void saveInvitation() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                child("invitations").child(userToInvite.getId());

        Invitation invitation = new Invitation();
        String invitationId = reference.push().getKey();

        invitation.setId(invitationId);
        invitation.setGroupId(groupId);
        invitation.setInvitationSender(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.child(invitationId).setValue(invitation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mContext, "Invitation was send", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

