package com.punkmkt.formula12016.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.punkmkt.formula12016.MainActivity;
import com.punkmkt.formula12016.Manifest;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.fragments.MapDetailFragment;
import com.punkmkt.formula12016.models.Hotel;

import java.util.List;

/**
 * Created by germanpunk on 30/08/16.
 */
public class HotelesAdapter extends RecyclerView.Adapter<HotelesAdapter.HospedajeViewHolder> {
    private List<Hotel> items;
    private Context context;
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
public static class HospedajeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView nombre;
    public TextView telefono;
    public TextView vermas;
    public TextView sitiourl;
    public ImageView sitioicon;
    public NetworkImageView imagen;
    public ImageButton vermasbutton;
    public IMyViewHolderClicks mListener;

    public HospedajeViewHolder(View v, IMyViewHolderClicks listener) {
        super(v);
        mListener = listener;
        imagen = (NetworkImageView) v.findViewById(R.id.imageView);
        vermasbutton = (ImageButton) v.findViewById(R.id.vermasbutton);
        vermas = (TextView) v.findViewById(R.id.vermas);
        nombre = (TextView) v.findViewById(R.id.name);
        sitiourl = (TextView) v.findViewById(R.id.sitiourl);
        sitioicon = (ImageView) v.findViewById(R.id.sitioicon);
        telefono = (TextView) v.findViewById(R.id.telefono);
        telefono.setOnClickListener(this);
        sitiourl.setOnClickListener(this);
        vermas.setOnClickListener(this);
        vermasbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageButton){
            mListener.onTomato((ImageButton) v, getLayoutPosition());
        } else if (v.getId() == R.id.vermas || v.getId() == R.id.vermasbutton ){
            mListener.onPotato((TextView) v, getLayoutPosition());
        }else if (v.getId() == R.id.telefono){
            mListener.callPlace((TextView) v, getLayoutPosition());
        }else if (v.getId() == R.id.sitiourl ){
            mListener.openSiteUrl((TextView) v, getLayoutPosition());
        }
    }
    public static interface IMyViewHolderClicks {
        public void openSiteUrl(TextView textCall, int i);
        public void callPlace(TextView textCall, int i);
        public void onPotato(TextView caller, int i);
        public void onTomato(ImageButton callerImage, int i);
    }

}

    public HotelesAdapter(List<Hotel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public HotelesAdapter.HospedajeViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_hoteles, viewGroup, false);
        if (imageLoader == null)
            imageLoader = MyVolleySingleton.getInstance().getImageLoader();

        HotelesAdapter.HospedajeViewHolder vh = new HospedajeViewHolder(v, new HotelesAdapter.HospedajeViewHolder.IMyViewHolderClicks() {
            public void onPotato(TextView caller, int i) {
                Hotel hotel  = items.get(i);
                ((MainActivity)viewGroup.getContext()).getSupportActionBar().setTitle(context.getResources().getString(R.string.app_name));
                Fragment fH = new MapDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",Integer.toString(hotel.getId()));
                bundle.putString("titulo",hotel.getNombre());
                bundle.putString("ubicacion",hotel.getUbicacion());
                bundle.putString("latitud_mapa",hotel.getLatitud_mapa());
                bundle.putString("longitud_mapa",hotel.getLongitud_mapa());

                fH.setArguments(bundle);
                FragmentTransaction ftH = ((MainActivity) viewGroup.getContext()).getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            };
            public void onTomato(ImageButton callerImage, int i) {
                Hotel hotel  = items.get(i);

                ((MainActivity)viewGroup.getContext()).getSupportActionBar().setTitle(context.getResources().getString(R.string.app_name));
                Fragment fH = new MapDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",Integer.toString(hotel.getId()));
                bundle.putString("titulo",hotel.getNombre());
                bundle.putString("ubicacion",hotel.getUbicacion());
                bundle.putString("latitud_mapa",hotel.getLatitud_mapa());
                bundle.putString("longitud_mapa",hotel.getLongitud_mapa());

                fH.setArguments(bundle);
                FragmentTransaction ftH = ((MainActivity) viewGroup.getContext()).getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            };
            public void callPlace(TextView textCall, int i){
                Hotel hotel = items.get(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(viewGroup.getContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + hotel.getTelefono()));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        viewGroup.getContext().startActivity(callIntent);
                    }
                }

            };
            public void openSiteUrl(TextView urlLink, int i){
                Hotel hotel = items.get(i);
                try{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hotel.getWebsite()));
                    viewGroup.getContext().startActivity(browserIntent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            };
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final HospedajeViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageUrl(items.get(i).getImagen(), imageLoader);
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.telefono.setText(items.get(i).getTelefono());
        if (items.get(i).getWebsite() == null || items.get(i).getWebsite().equals("")) {
            viewHolder.sitiourl.setVisibility(View.GONE);
            viewHolder.sitioicon.setVisibility(View.GONE);
        }else {
            viewHolder.sitiourl.setText(items.get(i).getWebsite());
        }
    }
}