package autodromo.punkmkt.com.ahrapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.HotelesAdapter;
import autodromo.punkmkt.com.ahrapp.models.Hotel;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by germanpunk on 30/08/16.
 */
public class HotelesFragment extends Fragment{
    String AHR_API_URL = "http://104.236.3.158:82/api/premio/la_ciudad/hoteles/";
    private ArrayList<Hotel> hoteles = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    String TAG = HotelesFragment.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_la_ciudad_layout, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HotelesAdapter(hoteles,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())){
        StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_API_URL, "UTF-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG,response);
                    hoteles.clear();
                    JSONObject object = new JSONObject(response);

                    JSONArray results = object.getJSONArray("results");
                    for (int count = 0; count < results.length(); count++) {
                        JSONObject anEntry = results.getJSONObject(count);
                        Log.d(TAG,anEntry.toString());
                        Hotel hotel = new Hotel();
                        hotel.setId(Integer.parseInt(anEntry.optString("id")));
                        hotel.setNombre(anEntry.getString("name"));
                        hotel.setUbicacion(anEntry.getString("location"));
                        hotel.setTelefono(anEntry.getString("phone"));
                        hotel.setImagen(anEntry.getString("picture"));
                        hotel.setLatitud_mapa(anEntry.getString("latitude"));
                        hotel.setLongitud_mapa(anEntry.getString("longitude"));
                        hotel.setUrlmap(anEntry.getString("website"));
                        hotel.setWebsite(anEntry.getString("website"));
                        hoteles.add(hotel);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MyVolleySingleton.getInstance().addToRequestQueue(request);
        }
        else {
            Cache mCache = MyVolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry mEntry = mCache.get(AHR_API_URL);
            if (mEntry != null) {
                try {
                    String cacheData = new String(mEntry.data, "UTF-8");
                    JSONObject object = new JSONObject(cacheData);
                    JSONArray results = object.getJSONArray("results");
                    for (int count = 0; count < results.length(); count++) {
                        JSONObject anEntry = results.getJSONObject(count);
                        Log.d(TAG,anEntry.toString());
                        Hotel hotel = new Hotel();
                        hotel.setId(Integer.parseInt(anEntry.optString("id")));
                        hotel.setNombre(anEntry.getString("name"));
                        hotel.setUbicacion(anEntry.getString("location"));
                        hotel.setTelefono(anEntry.getString("phone"));
                        hotel.setImagen(anEntry.getString("picture"));
                        hotel.setLatitud_mapa(anEntry.getString("latitude"));
                        hotel.setLongitud_mapa(anEntry.getString("longitude"));
                        hotel.setUrlmap(anEntry.getString("website"));
                        hotel.setWebsite(anEntry.getString("website"));
                        hoteles.add(hotel);
                    }
                    adapter.notifyDataSetChanged();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "No entry cache");
            }
        }
        return v;
    }
}
