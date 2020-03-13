package com.idn99.project.marketplacemerchant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.idn99.project.marketplacemerchant.Network.VolleyService;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.fragment.Dashboard;
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    public static int WAKTU = 3000;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler hn = new Handler();
        hn.postDelayed(new Runnable() {
            @Override
            public void run() {
                validToken();
            }
        }, WAKTU);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void validToken(){
        String url = "http://210.210.154.65:4444/api/auth/getuser";
        accessToken = TokenManager.getInstance(getSharedPreferences("pref", MODE_PRIVATE)).getToken();

        if (accessToken.getAccessToken() == null){

            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else if (accessToken.getAccessToken() != null){
            JsonObjectRequest profileReq = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int responseCode = error.networkResponse.statusCode;
                            if (responseCode == 401){

                                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SplashScreen.this, "Sesi Telah Berakhir, Silahkan Login Kembali", Toast.LENGTH_SHORT).show();

                            }else if (responseCode == 403){

                                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(SplashScreen.this, "Silahkan Login Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new Hashtable<>();

                    headers.put("Accept", "application/json");
                    headers.put("Authorization", accessToken.getTokenType()+" "+accessToken.getAccessToken());

                    return headers;
                }
            };

            VolleyService.getInstance(getApplicationContext()).addToRequestQueue(profileReq);
        }

    }
}
