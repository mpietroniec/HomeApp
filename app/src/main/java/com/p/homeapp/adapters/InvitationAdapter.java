package com.p.homeapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.Invitation;
import com.p.homeapp.entities.User;

import java.util.ArrayList;
import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

    private Context mContext;
    private List<Invitation> mInvitations;

    private FirebaseUser firebaseUser;

    public InvitationAdapter(Context mContext, List<Invitation> mInvitations) {
        this.mContext = mContext;
        this.mInvitations = mInvitations;
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.invitation_item, parent, false);
        return new InvitationAdapter.InvitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Invitation invitation = mInvitations.get(position);

        getGroupName(invitation, holder);

        getInvitationSenderName(invitation, holder);

        holder.ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAcceptDialog(invitation);
            }
        });

        holder.ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRejectDialog(invitation);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mInvitations.size();
    }

    public class InvitationViewHolder extends RecyclerView.ViewHolder {

        private TextView txtGroupName, txtInvitationSender;
        private ImageView ivAccept, ivReject;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            txtGroupName = itemView.findViewById(R.id.txt_group_name);
            txtInvitationSender = itemView.findViewById(R.id.txt_invitation_sender);
            ivAccept = itemView.findViewById(R.id.iv_accept);
            ivReject = itemView.findViewById(R.id.iv_reject);
        }
    }


    private void getInvitationSenderName(Invitation invitation, InvitationViewHolder holder) {
        FirebaseDatabase.getInstance().getReference().child("users").child(invitation.getInvitationSender())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        holder.txtInvitationSender.setText(user.getLogin());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getGroupName(Invitation invitation, InvitationViewHolder holder) {
        FirebaseDatabase.getInstance().getReference().child("groups").child(invitation.getGroupId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Group group = snapshot.getValue(Group.class);
                        holder.txtGroupName.setText(group.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void createAcceptDialog(Invitation invitation) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Are you sure to accept this invitation?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase.getInstance().getReference().child("groups").child(invitation.getGroupId())
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Group group = task.getResult().getValue(Group.class);
                        addUserToMembersId(group, invitation);

                        addGroupIdToUsersGroups(group, invitation, alertDialog);
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void addGroupIdToUsersGroups(Group group, Invitation invitation, AlertDialog alertDialog) {
        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance()
                .getReference().child("users").child(firebaseUser.getUid());
        userDatabaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User user = task.getResult().getValue(User.class);
                List<String> userGroupsId = user.getUserGroupsId();
                if (userGroupsId == null) {
                    userGroupsId = new ArrayList<>();
                }
                userGroupsId.add(group.getId());
                user.setUserGroupsId(userGroupsId);
                userDatabaseReference.setValue(user);
                deleteInvitation(invitation, alertDialog);

            }
        });
    }

    private void deleteInvitation(Invitation invitation, AlertDialog alertDialog) {
        FirebaseDatabase.getInstance().getReference().child("invitations")
                .child(firebaseUser.getUid()).child(invitation.getId()).removeValue().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mContext, "Invitation was accepted", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
    }

    private void addUserToMembersId(Group group, Invitation invitation) {
        List<String> membersId = group.getMembersId();
        membersId.add(firebaseUser.getUid());
        group.setMembersId(membersId);
        FirebaseDatabase.getInstance().getReference().child("groups")
                .child(invitation.getGroupId()).setValue(group);
    }

    private void createRejectDialog(Invitation invitation) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Are you sure to reject invitation for this group?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase.getInstance().getReference().child("invitations")
                        .child(firebaseUser.getUid()).child(invitation.getId()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mContext, "Invitation was reject", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        });
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
