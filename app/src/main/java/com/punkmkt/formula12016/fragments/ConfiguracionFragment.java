package com.punkmkt.formula12016.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.LoginActivity;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.adapters.CustomizedSpinnerAdapter;
import com.punkmkt.formula12016.models.Zone;
import com.punkmkt.formula12016.utils.AuthRequest;
import com.punkmkt.formula12016.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by germanpunk on 30/08/16.
 */
public class ConfiguracionFragment extends Fragment {
    String TAG = ConfiguracionFragment.class.getName();
    String AHR_UPDATEPROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/user/";
    String AHR_CREATEPROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/user/";
    String Ukey,zone,grada,section,fila,seat,speed_lover,profile_id;
    ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;

    private Spinner zonas,gradas,seccion,filas, asientos;
    private String AHR_URL_ZONAS = "http://104.236.3.158:82/api/premio/zones/";
    private String AHR_URL_GRADAS = "http://104.236.3.158:82/api/premio/grandstands/";
    private String AHR_URL_SECCIONES = "http://104.236.3.158:82/api/premio/sections/";
    private String AHR_URL_FILAS = "http://104.236.3.158:82/api/premio/rows/";
    private String AHR_URL_ASIENTOS = "http://104.236.3.158:82/api/premio/seats/";
    ArrayList<Zone> array_zones = new ArrayList<>();
    ArrayList<Zone> array_grandstands = new ArrayList<>();
    ArrayList<Zone> array_sections = new ArrayList<>();
    ArrayList<Zone> array_rows = new ArrayList<>();
    ArrayList<Zone> array_seats = new ArrayList<>();

    Map<Integer, String> dict_zonas = new LinkedHashMap<>();
    Map<Integer, String> dict_grandstands = new LinkedHashMap<>();
    Map<Integer, String> dict_sections = new LinkedHashMap<>();
    Map<Integer, String> dict_rows = new LinkedHashMap<>();
    Map<Integer, String> dict_seats = new LinkedHashMap<>();


