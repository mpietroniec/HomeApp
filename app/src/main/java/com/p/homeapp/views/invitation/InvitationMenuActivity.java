package com.p.homeapp.views.invitation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.InvitationAdapter;
import com.p.homeapp.entities.Invitation;

import java.util.ArrayList;
import java.util.List;

public class InvitationMenuActivity extends AppCompatActivity {

    RecyclerView rvInvitations;
    private List<Invitation> mInvitations;
    private InvitationAdapter invitationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_menu);

        mInvitations = new ArrayList<>();
        rvInvitations = findViewById(R.id.rv_invitations);
        invitationAdapter = new InvitationAdapter(this, mInvitations);

        rvInvitations.setHasFixedSize(true);
        rvInvitations.setLayoutManager(new LinearLayoutManager(this));
        rvInvitations.setAdapter(invitationAdapter);

        readInvitations();
    }

    private void readInvitations() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("invitations").child(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mInvitations.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Invitation invitation = dataSnapshot.getValue(Invitation.class);
                            mInvitations.add(invitation);
                        }
                        invitationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}