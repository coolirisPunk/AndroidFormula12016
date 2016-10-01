package autodromo.punkmkt.com.ahrapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.adapters.CustomizedSpinnerAdapter;
import autodromo.punkmkt.com.ahrapp.models.Zone;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by DaniPunk on 25/07/16.
 */
    public class UbicaAsientoDialogFragment extends DialogFragment {
    private Spinner zonas,gradas,seccion,filas, asientos;
    String TAG = UbicaAsientoDialogFragment.class.getName();
    String TAG_REQUESTS = "ASIENTOS";
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
    public UbicaAsientoDialogFragment() {
        // Empty constructor required for DialogFragment
    }
    public interface UbicaAsientoDialogListener {
        void onFinishEditDialog(String zonas,String gradas,String seccion,String filas, String asientos);
    }
    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        UbicaAsientoDialogListener listener = (UbicaAsientoDialogListener) getTargetFragment();
        listener.onFinishEditDialog(current_zone,current_grandstand, current_section
                ,current_fila,current_asiento);
        dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ubica_asiento, container);
        //getDialog().setTitle("Hello");
        Button cancelar = (Button) view.findViewById(R.id.cancel_action);
        Button encontrar = (Button) view.findViewById(R.id.encontrar_action);

        cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        encontrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendBackResult();
            }
        });
        zonas = (Spinner) view.findViewById(R.id.zonas_spinner);
        gradas = (Spinner) view.findViewById(R.id.gradas_spinner);
        seccion = (Spinner) view.findViewById(R.id.seccion_spinner);
        filas = (Spinner) view.findViewById(R.id.filas_spinner);
        asientos = (Spinner) view.findViewById(R.id.asientos_spinner);

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

        zonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(((TextView) parentView.getChildAt(0))!=null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                }
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
                if(((TextView) parentView.getChildAt(0))!=null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                }

                current_grandstand= array_grandstands.get(position).getId();
                getsections(current_grandstand);

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
                }

                current_section = array_sections.get(position).getId();
                getrows(current_section);
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
                }

                    current_fila = array_rows.get(position).getId();
                    getseats(current_fila);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(((TextView) parentView.getChildAt(0))!=null){
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                }

                current_asiento = array_seats.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        return view;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
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

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onpause");
        MyVolleySingleton.getInstance().cancelPendingRequests(TAG_REQUESTS);

    }
    @Override
    public void onDestroy() {
        super.onPause();
        Log.d(TAG,"ondestroy");
        MyVolleySingleton.getInstance().cancelPendingRequests(TAG_REQUESTS);

    }

}
