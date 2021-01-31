package com.p.homeapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.views.groupView.GroupMenuActivity;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private ArrayList<Group> mGroup;
    private OnItemClickListener mItemClickListener;

    public GroupAdapter(ArrayList<Group> mGroup, OnItemClickListener mItemClickListener) {
        this.mGroup = mGroup;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_group_row, parent, false);
        return new GroupViewHolder(view, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        holder.groupName.setText(mGroup.get(position).getName());
        holder.groupDescription.setText(mGroup.get(position).getDescription());
        holder.groupReviewLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), GroupMenuActivity.class);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mGroup.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener {
        private TextView groupName, groupDescription;
        CardView groupReviewLayout;
        OnItemClickListener itemClickListener;

        public GroupViewHolder(@NonNull View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name_txt);
            groupDescription = itemView.findViewById(R.id.group_description_txt);
            groupReviewLayout = itemView.findViewById(R.id.group_review_layout);
           this.itemClickListener=itemClickListener;
        }

        @Override
        public void onItemClick(int position) {
            itemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}
