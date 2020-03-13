package com.idn99.project.marketplacemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.model.RegisterErrorResponse;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView tvCreateAccount;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;

    private ConnectivityManager connectivityManager;

    String email, password;
    RequestQueue requestQueue;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inisial();

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null){
                    requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    login();
                }else{
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void inisial(){
        edtEmail = findViewById(R.id.edt_username_login);
        edtPassword = findViewById(R.id.edt_password_login);
        tvCreateAccount = findViewById(R.id.create_account_tv);
        btnLogin = findViewById(R.id.btn_login);
    }

    public String validasi(EditText edt){
        String text = null;
        if (edt.getText().toString().length() <= 0){
            edt.setError("Silahkan Isi Username / Password");
        }else {
            text = edt.getText().toString();
        }
        return text;
    }

    public void login(){

        String url = "http://210.210.154.65:4444/api/auth/login";

        StringRequest loginReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        accessToken = new Gson().fromJson(response, AccessToken.class);

                        TokenManager.getInstance(getSharedPreferences("pref",MODE_PRIVATE)).saveToken(accessToken);
//                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String body = "";
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(body);

                            if (error.networkResponse.statusCode == 404){
                                RegisterErrorResponse errorResponse = new Gson().fromJson(res.getJSONObject("error").toString(),RegisterErrorResponse.class);
                                if(errorResponse.getEmailError().size() > 0){
                                    if(errorResponse.getEmailError().get(0) != null){
                                        edtEmail.setError(errorResponse.getEmailError().get(0));
                                    }
                                }

                                if(errorResponse.getPasswordError().size() > 0){
                                    if(errorResponse.getPasswordError().get(0) != null){
                                        edtPassword.setError(errorResponse.getPasswordError().get(0));
                                    }
                                }
                            }else{
                                String err = res.getString("error");
                                Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                      /*  String notValid = "";
                        try {
                            notValid = new String(error.networkResponse.data, "UTF-8");
                            JSONObject res = new JSONObject(notValid);
                            String err = res.getString("error");
                            if (!edtPassword.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()){
                                Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();
                params.put("email", email);
                params.put("password",password);

                return params;
            }
        };

        requestQueue.add(loginReq);
    }
}
