package com.example.myapplication;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    system.out.println("data");
    //Global variable
    GridView rv_recycler;
    private SwipeRefreshLayout mswipe;
    TextView et_notfound;
    RelativeLayout rl_response;
    SearchView sv_search;
    PhotoAdapter aAdapter;
    RequestQueue rq;
    String searchquery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startactivity();
    }

    private void startactivity() {
        //Initialise views
        mswipe = findViewById(R.id.mswipe);
        rv_recycler = findViewById(R.id.rv_recycler);
        sv_search = findViewById(R.id.sv_search);
        et_notfound = findViewById(R.id.et_notfound);
        rl_response = findViewById(R.id.rl_responce);

        rq = Volley.newRequestQueue(this);
        mswipe.setOnRefreshListener(this);

        //Search field for user input
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                CharSequence query = sv_search.getQuery();
                searchquery = query.toString();
                getvollyData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    //using volly search api
    private void getvollyData(){
        String URL = "https://api.imgur.com/3/gallery/search/1?q="+searchquery;
        Log.d("vanilla", URL);

        //GET request to Imgur API to search for pictures
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null, response -> {
            Log.d("vanilla1", response.toString());
            if(response.length() < -1 ){
                        Log.d("vanilll", "karry");
                    }else {
                        MyData objects = new Gson().fromJson(response.toString(), MyData.class);
                        aAdapter = new PhotoAdapter(MainActivity.this, objects);
                        rv_recycler.setAdapter(aAdapter);
                   }
        },
                error -> Log.d("vanilla1","error" )){
            // set Authorization key and value
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Client-ID 137cda6b5008a7c");
                return params;
            }
        };
        //RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsObjRequest);
    }

    @Override
    public void onRefresh() {
        mswipe.setRefreshing(false);
        getvollyData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
