package com.p.homeapp.views.mainView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.p.homeapp.MainActivity;
import com.p.homeapp.R;
import com.p.homeapp.views.groupView.GroupActivity;

public class FragmentActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.id_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container, new FragmentTasks()).commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.id_nav_task:
                        selectedFragment = new FragmentTasks();
                        break;
                    case R.id.id_nav_money_balance:
                        selectedFragment = new FragmentMoneyBalance();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.id_fragment_container,
                        selectedFragment).commit();
                return true;
            };


    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(FragmentActivity.this, R.string.logged_out, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(FragmentActivity.this, MainActivity.class));
        finish();
    }

    public void startGroupActivity(MenuItem item) {
        Intent intent = new Intent(FragmentActivity.this, GroupActivity.class);
        startActivityForResult(intent,1);
        finish();
    }
}

