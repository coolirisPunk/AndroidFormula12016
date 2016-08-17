package com.punkmkt.formula12016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.adapters.CustomizedSpinnerAdapter;
import com.punkmkt.formula12016.adapters.CustomizedSpinnerArrayListAdapter;
import com.punkmkt.formula12016.adapters.PilotosAdapter;
import com.punkmkt.formula12016.adapters.PremiosAdapter;
import com.punkmkt.formula12016.adapters.ServicioSpinnerAdapter;
import com.punkmkt.formula12016.models.Piloto;
import com.punkmkt.formula12016.models.Premio;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PilotosActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private final String AHR_URL_PILOTOS = "http://104.236.3.158:82/api/premio/pilotos/";
    private ArrayList<Piloto> pilotos = new ArrayList<>();
    private ArrayList<String> data_array_pilotos = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    Spinner spinner_pilotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilotos);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new PilotosAdapter(pilotos,getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_PILOTOS, "UTF-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array_object = object.getJSONArray("results");
                    for (int count = 0; count < array_object.length(); count++) {
                        JSONObject anEntry = array_object.getJSONObject(count);
                        Piloto piloto = new Piloto();
                        piloto.setId(Integer.parseInt(anEntry.optString("id")));
                        piloto.setName(anEntry.optString("name"));
                        piloto.setPicture(anEntry.optString("picture"));
                        pilotos.add(piloto);
                        data_array_pilotos.add(piloto.getName());
                    }
                    adapter.notifyDataSetChanged();

                    data_array_pilotos.add(0,getResources().getString(R.string.buscar_piloto));
                    final Spinner spinner_pilotos = (Spinner) findViewById(R.id.buscar_piloto);
                    final ArrayAdapter<String> adapter_pilotos = new CustomizedSpinnerArrayListAdapter(
                            PilotosActivity.this, R.layout.spinner_layout_pilotos, data_array_pilotos);
                    spinner_pilotos.setAdapter(adapter_pilotos);
                    spinner_pilotos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if(ValidateSpinner(spinner_pilotos,getResources().getString(R.string.buscar_piloto))){
                                Piloto piloto = pilotos.get(position-1);
                                Intent Idetail = new Intent (getApplicationContext(), SinglePilotoActivity.class);
                                Idetail.putExtra("id", Integer.toString(piloto.getId()));
                                startActivity(Idetail);
                            }

                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) { }
                    });
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


//        boolean validationError = false;
//        if(!ValidateSpinner(spinner_pilotos,getResources().getString(R.string.buscar_piloto))){
//            validationError = true;
//        }
//        if (!validationError) {
//
//        }
    }


    public final static boolean ValidateSpinner(Spinner s,String s_option){
        String st =s.getSelectedItem().toString();
        int pos =s.getSelectedItemPosition();
        Log.d("test",st);
        Log.d("test",String.valueOf(pos));
        if(pos==0)
        {
            return false;
        }
        if(!st.equals(s_option))
        {
            return true;
        }
        else{
            return false;
        }
    }
}
