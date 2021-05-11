package com.p.homeapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.groupView.GroupMenuActivity;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;
    private List<Group> mGroups;

    private FirebaseUser firebaseUser;

    private Activity activity;

    public GroupAdapter(Context context, List<Group> mGroups, Activity activity) {
        this.context = context;
        this.mGroups = mGroups;
        this.activity = activity;
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

        holder.groupName.setText(group.getName());
        holder.groupDescription.setText(group.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupMenuActivity.class);
                intent.putExtra("groupId", group.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(intent);
                activity.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView groupName, groupDescription;
        CardView reviewLayoutGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.txt_group_name_in_row);
            groupDescription = itemView.findViewById(R.id.txt_group_description_in_row);
            reviewLayoutGroup = itemView.findViewById(R.id.group_review_layout);
        }
    }
}
