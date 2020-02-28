package com.idn99.project.marketplacemerchant.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.adapter.CategoriesAdapter;
import com.idn99.project.marketplacemerchant.model.Categories;
import com.idn99.project.marketplacemerchant.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity{

    private EditText nameP, priceP, desP, qtyP, idM, idC;
    private Button imgAdd, addP;
    private ImageView imgP;

    private RequestQueue requestQueue;

    private String slug;
    private Product product;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc,productQty, productPrice, categoryId, merchantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Bundle bundle = getIntent().getExtras();
        slug = bundle.getString("slug");

        inisial();

        getDataSebelumnya();

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        addP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productName = validasiEdt(nameP);
                productDesc = validasiEdt(desP);
                productPrice = validasiEdt(priceP);
                productQty = validasiEdt(qtyP);
                categoryId = validasiEdt(idC);
                merchantId = validasiEdt(idM);

                merchantId = "2";

                if (productImage == null){
                    productImage = null;
                }

                VolleyLoad();

                Intent intent = new Intent(EditProductActivity.this, ListProduct.class);
                startActivity(intent);
            }
        });


    }

    public String validasiEdt(EditText edt){
        String text = null;
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Isi dulu cuk , matalu siwer apa");
        }else {
            text = edt.getText().toString();
        }
        return text;
    }


    public void inisial(){
        nameP = findViewById(R.id.name_product);
        priceP = findViewById(R.id.price_product);
        desP = findViewById(R.id.des_product);
        qtyP = findViewById(R.id.qty_product);

        idM = findViewById(R.id.id_merchant);
        idC = findViewById(R.id.id_category);

        imgAdd = findViewById(R.id.add_image);
        addP = findViewById(R.id.add_product);

        imgP = findViewById(R.id.img_product);
    }


    public void VolleyLoad(){

        String url = "http://210.210.154.65:4444/api/product/"+product.getProductId()+"/update";
        requestQueue = Volley.newRequestQueue(this);

//        http://{baseurl}/api/product/{id}/update
//        Method : PUT

        StringRequest addProductReq = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response :", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i("response :", response);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                        Toast.makeText(EditProductActivity.this, "data telah terupdate", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("productName", productName);
                params.put("productDesc", productDesc);
                params.put("productQty", productQty);

                if (productImage != null){
                    params.put("productImage", productImage);
                }

                params.put("productPrice", productPrice);
                params.put("categoryId", categoryId);
                params.put("merchantId", merchantId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();
                params.put("Content-type","application/x-www-form-urlencoded");
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        addProductReq.setRetryPolicy(retryPolicy);
        requestQueue.add(addProductReq);
    }

    private void getDataSebelumnya(){

        String url = "http://210.210.154.65:4444/api/product/"+slug;

        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = response.getJSONObject("data");
                            product = gson.fromJson(jsonObject.toString(), Product.class);

                            nameP.setText(product.getProductName());
                            desP.setText(product.getProductDesc());
                            qtyP.setText(String.valueOf(product.getProductQty()));
                            Glide.with(getApplicationContext()).load(product.getProductImage()).into(imgP);
                            priceP.setText(String.valueOf(product.getProductPrice()));
                            idM.setText(String.valueOf(product.getMerchant().getMerchantId()));
                            idC.setText(String.valueOf(product.getCategory().getCategoryId()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProductActivity.this, "Data tidak terbaca", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req);
    }

    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //encoding image to string
                productImage = getStringImage(bitmap); // call getStringImage() method below this code
                Log.d("image",productImage);

                Glide.with(getApplicationContext())
                        .load(bitmap)
                        .override(imgP.getWidth())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imgP);
                System.out.println("image : "+productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // convert image bitmap to string base64
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

}
