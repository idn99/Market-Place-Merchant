package com.idn99.project.marketplacemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.adapter.ListProductAdapter;
import com.idn99.project.marketplacemerchant.model.List;
import com.idn99.project.marketplacemerchant.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListProductAdapter adapterRv = new ListProductAdapter(this);
    private FloatingActionButton fab;
    private String url = "http://210.210.154.65:4444/api/products";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);

        recyclerView = findViewById(R.id.list_item_rv);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProduct.this, AddProduct.class);
                startActivity(intent);
            }
        });

        VolleyLoad();

    }


    public void VolleyLoad(){
        RequestQueue con = Volley.newRequestQueue(this);
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ArrayList<Product> data = new ArrayList<>();
                            Gson gson = new Gson();

                            List listProducts = gson.fromJson(response.toString(), List.class);
                            data.addAll(listProducts.getProduct());

                            recyclerView.setLayoutManager(new GridLayoutManager(ListProduct.this, 2));
                            recyclerView.setAdapter(adapterRv);
                            adapterRv.addData(data);
                            adapterRv.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListProduct.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        con.add(req);
        //VolleyLoad.getInstance().addToRequestQueue(req, "getRequestList");
    }

}
