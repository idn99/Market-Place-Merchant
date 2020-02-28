package com.idn99.project.marketplacemerchant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.model.Categories;

import java.util.ArrayList;

public class CategoriesAdapter extends BaseAdapter {

    private ArrayList<Categories> categories = new ArrayList<>();

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Categories getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getCategoryId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null ){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.categories_spinner, parent, false);
        }

        Categories categories = getItem(position);
        TextView catText = convertView.findViewById(R.id.cat_text);
        catText.setText(categories.getCategoryName());

        return convertView;
    }

    public void addData(ArrayList<Categories> categories){

        this.categories = categories;

    }
}
