package com.idn99.project.marketplacemerchant.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.fragment.AddProduct;
import com.idn99.project.marketplacemerchant.fragment.Dashboard;
import com.idn99.project.marketplacemerchant.fragment.Profile;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView botNav;
    public static Activity dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dashboard = this;

        Fragment fragment = new Dashboard();
        loadFragment(fragment);
        botNav = findViewById(R.id.bn_main);
        botNav.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.dashboard_menu:
                fragment = new Dashboard();
                break;
            case R.id.profile_menu:
                fragment = new Profile();
                break;
            case R.id.add_product_menu:
                fragment = new AddProduct();
                break;
        }
        return loadFragment(fragment);
    }
}
