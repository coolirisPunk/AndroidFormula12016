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
import com.punkmkt.formula12016.LoginActivity;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.adapters.CustomizedSpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by germanpunk on 30/08/16.
 */
public class ConfiguracionFragment extends Fragment {
    private Spinner zonas,gradas,seccion,filas, asientos;
    String TAG = ConfiguracionFragment.class.getName();
    String AHR_UPDATEPROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/user/";
    String AHR_CREATEPROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/user/";
    String Ukey,zone,grada,section,fila,seat,speed_lover,profile_id;
    ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
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

        zone = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("zone",null);
        grada = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("grada",null);
        section = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("section",null);
        fila = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("fila",null);
        seat = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("seat",null);
        speed_lover = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("speed_lover",null);
        profile_id = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("profile_id",null);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);
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

        final String[] data_gradas = new String[16];
        final String[] data_seccion = new String[16];
        final String[] data_filas = new String[16];
        final String[] data_asientos = new String[16];

        for (int i = 0; i<16;i++){
            data_gradas[i] = Integer.toString(i+1);
            data_seccion[i] = Integer.toString(i+1);
            data_filas[i] = Integer.toString(i+1);
            data_asientos[i] = Integer.toString(i+1);
        }

        final ArrayAdapter<String> adapter_zonas = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_array_zonas);

        final ArrayAdapter<String> adapter_gradas = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_gradas);

        final ArrayAdapter<String> adapter_secciones = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_gradas);

        final ArrayAdapter<String> adapter_filas = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_filas);

        final ArrayAdapter<String> adapter_asientos = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_asientos);

        zonas.setAdapter(adapter_zonas);
        gradas.setAdapter(adapter_gradas);
        seccion.setAdapter(adapter_secciones);
        filas.setAdapter(adapter_filas);
        asientos.setAdapter(adapter_asientos);

        zonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                switch (position){
                    case 0:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLACK);
                        break;
                    case 1:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.RED);
                        break;
                    case 2:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLUE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        gradas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        filas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
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
}
