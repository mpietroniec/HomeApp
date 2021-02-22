package com.p.homeapp.views.addingSettlementViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.p.homeapp.R;

public class AddSettlementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_settlement);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_add_task_owners,new FragmentAddSettlement()).commit();
    }
}