package com.p.homeapp.adapters.userAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.p.homeapp.R;
import com.p.homeapp.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserWithCheckBoxAdapter extends RecyclerView.Adapter<UserWithCheckBoxAdapter.UserWithCheckBoxViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;

    public UserWithCheckBoxAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }


    @NonNull
    @NotNull
    @Override
    public UserWithCheckBoxAdapter.UserWithCheckBoxViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item_with_check_box, parent, false);
        return new UserWithCheckBoxAdapter.UserWithCheckBoxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserWithCheckBoxAdapter.UserWithCheckBoxViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        User user = mUsers.get(position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class UserWithCheckBoxViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName;


        public UserWithCheckBoxViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtUserName = itemView.findViewById(R.id.txt_user_name);
        }
    }
}


