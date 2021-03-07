package com.p.homeapp.views.moneyBalance;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.p.homeapp.R;
import com.p.homeapp.views.moneyBalance.pagerAdapter.PagerAdapter;

public class MoneyBalanceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_balance);
        TabLayout tabLayout = findViewById(R.id.tl_money_balance);
        TabItem tabExpenditures = findViewById(R.id.ti_expenditures);
        TabItem tabSettlement = findViewById(R.id.ti_settlement);
        ViewPager viewPager = findViewById(R.id.vp_money_balance);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
       viewPager.setAdapter(pagerAdapter);

       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });

    }
}