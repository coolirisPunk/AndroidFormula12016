package com.punkmkt.formula12016.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.punkmkt.formula12016.R;

/**
 * Created by DaniPunk on 19/07/16.
 */
public class UbicaTuAsientoFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, UbicaAsientoDialogFragment.UbicaAsientoDialogListener {
    private Spinner zonas,gradas,seccion,filas, asientos;
    String[] data_array_zonas;
    GoogleMap map;
    View view;
    SupportMapFragment mapFragment;
    //private OverscrollHandler mOverscrollHandler = new OverscrollHandler();
    int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 4;
    /**
     * Represents a geographical location.
     */
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected static final String TAG = UbicaTuAsientoFragment.class.getName();
    GoogleMap mGoogleMap;
    Marker currLocationMarker;
    LatLng latLng;                                                  //suroeste       //Noreste
    private final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(19.373472923292397,-99.1301617026329), new LatLng(19.441751639782964,-99.04482152312994));
    LatLng NEWARK = new LatLng(19.402620, -99.089944); //autodromo

    private final int MAX_ZOOM = 16;
    private final int MIN_ZOOM = 13;
    private final int ZOOM_MAP = 13;

    public UbicaTuAsientoFragment() {
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
            view = inflater.inflate(R.layout.fragment_autodromo_ubica_tu_asiento, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
            return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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
//        mGoogleApiClient.connect();
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
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "no location detected", Toast.LENGTH_LONG).show();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.map_autodromo, options);
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.map_autodromo, 500, 500)))
                .position(NEWARK, 8600f, 6500f);
        mGoogleMap.addGroundOverlay(newarkMap);

        MarkerOptions m = new MarkerOptions();
        m.position(NEWARK);
        mGoogleMap.addMarker(m).setVisible(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEWARK, ZOOM_MAP));
        //map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
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

        //Toast.makeText(getActivity().getApplicationContext(),"Location Changed",Toast.LENGTH_SHORT).show();

//        CameraPosition cameraPosition = new CameraPosition.Builder()
 //               .target(latLng).zoom(14).build();

       // mMap.animateCamera(CameraUpdateFactory
        //        .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

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

    public void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        UbicaAsientoDialogFragment editNameDialogFragment = new UbicaAsientoDialogFragment();
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(UbicaTuAsientoFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_dialog");
    }
    // This is called when the dialog is completed and the results have been passed
    @Override
    public void onFinishEditDialog(String zonas,String gradas,String seccion,String filas, String asientos) {
        Toast.makeText(getActivity().getApplicationContext(),"Hi, " + zonas + gradas+ seccion + filas + asientos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser ) {
                showEditDialog();
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.valueOf(requestCode));
        if (requestCode == MY_PERMISSIONS_REQUEST_FINE_LOCATION && resultCode == getActivity().RESULT_OK) {
            Log.d(TAG,"location ok");
        }

    }

}