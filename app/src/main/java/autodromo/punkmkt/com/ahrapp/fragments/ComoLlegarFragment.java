package autodromo.punkmkt.com.ahrapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.utils.NetworkUtils;

/**
 * Created by DaniPunk on 19/07/16.
 */
public class ComoLlegarFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleMap map;
    View view;
    SupportMapFragment mapFragment;

    private WebView mWebView;
    /**
     * Represents a geographical location.
     */

    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    String targetLat = "19.4034641";
    String targetLang ="-99.0909719";
    String currentLattitude;
    String currentLongitude;
    protected static final String TAG = "MainActivity";
    GoogleMap mGoogleMap;

    public ComoLlegarFragment() {
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
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_autodromo_ubica_tu_asiento, container, false);

        try {
            view = inflater.inflate(R.layout.fragment_autodromo_como_llegar, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }


        mWebView = (WebView) view.findViewById(R.id.mapwebview);
        WebSettings webSettings = mWebView.getSettings();
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webSettings.setJavaScriptEnabled(true);
        //mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView webview, String url) {
                //hide loading image
                view.findViewById(R.id.imageLoading).setVisibility(View.GONE);
                //show webview
                view.findViewById(R.id.mapwebview).setVisibility(View.VISIBLE);
            }

        });
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        return view;
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

            //DO OP WITH LOCATION SERVICE
        }
        String url= "";
        if (mLastLocation != null) {
            url = "http://maps.google.com/maps?saddr="+mLastLocation.getLatitude()+","+mLastLocation.getLongitude()+"&daddr="+targetLat+","+targetLang;
        } else {
            url = "http://maps.google.com/maps?saddr="+currentLattitude+","+currentLongitude+"&daddr="+targetLat+","+targetLang;
        }
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())) {
            mWebView.loadUrl(url);
        }
        else{

        }
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

}