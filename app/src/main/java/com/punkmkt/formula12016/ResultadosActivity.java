package com.punkmkt.formula12016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.adapters.PremiosAdapter;
import com.punkmkt.formula12016.models.Noticia;
import com.punkmkt.formula12016.models.Premio;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultadosActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private final String AHR_URL_PREMIOS = "http://104.236.3.158:82/api/premio/premios/";
    private ArrayList<Premio> premios = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new PremiosAdapter(premios,getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        TextView trg = (TextView) findViewById(R.id.ranking_general);
        ImageButton brg = (ImageButton) findViewById(R.id.go);


        StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_PREMIOS, "UTF-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response",response);
                    JSONObject object = new JSONObject(response);
                    JSONArray array_object = object.getJSONArray("results");
                    for (int count = 0; count < array_object.length(); count++) {
                        JSONObject anEntry = array_object.getJSONObject(count);
                        Premio premio = new Premio();
                        premio.setId(Integer.parseInt(anEntry.optString("id")));
                        premio.setName(anEntry.optString("name"));
                        premio.setPicture(anEntry.optString("picture"));
                        premios.add(premio);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyVolleySingleton.getInstance().addToRequestQueue(request);

        brg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RankingGeneralActivity.class);
                startActivity(intent);
            }
        });
        trg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RankingGeneralActivity.class);
                startActivity(intent);
            }
        });
    }
    }
