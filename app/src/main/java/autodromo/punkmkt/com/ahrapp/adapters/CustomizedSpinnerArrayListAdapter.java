package autodromo.punkmkt.com.ahrapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import autodromo.punkmkt.com.ahrapp.R;

import java.util.ArrayList;

/**
 * Created by DaniPunk on 28/07/16.
 */
public class CustomizedSpinnerArrayListAdapter extends ArrayAdapter<String> {

        private Activity context;
        ArrayList<String> data = null;
        int resource;

        public CustomizedSpinnerArrayListAdapter(Activity context, int resource, ArrayList<String> data2) {
                super(context, resource, data2);
                this.context = context;
                this.data = data2;
            this.resource = resource;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
                        View row = convertView;
                        if (row == null) {
                                LayoutInflater inflater = context.getLayoutInflater();
                                row = inflater.inflate(this.resource, parent, false);
                        }
                        //put the data in it
                        String item = data.get(position);
                        if (item != null) {
                                TextView text1 = (TextView) row.findViewById(R.id.textView);
                                text1.setText(item);
                        }

                        return row;
        }
}