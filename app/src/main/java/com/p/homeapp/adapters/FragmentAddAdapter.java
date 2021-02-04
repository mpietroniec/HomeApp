package com.p.homeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p.homeapp.R;
import com.p.homeapp.entities.User;

import java.util.ArrayList;

public class FragmentAddAdapter extends RecyclerView.Adapter<FragmentAddAdapter.MembersViewHolder> {
    private ArrayList<User> mUsers;

    public FragmentAddAdapter(ArrayList<User> users) {
        this.mUsers = users;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_add_member_row, parent, false);
        return new MembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        holder.userName.setText(mUsers.get(position).getLogin());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class MembersViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;

        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_txt);
        }
    }
}
