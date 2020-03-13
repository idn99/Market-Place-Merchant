package com.idn99.project.marketplacemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.idn99.project.marketplacemerchant.Network.VolleyService;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.model.RegisterErrorResponse;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

public class RegistActivity extends AppCompatActivity {

    private EditText edtEmail, edtNamaMerchant, edtNamaDepan, edtNamaBelakang, edtPassword, edtConfirmPassword;
    private Button btnCreate;

    private AccessToken accessToken;

    String email, namaMerchant, namaDepan, namaBelakang, password, confirmPassword, isMerchant="1";

    private boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        inisial();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = validasi(edtEmail);
                namaMerchant = validasi(edtNamaMerchant);
                namaDepan = validasi(edtNamaDepan);
                namaBelakang = validasi(edtNamaBelakang);

                if (edtPassword.getText().toString().length() < 8){
                    edtPassword.setError("Sandi minimal 8 digit");
                    error = true;
                }else {
                    password = validasi(edtPassword);
                    error = false;
                }

                if (edtConfirmPassword.getText().toString().equals(password)){
                    confirmPassword = validasi(edtConfirmPassword);
                    error = false;
                }else {
                    edtConfirmPassword.setError("Konfirmasi Sandi Tidak Sesuai");
                    error = true;
                }

//                if (!error){
//                    Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }


                register();

            }
        });
    }

    public void inisial(){
        edtEmail = findViewById(R.id.edt_email);
        edtNamaMerchant = findViewById(R.id.edt_nama_merchant);
        edtNamaDepan = findViewById(R.id.edt_nama_depan);
        edtNamaBelakang = findViewById(R.id.edt_nama_belakang);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_password_confirm);

        btnCreate = findViewById(R.id.create_account_btn);
    }

    private String validasi(EditText edt){
        String text = null;
        if (edt.getText().toString().isEmpty()){
            edt.setError("Form ini tidak boleh kosong");
            error = true;
        }else {
            text = edt.getText().toString();
            error = false;
        }
        return text;
    }

    public void register(){
        String url = "http://210.210.154.65:4444/api/auth/signup";

        StringRequest registerReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accessToken = new Gson().fromJson(response,AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).saveToken(accessToken);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        String body = "";
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);
                            RegisterErrorResponse errorResponse = new Gson().fromJson(res.getJSONObject("error").toString(),RegisterErrorResponse.class);
                            if(errorResponse.getEmailError().size() > 0){
                                if(errorResponse.getEmailError().get(0) != null){
                                    edtEmail.setError(errorResponse.getEmailError().get(0));
                                }
                            }
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<>();

                params.put("email", email);
                params.put("first_name", namaDepan);
                params.put("last_name", namaBelakang);
                params.put("password", password);
                params.put("confirm_password", confirmPassword);
                params.put("is_merchant", isMerchant);
                params.put("merchant_name", namaMerchant);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();

                headers.put("Content_type","application/x-www-form-urlencoded");
                headers.put("Accept","application/json");

                return headers;
            }
        };
        VolleyService.getInstance(this).addToRequestQueue(registerReq);
    }
}
