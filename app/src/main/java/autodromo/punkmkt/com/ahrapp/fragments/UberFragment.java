package autodromo.punkmkt.com.ahrapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import autodromo.punkmkt.com.ahrapp.BuildConfig;
import autodromo.punkmkt.com.ahrapp.R;
import autodromo.punkmkt.com.ahrapp.utils.BitmapManager;
import autodromo.punkmkt.com.ahrapp.utils.BitmapWorkerTask;
import autodromo.punkmkt.com.ahrapp.utils.RecyclingBitmapDrawable;

import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivity;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.android.rides.RideRequestViewError;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;


import static com.uber.sdk.android.core.utils.Preconditions.checkNotNull;

/**
 * Created by DaniPunk on 19/07/16.
 */
public class UberFragment extends Fragment implements RideRequestButtonCallback {

    public UberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static final String DROPOFF_ADDR = "Gran premio de México";
    private static  Double DROPOFF_LAT = 19.403419;
    private static  Double DROPOFF_LONG = -99.088515;
    private static final String DROPOFF_NICK = "Autódromo Hermanos Rodriguez";
    private static final String ERROR_LOG_TAG = "UberSDK-SampleActivity";
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";
    private static final int WIDGET_REQUEST_CODE = 1234;

    private static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    private static final String REDIRECT_URI = BuildConfig.REDIRECT_URI;
    private static final String SERVER_TOKEN = BuildConfig.SERVER_TOKEN;
    String TAG = UberFragment.class.getName();

