package com.p.homeapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p.homeapp.R;
import com.p.homeapp.entities.Expenditure;

import java.util.ArrayList;

public class ExpenditureAdapter extends RecyclerView.Adapter<ExpenditureAdapter.ExpendituresViewHolder> {
    private ArrayList<Expenditure> mExpenditures;

    public ExpenditureAdapter(ArrayList<Expenditure> expenditures) {
        this.mExpenditures = expenditures;
    }

    @NonNull
    @Override
    public ExpendituresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_fragment_money_balance_row, parent, false);
        return new ExpendituresViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpendituresViewHolder holder, int position) {
        holder.expenditureName.setText(mExpenditures.get(position).getExpenditureName());
        holder.expenditureDate.setText(mExpenditures.get(position).getExpenditureDate());
        holder.expenditureAmount.setText(String.valueOf(mExpenditures.get(position).getExpenditureAmount()));
    }

    @Override
    public int getItemCount() {
        return mExpenditures.size();
    }

    public class ExpendituresViewHolder extends RecyclerView.ViewHolder {
        private TextView expenditureName, expenditureDate, expenditureAmount;

        public ExpendituresViewHolder(@NonNull View itemView) {
            super(itemView);
            expenditureName = itemView.findViewById(R.id.expenditure_name_txt);
            expenditureDate = itemView.findViewById(R.id.expenditure_date_txt);
            expenditureAmount = itemView.findViewById(R.id.expenditure_amount_txt);
        }
    }
}
