package autodromo.punkmkt.com.ahrapp.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.helpers.TiendaDBHelper;
import autodromo.punkmkt.com.ahrapp.models.TiendaRestauranteItem;
import autodromo.punkmkt.com.ahrapp.utils.AuthRequest;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sebastianmendezgiron on 15/08/16.
 */

public class TiendaFragmentProducts extends Fragment{

    TableLayout tabla_informacion;
    NumberPicker numberPicker;
    private String AHR_TIENDA_PRODUCTOS_URL = "http://104.236.3.158:82/api/store/category_products/1/sellers/";
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    NetworkImageView mNetworkImageView;
    private ArrayList<TiendaRestauranteItem> productos = new ArrayList<TiendaRestauranteItem>();
    ImageButton imageButton;
    private TiendaDBHelper tiendaDBHelper;
    TextView verWishList;
    private ImageView imgdown,imgup;

    private static final String TAG = "WishList";
    private SlidingUpPanelLayout mLayout;
    TextView total_final, total_texto;

    /********************************************/
    TableLayout tabla_informacion_wishlist;
    private Context context;
    float x1,x2;
    float y1, y2;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tienda_lista_items_restaurante,container,false);
        view = v;
        Tracker tracker = ((MyVolleySingleton) getActivity().getApplication()).getTracker(MyVolleySingleton.TrackerName.APP_TRACKER);
        tracker.setScreenName(getString(R.string.tienda_titulo));
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        tabla_informacion = (TableLayout) view.findViewById(R.id.tabla_informacion);
        tabla_informacion_wishlist = (TableLayout) view.findViewById(R.id.tabla_info_wl);
        loadSellerProduts();
        tituloListaWishList();
        /*imgup = (ImageView) getActivity().findViewById(R.id.up);
        imgdown = (ImageView) getActivity().findViewById(R.id.donw);*/
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                tituloListaWishList();
                cargaWishListItems();
                /*imgdown.setVisibility(View.VISIBLE);
                imgup.setVisibility(View.GONE);*/
            }
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                tabla_informacion_wishlist.removeAllViewsInLayout();
                total_texto = (TextView) view.findViewById(R.id.total);
                total_texto.setText(null);
                total_final = (TextView) view.findViewById(R.id.sum_total);
                total_final.setText(null);
                /*imgdown.setVisibility(View.GONE);
                imgup.setVisibility(View.VISIBLE);*/
            }

        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        return v;

    }



    public void loadSellerProduts(){

        Bundle tiendaRId = getArguments();
        String rId = tiendaRId.getString("restauranteId");
        String pR = tiendaRId.getString("portadaRestaurante");

        mNetworkImageView = (NetworkImageView) view.findViewById(R.id.bg_seller);
        imageLoader = MyVolleySingleton.getInstance().getImageLoader();
        mNetworkImageView.setImageUrl(pR, imageLoader);
        //Toast.makeText(getActivity(), rId, Toast.LENGTH_LONG).show();

        AHR_TIENDA_PRODUCTOS_URL += rId + "/products/";

        if(NetworkUtils.haveNetworkConnection(getActivity())) {
            StringRequest request = new AuthRequest(getActivity().getApplicationContext(), Request.Method.GET, AHR_TIENDA_PRODUCTOS_URL, "UTF-8", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        productos.clear();
                        JSONArray object = new JSONArray(response);
                        for (int count = 0; count < object.length(); count++) {
                            JSONObject anEntry = object.getJSONObject(count);
                            TiendaRestauranteItem tiendaRestauranteItem = new TiendaRestauranteItem();
                            tiendaRestauranteItem.setId(Integer.parseInt(anEntry.optString("id")));
                            tiendaRestauranteItem.setNombre(anEntry.optString("name"));
                            tiendaRestauranteItem.setPrecio(Float.parseFloat(anEntry.optString("price")));
                            productos.add(tiendaRestauranteItem);
                        }
                        createContentRow(productos);
                        Log.d("productos", productos.toString());

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
    public void createContentRow(ArrayList<TiendaRestauranteItem> restauranteItems){

        tiendaDBHelper = new TiendaDBHelper(getActivity());
        //ShapeDrawable border = new ShapeDrawable(new RectShape());

        for(int count=0; count<restauranteItems.size();count++){
            /*border.getPaint().setStyle(Paint.Style.STROKE);
            border.getPaint().setColor(Color.BLACK);*/
            final TiendaRestauranteItem item = restauranteItems.get(count);
            final LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LAYOUT_INFLATER_SERVICE );
            final TableRow row_pos = (TableRow)inflater.inflate(R.layout.tienda_listaitems_retaurante_tablerow, null);
            ((TextView)row_pos.findViewById(R.id.item_id)).setText(item.getId().toString());
            ((TextView)row_pos.findViewById(R.id.nombre)).setText(item.getNombre());
            //((TextView)row_pos.findViewById(R.id.precio)).setVisibility(View.INVISIBLE);
            ((TextView)row_pos.findViewById(R.id.precio)).setText("$"+item.getPrecio().toString()+"MXN");
            imageButton = ((ImageButton)row_pos.findViewById(R.id.addToWishlis));
            imageButton.setId(item.getId());
            //(row_pos.setBackground(border);

            ((TextView)row_pos.findViewById(R.id.item_price)).setText(item.getPrecio().toString());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    //LayoutInflater inflater = getActivity().getLayoutInflater();
                    final View view = LayoutInflater.from(getActivity()).inflate(R.layout.tienda_agregar_awishlist, null);// g etActivity().getLayoutInflater();

                    builder.setView(view);

                    numberPicker = (NumberPicker) view.findViewById(R.id.item_cantidad); //builder.this.findViewById(R.id.item_cantidad));
                    numberPicker.setMinValue(1);
                    numberPicker.setMaxValue(10);
                    setDividerColor(numberPicker, getResources().getColor(R.color.white));
                    final AlertDialog alertDialog = builder.create();
                    Button agregar = (Button) view.findViewById(R.id.agregar_awl);
                    agregar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int item_id = Integer.parseInt(((TextView)row_pos.findViewById(R.id.item_id)).getText().toString());
                            float item_price = Float.parseFloat(((TextView)row_pos.findViewById(R.id.item_price)).getText().toString());
                            String item_name = ((TextView)row_pos.findViewById(R.id.nombre)).getText().toString();
                            int item_cant = numberPicker.getValue();

                            if(tiendaDBHelper.insertItem(item_name,item_cant,item_price)) {
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), item_name +" agregado a tu lista de deseos.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "Algo anda mal.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Button cancelar = (Button) view.findViewById(R.id.cancelar);

                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            });
            tabla_informacion.addView(row_pos);

        }
    }

    public void tituloListaWishList(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LAYOUT_INFLATER_SERVICE );


        TableRow row_title = (TableRow) inflater.inflate(R.layout.tienda_wishlist_header, null);
        tabla_informacion_wishlist.addView(row_title);

    }
    public void cargaWishListItems() {
        tiendaDBHelper = new TiendaDBHelper(getActivity());
        Cursor cursor = tiendaDBHelper.getAllItems();
        ShapeDrawable border = new ShapeDrawable(new RectShape());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                border.getPaint().setStyle(Paint.Style.STROKE);
                border.getPaint().setColor(Color.BLACK);
                final int item_id = cursor.getInt(cursor.getColumnIndex("id"));
                final String item_name = cursor.getString(cursor.getColumnIndex("name"));
                int item_qty = cursor.getInt(cursor.getColumnIndex("qty"));
                //String item_price = cursor.getFloat(cursor.getColumnIndex("price"));
                Float item_price = cursor.getFloat(cursor.getColumnIndex("price"));
                String final_qty = Integer.toString(item_qty);
                String final_price = Float.toString(item_price);
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().LAYOUT_INFLATER_SERVICE);
                final TableRow row_pos = (TableRow) inflater.inflate(R.layout.tienda_wishlist_item_row, null);
                ((TextView) row_pos.findViewById(R.id.nombre)).setText(item_name);
                ((TextView) row_pos.findViewById(R.id.cantidad)).setText(final_qty);
                ((TextView) row_pos.findViewById(R.id.precio)).setText("$"+final_price);
                row_pos.setBackground(border);
                tabla_informacion_wishlist.addView(row_pos);

                ImageButton eliminar = (ImageButton) row_pos.findViewById(R.id.eliminar);
                eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.tienda_eliminar_dewl, null);

                        TextView deseasEliminar = (TextView) view.findViewById(R.id.eliminar_item);
                        String deseasEliminarItem = "¿Deseas eliminar " + item_name.toUpperCase() + " de tu WishList?";
                        deseasEliminar.setText(deseasEliminarItem);
                        builder.setView(view);
                        final AlertDialog alertDialog = builder.create();

                        Button eliminar = (Button) view.findViewById(R.id.eliminar);
                        eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tiendaDBHelper.deleteItem(item_id);
                                tabla_informacion_wishlist.removeView(row_pos);
                                Double item_total = tiendaDBHelper.getTotal();
                                TextView total_final = (TextView) getActivity().findViewById(R.id.sum_total);
                                total_final.setText("$"+String.valueOf(item_total)+" MXN");
                                alertDialog.dismiss();
                                Toast.makeText(getActivity(), getResources().getString(R.string.prod_eliminado), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Button cancelar = (Button) view.findViewById(R.id.cancelar);

                        cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.show();

                    }
                });
                total_texto = (TextView) view.findViewById(R.id.total);
                total_texto.setText(getResources().getString(R.string.wishlist_total));
                Double item_total = tiendaDBHelper.getTotal();
                total_final = (TextView) view.findViewById(R.id.sum_total);
                total_final.setText("$"+String.valueOf(item_total)+" MXN");

            }
        }
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }



    //@Override
    //public void onBackPressed() {
}