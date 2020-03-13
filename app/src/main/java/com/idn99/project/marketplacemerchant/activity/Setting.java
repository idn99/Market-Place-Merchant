package com.idn99.project.marketplacemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

public class Setting extends AppCompatActivity {

    private TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        inisial();

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, LoginActivity.class);
                TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).deleteToken();
                startActivity(intent);
                HomeActivity.dashboard.finish();
            }
        });
    }

    public void inisial(){
        tvLogout = findViewById(R.id.logout_tv);
    }
}
