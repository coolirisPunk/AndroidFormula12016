package com.punkmkt.formula12016;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.adapters.NewsAdapter;
import com.punkmkt.formula12016.models.Noticia;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configuracionEvento();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this,2);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new NewsAdapter(noticias,getApplicationContext());

        StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_NOTICIAS, "UTF-8", new Response.Listener<String>() {
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
        mRecyclerView.setAdapter(mAdapter);

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
                    TextCounter = (TextView) findViewById(R.id.counter);
                    TextCounter.setText(dias + ":" + hrs + ":" + min + ":" +seg);
                    //TextDescription = (TextView) findViewById(R.id.counter);
                    //TextDescription.setText(fDias+"\u00A0"+"\u00A0 \u00A0 \u00A0"+fHoras+"\u00A0"+"\u00A0 \u00A0"+fMinutos+"\u00A0"+"\u00A0 \u00A0"+fSegundos);

                } catch (Exception e) {

                }
            }
            @Override
            public void onFinish() {
                TextCounter = (TextView) findViewById(R.id.counter);
                TextCounter.setText("00:00:00:00");
                //TextDescription = (TextView) findViewById(R.id.definicion);
                //TextDescription.setText("Sigan disfrutando del evento");
            }
        }.start();
    }
}
