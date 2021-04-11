package com.p.homeapp.adapters.userAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.p.homeapp.R;
import com.p.homeapp.controllers.GroupController;
import com.p.homeapp.entities.User;
import com.p.homeapp.views.groupView.EditGroupActivity;

import java.util.List;

public class UserAdapterWithThrowAway extends RecyclerView.Adapter<UserAdapterWithThrowAway.ViewHolder> {

    private Context context;
    private List<User> mUsers;
    private String groupId;
    private GroupController groupController;

    private FirebaseUser firebaseUser;

    public UserAdapterWithThrowAway(Context context, List<User> mUsers, String groupId) {
        this.context = context;
        this.mUsers = mUsers;
        this.groupId = groupId;
        groupController = new GroupController(context);
    }

    @NonNull
    @Override
    public UserAdapterWithThrowAway.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_with_throw_away, parent, false);
        return new UserAdapterWithThrowAway.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterWithThrowAway.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = mUsers.get(position);

        if (user.getId().equals(firebaseUser.getUid())) {
            holder.txtUsername.setText(user.getLogin() + "(Ja)");
            holder.ivThrowUserAway.setVisibility(View.GONE);
        } else {
            holder.txtUsername.setText(user.getLogin());
        }

        holder.ivThrowUserAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setTitle("Are you sure to remove " + user.getLogin() + " from group?");
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        groupController.facadeRemoveUserFromGroup(groupId, user.getId());
                        Toast.makeText(context, "Member successfully removed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtUsername;
        private ImageView ivThrowUserAway;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUsername = itemView.findViewById(R.id.txt_user_name);
            ivThrowUserAway = itemView.findViewById(R.id.iv_throw_user_away);
        }
    }
}
