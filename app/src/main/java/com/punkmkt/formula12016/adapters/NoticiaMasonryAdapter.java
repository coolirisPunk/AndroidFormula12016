package com.punkmkt.formula12016.adapters;

/**
 * Created by germanpunk on 09/09/16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.fragments.NoticiaFichaIndividual;
import com.punkmkt.formula12016.models.Noticia;

import java.util.List;


public class NoticiaMasonryAdapter extends RecyclerView.Adapter<NoticiaMasonryAdapter.MasonryView> {
    private List<Noticia> items;
    private Context context;

    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();

    public static class MasonryView extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nombre, descripcion_corta;
        public NetworkImageView imagen;
        public IMyViewHolderClicks mListener;

        public MasonryView(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            imagen = (NetworkImageView) itemView.findViewById(R.id.netork_imageView);
            nombre = (TextView) itemView.findViewById(R.id.name);
            imagen.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof NetworkImageView){
                mListener.onTomato((NetworkImageView)v, getLayoutPosition());
            } else {
                mListener.onPotato(v,getLayoutPosition());
            }
        }
        public static interface IMyViewHolderClicks {
            public void onPotato(View caller, int i);
            public void onTomato(NetworkImageView callerImage, int i);
        }

    }

    @Override
    public NoticiaMasonryAdapter.MasonryView onCreateViewHolder(final ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_items, viewGroup, false);
        if (imageLoader == null)
            imageLoader = MyVolleySingleton.getInstance().getImageLoader();

        NoticiaMasonryAdapter.MasonryView vh = new NoticiaMasonryAdapter.MasonryView(v, new NoticiaMasonryAdapter.MasonryView.IMyViewHolderClicks() {

            public void onPotato(View caller, int i) {
                Noticia noticia  = items.get(i);
                Fragment noticiaIndividual = new NoticiaFichaIndividual();
                Bundle bundle = new Bundle();
                bundle.putString("noticiaId", noticia.getId().toString());
                bundle.putString("titulo", noticia.getTitle());
                bundle.putString("descripcion", noticia.getDescription());
                bundle.putString("imagen", noticia.getPicture());
                noticiaIndividual.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, noticiaIndividual);
                transaction.addToBackStack(null);
                transaction.commit();
            };
            public void onTomato(NetworkImageView callerImage, int i) {
                Noticia noticia  = items.get(i);
                Fragment noticiaIndividual = new NoticiaFichaIndividual();
                Bundle bundle = new Bundle();
                bundle.putString("noticiaId", noticia.getId().toString());
                bundle.putString("titulo", noticia.getTitle());
                bundle.putString("descripcion", noticia.getDescription());
                bundle.putString("imagen", noticia.getPicture());
                noticiaIndividual.setArguments(bundle);

                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, noticiaIndividual);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        });

        return vh;
    }

    @Override
    public void onBindViewHolder(NoticiaMasonryAdapter.MasonryView holder, int position) {
        holder.imagen.setImageUrl(items.get(position).getThumbnail(), imageLoader);
        holder.nombre.setText(items.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public NoticiaMasonryAdapter(List<Noticia> items, Context context) {
        this.items = items;
        this.context = context;
    }
}
