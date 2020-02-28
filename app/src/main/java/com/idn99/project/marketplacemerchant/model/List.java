package com.idn99.project.marketplacemerchant.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class List {

    @SerializedName("data")
    private ArrayList<Product> product;

    public List(ArrayList<Product> product) {
        this.product = product;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }
}
