package com.p.homeapp.adapters;

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
import com.p.homeapp.entities.Group;

import java.util.ArrayList;

public class ExpenditureAdapter extends RecyclerView.Adapter<ExpenditureAdapter.ExpendituresViewHolder> {
    private ArrayList<Group> mGroups;
    private Context context;

    private FirebaseUser firebaseUser;

    public ExpenditureAdapter(ArrayList<Group> mGroups, Context context) {
        this.mGroups = mGroups;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpendituresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_fragment_money_balance_row, parent, false);
        return new ExpendituresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpendituresViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Group group = mGroups.get(position);
        holder.expenditureGroupName.setText(group.getName());
        //holder.expenditureAmount.setText(String.valueOf(mExpenditures.get(position).getExpenditureAmount()));
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public class ExpendituresViewHolder extends RecyclerView.ViewHolder {

        private TextView expenditureAmount, expenditureGroupName;

        public ExpendituresViewHolder(@NonNull View itemView) {
            super(itemView);
            expenditureGroupName = itemView.findViewById(R.id.txt_expenditure_group_name);
            expenditureAmount = itemView.findViewById(R.id.txt_expenditure_amount);
        }
    }
}
