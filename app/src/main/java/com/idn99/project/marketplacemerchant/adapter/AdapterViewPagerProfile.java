package com.idn99.project.marketplacemerchant.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.idn99.project.marketplacemerchant.fragment.ProfileMerchant;
import com.idn99.project.marketplacemerchant.fragment.ProfileUser;

import java.util.ArrayList;

public class AdapterViewPagerProfile extends FragmentStatePagerAdapter {

    private ArrayList<String> judul = new ArrayList<>();

    public AdapterViewPagerProfile(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return new ProfileUser();
            case 1 : return new ProfileMerchant();
        }
        return null;
    }

    @Override
    public int getCount() {
        return judul.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return judul.get(position);

    }

    public static AdapterViewPagerProfile newInstance(FragmentManager fm){
        AdapterViewPagerProfile adapterViewPagerProfile = new AdapterViewPagerProfile(fm);
        return adapterViewPagerProfile;
    }

    public void addData(String jd){
        judul.add(jd);
    }
}
