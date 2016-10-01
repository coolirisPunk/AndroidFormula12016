package autodromo.punkmkt.com.ahrapp.fragments;

/**
 * Created by germanpunk on 09/09/16.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.NoticiaMasonryAdapter;
import autodromo.punkmkt.com.ahrapp.models.Noticia;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



public class NoticiaFichaIndividual extends Fragment {

    private String SINGLE_ONE = "http://104.236.3.158:82/api/premio/news/category_news/"; // 1/news/";
    private String RELATED_NEWS = "http://104.236.3.158:82/api/premio/news/related_news/"; //1/news/";
    private ArrayList<Noticia> noticias = new ArrayList<Noticia>();
    RecyclerView mRecyclerView;
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    NetworkImageView mNetworkImageView;
    private TextView mTitulo, mDescripcion;
    private ImageView share;
    private String url_share;
    private String text_share;
    NetworkImageView last,last2,last3;
    public TextView idn1, idn2, tituloNoticia1, tituloNoticia2;
    public String img1, img2, id, id2, nom1, nom2, desc1, desc2;
    public RecyclerView.Adapter adapter;
    String nId;
    public int nIdCat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.noticia_individual,container,false);

        Bundle noticiaArg = getArguments();
        nId = noticiaArg.getString("noticiaId",null);
        nIdCat = noticiaArg.getInt("idCat");
        mTitulo = (TextView) v.findViewById(R.id.titulo);
        mDescripcion = (TextView) v.findViewById(R.id.descripcion);
        share = (ImageView) v.findViewById(R.id.share);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("noticia",text_share);
                Log.d("noticia",url_share);
                if(url_share!=null
                        ){
                    if(!url_share.equals("null") || !url_share.equals("")){
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, text_share);
                        i.putExtra(Intent.EXTRA_TEXT, url_share);
                        startActivity(Intent.createChooser(i, getResources().getText(R.string.share)));
                    }
                }
            }
        });
        /*if(nId==null){
            nId = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("new_id", null);
        }
        else{
            getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("new_id", nId).commit();
        }*/

        //getActivity().getActionBar().setTitle(titulo);
       // Tracker tracker = ((MyVolleySingleton) getActivity().getApplication()).getTracker(MyVolleySingleton.TrackerName.APP_TRACKER);
       // tracker.setScreenName(titulo);
       // tracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, SINGLE_ONE+nIdCat + "/news/" + nId +"/", "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("SINGLE-NEWS",jsonObject.toString());
                        String titulo = jsonObject.optString("title");
                        String descripcion = jsonObject.optString("description");
                        String img = jsonObject.optString("picture");
                        String link_share = jsonObject.optString("post_url");

                        if (link_share!=null){
                            if(link_share.equals("null") || link_share.equals("")){
                                share.setVisibility(View.GONE);
                            }
                            else{Log.d("noticias", "listo para compartir");
                                url_share = link_share;
                                text_share = titulo;
                            }
                        }
                        else{
                            share.setVisibility(View.GONE);
                        }

                        mNetworkImageView = (NetworkImageView) v.findViewById(R.id.imagen_principal);
                        imageLoader = MyVolleySingleton.getInstance().getImageLoader();
                        mNetworkImageView.setImageUrl(img, imageLoader);


                        mTitulo.setText(titulo);

                        mDescripcion.setText(descripcion);
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
        }
        else{
            Cache mCache = MyVolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry mEntry = mCache.get(SINGLE_ONE);
            if (mEntry != null) {
                try {
                    String cacheData = new String(mEntry.data, "UTF-8");
                    JSONObject jsonObject = new JSONObject(cacheData);
                    String titulo = jsonObject.optString("title");
                    String descripcion = jsonObject.optString("description");
                    String img = jsonObject.optString("picture");
                    String link_share = jsonObject.optString("post_url");

                    if (link_share!=null){
                        if(link_share.equals("null")){
                            share.setVisibility(View.GONE);
                        }
                        else{Log.d("noticias", "listo para compartir");
                            url_share = link_share;
                            text_share = titulo;
                        }
                    }
                    else{
                        share.setVisibility(View.GONE);
                    }

                    mNetworkImageView = (NetworkImageView) v.findViewById(R.id.imagen_principal);
                    imageLoader = MyVolleySingleton.getInstance().getImageLoader();
                    mNetworkImageView.setImageUrl(img, imageLoader);

                    mTitulo.setText(titulo);

                    mDescripcion.setText(descripcion);
                } catch (UnsupportedEncodingException |JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

            }
        }

        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        noticiasRelacionadas();
    }

    public void noticiasRelacionadas(){

        Bundle noticiaBundle = getArguments();
        final String notId = noticiaBundle.getString("noticiaId");
        //RELATED_NEWS += notId + "/";
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        //mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        //mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        final NoticiaMasonryAdapter masonryAdapter = new NoticiaMasonryAdapter(noticias, getActivity());

        mRecyclerView.setAdapter(masonryAdapter);
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, RELATED_NEWS+nIdCat+"/news/"+ notId+"/", "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        noticias.clear();
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            Noticia noticia = new Noticia();
                            noticia.setId(Integer.parseInt(anEntry.optString("id")));
                            noticia.setShort_title(anEntry.optString("short_title"));
                            noticia.setTitle(anEntry.optString("title"));
                            noticia.setShort_description(anEntry.optString("short_description"));
                            noticia.setThumbnail(anEntry.optString("thumbnail"));
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
            //request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyVolleySingleton.getInstance().addToRequestQueue(request);

            mRecyclerView.setAdapter(masonryAdapter);
        }
        else{
            Cache mCache = MyVolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry mEntry = mCache.get(RELATED_NEWS);
            if (mEntry != null) {
                try {
                    String cacheData = new String(mEntry.data, "UTF-8");
                    JSONObject object = new JSONObject(cacheData);
                    JSONArray results = object.getJSONArray("results");
                    for (int count = 0; count < results.length(); count++) {
                        JSONObject anEntry = results.getJSONObject(count);
                        Noticia noticia = new Noticia();
                        noticia.setId(Integer.parseInt(anEntry.optString("id")));
                        noticia.setShort_title(anEntry.optString("short_title"));
                        noticia.setShort_description(anEntry.optString("short_description"));
                        noticia.setThumbnail(anEntry.optString("thumbnail"));
                        noticia.setIdCat(anEntry.optInt("category_new"));
                        noticias.add(noticia);
                    }
                    Log.d("news",noticias.toString());
                    masonryAdapter.notifyDataSetChanged();
                } catch (UnsupportedEncodingException |JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

            }
        }
    }

}