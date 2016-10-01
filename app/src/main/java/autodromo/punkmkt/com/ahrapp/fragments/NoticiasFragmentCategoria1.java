package autodromo.punkmkt.com.ahrapp.fragments;

/**
 * Created by germanpunk on 09/09/16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.NoticiaMasonryAdapter;
import autodromo.punkmkt.com.ahrapp.models.Noticia;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.SpacesItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sebastianmendezgiron on 31/08/16.
 */
public class NoticiasFragmentCategoria1 extends Fragment {

    RecyclerView recyclerView;
    private final String AHZ_URL_NOTICIAS = "http://104.236.3.158:82/api/premio/news/category_news/1/news/";
    private ArrayList<Noticia> noticias = new ArrayList<Noticia>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.noticias_categorias_fragments,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        final NoticiaMasonryAdapter masonryAdapter = new NoticiaMasonryAdapter(noticias, getActivity());

        StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHZ_URL_NOTICIAS, "UTF-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    noticias.clear();

                    JSONObject object = new JSONObject(response);
                    JSONArray array_object = object.getJSONArray("results");
                    for (int count = 0; count < array_object.length(); count++) {
                        JSONObject anEntry = array_object.getJSONObject(count);
                        Noticia noticia = new Noticia();
                        noticia.setId(Integer.parseInt(anEntry.optString("id")));
                        noticia.setTitle(anEntry.optString("title"));
                        noticia.setDescription(anEntry.optString("description"));
                        noticia.setThumbnail(anEntry.optString("thumbnail"));
                        noticia.setPicture(anEntry.optString("picture"));
                        noticia.setIdCat(anEntry.optInt("category_new"));
                        noticias.add(noticia);

                    }
                    Log.d("news",noticias.toString());

                    masonryAdapter.notifyDataSetChanged();
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

        recyclerView.setAdapter(masonryAdapter);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(20);
        recyclerView.addItemDecoration(spacesItemDecoration);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Tracker tracker = ((MyVolleySingleton) getActivity().getApplication()).getTracker(MyVolleySingleton.TrackerName.APP_TRACKER);
        //tracker.setScreenName(getString(R.string.menu_noticias));
        //tracker.send(new HitBuilders.ScreenViewBuilder().build());


    }

}
