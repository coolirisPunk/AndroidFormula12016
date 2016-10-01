package autodromo.punkmkt.com.ahrapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import autodromo.punkmkt.com.ahrapp.R;

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
            switch (position){
                case 0:
                    img.setImageResource(R.drawable.icono_activaciones);
                    break;
                case 1:
                    img.setImageResource(R.drawable.icono_alimentos);
                    break;
                case 2:
                    img.setImageResource(R.drawable.icono_banos);
                    break;
                case 3:
                    img.setImageResource(R.drawable.icono_barra);
                    break;
                case 4:
                    img.setImageResource(R.drawable.icono_beer);
                    break;
                case 5:
                    img.setImageResource(R.drawable.icono_blenders);
                    break;
                case 6:
                    img.setImageResource(R.drawable.icono_banos);
                    break;
                case 7:
                    img.setImageResource(R.drawable.icono_bus);
                    break;
                case 8:
                    img.setImageResource(R.drawable.icono_cajero);
                    break;
                case 9:
                    img.setImageResource(R.drawable.icono_entrada_parking);
                    break;
                case 10:
                    img.setImageResource(R.drawable.icono_escenario);
                    break;
                case 11:
                    img.setImageResource(R.drawable.icono_food_court);
                    break;
                case 12:
                    img.setImageResource(R.drawable.icono_game_zone);
                    break;
                case 13:
                    img.setImageResource(R.drawable.icono_info);
                    break;
                case 14:
                    img.setImageResource(R.drawable.icono_merchandise);
                    break;
                case 15:
                    img.setImageResource(R.drawable.icono_metro);
                    break;
                case 16:
                    img.setImageResource(R.drawable.icono_metrobus);
                    break;
                case 17:
                    img.setImageResource(R.drawable.icono_parking);
                    break;
                case 18:
                    img.setImageResource(R.drawable.icono_primeros_auxilios);
                    break;
                case 19:
                    img.setImageResource(R.drawable.icono_ticket);
                    break;
                default:
                    break;
            }
        }

        return row;
    }
}