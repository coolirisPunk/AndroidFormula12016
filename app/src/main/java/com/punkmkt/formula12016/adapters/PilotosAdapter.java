package com.punkmkt.formula12016.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.punkmkt.formula12016.MainActivity;
import com.punkmkt.formula12016.MyVolleySingleton;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.fragments.SinglePilotoFragment;
import com.punkmkt.formula12016.models.Piloto;


import java.util.List;

/**
 * Created by DaniPunk on 28/07/16.
 */
public class PilotosAdapter extends RecyclerView.Adapter<PilotosAdapter.PremiosViewHolder> {

    private List<Piloto> items;
    private Context context;

    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();

    public static class PremiosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, short_description;
        public NetworkImageView thumbnail;
        public IMyViewHolderClicks mListener;

        public PremiosViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            thumbnail = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
            thumbnail.setOnClickListener(this);
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
    public PremiosViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.piloto_card, viewGroup, false);
        if (imageLoader == null)
            imageLoader = MyVolleySingleton.getInstance().getImageLoader();

        PilotosAdapter.PremiosViewHolder vh = new PilotosAdapter.PremiosViewHolder(v, new PilotosAdapter.PremiosViewHolder.IMyViewHolderClicks() {

            public void onPotato(View caller, int i) {
                Log.d("click","onPotato");
                Piloto piloto  = items.get(i);
                //Intent Idetail = new Intent (viewGroup.getContext(), getClass());
                //Idetail.putExtra("id", Integer.toString(noticia.getId()));
                //viewGroup.getContext().startActivity(Idetail);

            };
            public void onTomato(NetworkImageView callerImage, int i) {
                Piloto piloto  = items.get(i);
                ((MainActivity)viewGroup.getContext()).getSupportActionBar().setTitle(context.getResources().getString(R.string.app_name));
                Fragment fH = new SinglePilotoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",Integer.toString(piloto.getId()));
                fH.setArguments(bundle);
                FragmentTransaction ftH = ((MainActivity) viewGroup.getContext()).getSupportFragmentManager().beginTransaction();
                ftH.replace(R.id.frame, fH);
                ftH.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftH.addToBackStack(null);
                ftH.commit();
            }

        });

        return vh;

    }

    @Override
    public void onBindViewHolder(PremiosViewHolder premioViewHolder, int i) {
        premioViewHolder.thumbnail.setImageUrl(items.get(i).getPicture(), imageLoader);
        premioViewHolder.name.setText(items.get(i).getName());
//        if (((i+1) % 2) == 0) {
//            Log.d("test",String.valueOf(i+1)+"par");
//            RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams)premioViewHolder.name.getLayoutParams();
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
//            premioViewHolder.name.setLayoutParams(layoutParams);
//        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public PilotosAdapter(List<Piloto> items, Context context) {
        this.items = items;
        this.context = context;
    }

}