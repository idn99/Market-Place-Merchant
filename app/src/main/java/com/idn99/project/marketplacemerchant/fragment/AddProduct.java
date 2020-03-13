package com.idn99.project.marketplacemerchant.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.idn99.project.marketplacemerchant.Network.VolleyService;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.activity.HomeActivity;
import com.idn99.project.marketplacemerchant.adapter.CategoriesAdapter;
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.model.Categories;
import com.idn99.project.marketplacemerchant.model.ProductErrorResponse;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProduct extends Fragment implements AdapterView.OnItemSelectedListener{

    private EditText nameP, priceP, desP, qtyP;
    private Button imgAdd, addP;
    private Spinner catP;
    private ImageView imgP;

    private RequestQueue requestQueue;
    private ArrayList<Categories> categories;
    private CategoriesAdapter adapter;

    //set default request code for intent result
    private int PICK_IMAGE_REQUEST = 1;
    private String productImage = null; // image string yang akan dikirim  ke server (bukan dalam bentuk gambar tapi dalam bentuk string base64.
    private String productName, productDesc,productQty, productPrice, categoryId;

    AccessToken accessToken;

    public AddProduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_product, container, false);

        nameP = rootView.findViewById(R.id.name_product);
        priceP = rootView.findViewById(R.id.price_product);
        desP = rootView.findViewById(R.id.des_product);
        qtyP = rootView.findViewById(R.id.qty_product);

        imgAdd = rootView.findViewById(R.id.add_image);
        addP = rootView.findViewById(R.id.add_product);

        catP = rootView.findViewById(R.id.cat_product);

        imgP = rootView.findViewById(R.id.img_product);

        categories = new ArrayList<>();
        // adapter
        adapter = new CategoriesAdapter();
        catP.setAdapter(adapter);
        catP.setOnItemSelectedListener(this);

        getAllCategories();

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

                if (productImage == null){
                    productImage = null;
                }

                VolleyLoad();

            }
        });

        return rootView;
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
                            Toast.makeText(getContext(),String.valueOf(adapter.getCount()),Toast.LENGTH_LONG).show();

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
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(listCatReq);
    }

    public void VolleyLoad(){
        accessToken = TokenManager.getInstance(this.getActivity().getSharedPreferences("pref", MODE_PRIVATE)).getToken();

        String url = "http://210.210.154.65:4444/api/merchant/products";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("response", response);
                System.out.println(response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if(code == 200){
                        Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        nameP.setText("");
                        priceP.setText("");
                        desP.setText("");
                        qtyP.setText("");
                        imgP.setImageResource(0);
                    }
                    else{
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        ProductErrorResponse errorResponse = new Gson().fromJson(jsonObject.getString("message"),ProductErrorResponse.class);
                        if(errorResponse.getProductNameError().size() != 0) {
                            if (errorResponse.getProductNameError().get(0) != null) {
                                nameP.setError(errorResponse.getProductNameError().get(0));
                                Toast.makeText(getContext(), errorResponse.getProductNameError().get(0), Toast.LENGTH_SHORT).show();
                            }
                        }

                        if(errorResponse.getProductQtyError().size() != 0) {
                            if (errorResponse.getProductQtyError().get(0) != null) {
                                qtyP.setError(errorResponse.getProductQtyError().get(0));
                                Toast.makeText(getContext(), errorResponse.getProductQtyError().get(0), Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(errorResponse.getProductPriceError().size() != 0) {
                            if (errorResponse.getProductPriceError().get(0) != null) {
                                priceP.setError(errorResponse.getProductPriceError().get(0));
                                Toast.makeText(getContext(), errorResponse.getProductPriceError().get(0), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    Log.i("response", response);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if(error.networkResponse.statusCode == 400){
                    Toast.makeText(getContext(), String.valueOf(error.networkResponse), Toast.LENGTH_LONG).show();
                }

                try {
                    String body = new String(error.networkResponse.data, "UTF-8");
                    Toast.makeText(getActivity(), body, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                params.put("productName", productName);
                params.put("productPrice", productPrice);
                params.put("productDesc", productDesc);
                params.put("productQty", productQty);
                if(productImage != null) {
                    params.put("productImage", productImage);
                }
                params.put("categoryId", categoryId);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();

                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", accessToken.getTokenType()+" "+accessToken.getAccessToken());

                return headers;
            }

        };
        int socketTimeout = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        VolleyService.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //encoding image to string
                productImage = getStringImage(bitmap); // call getStringImage() method below this code
                Log.d("image",productImage);

                Glide.with(getContext())
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
}