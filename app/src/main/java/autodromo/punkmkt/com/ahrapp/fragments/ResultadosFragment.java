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
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MainActivity;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.PremiosAdapter;
import autodromo.punkmkt.com.ahrapp.models.Premio;
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
public class ResultadosFragment extends Fragment {
    private final String AHR_URL_PREMIOS = "http://104.236.3.158:82/api/premio/premios/";
    private ArrayList<Premio> premios = new ArrayList<>();
    private RecyclerView.Adapter adapter = null;
    String TAG = ResultadosFragment.class.getName();
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_resultados, container, false);
        Log.d(TAG,"oncreateview");


        //adapter = null;
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setHasFixedSize(true);
        //adapter = new PremiosAdapter(premios,getActivity().getApplicationContext());
        adapter = new PremiosAdapter(premios,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        TextView trg = (TextView) v.findViewById(R.id.ranking_general);
        ImageButton brg = (ImageButton) v.findViewById(R.id.go);
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_PREMIOS, "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        premios.clear();
                        Log.d("response", response);
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

                        //adapter.notifyDataSetChanged();
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
            Cache.Entry mEntry = mCache.get(AHR_URL_PREMIOS);
            if (mEntry != null) {
                try {
                    String cacheData = new String(mEntry.data, "UTF-8");
                    JSONObject object = new JSONObject(cacheData);
                    JSONArray results = object.getJSONArray("results");
                    for (int count = 0; count < results.length(); count++) {
                        JSONObject anEntry = results.getJSONObject(count);
                        Premio premio = new Premio();
                        premio.setId(Integer.parseInt(anEntry.optString("id")));
                        premio.setName(anEntry.optString("name"));
                        premio.setPicture(anEntry.optString("picture"));
                        premios.add(premio);
                    }
                    adapter.notifyDataSetChanged();

                } catch (UnsupportedEncodingException |JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

            }

        }


        brg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                Fragment fH = new RankingGeneralFragment();

                FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }
        });
        trg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                Fragment fH = new RankingGeneralFragment();
                FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }
        });
        return v;
    }
}
