package com.punkmkt.formula12016;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.punkmkt.formula12016.models.Posicion;
import com.punkmkt.formula12016.utils.AuthRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RankingGeneralActivity extends AppCompatActivity {

    private String AHR_RANKING_GENERAL = "http://104.236.3.158:82/api/premio/ranking_general/";
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    RelativeLayout MyrLayout;
    TableLayout tabla_resultados;
    String TAG  = RankingGeneralActivity.class.getName();
    private ArrayList<Posicion> posiciones_ranking_general = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ranking_general);
            tabla_resultados = (TableLayout) findViewById(R.id.tabla_resultados);

            StringRequest request = new AuthRequest(getApplicationContext(), Request.Method.GET, AHR_RANKING_GENERAL, "utf-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray object = new JSONArray(response);
                        for (int count = 0; count < object.length(); count++) {
                            JSONObject anEntry = object.getJSONObject(count);
                            Posicion posicion = new Posicion();
                            posicion.setPiloto_sobrenombre(anEntry.optString("driver"));
                            posicion.setEscuderia(anEntry.optString("team"));
                            posicion.setPuntos(anEntry.optString("points"));
                            posicion.setEscuderia_img(anEntry.optString("picture_team"));
                            posiciones_ranking_general.add(posicion);
                        }
                        iniciarranking();

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

    public void iniciarranking() {
        ArrayList<Posicion> copia;
        copia = posiciones_ranking_general;
        tabla_resultados.removeAllViews();
        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.encabezado_ranking_general, null);
        tabla_resultados.addView(row);

        for (int count = 0; count < copia.size(); count++) {
            Posicion posicion = copia.get(count);
            Log.d(TAG,posicion.getEscuderia());
            Log.d(TAG,posicion.getEscuderia_img());
            TableRow row_pos = (TableRow) getLayoutInflater().inflate(R.layout.row_ranking_general, null);

            if (((count+1) % 2) == 0) {
                row_pos.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey_color_low));

            }
            ((TextView) row_pos.findViewById(R.id.pos)).setText(Integer.toString(count + 1));
            ((TextView) row_pos.findViewById(R.id.piloto)).setText(posicion.getPiloto_sobrenombre());
            ((NetworkImageView) row_pos.findViewById(R.id.escuderia_img)).setImageUrl(posicion.getEscuderia_img(), imageLoader);
            ((TextView) row_pos.findViewById(R.id.escuderia)).setText(posicion.getEscuderia());
            ((TextView) row_pos.findViewById(R.id.puntos)).setText(posicion.getPuntos());
            tabla_resultados.addView(row_pos);

        }
    }

}