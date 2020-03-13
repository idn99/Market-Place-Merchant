package com.idn99.project.marketplacemerchant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.activity.DeskripsiProduct;
import com.idn99.project.marketplacemerchant.model.Product;

import org.json.JSONObject;

import java.util.ArrayList;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.MyViewHolder> {
    private Context ctx;
    private ArrayList<Product> products = new ArrayList<>();
    private View view;

    public ListProductAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_product_design, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvProduct.setText(products.get(position).getProductName());

        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl+products.get(position).getProductImage();
        Glide.with(ctx).load(url).into(holder.imageProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx, DeskripsiProduct.class);
                String json = new Gson().toJson(products.get(position));
                intent.putExtra("data", json);
                ctx.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvProduct, tvMerchant;
        private ImageView imageProduct;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProduct = itemView.findViewById(R.id.list_nama_product);
            tvMerchant = itemView.findViewById(R.id.list_merchant);
            imageProduct = itemView.findViewById(R.id.list_image_product);
        }
    }

    public void addData(ArrayList<Product> products){
        this.products = products;
    }
}
