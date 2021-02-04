package com.p.homeapp.views.mainView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.p.homeapp.ItemClickListener;
import com.p.homeapp.R;
import com.p.homeapp.adapters.ExpenditureAdapter;
import com.p.homeapp.entities.Expenditure;
import com.p.homeapp.views.addingSettlementViews.AddSettlementActivity;

import java.util.ArrayList;

public class FragmentMoneyBalance extends Fragment implements ItemClickListener {
    // UI components
    private RecyclerView mRecyclerView;
    private FloatingActionButton addExpenditureButton;

    //vars
    private ArrayList<Expenditure> mExpenditures = new ArrayList<>();

    private static final String TAG = "FragmentMoneyBalance";
    private ExpenditureAdapter mExpenditureAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_money_balance, container, false);
        mRecyclerView = view.findViewById(R.id.money_balance_recyclerView);
        addExpenditureButton = view.findViewById(R.id.id_add_button);
        addExpenditureButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddSettlementActivity.class);
            startActivity(intent);
        });
        intiRecyclerView();
        insertFakeExpenditures();
        return view;
    }

    private void intiRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mExpenditureAdapter = new ExpenditureAdapter(mExpenditures);
        mRecyclerView.setAdapter(mExpenditureAdapter);
    }

    private void insertFakeExpenditures(){
        for (int i = 1; i<50; i++){
            Expenditure expenditure = new Expenditure();
            expenditure.setExpenditureName("Expenditure name: " + i);
            expenditure.setExpenditureDate("2021-01-30");
            expenditure.setExpenditureAmount(6.66f);
            mExpenditures.add(expenditure);
        }
        mExpenditureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onNoteClick: clicked." + position);
    }
}
