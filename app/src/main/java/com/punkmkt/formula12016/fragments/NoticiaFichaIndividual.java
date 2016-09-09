package com.punkmkt.formula12016.fragments;

/**
 * Created by germanpunk on 09/09/16.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.models.Noticia;
import com.punkmkt.formula12016.utils.AuthRequest;
import com.punkmkt.formula12016.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class NoticiaFichaIndividual extends Fragment {

    private String RELATED_NEWS = "http://104.236.3.158:82/api/premio/news/related_news/1/news/";
    private ArrayList<Noticia> noticias = new ArrayList<Noticia>();
    RecyclerView recyclerView;
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    NetworkImageView mNetworkImageView;
    private TextView mTitulo, mDescripcion;
    NetworkImageView last,last2,last3;
    public TextView idn1, idn2, tituloNoticia1, tituloNoticia2;
    public String img1, img2, id, id2, nom1, nom2, desc1, desc2;
    //public RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.noticia_individual,container,false);

        Bundle noticiaArg = getArguments();
        final String nId = noticiaArg.getString("noticiaId");
        String titulo = noticiaArg.getString("titulo");
        String img = noticiaArg.getString("imagen");
        String descripcion = noticiaArg.getString("descripcion");

        //getActivity().getActionBar().setTitle(titulo);
       // Tracker tracker = ((MyVolleySingleton) getActivity().getApplication()).getTracker(MyVolleySingleton.TrackerName.APP_TRACKER);
       // tracker.setScreenName(titulo);
       // tracker.send(new HitBuilders.ScreenViewBuilder().build());

        mNetworkImageView = (NetworkImageView) v.findViewById(R.id.imagen_principal);
        imageLoader = MyVolleySingleton.getInstance().getImageLoader();
        mNetworkImageView.setImageUrl(img, imageLoader);

        mTitulo = (TextView) v.findViewById(R.id.titulo);
        mTitulo.setText(titulo);

        mDescripcion = (TextView) v.findViewById(R.id.descripcion);
        mDescripcion.setText(descripcion);

        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        noticiasRelacionadas();
        RelativeLayout relativeSecondNew =(RelativeLayout) getActivity().findViewById(R.id.noticia_1);
        relativeSecondNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment noticiaIndividual = new NoticiaFichaIndividual();
                Bundle bundle = new Bundle();
                bundle.putString("noticiaId", id);
                bundle.putString("titulo", nom1);
                bundle.putString("descripcion", desc1);
                bundle.putString("imagen", img1);
                noticiaIndividual.setArguments(bundle);

                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, noticiaIndividual);
                transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        RelativeLayout relativeThirdNew =(RelativeLayout) getActivity().findViewById(R.id.noticia_2);
        relativeThirdNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment noticiaIndividual = new NoticiaFichaIndividual();
                Bundle bundle = new Bundle();
                bundle.putString("noticiaId", id2);
                bundle.putString("titulo", nom2);
                bundle.putString("descripcion", desc2);
                bundle.putString("imagen", img2);
                noticiaIndividual.setArguments(bundle);

                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, noticiaIndividual);
                transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment noticiasFragment = new NoticiasFragment();
                    android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, noticiasFragment);
                    transaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    //Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                    return true;
                }else {
                    return false;
                }
            }
        });

    }

    public void noticiasRelacionadas(){
        Bundle noticiaBundle = getArguments();
        final String notId = noticiaBundle.getString("noticiaId");
        RELATED_NEWS += notId + "/";

        if (NetworkUtils.haveNetworkConnection(getActivity())) {

            StringRequest request = new AuthRequest(getActivity().getApplicationContext(),Request.Method.GET, RELATED_NEWS, "utf-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONArray array_object = object.getJSONArray("results");
                        //JSONArray newsArray = new object.getJSONArray() object.getJSONArray("results");

                        id = array_object.getJSONObject(0).optString("id").toString();
                        nom1 = array_object.getJSONObject(0).optString("title").toString();
                        desc1 = array_object.getJSONObject(0).optString("description").toString();
                        img1 = array_object.getJSONObject(0).optString("thumbnail").toString();
                        tituloNoticia1 = (TextView) getActivity().findViewById(R.id.titulo_noticia_1);
                        tituloNoticia1.setText(nom1);
                        idn1 = (TextView) getActivity().findViewById(R.id.idn1);
                        idn1.setText(id);

                        last = (NetworkImageView) getActivity().findViewById(R.id.img_noticia1);
                        last.setImageUrl(img1, imageLoader);

                        id2 = array_object.getJSONObject(1).optString("id").toString();
                        nom2 = array_object.getJSONObject(1).optString("title").toString();
                        desc2 = array_object.getJSONObject(1).optString("description").toString();
                        img2 = array_object.getJSONObject(1).optString("thumbnail").toString();
                        tituloNoticia2 = (TextView) getActivity().findViewById(R.id.titulo_noticia_2);
                        tituloNoticia2.setText(nom2);
                        idn2 = (TextView) getActivity().findViewById(R.id.idn2);
                        idn2.setText(id2);

                        last2 = (NetworkImageView) getActivity().findViewById(R.id.img_noticia2);
                        last2.setImageUrl(img2, imageLoader);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MyVolleySingleton.getInstance().addToRequestQueue(request);

        }
    }

}