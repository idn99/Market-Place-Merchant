package com.idn99.project.marketplacemerchant.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.model.Product;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DeskripsiProduct extends AppCompatActivity {

    private ImageView imgP,imgM;
    private TextView priceP, qtyP, nameP, nameC, nameM, desP;
    private Button btnEdit, btnDelete;
    private Product product = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_product);

        Bundle bundle = getIntent().getExtras();
        String json = bundle.getString("data");

        product = new Gson().fromJson(json,Product.class);

        inisialisasi();
        String baseUrl = "http://210.210.154.65:4444/storage/";
        String url = baseUrl + product.getProductImage();

        Glide.with(this).load(url).into(imgP);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        priceP.setText(formatRupiah.format((double)product.getProductPrice()));

        qtyP.setText("Stok : "+String.valueOf(product.getProductQty()));
        nameC.setText(product.getCategory().getCategoryName());
        nameP.setText(product.getProductName());
        nameM.setText(product.getMerchant().getMerchantName());
        desP.setText(product.getProductDesc());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DeskripsiProduct.this);
                alert.setTitle("Delete Item");
                alert.setMessage("Apakah Produk Akan dihapus ? ");
                alert.setNegativeButton("Tidak", null);
                alert.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct();

                        Intent intent = new Intent(DeskripsiProduct.this, ListProduct.class);
                        startActivity(intent);
                    }
                });
                alert.create().show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeskripsiProduct.this, EditProductActivity.class);
                intent.putExtra("slug",product.getProductSlug());
                startActivity(intent);
            }
        });

    }

    public void inisialisasi() {
        imgP = findViewById(R.id.image_product_des);
        imgM = findViewById(R.id.img_merchant);
        priceP = findViewById(R.id.price_product_tv);
        nameP = findViewById(R.id.name_product_tv);
        qtyP = findViewById(R.id.qty_product_tv);
        nameM = findViewById(R.id.name_merchant_tv);
        nameC = findViewById(R.id.name_category_tv);
        desP = findViewById(R.id.tv_product_des);

        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);

    }

    public void deleteProduct(){

        String url = "http://210.210.154.65:4444/api/product/"+product.getProductId()+"/delete";

        RequestQueue con = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(DeskripsiProduct.this, "Data Telah Terhapus", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeskripsiProduct.this, "Data Belum Terhapus , Cek Koneksi Internet", Toast.LENGTH_SHORT).show();
                    }
                });
        con.add(req);
    }
}
