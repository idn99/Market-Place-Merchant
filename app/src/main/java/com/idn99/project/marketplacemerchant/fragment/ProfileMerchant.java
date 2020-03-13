package com.idn99.project.marketplacemerchant.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.idn99.project.marketplacemerchant.Network.VolleyService;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileMerchant extends Fragment {

    AccessToken accessToken;
    private TextView namaMercant;

    public ProfileMerchant() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_merchant, container, false);

        namaMercant = rootView.findViewById(R.id.tv_edit_nama_merchant);

        String url = "http://210.210.154.65:4444/api/auth/getuser";
        accessToken = TokenManager.getInstance(this.getActivity().getSharedPreferences("pref", MODE_PRIVATE)).getToken();

        JsonObjectRequest merchantReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            JSONObject merchant = user.getJSONObject("merchant");

                            namaMercant.setText(merchant.getString("merchantName"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Gagal Load Data", Toast.LENGTH_SHORT).show();
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

        VolleyService.getInstance(getContext()).addToRequestQueue(merchantReq);

        return rootView;
    }
}
