package autodromo.punkmkt.com.ahrapp.adapters;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import autodromo.punkmkt.com.ahrapp.MainActivity;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.fragments.MapDetailFragment;
import autodromo.punkmkt.com.ahrapp.models.Lugar;

import java.util.List;

/**
 * Created by germanpunk on 31/08/16.
 */
public class AdondeIrAdapter extends RecyclerView.Adapter<AdondeIrAdapter.HospedajeViewHolder> {
    private List<Lugar> items;
    private Context context;
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public static class HospedajeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nombre;
        public TextView ubicacion;
        public TextView telefono;
        public TextView vermas;
        public TextView exposicion;
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
            ubicacion = (TextView) v.findViewById(R.id.ubicacion);
            telefono = (TextView) v.findViewById(R.id.telefono);
            exposicion = (TextView) v.findViewById(R.id.exposicion);
            telefono.setOnClickListener(this);
            vermas.setOnClickListener(this);
            vermasbutton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageButton){
                mListener.onTomato((ImageButton) v, getLayoutPosition());
            } else if (v.getId() == R.id.vermas){
                mListener.onPotato((TextView) v, getLayoutPosition());
            }else if (v.getId() == R.id.telefono){
                mListener.callPlace((TextView) v, getLayoutPosition());
            }else if (v.getId() == R.id.sitiourl){
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

    public AdondeIrAdapter(List<Lugar> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AdondeIrAdapter.HospedajeViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_la_ciudad, viewGroup, false);
        if (imageLoader == null)
            imageLoader = MyVolleySingleton.getInstance().getImageLoader();

        AdondeIrAdapter.HospedajeViewHolder vh = new HospedajeViewHolder(v, new AdondeIrAdapter.HospedajeViewHolder.IMyViewHolderClicks() {
            public void onPotato(TextView caller, int i) {
                Lugar lugar  = items.get(i);
                ((MainActivity)viewGroup.getContext()).getSupportActionBar().setTitle(context.getResources().getString(R.string.app_name));
                Fragment fH = new MapDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",Integer.toString(lugar.getId()));
                bundle.putString("titulo",lugar.getNombre());
                bundle.putString("ubicacion",lugar.getUbicacion());
                bundle.putString("latitud_mapa",lugar.getLatitud_mapa());
                bundle.putString("longitud_mapa",lugar.getLongitud_mapa());

                fH.setArguments(bundle);
                FragmentTransaction ftH = ((MainActivity) viewGroup.getContext()).getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            };
            public void onTomato(ImageButton callerImage, int i) {
                Lugar lugar  = items.get(i);

                ((MainActivity)viewGroup.getContext()).getSupportActionBar().setTitle(context.getResources().getString(R.string.app_name));
                Fragment fH = new MapDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",Integer.toString(lugar.getId()));
                bundle.putString("titulo",lugar.getNombre());
                bundle.putString("ubicacion",lugar.getUbicacion());
                bundle.putString("latitud_mapa",lugar.getLatitud_mapa());
                bundle.putString("longitud_mapa",lugar.getLongitud_mapa());

                fH.setArguments(bundle);
                FragmentTransaction ftH = ((MainActivity) viewGroup.getContext()).getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            };
            public void callPlace(TextView textCall, int i){
                Lugar lugar = items.get(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(viewGroup.getContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + lugar.getTelefono()));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        viewGroup.getContext().startActivity(callIntent);
                    }
                }

            };
            public void openSiteUrl(TextView urlLink, int i){
                Lugar lugar = items.get(i);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrlmap()));
                viewGroup.getContext().startActivity(browserIntent);
            };
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final HospedajeViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageUrl(items.get(i).getImagen(), imageLoader);
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.exposicion.setText(items.get(i).getExposicion());
        if(items.get(i).getExposicion() == null) {
            viewHolder.exposicion.setVisibility(View.GONE);
        }
        else if(items.get(i).getExposicion().equals("")){
            viewHolder.exposicion.setVisibility(View.GONE);
        }
        else{
            viewHolder.exposicion.setVisibility(View.VISIBLE);
            viewHolder.exposicion.setText(items.get(i).getExposicion());
        }
        if(items.get(i).getTelefono() == null) {
            viewHolder.telefono.setVisibility(View.GONE);
        }
        else if(items.get(i).getTelefono().equals("")){
            viewHolder.telefono.setVisibility(View.GONE);
        }
        else{
            viewHolder.telefono.setVisibility(View.VISIBLE);
            viewHolder.telefono.setText(items.get(i).getTelefono());
        }

    }

}