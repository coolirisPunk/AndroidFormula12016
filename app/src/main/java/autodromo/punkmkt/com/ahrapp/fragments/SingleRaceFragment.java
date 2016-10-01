package autodromo.punkmkt.com.ahrapp.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.models.Etapa;
import autodromo.punkmkt.com.ahrapp.models.Posicion;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by germanpunk on 17/08/16.
 */
public class SingleRaceFragment extends Fragment {
    private String AHR_PREMIOS_JSON_API_URL = "http://104.236.3.158:82/api/premio/premios/";
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    TextView nombre;
    NetworkImageView imagen;
    RelativeLayout MyrLayout;
    TableLayout tabla_resultados;
    private ArrayList<Etapa> etapas = new ArrayList<>();

    private ArrayList<Posicion> posiciones_p1 = new ArrayList<>();
    private ArrayList<Posicion> posiciones_p2 = new ArrayList<>();
    private ArrayList<Posicion> posiciones_p3 = new ArrayList<>();
    private ArrayList<Posicion> posiciones_clasificatoria = new ArrayList<>();
    private ArrayList<Posicion> posiciones_carrera = new ArrayList<>();

    Button p1;
    Button p2;
    Button p3;
    Button clasificatoria;
    Button carrera;
    StringRequest request;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_single_premio, container, false);
        imagen = (NetworkImageView) v.findViewById(R.id.bandera_premio);
        nombre = (TextView) v.findViewById(R.id.nombre_premio);
        MyrLayout = (RelativeLayout) v.findViewById(R.id.container);
        tabla_resultados = (TableLayout) v.findViewById(R.id.tabla_resultados);
        p1 = (Button) v.findViewById(R.id.p1);
        p2 = (Button) v.findViewById(R.id.p2);
        p3 = (Button) v.findViewById(R.id.p3);
        clasificatoria = (Button) v.findViewById(R.id.clasificacion);
        carrera = (Button) v.findViewById(R.id.carrera);
        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        AHR_PREMIOS_JSON_API_URL = AHR_PREMIOS_JSON_API_URL + id + "/";
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_PREMIOS_JSON_API_URL, "utf-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject object = new JSONObject(response);
                        nombre.setText(object.optString("name"));
                        imagen.setImageUrl(object.optString("flag"), imageLoader);
                        JSONArray etapa_set = object.getJSONArray("phase_set");

                        for (int count = 0; count < etapa_set.length(); count++) {
                            JSONObject anEntry = etapa_set.getJSONObject(count);
                            Etapa etapa = new Etapa();
                            etapa.setId(Integer.parseInt(anEntry.optString("id")));
                            etapa.setNombre(anEntry.optString("name"));
                            etapa.setTipo(anEntry.optString("phase_type"));
                            etapas.add(etapa);
                            JSONArray posicion_set = anEntry.getJSONArray("position_set");
                            ArrayList<Posicion> array_posiciones = new ArrayList<>();
                            for (int count2 = 0; count2 < posicion_set.length(); count2++) {
                                JSONObject anSecondEntry = posicion_set.getJSONObject(count2);
                                //Log.d("volley",anSecondEntry.toString());
                                Posicion posicion = new Posicion();
                                posicion.setId(Integer.parseInt(anSecondEntry.optString("id")));
                                posicion.setPosicion(Integer.parseInt(anSecondEntry.optString("number")));
                                if (anSecondEntry.has("time") && !anSecondEntry.optString("time").equals("null")) {
                                    posicion.setTiempo(anSecondEntry.optString("time"));
                                }
                                if (anSecondEntry.has("gap") && !anSecondEntry.optString("gap").equals("null")) {
                                    posicion.setGap(anSecondEntry.optString("gap"));
                                }
                                if (anSecondEntry.has("laps") && !anSecondEntry.optString("laps").equals("null")) {
                                    posicion.setLaps(anSecondEntry.optString("laps"));
                                }
                                if (anSecondEntry.has("q1") && !anSecondEntry.optString("q1").equals("null")) {
                                    posicion.setQ1(anSecondEntry.optString("q1"));
                                }
                                if (anSecondEntry.has("q2") && !anSecondEntry.optString("q2").equals("null")) {
                                    posicion.setQ2(anSecondEntry.optString("q2"));
                                }
                                if (anSecondEntry.has("q3") && !anSecondEntry.optString("q3").equals("null")) {
                                    posicion.setQ3(anSecondEntry.optString("q3"));
                                }
                                if (anSecondEntry.has("points") && !anSecondEntry.optString("points").equals("null")) {
                                    posicion.setPuntos(anSecondEntry.optString("points"));
                                }
                                posicion.setPiloto_sobrenombre(anSecondEntry.optString("driver"));
                                posicion.setEscuderia(anSecondEntry.optString("team"));
                                array_posiciones.add(posicion);
                            }
                            if (etapa.getNombre().equals("P1")) {
                                posiciones_p1 = array_posiciones;
                            } else if (etapa.getNombre().equals("P2")) {
                                posiciones_p2 = array_posiciones;
                            } else if (etapa.getNombre().equals("P3")) {
                                posiciones_p3 = array_posiciones;
                            } else if (etapa.getNombre().equals("Q")) {
                                posiciones_clasificatoria = array_posiciones;
                            } else if (etapa.getNombre().equals("R")) {
                                posiciones_carrera = array_posiciones;
                            }
                        }
                        ButtonPressed(R.id.p1);
                        iniciarpractica("practica1");

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
        }
        else{
            Cache mCache = MyVolleySingleton.getInstance().getRequestQueue().getCache();
            Cache.Entry mEntry = mCache.get(AHR_PREMIOS_JSON_API_URL);
            if (mEntry != null) {
                try {
                    String cacheData = new String(mEntry.data, "UTF-8");
                    JSONObject object = new JSONObject(cacheData);
                    nombre.setText(object.optString("name"));
                    imagen.setImageUrl(object.optString("flag"), imageLoader);
                    JSONArray etapa_set = object.getJSONArray("phase_set");
                    for (int count = 0; count < etapa_set.length(); count++) {
                        JSONObject anEntry = etapa_set.getJSONObject(count);
                        Etapa etapa = new Etapa();
                        etapa.setId(Integer.parseInt(anEntry.optString("id")));
                        etapa.setNombre(anEntry.optString("name"));
                        etapa.setTipo(anEntry.optString("phase_type"));
                        etapas.add(etapa);
                        JSONArray posicion_set = anEntry.getJSONArray("position_set");
                        ArrayList<Posicion> array_posiciones = new ArrayList<>();
                        for (int count2 = 0; count2 < posicion_set.length(); count2++) {
                            JSONObject anSecondEntry = posicion_set.getJSONObject(count2);
                            Posicion posicion = new Posicion();
                            posicion.setId(Integer.parseInt(anSecondEntry.optString("id")));
                            posicion.setPosicion(Integer.parseInt(anSecondEntry.optString("number")));
                            if (anSecondEntry.has("time") && !anSecondEntry.optString("time").equals("null")) {
                                posicion.setTiempo(anSecondEntry.optString("time"));
                            }
                            if (anSecondEntry.has("gap") && !anSecondEntry.optString("gap").equals("null")) {
                                posicion.setGap(anSecondEntry.optString("gap"));
                            }
                            if (anSecondEntry.has("laps") && !anSecondEntry.optString("laps").equals("null")) {
                                posicion.setLaps(anSecondEntry.optString("laps"));
                            }
                            if (anSecondEntry.has("q1") && !anSecondEntry.optString("q1").equals("null")) {
                                posicion.setQ1(anSecondEntry.optString("q1"));
                            }
                            if (anSecondEntry.has("q2") && !anSecondEntry.optString("q2").equals("null")) {
                                posicion.setQ2(anSecondEntry.optString("q2"));
                            }
                            if (anSecondEntry.has("q3") && !anSecondEntry.optString("q3").equals("null")) {
                                posicion.setQ3(anSecondEntry.optString("q3"));
                            }
                            if (anSecondEntry.has("points") && !anSecondEntry.optString("points").equals("null")) {
                                posicion.setPuntos(anSecondEntry.optString("points"));
                            }
                            posicion.setPiloto_sobrenombre(anSecondEntry.optString("driver"));
                            posicion.setEscuderia(anSecondEntry.optString("team"));
                            array_posiciones.add(posicion);
                        }
                        if (etapa.getNombre().equals("P1")) {
                            posiciones_p1 = array_posiciones;
                        } else if (etapa.getNombre().equals("P2")) {
                            posiciones_p2 = array_posiciones;
                        } else if (etapa.getNombre().equals("P3")) {
                            posiciones_p3 = array_posiciones;
                        } else if (etapa.getNombre().equals("Q")) {
                            posiciones_clasificatoria = array_posiciones;
                        } else if (etapa.getNombre().equals("R")) {
                            posiciones_carrera = array_posiciones;
                        }
                    }
                    ButtonPressed(R.id.p1);
                    iniciarpractica("practica1");
                } catch (UnsupportedEncodingException |JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

            }
        }

        p1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonPressed(R.id.p1);
                iniciarpractica("practica1");

            }
        });
        p2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonPressed(R.id.p2);
                iniciarpractica("practica2");
            }
        });
        p3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonPressed(R.id.p3);
                iniciarpractica("practica3");
            }
        });
        clasificatoria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonPressed(R.id.clasificacion);
                iniciarclasificacion();
            }
        });
        carrera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ButtonPressed(R.id.carrera);
                iniciarcarrera();
            }
        });
        return v;
    }
    public void iniciarpractica(String practica) {
        ArrayList<Posicion> copia = new ArrayList<>();
        if (practica.equals("practica1")) {
            copia = posiciones_p1;
        } else if (practica.equals("practica2")) {
            copia = posiciones_p2;
        } else if (practica.equals("practica3")) {
            copia = posiciones_p3;
        }
        tabla_resultados.removeAllViews();
        TableRow row = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.encabezado_practica, null);
        tabla_resultados.addView(row);
        for (int count = 0; count < copia.size(); count++) {
            Posicion posicion = copia.get(count);
            TableRow row_pos = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.row_practica, null);
            if (((count+1) % 2) == 0) {
                row_pos.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.grey_color_low));

            }
            ((TextView) row_pos.findViewById(R.id.pos)).setText(Integer.toString(posicion.getPosicion()));
            ((TextView) row_pos.findViewById(R.id.piloto)).setText(posicion.getPiloto_sobrenombre());
            ((NetworkImageView) row_pos.findViewById(R.id.escuderia)).setImageUrl(posicion.getEscuderia(), imageLoader);
            ((TextView) row_pos.findViewById(R.id.tiempo)).setText(posicion.getTiempo());
            ((TextView) row_pos.findViewById(R.id.laps)).setText(posicion.getLaps());
            tabla_resultados.addView(row_pos);
        }
    }

    public void iniciarclasificacion() {
        ArrayList<Posicion> copia;
        copia = posiciones_clasificatoria;
        tabla_resultados.removeAllViews();
        TableRow row = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.encabezado_clasificacion, null);
        tabla_resultados.addView(row);
        for (int count = 0; count < copia.size(); count++) {
            Posicion posicion = copia.get(count);
            TableRow row_pos = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.row_clasificacion, null);
            if (((count+1) % 2) == 0) {
                row_pos.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.grey_color_low));

            }
            ((TextView) row_pos.findViewById(R.id.pos)).setText(Integer.toString(posicion.getPosicion()));
            ((TextView) row_pos.findViewById(R.id.piloto)).setText(posicion.getPiloto_sobrenombre());
            ((NetworkImageView) row_pos.findViewById(R.id.escuderia)).setImageUrl(posicion.getEscuderia(), imageLoader);
            ((TextView) row_pos.findViewById(R.id.textview_q1)).setText(posicion.getQ1());
            ((TextView) row_pos.findViewById(R.id.textview_q2)).setText(posicion.getQ2());
            ((TextView) row_pos.findViewById(R.id.textview_q3)).setText(posicion.getQ3());
            tabla_resultados.addView(row_pos);
        }
    }

    public void iniciarcarrera() {
        ArrayList<Posicion> copia;
        copia = posiciones_carrera;
        tabla_resultados.removeAllViews();
        TableRow row = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.encabezado_carrera, null);
        tabla_resultados.addView(row);
        for (int count = 0; count < copia.size(); count++) {
            Posicion posicion = copia.get(count);
            TableRow row_pos = (TableRow) getActivity().getLayoutInflater().inflate(R.layout.row_carrera, null);
            if (((count+1) % 2) == 0) {
                row_pos.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.grey_color_low));

            }
            ((TextView) row_pos.findViewById(R.id.pos)).setText(Integer.toString(posicion.getPosicion()));
            ((TextView) row_pos.findViewById(R.id.piloto)).setText(posicion.getPiloto_sobrenombre());
            ((NetworkImageView) row_pos.findViewById(R.id.escuderia)).setImageUrl(posicion.getEscuderia(), imageLoader);
            ((TextView) row_pos.findViewById(R.id.tiempo)).setText(posicion.getTiempo());
            ((TextView) row_pos.findViewById(R.id.puntos)).setText(posicion.getPuntos());
            tabla_resultados.addView(row_pos);
        }
    }
    public void ButtonPressed(int id) {
        Activity activity = getActivity();
        if(activity != null && isAdded()){
            Drawable dr = getResources().getDrawable(R.drawable.button_pressed);
            dr.setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_ATOP);
        }

        p1.setBackgroundResource(0);
        p2.setBackgroundResource(0);
        p3.setBackgroundResource(0);
        clasificatoria.setBackgroundResource(0);
        carrera.setBackgroundResource(0);
        switch (id) {
            case R.id.p1:
                p1.setBackgroundResource(R.drawable.button_pressed);
                break;

            case R.id.p2:
                p2.setBackgroundResource(R.drawable.button_pressed);
                break;
            case R.id.p3:
                p3.setBackgroundResource(R.drawable.button_pressed);
                break;
            case R.id.clasificacion:
                clasificatoria.setBackgroundResource(R.drawable.button_pressed);
                break;
            case R.id.carrera:
                carrera.setBackgroundResource(R.drawable.button_pressed);
                break;
            default:
                break;
        }
    }

}
