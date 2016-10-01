package autodromo.punkmkt.com.ahrapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.fragments.TiendaFragmentProducts;
import autodromo.punkmkt.com.ahrapp.models.TiendaRestaurante;

import java.util.List;


/**
 * Created by sebastianmendezgiron on 11/08/16.
 */

public class TiendaRestauranteAdapter extends RecyclerView.Adapter<TiendaRestauranteAdapter.TiendaViewHolder> {

    private List<TiendaRestaurante> items;
    private Context context;
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();

    public static class TiendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nombre;
        public NetworkImageView imagen;
        public ImageButton vermasbutton;
        public TiendaRestauranteAdapter.TiendaViewHolder.IMyViewHolderClicks mListener;

        public TiendaViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            mListener = listener;
            imagen = (NetworkImageView) v.findViewById(R.id.imageView);
            vermasbutton = (ImageButton) v.findViewById(R.id.vermasbutton);
            nombre = (TextView) v.findViewById(R.id.item_name);
            if(imagen!=null)
            {
                imagen.setOnClickListener(this);
            }

            vermasbutton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof NetworkImageView){
                mListener.onImage((NetworkImageView) v, getLayoutPosition());
            } else {
                mListener.onPotato(v,getLayoutPosition());
            }
        }
        public static interface IMyViewHolderClicks {
            public void onPotato(View caller, int i);
            public void onImage(NetworkImageView callerImage, int i);
        }

    }

    @Override
    public TiendaViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tienda_row_restaurante, viewGroup, false);
        if (imageLoader == null)
            imageLoader = MyVolleySingleton.getInstance().getImageLoader();

        TiendaRestauranteAdapter.TiendaViewHolder vh = new TiendaRestauranteAdapter.TiendaViewHolder(v, new TiendaRestauranteAdapter.TiendaViewHolder.IMyViewHolderClicks() {
            public void onPotato(View caller, int i) {
                TiendaRestaurante tiendaRestauranteId = items.get(i);

                Fragment fragmentProductos = new TiendaFragmentProducts();
                Bundle bundle = new Bundle();
                bundle.putInt("restauranteId", tiendaRestauranteId.getId());
                bundle.putString("portadaRestaurante", tiendaRestauranteId.getImagen());
                fragmentProductos.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, fragmentProductos);
                transaction.addToBackStack(null);
                transaction.commit();

            };
            public void onTomato(ImageButton callerImage, int i) {
                TiendaRestaurante tiendaRestauranteId = items.get(i);

                Fragment fragmentProductos = new TiendaFragmentProducts();
                Bundle bundle = new Bundle();
                bundle.putString("restauranteId", tiendaRestauranteId.getId().toString());
                bundle.putString("portadaRestaurante", tiendaRestauranteId.getImagen());
                fragmentProductos.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, fragmentProductos);
                transaction.addToBackStack(null);
                transaction.commit();
            };
            public void onImage(NetworkImageView callerImage, int i){
                TiendaRestaurante tiendaRestauranteId = items.get(i);

                Fragment fragmentProductos = new TiendaFragmentProducts();
                Bundle bundle = new Bundle();
                bundle.putString("restauranteId", tiendaRestauranteId.getId().toString());
                bundle.putString("portadaRestaurante", tiendaRestauranteId.getImagen());
                fragmentProductos.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, fragmentProductos);
                transaction.addToBackStack(null);
                transaction.commit();
            };
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final TiendaRestauranteAdapter.TiendaViewHolder viewHolder, int i) {
        if(viewHolder.imagen!=null){
            viewHolder.imagen.setImageUrl(items.get(i).getImagen(), imageLoader);
        }
        viewHolder.nombre.setText(items.get(i).getNombre());
    }
    public TiendaRestauranteAdapter(List<TiendaRestaurante> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
