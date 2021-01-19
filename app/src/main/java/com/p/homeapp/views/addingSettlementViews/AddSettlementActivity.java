package com.p.homeapp.views.addingSettlementViews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.p.homeapp.R;

public class AddSettlementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_settlement);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_task_owners_container,new FragmentAddSettlement()).commit();
    }
}