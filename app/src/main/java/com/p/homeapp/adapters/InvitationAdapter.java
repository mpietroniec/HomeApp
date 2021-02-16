package com.p.homeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.Invitation;
import com.p.homeapp.entities.User;

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

    @Override
    public int getItemCount() {
        return mInvitations.size();
    }

    public class InvitationViewHolder extends RecyclerView.ViewHolder {

        private TextView txtGroupName, txtInvitationSender;
        private Button btnAccept, btnReject;

        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            txtGroupName = itemView.findViewById(R.id.txt_group_name);
            txtInvitationSender = itemView.findViewById(R.id.txt_invitation_sender);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }
}
