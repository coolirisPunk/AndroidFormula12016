package com.punkmkt.formula12016.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.punkmkt.formula12016.MainActivity;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.adapters.TiendaRestauranteAdapter;
import com.punkmkt.formula12016.models.TiendaRestaurante;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by sebastianmendezgiron on 03/08/16.
 */

public class TiendaFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private final String AHZ_URL_SELLERS = "http://104.236.3.158:82/api/store/category_products/1/sellers/";
    private ArrayList<TiendaRestaurante> tiendaRestaurantes = new ArrayList<TiendaRestaurante>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tienda_fragment,container,false);

        final Tracker tracker = ((MyVolleySingleton) getActivity().getApplication()).getTracker(MyVolleySingleton.TrackerName.APP_TRACKER);
        tracker.setScreenName(getString(R.string.tienda_titulo));
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        adapter = new TiendaRestauranteAdapter(tiendaRestaurantes, getActivity());

        StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHZ_URL_SELLERS, "UTF-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    tiendaRestaurantes.clear();
                    JSONArray object = new JSONArray(response);
                    //JSONArray array_object = object.getJSONArray("sellers");
                    for (int count = 0; count < object.length(); count++) {
                        JSONObject anEntry = object.getJSONObject(count);
                        TiendaRestaurante tiendaRestaurante = new TiendaRestaurante();
                        tiendaRestaurante.setId(Integer.parseInt(anEntry.optString("id")));
                        tiendaRestaurante.setNombre(anEntry.optString("name"));
                        tiendaRestaurante.setImagen(anEntry.optString("picture"));
                        tiendaRestaurantes.add(tiendaRestaurante);
                    }
                    Log.d("restaurantes",tiendaRestaurantes.toString());
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

        MyVolleySingleton.getInstance().addToRequestQueue(request);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(myIntent);
                    //Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                    return true;
                }else {
                    return false;
                }
            }
        });
    }


}