    String current_zone, current_grandstand, current_section, current_fila, current_asiento;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_configuracion, container, false);
        final Button guardar_cambios = (Button) view.findViewById(R.id.guardar_cambios);
        Ukey = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("access_token",null);
        if(Ukey==null){
            LinearLayout container_configuracion_asiento = (LinearLayout) view.findViewById(R.id.container_configuracion_asiento);
            container_configuracion_asiento.setVisibility(View.GONE);
            guardar_cambios.setVisibility(View.GONE);
        }
        else{
            Log.d(TAG,Ukey);
            TextView instrucciones_conf = (TextView) view.findViewById(R.id.instrucciones_conf);
            instrucciones_conf.setVisibility(View.GONE);
        }


        speed_lover = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("speed_lover",null);
        profile_id = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("profile_id",null);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);

        current_zone = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("zone",null);
        current_grandstand = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("grada",null);
        current_section = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("section",null);
        current_fila = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("fila",null);
        current_asiento = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("seat",null);


        getzones();
        if((current_zone !=null)){
            if(!current_zone.equals("null")){
                getgrandstands(current_zone);
            }
        }
        if((current_grandstand !=null)){
            if(!current_grandstand.equals("null")){
                Log.d(TAG+" fcg",current_grandstand);
                getsections(current_grandstand);
            }
        }
        if((current_section !=null)){
            if(!current_section.equals("null")){
                getrows(current_section);
            }
        }
        if((current_fila !=null)){
            if(!current_fila.equals("null")){
                getseats(current_fila);
            }
        }


        final ImageView notificacion1 = (ImageView) view.findViewById(R.id.notificacion1);
        final ImageView notificacion2 = (ImageView) view.findViewById(R.id.notificacion2);
        final ImageView notificacion3 = (ImageView) view.findViewById(R.id.notificacion3);


        zonas = (Spinner) view.findViewById(R.id.zonas_spinner);
        gradas = (Spinner) view.findViewById(R.id.gradas_spinner);
        seccion = (Spinner) view.findViewById(R.id.seccion_spinner);
        filas = (Spinner) view.findViewById(R.id.filas_spinner);
        asientos = (Spinner) view.findViewById(R.id.asientos_spinner);
        final String[] data_array_zonas = getResources().getStringArray(R.array.zonas_array);
        notificacion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificacion1.setImageResource(R.drawable.notificacion_activa);
            }
        });
        notificacion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificacion2.setImageResource(R.drawable.notificacion_activa);
            }
        });
        notificacion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificacion3.setImageResource(R.drawable.notificacion_activa);
            }
        });



        zonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                switch (position){
                    case 0:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.YELLOW);
                        break;
                    case 1:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLUE);
                        break;
                    case 2:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLACK);
                        break;
                    case 3:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLACK);
                        break;
                    case 4:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLACK);
                        break;
                    case 5:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLACK);
                        break;
                }
                current_zone = array_zones.get(position).getId();
                getgrandstands(current_zone);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        gradas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                current_grandstand= array_grandstands.get(position).getId();
                getsections(current_grandstand);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                current_section = array_sections.get(position).getId();
                getrows(current_section);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        filas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                current_fila = array_rows.get(position).getId();
                getseats(current_fila);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                current_asiento = array_seats.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        guardar_cambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Actualizando la información....");
                progressDialog.show();
                Log.d(TAG,profile_id);
                if (Ukey==null){
                    Intent myIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(myIntent);
                }
                else if (profile_id==null || profile_id.equals("null")){
                    Log.d(TAG,"create");
                    createinfo();
                }
                else{
                    Log.d(TAG,"update");
                    AHR_UPDATEPROFILE_JSON_API_URL = AHR_UPDATEPROFILE_JSON_API_URL + profile_id + "/";
                    updateinfo();
                }
            }
        });

        return view;
    }
    public void createinfo() {
        Log.d(TAG,"create");
        JSONObject js = new JSONObject();

        try {
            js.put("zone", zonas.getSelectedItem().toString());
            js.put("grada", gradas.getSelectedItem().toString());
            js.put("section", seccion.getSelectedItem().toString());
            js.put("fila", filas.getSelectedItem().toString());
            js.put("seat", asientos.getSelectedItem().toString());
            js.put("speed_lover", "speed_lovers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,js.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,AHR_CREATEPROFILE_JSON_API_URL,js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            //Toast.makeText(getActivity().getApplicationContext(), object.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("response", object.toString());
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("userid", object.optString("userid")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("zone", object.optString("zone")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("grada", object.optString("grada")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("section", object.optString("section")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("fila", object.optString("fila")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("seat", object.optString("seat")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("speed_lover", object.optString("speed_lover")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("username", object.optString("username")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("email", object.optString("email")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("profile_id", object.optString("id")).commit();
                                profile_id =object.optString("id");

                                progressDialog.dismiss();

                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Información guardada.", Snackbar.LENGTH_SHORT);
                                snackbar.setActionTextColor(Color.WHITE);
                                snackbar.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            //alert con error
                            progressDialog.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, e.getMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("V", "Error: " + error.getMessage());
                progressDialog.dismiss();
                String json = null;
                NetworkResponse response = error.networkResponse;

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            json = new String(response.data);
                            try{
                                JSONObject obj = new JSONObject(json);
                                Log.d(TAG,obj.toString());
                                if(obj.has("non_field_errors")){
                                    if(obj.optString("non_field_errors")!=null){
                                        JSONArray array_object = obj.getJSONArray("non_field_errors");
                                        for (int count = 0; count < array_object.length(); count++) {
                                            Toast.makeText(getActivity().getApplicationContext(),array_object.get(count).toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            break;
                        case 404:
                            json = new String(response.data);
                            try{
                                JSONObject obj = new JSONObject(json);
                                Log.d(TAG,obj.toString());
                                if(obj.has("detail")){
                                    if(obj.optString("detail").equals("Not found.")){
                                        createinfo();
                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            break;
                        case 500:
                            json = new String(response.data);
                            try{
                                JSONObject obj = new JSONObject(json);
                                Log.d(TAG,obj.toString());
                                if(obj.has("detail")){
                                    if(obj.optString("detail").equals("Not found.")){
                                        createinfo();
                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            break;
                        default:

                            break;
                    }
                    //Additional cases
                }

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token " + Ukey);
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyVolleySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }
    public void updateinfo() {
        Log.d(TAG,"update");
        JSONObject js = new JSONObject();
        try {
            js.put("zone", zonas.getSelectedItem().toString());
            js.put("grada", gradas.getSelectedItem().toString());
            js.put("section", seccion.getSelectedItem().toString());
            js.put("fila", filas.getSelectedItem().toString());
            js.put("seat", asientos.getSelectedItem().toString());
            js.put("speed_lover", "euphoric_fans");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG,js.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,AHR_UPDATEPROFILE_JSON_API_URL,js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response;
                            //Toast.makeText(getActivity().getApplicationContext(), object.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("response", object.toString());
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("userid", object.optString("userid")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("zone", object.optString("zone")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("grada", object.optString("grada")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("section", object.optString("section")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("fila", object.optString("fila")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("seat", object.optString("seat")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("speed_lover", object.optString("speed_lover")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("username", object.optString("username")).commit();
                                getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).edit().putString("email", object.optString("email")).commit();

                                //Intent myIntent = new Intent(getActivity().getApplicationContext(), WelcomeActivity.class);
                                //startActivity(myIntent);
                            progressDialog.dismiss();
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "Información actualizada.", Snackbar.LENGTH_SHORT);
                                snackbar.setActionTextColor(Color.WHITE);
                                snackbar.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            //alert con error
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout,e.getMessage().toString(), Snackbar.LENGTH_SHORT);
                            snackbar.setActionTextColor(Color.RED);
                            snackbar.show();

                        }
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse response = error.networkResponse;

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            json = new String(response.data);
                            try{
                                JSONObject obj = new JSONObject(json);
                                Log.d(TAG,obj.toString());
                                if(obj.has("non_field_errors")){
                                    if(obj.optString("non_field_errors")!=null){
                                        JSONArray array_object = obj.getJSONArray("non_field_errors");
                                        for (int count = 0; count < array_object.length(); count++) {
                                            Toast.makeText(getActivity().getApplicationContext(),array_object.get(count).toString(),Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            break;
                        case 404:
                            json = new String(response.data);
                            try{
                                JSONObject obj = new JSONObject(json);
                                Log.d(TAG,obj.toString());
                                if(obj.has("detail")){
                                    if(obj.optString("detail").equals("Not found.")){
                                    createinfo();
                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            break;
                        default:

                            break;
                    }
                    //Additional cases
                }

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Token " + Ukey);
                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyVolleySingleton.getInstance().addToRequestQueue(jsonObjReq);
        AHR_UPDATEPROFILE_JSON_API_URL = AHR_CREATEPROFILE_JSON_API_URL;
    }


    private void getzones(){
        Log.d(TAG,"ZONES");
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_ZONAS, "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        Log.d(TAG,results.toString());
                        final String[] data_array_zonas = new String[results.length()];
                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            data_array_zonas[count] = anEntry.optString("title");
                            array_zones.add(new Zone(anEntry.optString("id"),anEntry.optString("title")));
                            dict_zonas.put(Integer.valueOf(anEntry.optString("id")), anEntry.optString("title"));
                        }
                        final ArrayAdapter<String> adapter_zonas = new CustomizedSpinnerAdapter(
                                getActivity(), android.R.layout.simple_spinner_item, data_array_zonas);
                        zonas.setAdapter(adapter_zonas);
                        if(current_zone != null){
                            if(!current_zone.equals("null")){
                                zonas.setSelection(getKeyByValue(dict_zonas, dict_zonas.get(Integer.valueOf(current_zone))));
                            }
                        }


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
    }
    private void getgrandstands(String id){
        Log.d(TAG+"GRANDSTANDS",id);
        final ArrayList<Zone> copy_array_grandstands = new ArrayList<>();
        final Map<Integer, String> copy_dict_grandstands = new LinkedHashMap<>();
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {

            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_GRADAS + id + "/" , "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        Log.d(TAG,results.toString());
                        final String[] data_array_grandstands = new String[results.length()];

                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            data_array_grandstands[count] = anEntry.optString("title");
                            copy_array_grandstands.add(new Zone(anEntry.optString("id"),anEntry.optString("title")));
                            copy_dict_grandstands.put(Integer.valueOf(anEntry.optString("id")), anEntry.optString("title"));
                        }
                        array_grandstands = copy_array_grandstands;
                        dict_grandstands = copy_dict_grandstands;
                        Log.d(TAG,dict_grandstands.values().toString());
                        Log.d(TAG,current_grandstand);
                        final ArrayAdapter<String> adapter_grandstands = new CustomizedSpinnerAdapter(
                                getActivity(), android.R.layout.simple_spinner_item, data_array_grandstands);
                        gradas.setAdapter(adapter_grandstands);
                        if(current_grandstand != null){
                            if(!current_grandstand.equals("null")){
                                gradas.setSelection(getKeyByValue(dict_grandstands, dict_grandstands.get(Integer.valueOf(current_grandstand))));

                            }
                        }

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
    }
    private void getsections(String id){
        Log.d(TAG+"SECTIONS",id);
        final ArrayList <Zone> copy_array_sections = new ArrayList<>();
        final Map<Integer, String> copy_dict_sections = new LinkedHashMap<>();
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {

            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_SECCIONES + id + "/" , "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        final String[] data_array_sections = new String[results.length()];

                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            data_array_sections[count] = anEntry.optString("title");
                            copy_array_sections.add(new Zone(anEntry.optString("id"),anEntry.optString("title")));
                            copy_dict_sections.put(Integer.valueOf(anEntry.optString("id")), anEntry.optString("title"));
                        }
                        array_sections = copy_array_sections;
                        dict_sections = copy_dict_sections;
                        final ArrayAdapter<String> adapter_sections = new CustomizedSpinnerAdapter(
                                getActivity(), android.R.layout.simple_spinner_item, data_array_sections);
                        seccion.setAdapter(adapter_sections);
                        if(current_section != null){
                            if(!current_section.equals("null")){
                                seccion.setSelection(getKeyByValue(dict_sections, dict_sections.get(Integer.valueOf(current_section))));
                            }
                        }

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
    }
    private void getrows(String id){
        Log.d(TAG+"ROWS",id);
        final ArrayList <Zone> copy_array_rows = new ArrayList<>();
        final Map<Integer, String> copy_dict_rows = new LinkedHashMap<>();
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {

            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_FILAS + id + "/" , "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        final String[] data_array_rows = new String[results.length()];

                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            data_array_rows[count] = anEntry.optString("title");
                            copy_array_rows.add(new Zone(anEntry.optString("id"),anEntry.optString("title")));
                            copy_dict_rows.put(Integer.valueOf(anEntry.optString("id")), anEntry.optString("title"));
                        }
                        array_rows = copy_array_rows;
                        dict_rows = copy_dict_rows;
                        final ArrayAdapter<String> adapter_rows = new CustomizedSpinnerAdapter(
                                getActivity(), android.R.layout.simple_spinner_item, data_array_rows);
                        filas.setAdapter(adapter_rows);
                        if(current_fila != null){
                            if(!current_fila.equals("null")){
                                filas.setSelection(getKeyByValue(dict_rows, dict_rows.get(Integer.valueOf(current_fila))));
                            }
                        }
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
    }
    private void getseats(String id){
        Log.d(TAG+"SEATS",id);
        final ArrayList <Zone> copy_array_seats = new ArrayList<>();
        final Map<Integer, String> copy_dict_seats = new LinkedHashMap<>();
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_URL_ASIENTOS + id + "/" , "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d(TAG+"detale",response);
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        final String[] data_array_seats = new String[results.length()];

                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            data_array_seats[count] = anEntry.optString("title");
                            copy_array_seats.add(new Zone(anEntry.optString("id"),anEntry.optString("title")));
                            copy_dict_seats.put(Integer.valueOf(anEntry.optString("id")), anEntry.optString("title"));
                        }
                        array_seats = copy_array_seats;
                        dict_seats = copy_dict_seats;
                        final ArrayAdapter<String> adapter_seats = new CustomizedSpinnerAdapter(
                                getActivity(), android.R.layout.simple_spinner_item, data_array_seats);
                        asientos.setAdapter(adapter_seats);
                        if(current_asiento != null){
                            if(!current_asiento.equals("null")){
                                asientos.setSelection(getKeyByValue(dict_seats, dict_seats.get(Integer.valueOf(current_asiento))));
                            }
                        }
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
    }



    public int getKeyByValue(Map<Integer, String> map, String value) {

        int position = 0;
        if (value!=null) {
            for (String map_value : map.values()) {
                Log.d(TAG, String.valueOf(value) + " " + map_value + " " + String.valueOf(map_value));
                if (value.equals(map_value)) {
                    return position;
                }
                position++;
            }
        }
        return -1;
    }
}
