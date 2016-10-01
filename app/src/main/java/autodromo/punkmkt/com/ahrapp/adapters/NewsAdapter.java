package autodromo.punkmkt.com.ahrapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import autodromo.punkmkt.com.ahrapp.MyVolleySingleton;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.models.Noticia;

import java.util.List;

/**
 * Created by DaniPunk on 18/07/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NoticiaViewHolder> {

    private List<Noticia> items;
    private Context context;

    ImageLoader imageLoader = MyVolleySingleton.getInstance().getImageLoader();

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name, short_description;
        public NetworkImageView thumbnail;
        public IMyViewHolderClicks mListener;

        public NoticiaViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            mListener = listener;
            thumbnail = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
            short_description = (TextView) itemView.findViewById(R.id.short_description);
            if(thumbnail!=null)
            {
                thumbnail.setOnClickListener(this);
            }

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
    public NoticiaViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.new_card, viewGroup, false);
        if (imageLoader == null)
            imageLoader = MyVolleySingleton.getInstance().getImageLoader();

        NewsAdapter.NoticiaViewHolder vh = new NewsAdapter.NoticiaViewHolder(v, new NewsAdapter.NoticiaViewHolder.IMyViewHolderClicks() {

            public void onPotato(View caller, int i) {
                Noticia noticia  = items.get(i);
                Intent Idetail = new Intent (viewGroup.getContext(), getClass());
                Idetail.putExtra("id", Integer.toString(noticia.getId()));
                viewGroup.getContext().startActivity(Idetail);

            };
            public void onTomato(NetworkImageView callerImage, int i) {
                Noticia noticia  = items.get(i);
                //Intent Idetail = new Intent (viewGroup.getContext(), SingleNewDetailActivity.class);
                //Idetail.putExtra("id", Integer.toString(noticia.getId()));
               // viewGroup.getContext().startActivity(Idetail);
            }

        });

        return vh;

    }

    @Override
    public void onBindViewHolder(NoticiaViewHolder noticiaViewHolder, int i) {
        if(noticiaViewHolder.thumbnail!=null){
            noticiaViewHolder.thumbnail.setImageUrl(items.get(i).getThumbnail(), imageLoader);

        }

        noticiaViewHolder.name.setText(items.get(i).getShort_title());
        noticiaViewHolder.short_description.setText(items.get(i).getShort_description());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public NewsAdapter(List<Noticia> items, Context context) {
        this.items = items;
        this.context = context;
    }

}