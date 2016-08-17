package com.punkmkt.formula12016.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.adapters.CustomizedSpinnerAdapter;
import com.punkmkt.formula12016.adapters.ServicioSpinnerAdapter;
import com.punkmkt.formula12016.models.Coordenada;

import java.util.ArrayList;

/**
 * Created by DaniPunk on 19/07/16.
 */
public class ServiciosFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerClickListener {
    View view;
    SupportMapFragment mapFragment;
   // private OverscrollHandler mOverscrollHandler = new OverscrollHandler();
    /**
     * Represents a geographical location.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    protected static final String TAG = ServiciosFragment.class.getName();
    GoogleMap mGoogleMap;
    Marker currLocationMarker;
    LatLng latLng;
    private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(19.373472923292397,-99.1301617026329), new LatLng(19.441751639782964,-99.04482152312994));
    LatLng NEWARK = new LatLng(19.402620, -99.089944); //autodromo
    private final int MAX_ZOOM = 16;
    private final int MIN_ZOOM = 13;
    private final int ZOOM_MAP = 13;
    private ArrayList<Marker> mMarkers;

    public ServiciosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_autodromo_servicios, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final String[] data_array_servicios = getResources().getStringArray(R.array.servicios_array);
        Spinner servicios = (Spinner) view.findViewById(R.id.servicios_spinner);
        final ArrayAdapter<String> adapter_servicios = new ServicioSpinnerAdapter(
                getActivity(), android.R.layout.simple_spinner_item, data_array_servicios);
        servicios.setAdapter(adapter_servicios);
        mMarkers = new ArrayList<>();
        servicios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position){
                    case 0:
                        AddMarkers1();
                        break;
                    case 1:
                        AddMarkers2();
                        break;
                    case 2:
                        AddMarkers3();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        //mOverscrollHandler.sendEmptyMessageDelayed(0,100);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //DO CHECK PERMISSION
        }
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mLastLocation != null) {
            LatLng NEWARK_CURRENT_LOCATION = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions m = new MarkerOptions();
            m.position(NEWARK_CURRENT_LOCATION).icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_colors));
            mGoogleMap.addMarker(m).setVisible(true);
        }
        AddGroundOverlay();

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWARK, ZOOM_MAP));
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow_colors));
        currLocationMarker = mGoogleMap.addMarker(markerOptions);

    }
    private LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
        double latitude=0, longitude=0;
        if(cameraBounds.southwest.latitude < BOUNDS.southwest.latitude) {
            latitude = BOUNDS.southwest.latitude - cameraBounds.southwest.latitude;
        }
        if(cameraBounds.southwest.longitude < BOUNDS.southwest.longitude) {
            longitude = BOUNDS.southwest.longitude - cameraBounds.southwest.longitude;
        }
        if(cameraBounds.northeast.latitude > BOUNDS.northeast.latitude) {
            latitude = BOUNDS.northeast.latitude - cameraBounds.northeast.latitude;
        }
        if(cameraBounds.northeast.longitude > BOUNDS.northeast.longitude) {
            longitude = BOUNDS.northeast.longitude - cameraBounds.northeast.longitude;
        }
        return new LatLng(latitude, longitude);
    }

//    private class OverscrollHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            CameraPosition position = mGoogleMap.getCameraPosition();
//            VisibleRegion region = mGoogleMap.getProjection().getVisibleRegion();
//            float zoom = 0;
//            if(position.zoom < MIN_ZOOM) zoom = MIN_ZOOM;
//            if(position.zoom > MAX_ZOOM) zoom = MAX_ZOOM;
//            LatLng correction = getLatLngCorrection(region.latLngBounds);
//            if(zoom != 0 || correction.latitude != 0 || correction.longitude != 0) {
//                zoom = (zoom==0)?position.zoom:zoom;
//                double lat = position.target.latitude + correction.latitude;
//                double lon = position.target.longitude + correction.longitude;
//                CameraPosition newPosition = new CameraPosition(new LatLng(lat,lon), zoom, position.tilt, position.bearing);
//                CameraUpdate update = CameraUpdateFactory.newCameraPosition(newPosition);
//                mGoogleMap.moveCamera(update);
//            }
//        /* Recursively call handler every 100ms */
//            sendEmptyMessageDelayed(0,100);
//        }
//    }

    @Override
    public void onMapClick(LatLng point) {
        Log.d(TAG, point.toString());
        //mTapTextView.setText("tapped, point=" + point);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Log.d(TAG, point.toString());
    }


    private void removeMarkers() {
        if(!mMarkers.isEmpty()){
            for (Marker marker: mMarkers) {
                marker.remove();
            }
            mMarkers.clear();
        }

    }

    private void AddPointsToMarker(ArrayList<Coordenada> coordenadas,int icon_marker){
        for(Coordenada coordenada : coordenadas ){

            MarkerOptions options = new MarkerOptions();
            options.title(coordenada.getTitulo());
            options.snippet(coordenada.getDescripcion());
            LatLng point = new LatLng(coordenada.getLatitud(), coordenada.getLongitud());
            options.position(point);
            options.icon(BitmapDescriptorFactory.fromResource(icon_marker));
            Marker m = mGoogleMap.addMarker(options);
            mMarkers.add(m);
            mGoogleMap.setOnMarkerClickListener(this);
        }
    }
    private void AddMarkers1(){
        removeMarkers();
        ArrayList<Coordenada> recolecciones = new ArrayList<>();
        recolecciones.add(new Coordenada(19.414000326921037,-99.07150246202946, "",""));
        recolecciones.add(new Coordenada(19.401510389605946,-99.09627065062523, "",""));
        recolecciones.add(new Coordenada(19.38763141622788,-99.06301762908697, "",""));
        AddPointsToMarker(recolecciones,  R.drawable.arrow_colors);
    }
    private void AddMarkers2(){
        removeMarkers();
        ArrayList<Coordenada> recolecciones = new ArrayList<>();
        recolecciones.add(new Coordenada(19.414000326921037,-99.07150246202946, "",""));
        recolecciones.add(new Coordenada(19.401510389605946,-99.09627065062523, "",""));
        recolecciones.add(new Coordenada(19.38763141622788,-99.06301762908697, "",""));
        AddPointsToMarker(recolecciones,  R.drawable.arrow_colors);
    }

    private void AddMarkers3(){
        removeMarkers();
        ArrayList<Coordenada> recolecciones = new ArrayList<>();
        recolecciones.add(new Coordenada(19.414000326921037,-99.07150246202946, "",""));
        recolecciones.add(new Coordenada(19.401510389605946,-99.09627065062523, "",""));
        recolecciones.add(new Coordenada(19.38763141622788,-99.06301762908697, "",""));
        AddPointsToMarker(recolecciones,  R.drawable.arrow_colors);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getSnippet() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (getActivity()).getLayoutInflater();
            builder.setTitle(marker.getTitle());
            builder.setMessage(marker.getSnippet());
            builder.setView(inflater.inflate(R.layout.map_alert, null)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.create();
            builder.show();
            return true;
        }else {
            return false;
        }
    }
    private void AddGroundOverlay(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.map_autodromo, options);
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.map_autodromo, 500, 500)))
                .position(NEWARK, 8600f, 6500f);
        mGoogleMap.addGroundOverlay(newarkMap);
    }
}