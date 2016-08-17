package com.punkmkt.formula12016.adapters;

/**
 * Created by DaniPunk on 14/07/16.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.punkmkt.formula12016.R;

import java.util.ArrayList;
public class CustomizedSpinnerAdapter extends ArrayAdapter<String> {

    private Activity context;
    String[] data = null;

    public CustomizedSpinnerAdapter(Activity context, int resource, String[] data2)
    {
        super(context, resource, data2);
        this.context = context;
        this.data = data2;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_layout, parent, false);
        }
        //put the data in it
        String item = data[position];
        if(item != null)
        {
            TextView text1 = (TextView) row.findViewById(R.id.textView);
            text1.setText(item);
        }
        return row;
    }
}