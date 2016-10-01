package autodromo.punkmkt.com.ahrapp.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MainActivity;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.CustomizedSpinnerArrayListAdapter;
import autodromo.punkmkt.com.ahrapp.adapters.PilotosAdapter;
import autodromo.punkmkt.com.ahrapp.models.Piloto;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by germanpunk on 17/08/16.
 */
public class PilotosFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private final String AHR_URL_PILOTOS = "http://104.236.3.158:82/api/premio/pilotos/";
    private ArrayList<Piloto> pilotos = new ArrayList<>();
    private ArrayList<String> data_array_pilotos = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    String TAG = PilotosFragment.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_pilotos, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PilotosAdapter(pilotos,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            Log.d(TAG,"internet");
            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_PILOTOS, "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        pilotos.clear();
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

                        data_array_pilotos.add(0, getResources().getString(R.string.buscar_piloto));
                        final Spinner spinner_pilotos = (Spinner) v.findViewById(R.id.buscar_piloto);
                        final ArrayAdapter<String> adapter_pilotos = new CustomizedSpinnerArrayListAdapter(
                                getActivity(), R.layout.spinner_layout_pilotos, data_array_pilotos);
                        spinner_pilotos.setAdapter(adapter_pilotos);
                        spinner_pilotos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                if (ValidateSpinner(spinner_pilotos, getResources().getString(R.string.buscar_piloto))) {
                                    Piloto piloto = pilotos.get(position - 1);
                                    //((MainActivity) getActivity().getApplicationContext()).getSupportActionBar().setTitle(getActivity().getApplicationContext().getResources().getString(R.string.app_name));
                                    Fragment fH = new SinglePilotoFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", Integer.toString(piloto.getId()));
                                    fH.setArguments(bundle);
                                    FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                                    ftH.replace(R.id.frame, fH);
                                    ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    ftH.addToBackStack(null);
                                    ftH.commit();
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
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
        }
        else{
            Cache mCache = MyVolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry mEntry = mCache.get(AHR_URL_PILOTOS);
            if (mEntry != null) {
                try {
                    String cacheData = new String(mEntry.data, "UTF-8");
                    JSONObject object = new JSONObject(cacheData);
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

                    data_array_pilotos.add(0, getResources().getString(R.string.buscar_piloto));
                    final Spinner spinner_pilotos = (Spinner) v.findViewById(R.id.buscar_piloto);
                    final ArrayAdapter<String> adapter_pilotos = new CustomizedSpinnerArrayListAdapter(
                            getActivity(), R.layout.spinner_layout_pilotos, data_array_pilotos);
                    spinner_pilotos.setAdapter(adapter_pilotos);
                    spinner_pilotos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            if (ValidateSpinner(spinner_pilotos, getResources().getString(R.string.buscar_piloto))) {
                                Piloto piloto = pilotos.get(position - 1);
                                ((MainActivity) getActivity().getApplicationContext()).getSupportActionBar().setTitle(getActivity().getApplicationContext().getResources().getString(R.string.app_name));
                                Fragment fH = new SinglePilotoFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("id", Integer.toString(piloto.getId()));
                                fH.setArguments(bundle);
                                FragmentTransaction ftH = ((MainActivity) getActivity().getApplicationContext()).getSupportFragmentManager().beginTransaction();
                                ftH.replace(R.id.frame, fH);
                                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ftH.addToBackStack(null);
                                ftH.commit();
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                } catch (UnsupportedEncodingException |JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

            }
        }
        return v;
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
