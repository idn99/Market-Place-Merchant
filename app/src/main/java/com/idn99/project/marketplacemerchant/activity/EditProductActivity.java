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
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.model.Categories;
import com.idn99.project.marketplacemerchant.model.Product;
import com.idn99.project.marketplacemerchant.model.ProductErrorResponse;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText nameP, priceP, desP, qtyP;
    private Button imgAdd, addP;
    private ImageView imgP;
    private Spinner catP;

    private RequestQueue requestQueue;
    private ArrayList<Categories> categories;
    private CategoriesAdapter adapter;

    private String slug;
    private Product product;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc,productQty, productPrice, categoryId, merchantId;

    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Bundle bundle = getIntent().getExtras();
        slug = bundle.getString("slug");

        inisial();

        categories = new ArrayList<>();
        // adapter
        adapter = new CategoriesAdapter();
        catP.setAdapter(adapter);
        catP.setOnItemSelectedListener(this);

        getAllCategories();

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

                productName = nameP.getText().toString();
                productDesc = desP.getText().toString();
                productPrice = priceP.getText().toString();
                productQty = qtyP.getText().toString();

                merchantId = String.valueOf(product.getMerchantId());

                if (productImage == null){
                    productImage = null;
                }

                VolleyLoad();

                Intent intent = new Intent(EditProductActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    public void inisial(){
        nameP = findViewById(R.id.name_product);
        priceP = findViewById(R.id.price_product);
        desP = findViewById(R.id.des_product);
        qtyP = findViewById(R.id.qty_product);

        imgAdd = findViewById(R.id.add_image);
        addP = findViewById(R.id.add_product);

        catP = findViewById(R.id.spinner_category);

        imgP = findViewById(R.id.img_product);
    }


    public void VolleyLoad(){

        accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();
        String url = "http://210.210.154.65:4444/api/merchant/product/"+product.getProductId()+"/update";
        requestQueue = Volley.newRequestQueue(this);

        StringRequest addProductReq = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if(code == 200){
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditProductActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                            else{
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                ProductErrorResponse errorResponse = new Gson().fromJson(jsonObject.getString("message"),ProductErrorResponse.class);
                                if(errorResponse.getProductNameError().size() != 0) {
                                    if (errorResponse.getProductNameError().get(0) != null) {
                                        nameP.setError(errorResponse.getProductNameError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductNameError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if(errorResponse.getProductQtyError().size() != 0) {
                                    if (errorResponse.getProductQtyError().get(0) != null) {
                                        qtyP.setError(errorResponse.getProductQtyError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductQtyError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if(errorResponse.getProductPriceError().size() != 0) {
                                    if (errorResponse.getProductPriceError().get(0) != null) {
                                        priceP.setError(errorResponse.getProductPriceError().get(0));
                                        Toast.makeText(getApplicationContext(), errorResponse.getProductPriceError().get(0), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            Log.i("response", response);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(error.networkResponse.statusCode == 400){
                            Toast.makeText(getApplicationContext(), String.valueOf(error.networkResponse), Toast.LENGTH_LONG).show();
                        }

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
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                params.put("Accept","application/json");
                params.put("Content-type","application/x-www-form-urlencoded");
                params.put("Authorization", accessToken.getTokenType()+" "+accessToken.getAccessToken());
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
                            String urlImg = "http://210.210.154.65:4444/storage/" + product.getProductImage();

                            nameP.setText(product.getProductName());
                            desP.setText(product.getProductDesc());
                            qtyP.setText(String.valueOf(product.getProductQty()));
                            Glide.with(EditProductActivity.this)
                                    .load(urlImg).into(imgP);
                            priceP.setText(String.valueOf(product.getProductPrice()));
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.categoryId = String.valueOf(adapter.getItemId(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getAllCategories(){
        String url = "http://210.210.154.65:4444/api/categories";

        JsonObjectRequest listCatReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // handle response
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                Gson gson = new Gson();
                                Categories category = gson.fromJson(data.getJSONObject(i).toString(),Categories.class);
                                categories.add(category);
                            }

                            adapter.addData(categories);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),String.valueOf(adapter.getCount()),Toast.LENGTH_LONG).show();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(listCatReq);
    }
}
