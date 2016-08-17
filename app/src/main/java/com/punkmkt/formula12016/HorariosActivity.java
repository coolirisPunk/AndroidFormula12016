package com.punkmkt.formula12016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.models.Notificacion;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HorariosActivity extends AppCompatActivity {
    TableLayout tabla_informacion;
    private String AHR_HORARIOS_JSON_API_URL = "http://104.236.3.158:82/api/premio/horarios/";
    private int currentIndex;
    ArrayList<Notificacion> notificaciones = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);
        tabla_informacion = (TableLayout) findViewById(R.id.tabla_informacion);


        StringRequest request = new AuthRequest(getApplicationContext(),Request.Method.GET, AHR_HORARIOS_JSON_API_URL, "utf-8", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray results = object.getJSONArray("results");
                    int count_events = 0;
                    for (int count = 0; count < results.length(); count++) {
                        JSONObject anEntry = results.getJSONObject(count);
                        CreateTitleRow(anEntry.optString("description"));

                        JSONArray event_types = anEntry.getJSONArray("event_types");
                        for(int count2 = 0; count2 < event_types.length();count2++){
                            JSONObject anSecondEntry = event_types.getJSONObject(count2);
                            CreateTitleTipoDiaCarrera(anSecondEntry.optString("description"));

                            JSONArray events = anSecondEntry.getJSONArray("events");
                            for (int count3 = 0; count3 < events.length(); count3++) {
                                JSONObject anThirdEntry = events.getJSONObject(count3);
                                Notificacion notificacion = new Notificacion(anThirdEntry.optString("id"),anThirdEntry.optString("description"),"disable");
                                notificaciones.add(notificacion);
                                CreateContentRow(anThirdEntry.optString("description"),anThirdEntry.optString("start_time"),count_events);
                                count_events++;
                            }

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "Error during request");
                error.printStackTrace();
            }
        });
        MyVolleySingleton.getInstance().addToRequestQueue(request);

    }
    public void CreateTitleRow(String description){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE );
        TableRow row_title = (TableRow) inflater.inflate(R.layout.row_event_day, null);
        ((TextView)row_title.findViewById(R.id.dia)).setText(description);
        tabla_informacion.addView(row_title);
    }

    public void CreateTitleTipoDiaCarrera(String title){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE );
        TableRow row_title = (TableRow) inflater.inflate(R.layout.row_event_type, null);
        ((TextView)row_title.findViewById(R.id.event_type)).setText(title);
        tabla_informacion.addView(row_title);
    }

    public void CreateContentRow(String description, String start_time, final int index){
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE );
            TableRow row_pos = (TableRow)inflater.inflate(R.layout.row_event, null);
            ((TextView)row_pos.findViewById(R.id.description)).setText(description);
            ((TextView)row_pos.findViewById(R.id.event_start)).setText(start_time);
            ImageView addImageView = (ImageView)row_pos.findViewById(R.id.notification);
            addImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex = index;
                    Log.d("Notificacion",notificaciones.get(currentIndex).getName().toString());
                }
            });

            tabla_informacion.addView(row_pos);
    }

}
