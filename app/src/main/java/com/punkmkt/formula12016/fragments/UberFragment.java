package com.punkmkt.formula12016.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.punkmkt.formula12016.BuildConfig;
import com.punkmkt.formula12016.R;
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
    private static final Double DROPOFF_LAT = 19.403419;
    private static final Double DROPOFF_LONG = -99.088515;
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
        configuration = new SessionConfiguration.Builder()
                .setRedirectUri(REDIRECT_URI)
                .setClientId(CLIENT_ID)
                .setServerToken(SERVER_TOKEN)
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
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
}