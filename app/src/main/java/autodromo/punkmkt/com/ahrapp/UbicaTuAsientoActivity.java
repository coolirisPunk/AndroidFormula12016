package autodromo.punkmkt.com.ahrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.adapters.CustomizedSpinnerAdapter;
import autodromo.punkmkt.com.ahrapp.models.Zone;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UbicaTuAsientoActivity extends AppCompatActivity {
    String TAG = UbicaTuAsientoActivity.class.getName();
    private Spinner zonas,gradas,seccion,filas, asientos;
    private CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDialog;
    private String AHR_CREATEPROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/user/";
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

    String current_zone, current_grandstand, current_section, current_fila, current_asiento;

    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubica_tu_asiento);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        activity = this;
        Button saltar = (Button) findViewById(R.id.saltar);
        Button guardar_cambios = (Button) findViewById(R.id.guardar_cambios);
        final String Ukey = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("access_token",null);
        if (Ukey!=null){
            Log.d(TAG,Ukey);
        }
        else{
            Toast.makeText(getApplicationContext(), "Sin token", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(intent);
        }
        zonas = (Spinner) findViewById(R.id.zonas_spinner);
        gradas = (Spinner) findViewById(R.id.gradas_spinner);
        seccion = (Spinner) findViewById(R.id.seccion_spinner);
        filas = (Spinner) findViewById(R.id.filas_spinner);
        asientos = (Spinner) findViewById(R.id.asientos_spinner);


//        final String[] data_gradas = new String[16];
        //final String[] data_seccion = new String[16];
       // final String[] data_filas = new String[16];
      //  final String[] data_asientos = new String[16];

      //  for (int i = 0; i<16;i++){
           // data_gradas[i] = Integer.toString(i+1);
            //data_seccion[i] = Integer.toString(i+1);
         //  data_filas[i] = Integer.toString(i+1);
          //  data_asientos[i] = Integer.toString(i+1);
      //  }


        //final ArrayAdapter<String> adapter_gradas = new CustomizedSpinnerAdapter(
        //        this, android.R.layout.simple_spinner_item, data_gradas);

        //final ArrayAdapter<String> adapter_secciones = new CustomizedSpinnerAdapter(
                //this, android.R.layout.simple_spinner_item, data_seccion);

       // final ArrayAdapter<String> adapter_filas = new CustomizedSpinnerAdapter(
       //         this, android.R.layout.simple_spinner_item, data_filas);

        //final ArrayAdapter<String> adapter_asientos = new CustomizedSpinnerAdapter(
        //        this, android.R.layout.simple_spinner_item, data_asientos);

        getzones();
        //gradas.setAdapter(adapter_gradas);
       // seccion.setAdapter(adapter_secciones);
       // filas.setAdapter(adapter_filas);
       // asientos.setAdapter(adapter_asientos);

        zonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(((TextView) parentView.getChildAt(0))!=null) {
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        gradas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(((TextView) parentView.getChildAt(0))!=null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    current_grandstand = array_grandstands.get(position).getId();
                    getsections(current_grandstand);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(((TextView) parentView.getChildAt(0))!=null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                    current_section = array_sections.get(position).getId();
                    getrows(current_section);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        filas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(((TextView) parentView.getChildAt(0))!=null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    current_fila = array_rows.get(position).getId();
                    getseats(current_fila);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(((TextView) parentView.getChildAt(0))!=null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    current_asiento = array_seats.get(position).getId();
                }
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

                progressDialog = new ProgressDialog(UbicaTuAsientoActivity.this);
                progressDialog.setMessage("Guardando...");
                progressDialog.show();
                JSONObject js = new JSONObject();

                try {
                    js.put("zone", current_zone);
                    js.put("grada", current_grandstand);
                    js.put("section", current_section);
                    js.put("fila", current_fila);
                    js.put("seat", current_asiento);
                    //js.put("speed_lover", "true_racers");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG,Ukey);
                Log.d(TAG,js.toString());
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,AHR_CREATEPROFILE_JSON_API_URL,js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject object = response;

                                    Log.d("response", object.toString());
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("userid", object.optString("userid")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("zone", object.optString("zone")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("grada", object.optString("grada")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("section", object.optString("section")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("fila", object.optString("fila")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("seat", object.optString("seat")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("speed_lover", object.optString("speed_lover")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("username", object.optString("username")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("email", object.optString("email")).commit();
                                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("profile_id", object.optString("id")).commit();
                                        progressDialog.dismiss();
                                        Snackbar snackbar = Snackbar
                                                .make(coordinatorLayout,"Perfil guardado.", Snackbar.LENGTH_SHORT);
                                        snackbar.setActionTextColor(Color.WHITE);
                                        snackbar.show();
                                        Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                       startActivity(myIntent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //alert con error
                                    progressDialog.dismiss();
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout,e.getMessage(), Snackbar.LENGTH_SHORT);
                                    snackbar.setActionTextColor(Color.RED);
                                    snackbar.show();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        VolleyLog.d("V", "Error: " + error.getMessage());
                        String json = null;
                        NetworkResponse response = error.networkResponse;

                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    json = new String(response.data);
                                    try{
                                        JSONObject obj = new JSONObject(json);
                                        Log.d(TAG,obj.toString());
                                        if(obj.has("detail")){
                                            if(obj.optString("detail")!=null){
                                                Toast.makeText(getApplicationContext(),obj.optString("detail"),Toast.LENGTH_SHORT).show();

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
                                    } catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                    break;
                                default:

                                    break;
                            }
                            //Additional cases
                        }
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

    private void getzones(){
        Log.d(TAG+"GRANDSTANDS","ZONES");
        if (NetworkUtils.haveNetworkConnection(getApplicationContext())) {
            StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_ZONAS, "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        final String[] data_array_zonas = new String[results.length()];
                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            data_array_zonas[count] = anEntry.optString("title");
                            array_zones.add(new Zone(anEntry.optString("id"),anEntry.optString("title")));
                        }
                        final ArrayAdapter<String> adapter_zonas = new CustomizedSpinnerAdapter(
                                activity, android.R.layout.simple_spinner_item, data_array_zonas);
                        zonas.setAdapter(adapter_zonas);

                        //
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
        final ArrayList <Zone> copy_array_grandstands = new ArrayList<>();
        if (NetworkUtils.haveNetworkConnection(getApplicationContext())) {

            StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_GRADAS + id + "/" , "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray results = object.getJSONArray("results");
                        final String[] data_array_grandstands = new String[results.length()];

                        for (int count = 0; count < results.length(); count++) {
                            JSONObject anEntry = results.getJSONObject(count);
                            data_array_grandstands[count] = anEntry.optString("title");
                            copy_array_grandstands.add(new Zone(anEntry.optString("id"),anEntry.optString("title")));
                        }
                        array_grandstands = copy_array_grandstands;
                        final ArrayAdapter<String> adapter_grandstands = new CustomizedSpinnerAdapter(
                                activity, android.R.layout.simple_spinner_item, data_array_grandstands);
                        gradas.setAdapter(adapter_grandstands);


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
        if (NetworkUtils.haveNetworkConnection(getApplicationContext())) {

            StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_SECCIONES + id + "/" , "UTF-8", new Response.Listener<String>() {
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
                        }
                        array_sections = copy_array_sections;
                        final ArrayAdapter<String> adapter_sections = new CustomizedSpinnerAdapter(
                                activity, android.R.layout.simple_spinner_item, data_array_sections);
                        seccion.setAdapter(adapter_sections);


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
        if (NetworkUtils.haveNetworkConnection(getApplicationContext())) {

            StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_FILAS + id + "/" , "UTF-8", new Response.Listener<String>() {
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
                        }
                        array_rows = copy_array_rows;
                        final ArrayAdapter<String> adapter_rows = new CustomizedSpinnerAdapter(
                                activity, android.R.layout.simple_spinner_item, data_array_rows);
                        filas.setAdapter(adapter_rows);

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
        if (NetworkUtils.haveNetworkConnection(getApplicationContext())) {
            StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_URL_ASIENTOS + id + "/" , "UTF-8", new Response.Listener<String>() {
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
                        }
                        array_seats = copy_array_seats;
                        final ArrayAdapter<String> adapter_seats = new CustomizedSpinnerAdapter(
                                activity, android.R.layout.simple_spinner_item, data_array_seats);
                        asientos.setAdapter(adapter_seats);
//                        asientos.setSelection();

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
}