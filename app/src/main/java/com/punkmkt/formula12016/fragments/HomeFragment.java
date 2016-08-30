package com.punkmkt.formula12016.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.adapters.NewsAdapter;
import com.punkmkt.formula12016.models.Noticia;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by germanpunk on 15/08/16.
 */
public class HomeFragment extends Fragment {

    protected TextView TextCounter;
    CountDownTimer c;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Time fechaEvento = new Time(Time.getCurrentTimezone());
    int hora = 00;
    int minuto = 01;
    int segundo = 00;
    int dia = 28;
    // month is zero based...7 == August
    int mes = 9;
    int ano;
    private int diasfaltantes;
    private int horasFaltantes;
    private int minutosFaltantes;
    private int segundosFaltantes;
    public String id;

    private final String AHR_URL_NOTICIAS = "http://104.236.3.158:82/api/premio/news/last_news/4/";
    private ArrayList<Noticia> noticias = new ArrayList<>();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);
        RelativeLayout header_speed_lover = (RelativeLayout) v.findViewById(R.id.header_speed_lover);
        ImageView header_speed_lover_image = (ImageView) v.findViewById(R.id.header_speed_lover_image);
        ImageView header_speed_lover_background = (ImageView) v.findViewById(R.id.header_speed_lover_background);


        String speed_lover = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("speed_lover", null);
        if(speed_lover==null){
            header_speed_lover.setVisibility(View.GONE);
        }
        else if(speed_lover.equals("speed_lover_1")){
            header_speed_lover_background.setImageResource(R.drawable.textura_speed_lovers);
            header_speed_lover_image.setImageResource(R.drawable.speed_lovers_logo);
        }
        else if(speed_lover.equals("speed_lover_2")){
            header_speed_lover_background.setImageResource(R.drawable.textura_speed_lovers);
            header_speed_lover_image.setImageResource(R.drawable.speed_lovers_logo);
        }

        configuracionEvento();
        view = v;
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getActivity(),2);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new NewsAdapter(noticias,getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_NOTICIAS, "UTF-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array_object = object.getJSONArray("results");
                    for (int count = 0; count < array_object.length(); count++) {
                        JSONObject anEntry = array_object.getJSONObject(count);
                        Noticia noticia = new Noticia();
                        noticia.setId(Integer.parseInt(anEntry.optString("id")));
                        noticia.setShort_title(anEntry.optString("short_title"));
                        noticia.setShort_description(anEntry.optString("short_description"));
                        noticia.setThumbnail(anEntry.optString("thumbnail"));
                        noticias.add(noticia);
                    }
                    Log.d("news",noticias.toString());
                    mAdapter.notifyDataSetChanged();
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
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void configuracionEvento(){

        this.fechaEvento.setToNow();
        this.ano = fechaEvento.year;

        fechaEvento.set(segundo, minuto, hora, dia, mes, ano);
        fechaEvento.normalize(true);
        long confMillis = fechaEvento.toMillis(true);

        Time nowTime = new Time(Time.getCurrentTimezone());
        nowTime.setToNow();
        nowTime.normalize(true);
        long nowMillis = nowTime.toMillis(true);

        long milliDiff = confMillis - nowMillis;

        c = new CountDownTimer(milliDiff, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                try{
                    segundosFaltantes = (int) (millisUntilFinished / 1000) % 60 ;
                    minutosFaltantes = (int) ((millisUntilFinished / (1000*60)) % 60);
                    horasFaltantes   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
                    diasfaltantes = (int) (millisUntilFinished/(1000*60*60*24));

                    String dias = ""+diasfaltantes;
                    if (diasfaltantes < 10){
                        dias = "0"+diasfaltantes;
                    }
                    String hrs = ""+horasFaltantes;
                    if (horasFaltantes < 10){
                        hrs = "0"+horasFaltantes;
                    }
                    String min = ""+minutosFaltantes;
                    if (minutosFaltantes < 10){
                        min = "0"+minutosFaltantes;
                    }
                    String seg = ""+segundosFaltantes;
                    if (segundosFaltantes < 10){
                        seg = "0"+segundosFaltantes;
                    }
                    TextCounter = (TextView) view.findViewById(R.id.counter);
                    TextCounter.setText(dias + ":" + hrs + ":" + min + ":" +seg);
                    //TextDescription = (TextView) findViewById(R.id.counter);
                    //TextDescription.setText(fDias+"\u00A0"+"\u00A0 \u00A0 \u00A0"+fHoras+"\u00A0"+"\u00A0 \u00A0"+fMinutos+"\u00A0"+"\u00A0 \u00A0"+fSegundos);

                } catch (Exception e) {

                }
            }
            @Override
            public void onFinish() {
                TextCounter = (TextView) view.findViewById(R.id.counter);
                TextCounter.setText("00:00:00:00");
                //TextDescription = (TextView) findViewById(R.id.definicion);
                //TextDescription.setText("Sigan disfrutando del evento");
            }
        }.start();
    }
}
