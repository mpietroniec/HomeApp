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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
                        sendInvite(usernameToFind);
                    }
                });
        return builder.create();
    }

    private void sendInvite(String usernameToFind) {

        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean flag = true;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getLogin().toLowerCase().equals(usernameToFind.toLowerCase())) {
                        userToInvite = dataSnapshot.getValue(User.class);
                        checkUserIsAlreadyInvited();
                        flag = false;
                    }
                }
                if (flag) {
                    Toast.makeText(mContext, "User does't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserIsAlreadyInvited() {

        FirebaseDatabase.getInstance().getReference().child("invitations").child(userToInvite.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean flag = true;

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Invitation invitationForCheck = dataSnapshot.getValue(Invitation.class);
                            if (invitationForCheck.getGroupId().equals(groupId)) {
                                flag = false;
                                Toast.makeText(mContext, "User is already invite", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (flag) {
                            checkUserIsAlreadyGroupMember();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void checkUserIsAlreadyGroupMember() {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Group group = snapshot.getValue(Group.class);
                        if (group.getMembersId().contains(userToInvite.getId())) {
                            Toast.makeText(mContext, "User is already group member", Toast.LENGTH_SHORT).show();
                        } else {
                            saveInvitation();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
        invitation.setAccepted(false);
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

