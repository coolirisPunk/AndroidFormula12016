package autodromo.punkmkt.com.ahrapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MainActivity;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.NewsAdapter;
import autodromo.punkmkt.com.ahrapp.adapters.NoticiaMasonryAdapter;
import autodromo.punkmkt.com.ahrapp.models.Noticia;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    int hora = 13;
    int minuto = 00;
    int segundo = 00;
    int dia = 30;
    // month is zero based...7 == August
    int mes = 9;
    int ano;
    private int diasfaltantes;
    private int horasFaltantes;
    private int minutosFaltantes;
    private int segundosFaltantes;
    public String id;
    int resource;

    private final String AHR_URL_NOTICIAS = "http://104.236.3.158:82/api/premio/news/last_news/4/";
    private ArrayList<Noticia> noticias = new ArrayList<>();
    LinearLayout container_links;
    View view;
    String TAG = HomeFragment.class.getName();

    String speed_lover;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home,container,false);
        RelativeLayout header_speed_lover = (RelativeLayout) v.findViewById(R.id.header_speed_lover);
        container_links = (LinearLayout)v.findViewById(R.id.container_links);
        ImageView header_speed_lover_image = (ImageView) v.findViewById(R.id.header_speed_lover_image);
        ImageView header_speed_lover_background = (ImageView) v.findViewById(R.id.header_speed_lover_background);
        final TextView text_welcome = (TextView) v.findViewById(R.id.text_welcome);
        speed_lover = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("speed_lover", null);
        int width = 150;
        int height = 150;

        if(speed_lover==null){
            header_speed_lover.setVisibility(View.GONE);
        }
        else if(speed_lover.equals("speed_lovers")){
            header_speed_lover_background.setImageResource(R.drawable.textura_speed_lovers);
            //header_speed_lover_image.setImageResource(R.drawable.speed_lovers_logo);
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.speed_lovers_logo);
            Drawable drawable = new BitmapDrawable(this.getResources(),getResizedBitmap(bMap,width,height));
            header_speed_lover_image.setImageDrawable(drawable);
            text_welcome.setText(getResources().getText(R.string.welcome).toString() + " " + getResources().getText(R.string.welcome_speed_lover).toString());
        }
        else if(speed_lover.equals("euphoric_fans")){
            header_speed_lover_background.setImageResource(R.drawable.textura_euphoric_fans);
            //header_speed_lover_image.setImageResource(R.drawable.euphoric_fans_logo);
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.euphoric_fans_logo);
            Drawable drawable = new BitmapDrawable(this.getResources(),getResizedBitmap(bMap,width,height));
            header_speed_lover_image.setImageDrawable(drawable);
            text_welcome.setText(getResources().getText(R.string.welcome).toString() + " " + getResources().getText(R.string.welcome_euphoric_fans).toString());
        }
        else if(speed_lover.equals("true_racers")){
            header_speed_lover_background.setImageResource(R.drawable.textura_true_racers);
            //header_speed_lover_image.setImageResource(R.drawable.true_racers_logo);
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.true_racers_logo);
            Drawable drawable = new BitmapDrawable(this.getResources(),getResizedBitmap(bMap,width,height));
            header_speed_lover_image.setImageDrawable(drawable);
            text_welcome.setText(getResources().getText(R.string.welcome).toString() + " " + getResources().getText(R.string.welcome_true_racers).toString());
        }
        else if(speed_lover.equals("vip_party_racers")){
            header_speed_lover_background.setImageResource(R.drawable.textura_vip_party_racers);
            //header_speed_lover_image.setImageResource(R.drawable.vip_party_racers_logo);
            Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.vip_party_racers_logo);
            Drawable drawable = new BitmapDrawable(this.getResources(),getResizedBitmap(bMap,width,height));
            header_speed_lover_image.setImageDrawable(drawable);
            text_welcome.setText(getResources().getText(R.string.welcome).toString() + " " + getResources().getText(R.string.welcome_vip_party_racers).toString());
        }
        else{
            header_speed_lover.setVisibility(View.GONE);
        }
        configuracionEvento();
        for(int i = 0; i<4; i++){
            if(speed_lover==null){
                if(i==0){
                    resource =R.drawable.icon_autodromo;
                }
                else if(i==1){
                    resource =R.drawable.icon_horario;
                }
                else if(i==2){
                    resource =R.drawable.icon_resultados;
                }
                else if(i==3){
                    resource =R.drawable.icon_noticias;
                }
            }

            else if(speed_lover.equals("speed_lovers")){
                if(i==0){
                    resource =R.drawable.icon_resultados;
                }
                else if(i==1){
                    resource =R.drawable.icon_horario;
                }
                else if(i==2){
                    resource =R.drawable.icon_pasion;
                }
                else if(i==3){
                    resource =R.drawable.icon_autodromo;
                }
            }
            else if(speed_lover.equals("euphoric_fans")){
                if(i==0){
                    resource =R.drawable.icon_pasion;
                }
                else if(i==1){
                    resource =R.drawable.icon_social_hub;
                }
                else if(i==2){
                    resource =R.drawable.icon_shop;
                }
                else if(i==3){
                    resource =R.drawable.icon_autodromo;
                }
            }
            else if(speed_lover.equals("true_racers")){
                if(i==0){
                    resource =R.drawable.icon_horario;
                }
                else if(i==1){
                    resource =R.drawable.icon_pilotos;
                }
                else if(i==2){
                    resource =R.drawable.icon_resultados;
                }
                else if(i==3){
                    resource =R.drawable.icon_autodromo;
                }
            }
            else if(speed_lover.equals("vip_party_racers")){
                if(i==0){
                    resource =R.drawable.icon_ciudad;
                }
                else if(i==1){
                    resource =R.drawable.icon_social_hub;
                }
                else if(i==2){
                    resource =R.drawable.icon_shop;
                }
                else if(i==3){
                    resource =R.drawable.icon_autodromo;
                }
            }
            else{
                    if(i==0){
                        resource =R.drawable.icon_autodromo;
                    }
                    else if(i==1){
                        resource =R.drawable.icon_horario;
                    }
                    else if(i==2){
                        resource =R.drawable.icon_resultados;
                    }
                    else if(i==3){
                        resource =R.drawable.icon_noticias;
                    }
            }

            LinearLayout l = getNewImageView(resource,i);
            container_links.addView(l);
        }
        view = v;
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new NewsAdapter(noticias,getActivity().getApplicationContext());
        final NoticiaMasonryAdapter masonryAdapter = new NoticiaMasonryAdapter(noticias, getActivity());

        mRecyclerView.setAdapter(masonryAdapter);
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
        StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_NOTICIAS, "UTF-8", new Response.Listener<String>() {
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
                        noticia.setTitle(anEntry.optString("title"));
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
        MyVolleySingleton.getInstance().addToRequestQueue(request);

        }
        else{
            Cache mCache = MyVolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry mEntry = mCache.get(AHR_URL_NOTICIAS);
            if (mEntry != null) {
                try {
                    String cacheData = new String(mEntry.data, "UTF-8");
                    JSONObject object = new JSONObject(cacheData);
                    JSONArray results = object.getJSONArray("results");
                    for (int count = 0; count < results.length(); count++) {
                        JSONObject anEntry = results.getJSONObject(count);
                        Noticia noticia = new Noticia();
                        noticia.setId(Integer.parseInt(anEntry.optString("id")));
                        noticia.setTitle(anEntry.optString("title"));
                        noticia.setThumbnail(anEntry.optString("thumbnail"));
                        noticia.setIdCat(anEntry.optInt("category_new"));
                        noticias.add(noticia);
                    }
                    Log.d("news",noticias.toString());
                    mAdapter.notifyDataSetChanged();
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

    private LinearLayout getNewImageView(int resource, int index){
        final int currentindex = index;
        LinearLayout ll = new LinearLayout(getActivity().getApplicationContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        LLParams.setMargins(5,0,5, 0);
        ll.setLayoutParams(LLParams);

        LinearLayout.LayoutParams ImParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView imageView = new ImageView(getActivity().getApplicationContext());
        imageView.setLayoutParams(ImParams);
        imageView.setImageResource(resource);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,String.valueOf(currentindex));

                if(speed_lover==null){
                    if(currentindex==0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new AutodromoFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==1){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new HorariosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==2){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new ResultadosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==3){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new NoticiasFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                }

                else if(speed_lover.equals("speed_lovers")){
                    if(currentindex==0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new ResultadosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==1){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new HorariosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==2){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new Pasion_f1();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==3){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new AutodromoFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                }
                else if(speed_lover.equals("euphoric_fans")){
                    if(currentindex==0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new Pasion_f1();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==1){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new SocialHubFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==2){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new TiendaFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==3){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new AutodromoFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                }
                else if(speed_lover.equals("true_racers")){
                    if(currentindex==0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new HorariosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==1){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new PilotosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==2){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new ResultadosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==3){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new AutodromoFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                }
                else if(speed_lover.equals("vip_party_racers")){
                    if(currentindex==0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new LaciudadFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==1){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new SocialHubFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==2){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new TiendaFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==3){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new AutodromoFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                }
                else{
                    if(currentindex==0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new AutodromoFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==1){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new HorariosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==2){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new ResultadosFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                    else if(currentindex==3){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                        Fragment fH = new NoticiasFragment();
                        FragmentTransaction ftH = getActivity().getSupportFragmentManager().beginTransaction();
                        ftH.replace(R.id.frame, fH);
                        ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftH.addToBackStack(null);
                        ftH.commit();
                    }
                }

            }
        });
        ll.addView(imageView);
        return ll;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

    // create a matrix for the manipulation
        Matrix matrix = new Matrix();

    // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

    // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }


}