    private SessionConfiguration configuration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_autodromo_uber, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.img_back_uber);
        loadBitmap(R.drawable.uber_bg,img,375,667);
        String grada = getActivity().getSharedPreferences("PREFERENCE", getActivity().MODE_PRIVATE).getString("grada",null);
        if(grada!=null){
            Log.d("Uber",grada);
            switch (grada){
                case "1": //1
                    DROPOFF_LAT = 19.408346 ;
                    DROPOFF_LONG = -99.092736;
                    break;
                case "2": //2
                    DROPOFF_LAT = 19.408346;
                    DROPOFF_LONG = -99.092736;
                    break;
                case "3": //2A
                    DROPOFF_LAT = 19.407009;
                    DROPOFF_LONG = -99.083155;
                    break;
                case "4": //3
                    DROPOFF_LAT = 19.407009;
                    DROPOFF_LONG = -99.083155;
                    break;
                case "5": //4
                    DROPOFF_LAT = 19.407009;
                    DROPOFF_LONG = -99.083155;
                    break;
                case "6": //5
                    DROPOFF_LAT = 19.407009;
                    DROPOFF_LONG = -99.083155;
                    break;
                case "7": //6
                    DROPOFF_LAT = 19.400348;
                    DROPOFF_LONG = -99.082758;
                    break;
                case "8": //6A
                    DROPOFF_LAT = 19.400348;
                    DROPOFF_LONG = -99.082758;
                    break;
                case "9": // 7
                    DROPOFF_LAT = 19.401566;
                    DROPOFF_LONG =  -99.090106;
                    break;
                case "10": // 8
                    DROPOFF_LAT = 19.401566;
                    DROPOFF_LONG =  -99.090106;
                    break;
                case "11": // 9
                    DROPOFF_LAT = 19.394842;
                    DROPOFF_LONG =  -99.088345;
                    break;
                case "12": // 10
                    DROPOFF_LAT = 19.394842;
                    DROPOFF_LONG =  -99.088345;
                    break;
                case "13": // 11
                    DROPOFF_LAT = 19.394842;
                    DROPOFF_LONG =  -99.088345;
                    break;
                case "14": // 14
                    DROPOFF_LAT = 19.405714;
                    DROPOFF_LONG =  -99.098005;
                    break;
                case "15": // 15
                    DROPOFF_LAT = 19.405714;
                    DROPOFF_LONG =  -99.098005;
                    break;
            }

        }
        else{
            Log.d("Uber","no uber");

        }
        configuration = new SessionConfiguration.Builder()
                .setRedirectUri(REDIRECT_URI)
                .setClientId(CLIENT_ID)
                .setServerToken(SERVER_TOKEN)
                .setEnvironment(SessionConfiguration.Environment.PRODUCTION)
                .build();

        // Optional: to use the SDK in China, set the region property
        // See https://developer.uber.com/docs/china for more details.
        // configuration.setEndpointRegion(SessionConfiguration.EndpointRegion.CHINA);

        validateConfiguration(configuration);
        ServerTokenSession session = new ServerTokenSession(configuration);

        RideParameters rideParametersForProduct = new RideParameters.Builder()
                .setProductId(UBERX_PRODUCT_ID)
                .setPickupToMyLocation()
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();


        RideParameters rideParametersCheapestProduct = new RideParameters.Builder()
                .setPickupToMyLocation()
                .setDropoffLocation(DROPOFF_LAT, DROPOFF_LONG, DROPOFF_NICK, DROPOFF_ADDR)
                .build();



        // This button demonstrates launching the RideRequestActivity (customized button behavior).
        // You can optionally setRideParameters for pre-filled pickup and dropoff locations.
        RideRequestButton uberButtonWhite = (RideRequestButton) v.findViewById(R.id.uber_button_white);
        RideRequestActivityBehavior rideRequestActivityBehavior = new RideRequestActivityBehavior(getActivity(),
                WIDGET_REQUEST_CODE, configuration);
        uberButtonWhite.setRequestBehavior(rideRequestActivityBehavior);
        uberButtonWhite.setRideParameters(rideParametersCheapestProduct);
        uberButtonWhite.setSession(session);
        //uberButtonWhite.loadRideInformation();
        return v;

    }

    /**
     * Validates the local variables needed by the Uber SDK used in the sample project
     * @param configuration
     */
    private void validateConfiguration(SessionConfiguration configuration) {
        String nullError = "%s must not be null";
        String sampleError = "Please update your %s in the gradle.properties of the project before " +
                "using the Uber SDK Sample app. For a more secure storage location, " +
                "please investigate storing in your user home gradle.properties ";

        checkNotNull(configuration, String.format(nullError, "SessionConfiguration"));
        checkNotNull(configuration.getClientId(), String.format(nullError, "Client ID"));
        checkNotNull(configuration.getRedirectUri(), String.format(nullError, "Redirect URI"));
        checkNotNull(configuration.getServerToken(), String.format(nullError, "Server Token"));
        Log.d(TAG,configuration.getClientId());
        Log.d(TAG,configuration.getRedirectUri());
        Log.d(TAG,configuration.getServerToken());
//        checkState(!configuration.getClientId().equals("ltiTmkxxyYbvWWC02eufPwwsfaTCjGYT"),
//                String.format(sampleError, "Client ID"));
//        checkState(!configuration.getRedirectUri().equals("ltiTmkxxyYbvWWC02eufPwwsfaTCjGYT://uberConnect"),
//                String.format(sampleError, "Redirect URI"));
//        checkState(!configuration.getServerToken().equals("KNTwHb1RxDQHBcVoBe4zHAgJ38RriS91rH6I2fyg"),
//                String.format(sampleError, "Server Token"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WIDGET_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED && data != null) {
            if (data.getSerializableExtra(RideRequestActivity.AUTHENTICATION_ERROR) != null) {
                AuthenticationError error = (AuthenticationError) data.getSerializableExtra(RideRequestActivity
                        .AUTHENTICATION_ERROR);
                Toast.makeText(getActivity().getApplicationContext(), "Auth error " + error.name(), Toast.LENGTH_SHORT).show();
                Log.d(ERROR_LOG_TAG, "Error occurred during authentication: " + error.toString
                        ().toLowerCase());
            } else if (data.getSerializableExtra(RideRequestActivity.RIDE_REQUEST_ERROR) != null) {
                RideRequestViewError error = (RideRequestViewError) data.getSerializableExtra(RideRequestActivity
                        .RIDE_REQUEST_ERROR);
                Toast.makeText(getActivity().getApplicationContext(), "RideRequest error " + error.name(), Toast.LENGTH_SHORT).show();
                Log.d(ERROR_LOG_TAG, "Error occurred in the Ride Request Widget: " + error.toString().toLowerCase());
            }
        }
    }

    @Override
    public void onRideInformationLoaded() {
        Toast.makeText(getActivity().getApplicationContext(), "Estimates have been refreshed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(ApiError apiError) {
        Toast.makeText(getActivity().getApplicationContext(), apiError.getClientErrors().get(0).getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Throwable throwable) {
        Log.e("SampleActivity", "Error obtaining Metadata", throwable);
        Toast.makeText(getActivity().getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
    }

    public void loadBitmap(int resId, ImageView imageView,int width, int height) {
        if (BitmapWorkerTask.cancelPotentialWork(resId, imageView)) {
            Bitmap bitmap;
            bitmap =  BitmapManager.decodeSampledBitmapFromResource(getActivity().getApplicationContext().getResources(), R.drawable.descarga, 100, 100);
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView,width,height, getActivity());
            final BitmapWorkerTask.AsyncDrawable asyncDrawable = new BitmapWorkerTask.AsyncDrawable(getActivity().getApplicationContext().getResources(),bitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
            new RecyclingBitmapDrawable(getActivity().getApplicationContext().getResources(),bitmap);
        }
    }
}