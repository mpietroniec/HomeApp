package com.p.homeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.groupView.GroupMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;
    private List<Group> mGroups;

    private FirebaseUser firebaseUser;

    public GroupAdapter(Context context, List<Group> mGroups){
        this.context = context;
        this.mGroups = mGroups;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_group_row, parent, false);
        return new GroupAdapter.GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Group group = mGroups.get(position);
        holder.txtGroupName.setText(group.getName());
        holder.txtGroupDescription.setText(group.getDescription());

    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder{

        private TextView txtGroupName, txtGroupDescription;
        CardView reviewLayoutGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            txtGroupName = itemView.findViewById(R.id.group_name_txt);
            txtGroupDescription = itemView.findViewById(R.id.group_description_txt);
            reviewLayoutGroup = itemView.findViewById(R.id.group_review_layout);
        }
    }
}
