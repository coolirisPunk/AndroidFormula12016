package autodromo.punkmkt.com.ahrapp.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import autodromo.punkmkt.com.ahrapp.R;

/**
 * Created by germanpunk on 31/08/16.
 */
public class MapDetailFragment extends Fragment implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    View view;
    GoogleMap mGoogleMap;
    String TAG = MapDetailFragment.class.getName();
    String id,latitud,longitud,titulo,ubicacion,textoCompleto;
    int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.activity_map, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle bundle = getArguments();
        id = bundle.getString("id");
        latitud = bundle.getString("latitud_mapa");
        longitud = bundle.getString("longitud_mapa");
        titulo = bundle.getString("titulo");
        ubicacion = bundle.getString("ubicacion");
        textoCompleto = ubicacion;
            return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mGoogleMap.setMyLocationEnabled(true);
            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(getActivity().getApplicationContext(),"Your Permission is needed to get access the location",Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        }
        else{
            mGoogleMap.setMyLocationEnabled(true);
        }
            try {
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng centerLatLng = new LatLng(Float.parseFloat(latitud),Float.parseFloat(longitud));
                CameraUpdate cameraUpdate  = CameraUpdateFactory.newLatLngZoom(centerLatLng, 10);
                mGoogleMap.moveCamera(cameraUpdate);
                mGoogleMap.addMarker(new MarkerOptions().position(centerLatLng).title(titulo).snippet(ubicacion)).showInfoWindow();

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.valueOf(requestCode));
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION && resultCode == getActivity().RESULT_OK) {

        }

    }

}
