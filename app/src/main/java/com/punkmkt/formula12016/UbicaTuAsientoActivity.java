package com.punkmkt.formula12016;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.AccessToken;
import com.punkmkt.formula12016.adapters.CustomizedSpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UbicaTuAsientoActivity extends AppCompatActivity {
    String TAG = UbicaTuAsientoActivity.class.getName();
    private Spinner zonas,gradas,seccion,filas, asientos;
    private String AHR_USERPROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user/";
    private String AHR_CREATEPROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/users/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubica_tu_asiento);
        Button saltar = (Button) findViewById(R.id.saltar);
        Button guardar_cambios = (Button) findViewById(R.id.guardar_cambios);
        final String Ukey = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("access_token",null);
        if (Ukey!=null){
            Toast.makeText(getApplicationContext(), Ukey, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Sin token", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        zonas = (Spinner) findViewById(R.id.zonas_spinner);
        gradas = (Spinner) findViewById(R.id.gradas_spinner);
        seccion = (Spinner) findViewById(R.id.seccion_spinner);
        filas = (Spinner) findViewById(R.id.filas_spinner);
        asientos = (Spinner) findViewById(R.id.asientos_spinner);

        final String[] data_array_zonas = getResources().getStringArray(R.array.zonas_array);

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
                this, android.R.layout.simple_spinner_item, data_array_zonas);

        final ArrayAdapter<String> adapter_gradas = new CustomizedSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, data_gradas);

        final ArrayAdapter<String> adapter_secciones = new CustomizedSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, data_gradas);

        final ArrayAdapter<String> adapter_filas = new CustomizedSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, data_filas);

        final ArrayAdapter<String> adapter_asientos = new CustomizedSpinnerAdapter(
                this, android.R.layout.simple_spinner_item, data_asientos);

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
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        filas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        saltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goHome);

            }
        });

        guardar_cambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject js = new JSONObject();

                try {
                    js.put("zone", zonas.getSelectedItem().toString());
                    js.put("grada", gradas.getSelectedItem().toString());
                    js.put("section", seccion.getSelectedItem().toString());
                    js.put("fila", filas.getSelectedItem().toString());
                    js.put("seat", asientos.getSelectedItem().toString());
                    js.put("speed_lover", "speed_lover_1");
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
                                    Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("response", object.toString());
                                    if (object.has("speed_lover") ){
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("userid", object.optString("userid")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("zone", object.optString("zone")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("grada", object.optString("grada")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("section", object.optString("section")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("fila", object.optString("fila")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("seat", object.optString("seat")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("seat", object.optString("seat")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("speed_lover", object.optString("speed_lover")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("username", object.optString("speed_lover")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("email", object.optString("email")).commit();

                                        Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                        startActivity(myIntent);
                                    }
                                    else {
                                        Log.d("response","No object created");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //alert con error
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("V", "Error: " + error.getMessage());
                        //alert con error
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
        });

    }
}