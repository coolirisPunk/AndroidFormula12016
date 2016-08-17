package com.punkmkt.formula12016.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.adapters.CustomizedSpinnerAdapter;

/**
 * Created by DaniPunk on 25/07/16.
 */
    public class UbicaAsientoDialogFragment extends DialogFragment {
    private Spinner zonas,gradas,seccion,filas, asientos;
    public UbicaAsientoDialogFragment() {
        // Empty constructor required for DialogFragment
    }
    public interface UbicaAsientoDialogListener {
        void onFinishEditDialog(String zonas,String gradas,String seccion,String filas, String asientos);
    }
    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        UbicaAsientoDialogListener listener = (UbicaAsientoDialogListener) getTargetFragment();
        listener.onFinishEditDialog(zonas.getSelectedItem().toString(),gradas.getSelectedItem().toString(),
                seccion.getSelectedItem().toString(),filas.getSelectedItem().toString(),asientos.getSelectedItem().toString());
        dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ubica_asiento, container);
        //getDialog().setTitle("Hello");
        Button cancelar = (Button) view.findViewById(R.id.cancel_action);
        Button encontrar = (Button) view.findViewById(R.id.encontrar_action);

        cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        encontrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendBackResult();
            }
        });
        zonas = (Spinner) view.findViewById(R.id.zonas_spinner);
        gradas = (Spinner) view.findViewById(R.id.gradas_spinner);
        seccion = (Spinner) view.findViewById(R.id.seccion_spinner);
        filas = (Spinner) view.findViewById(R.id.filas_spinner);
        asientos = (Spinner) view.findViewById(R.id.asientos_spinner);

        final String[] data_array_zonas = getResources().getStringArray(R.array.zonas_array);

        final String[] data_gradas = new String[16];
        final String[] data_seccion = new String[16];
        final String[] data_filas = new String[16];
        final String[] data_asientos = new String[16];

        for (int i = 0; i<16;i++){
            data_gradas[i] = Integer.toString(i+1);
            data_seccion[i] = Integer.toString(i+1);
            data_filas[i] = Integer.toString(i+1);
            data_asientos[i] = Integer.toString(i+1);
        }

        final ArrayAdapter<String> adapter_zonas = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_array_zonas);

        final ArrayAdapter<String> adapter_gradas = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_gradas);

        final ArrayAdapter<String> adapter_secciones = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_gradas);

        final ArrayAdapter<String> adapter_filas = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_filas);

        final ArrayAdapter<String> adapter_asientos = new CustomizedSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_asientos);

        zonas.setAdapter(adapter_zonas);
        gradas.setAdapter(adapter_gradas);
        seccion.setAdapter(adapter_secciones);
        filas.setAdapter(adapter_filas);
        asientos.setAdapter(adapter_asientos);

        zonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                switch (position){
                    case 0:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLACK);
                        break;
                    case 1:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.RED);
                        break;
                    case 2:
                        ((TextView) parentView.getChildAt(0)).setBackgroundColor(Color.BLUE);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        gradas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        seccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        filas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        asientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parentView.getChildAt(0)).setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        return view;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

}
