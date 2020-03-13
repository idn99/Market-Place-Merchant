package com.idn99.project.marketplacemerchant.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.activity.HomeActivity;
import com.idn99.project.marketplacemerchant.activity.LoginActivity;
import com.idn99.project.marketplacemerchant.activity.Setting;
import com.idn99.project.marketplacemerchant.adapter.AdapterViewPagerProfile;
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment{

    private ViewPager viewPager;
    private TabLayout tabMenu;
    private Button btnLogout;
    private Toolbar toolbar;


    public Profile() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = view.findViewById(R.id.toolbar_profile);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        AdapterViewPagerProfile adapterViewPagerProfile;

        viewPager = view.findViewById(R.id.pager_fragment);
        tabMenu = view.findViewById(R.id.tab_layout_menu);
//        btnLogout = view.findViewById(R.id.btn_logout);


        adapterViewPagerProfile = AdapterViewPagerProfile.newInstance(getFragmentManager());
        adapterViewPagerProfile.addData("Profile User");
        adapterViewPagerProfile.addData("Profile Merchant");
        viewPager.setAdapter(adapterViewPagerProfile);
        tabMenu.setupWithViewPager(viewPager);
        tabMenu.setTabIndicatorFullWidth(true);

//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                TokenManager.getInstance(getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)).deleteToken();
//                startActivity(intent);
//                HomeActivity.dashboard.finish();
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout :
                Intent intent = new Intent(getContext(), Setting.class);
                startActivity(intent);
                return true;
        }
        return false;
    }


}
