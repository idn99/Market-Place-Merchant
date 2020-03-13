package com.idn99.project.marketplacemerchant.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.idn99.project.marketplacemerchant.Network.VolleyService;
import com.idn99.project.marketplacemerchant.R;
import com.idn99.project.marketplacemerchant.activity.AddProduct;
import com.idn99.project.marketplacemerchant.adapter.ListProductAdapter;
import com.idn99.project.marketplacemerchant.model.AccessToken;
import com.idn99.project.marketplacemerchant.model.List;
import com.idn99.project.marketplacemerchant.model.Product;
import com.idn99.project.marketplacemerchant.utils.TokenManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private RecyclerView recyclerView;
    private ListProductAdapter adapterRv = new ListProductAdapter(getActivity());
//    private FloatingActionButton fab;
    private ArrayList<Product> data = new ArrayList<>();
    private String url = "http://210.210.154.65:4444/api/merchant/products";
    private GridLayoutManager manager;

    AccessToken accessToken;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.list_item_rv);
//        fab = view.findViewById(R.id.fab);

        manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterRv);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AddProduct.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });

        loadData();

        return view;
    }

//    public void VolleyLoad(){
//
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            Gson gson = new Gson();
//
//                            List listProducts = gson.fromJson(response.toString(), List.class);
//                            data.addAll(listProducts.getProduct());
//
//                            adapterRv.addData(data);
//                            adapterRv.notifyDataSetChanged();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new Hashtable<>();
//                headers.put("Accept", "application/json");
//                headers.put("Authorization", accessToken.getTokenType()+" "+accessToken.getAccessToken());
//
//                return headers;
//            }
//        };
//
//        VolleyService.getInstance(getActivity()).addToRequestQueue(req);
//        //VolleyLoad.getInstance().addToRequestQueue(req, "getRequestList");
//    }

    public void loadData(){

        accessToken = TokenManager.getInstance(this.getActivity().getSharedPreferences("pref", MODE_PRIVATE)).getToken();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();

                        List list = gson.fromJson(response.toString(), List.class);
                        data.addAll(list.getProduct());

                        adapterRv.addData(data);
                        adapterRv.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Gagal Load Data", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new Hashtable<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", accessToken.getTokenType()+" "+accessToken.getAccessToken());
                return headers;
            }
        };

        VolleyService.getInstance(getActivity()).addToRequestQueue(request);
    }
}
