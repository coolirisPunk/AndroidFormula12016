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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import autodromo.punkmkt.com.ahrapp.MainActivity;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.fragments.MapDetailFragment;
import autodromo.punkmkt.com.ahrapp.models.Restaurante;

import java.util.List;

/**
 * Created by germanpunk on 31/08/16.
 */
public class RestaurantesAdapter  extends RecyclerView.Adapter<RestaurantesAdapter.HospedajeViewHolder> {
    private List<Restaurante> items;
    private Context context;
    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    public static class HospedajeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nombre;
        public TextView chef;
        public TextView ubicacion;
        public TextView sitiourl;
        public ImageView sitioicon;
        public ImageView cheficon;
        public NetworkImageView imagen;
        public IMyViewHolderClicks mListener;

        public HospedajeViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            mListener = listener;
            imagen = (NetworkImageView) v.findViewById(R.id.imageView);
            nombre = (TextView) v.findViewById(R.id.name);
            sitiourl = (TextView) v.findViewById(R.id.sitiourl);
            sitioicon = (ImageView) v.findViewById(R.id.sitioicon);
            cheficon = (ImageView) v.findViewById(R.id.pinchef);
            ubicacion = (TextView) v.findViewById(R.id.ubicacion);

            chef = (TextView) v.findViewById(R.id.chef);

            sitiourl.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.telefono) {
                mListener.callPlace((TextView) v, getLayoutPosition());
            } else if (v.getId() == R.id.sitiourl) {
                mListener.openSiteUrl((TextView) v, getLayoutPosition());
            }
        }

        public static interface IMyViewHolderClicks {
            public void openSiteUrl(TextView textCall, int i);

            public void callPlace(TextView textCall, int i);


        }

    }

    public RestaurantesAdapter(List<Restaurante> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RestaurantesAdapter.HospedajeViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_layout_restaurantes, viewGroup, false);
        if (imageLoader == null)
            imageLoader = MyVolleySingleton.getInstance().getImageLoader();

        RestaurantesAdapter.HospedajeViewHolder vh = new HospedajeViewHolder(v, new RestaurantesAdapter.HospedajeViewHolder.IMyViewHolderClicks() {
            public void onPotato(TextView caller, int i) {
                Restaurante restaurante = items.get(i);
                //((MainActivity) viewGroup.getContext()).getSupportActionBar().setTitle(context.getResources().getString(R.string.app_name));
                Fragment fH = new MapDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", Integer.toString(restaurante.getId()));
                bundle.putString("titulo", restaurante.getNombre());
                bundle.putString("ubicacion", restaurante.getUbicacion());
                bundle.putString("latitud_mapa", restaurante.getLatitud_mapa());
                bundle.putString("longitud_mapa", restaurante.getLongitud_mapa());

                fH.setArguments(bundle);
                FragmentTransaction ftH = ((MainActivity) viewGroup.getContext()).getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }

            ;

            public void onTomato(ImageButton callerImage, int i) {
                Restaurante restaurante = items.get(i);

                //((MainActivity) viewGroup.getContext()).getSupportActionBar().setTitle(context.getResources().getString(R.string.app_name));
                Fragment fH = new MapDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", Integer.toString(restaurante.getId()));
                bundle.putString("titulo", restaurante.getNombre());
                bundle.putString("ubicacion", restaurante.getUbicacion());
                bundle.putString("latitud_mapa", restaurante.getLatitud_mapa());
                bundle.putString("longitud_mapa", restaurante.getLongitud_mapa());

                fH.setArguments(bundle);
                FragmentTransaction ftH = ((MainActivity) viewGroup.getContext()).getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }

            ;

            public void callPlace(TextView textCall, int i) {
                Restaurante hotel = items.get(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(viewGroup.getContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + hotel.getTelefono()));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        viewGroup.getContext().startActivity(callIntent);
                    }
                }

            }

            ;

            public void openSiteUrl(TextView urlLink, int i) {
                Restaurante hotel = items.get(i);
                try{
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hotel.getWebsite()));
                    viewGroup.getContext().startActivity(browserIntent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            ;
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final HospedajeViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageUrl(items.get(i).getImagen(), imageLoader);
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.ubicacion.setText(items.get(i).getUbicacion());

        if(items.get(i).getChef() == null) {
            Log.d("ad","null");
            viewHolder.chef.setVisibility(View.GONE);
            viewHolder.cheficon.setVisibility(View.GONE);
        }
        else if(items.get(i).getChef().equals("")){
            Log.d("ad","---");
            viewHolder.chef.setVisibility(View.GONE);
            viewHolder.cheficon.setVisibility(View.GONE);
        }
        else{
            Log.d("ad","hef");
            viewHolder.chef.setVisibility(View.VISIBLE);
            viewHolder.cheficon.setVisibility(View.VISIBLE);
            viewHolder.chef.setText(items.get(i).getChef());
        }

        if (items.get(i).getWebsite() == null || items.get(i).getWebsite().equals("")) {
            viewHolder.sitiourl.setVisibility(View.GONE);
            viewHolder.sitioicon.setVisibility(View.GONE);
        } else {
            viewHolder.sitiourl.setText(items.get(i).getWebsite());
        }
    }
}
