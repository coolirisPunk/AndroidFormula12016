package com.punkmkt.formula12016.adapters;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.punkmkt.formula12016.R;

/**
 * Created by DaniPunk on 25/07/16.
 */
public class ServicioSpinnerAdapter extends ArrayAdapter<String> {

    private Activity context;
    String[] data = null;

    public ServicioSpinnerAdapter(Activity context, int resource, String[] data2)
    {
        super(context, resource, data2);
        this.context = context;
        this.data = data2;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_layout_servicios, parent, false);
        }
        //put the data in it
        String item = data[position];
        if(item != null)
        {
            TextView text1 = (TextView) row.findViewById(R.id.textView);
            ImageView img = (ImageView) row.findViewById(R.id.img_service);
            text1.setText(item);
            img.setImageResource(R.drawable.arrow_colors);
        }

        return row;
    }
}