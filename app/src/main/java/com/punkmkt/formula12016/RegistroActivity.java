package com.punkmkt.formula12016;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    LoginButton loginButton;
    public Activity activity;

    CallbackManager callbackManager;
    String AHR_REGISTER_URL = "http://104.236.3.158:82/api/auth/rest-auth/registration/";
    String AHR_LOGIN_FACEBOOK_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/facebook/";
    String AHR_USER_PROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/user/";
    String TAG = RegistroActivity.class.getName();
    String intent_from = null;
    private CoordinatorLayout coordinatorLayout;
    String current_token = "";
    String INTENT_FROM_CONF = "CONF";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //disconnectFromFacebook();
        String shared_token = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("access_token", null);
        Intent intent = getIntent();
        String inf = intent.getStringExtra("intent_from");
        //Toast.makeText(getApplicationContext(), inf, Toast.LENGTH_SHORT).show();
        if(inf!=""){
            Log.d(TAG,"Intent from config");
            intent_from = inf;
        }
        if (shared_token!=null){
            //Toast.makeText(getApplicationContext(), shared_token, Toast.LENGTH_SHORT).show();
            current_token = shared_token;
            //Intent intent_home = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent_home);
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        callbackManager = CallbackManager.Factory.create();

        activity = this;

        setContentView(R.layout.activity_registro);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        TextView cancelar = (TextView) findViewById(R.id.cancelar_action);
        Button guardar_cambios_action = (Button) findViewById(R.id.guardar_cambios_action);
        final EditText form_name = (EditText) findViewById(R.id.form_name);
        final EditText form_password = (EditText) findViewById(R.id.form_password);
        final EditText form_password_match = (EditText) findViewById(R.id.form_password_match);
        final EditText form_email = (EditText) findViewById(R.id.form_email);
        final TextView go_login = (TextView) findViewById(R.id.go_login);
        //form_name.setText("cooliris");
        //form_password.setText("cooliris123123");
        //form_password_match.setText("cooliris123123");
        //form_email.setText("gm@gmail.com");


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        loginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                process_login_facebook();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login error"+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        guardar_cambios_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate the sign up data
                boolean validationError = false;
                boolean cancel = false;
                View focusView = null;
                form_name.setError(null);
                form_password.setError(null);
                form_password_match.setError(null);
                form_email.setError(null);
                StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro));
                if (isEmpty(form_name)) {
                    validationError = true;
                    validationErrorMessage.append(" Username");
                    form_name.setError(getString(R.string.error_field_required));
                    focusView = form_name;
                    cancel = true;
                }
                if (isEmpty(form_password)) {
                    validationError = true;
                    cancel = true;
                    validationErrorMessage.append(" Password");
                    focusView = form_password;
                    form_password.setError(getString(R.string.error_field_required));
                }
                if (!isPasswordValid(form_password.getText().toString())) {
                    validationError = true;
                    cancel = true;
                    validationErrorMessage.append(" Password");
                    focusView = form_password;
                    form_password.setError(getString(R.string.error_field_length));
                }

                if (isEmpty(form_password_match)) {
                    validationError = true;
                    cancel = true;
                    validationErrorMessage.append(" Password");
                    focusView = form_password_match;
                    form_password_match.setError(getString(R.string.error_field_required));
                }
                if(!isValidEmail(form_email.getText().toString())){
                    validationError = true;
                    validationErrorMessage.append(" Email");
                    focusView = form_email;
                    form_email.setError(getString(R.string.error_email));
                    cancel = true;
                }
                if(!isMatching(form_password,form_password_match)){
                    validationError = true;
                    validationErrorMessage.append("Error matching password");
                    form_password_match.setError(getString(R.string.error_password_match));
                    focusView = form_password_match;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    progressDialog = new ProgressDialog(RegistroActivity.this);
                    progressDialog.setMessage("Guardando...");
                    progressDialog.show();
                    JSONObject js = new JSONObject();

                    try {
                        js.put("username", form_name.getText().toString());
                        js.put("password1", form_password.getText().toString());
                        js.put("password2", form_password_match.getText().toString());
                        js.put("email", form_email.getText().toString());
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("json",js.toString());

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            AHR_REGISTER_URL, js,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        progressDialog.dismiss();
                                        Log.d("Response",response.toString());
                                        JSONObject object = response;
                                        if (object.has("key")){
                                            String key = object.optString("key");
                                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("access_token", key).commit();
                                            if(intent_from == null){
                                                Intent myIntent = new Intent(getApplicationContext(), UbicaTuAsientoActivity.class);
                                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(myIntent);
                                            }

                                            else{
                                                if(intent_from.equalsIgnoreCase(INTENT_FROM_CONF)){
                                                    Intent myIntent = new Intent(getApplicationContext(), UbicaTuAsientoActivity.class);
                                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(myIntent);
                                                }
                                                else{
                                                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(myIntent);
                                                }
                                            }
                                        }
                                        else {
                                            Snackbar snackbar = Snackbar
                                                    .make(coordinatorLayout, "Error al registrarse.", Snackbar.LENGTH_SHORT);
                                            snackbar.setActionTextColor(Color.RED);
                                            snackbar.show();
                                        }
                                    } catch (Exception e) {
                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                        Snackbar snackbar = Snackbar
                                                .make(coordinatorLayout, e.getMessage().toString(), Snackbar.LENGTH_SHORT);
                                        snackbar.setActionTextColor(Color.RED);
                                        snackbar.show();

                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;

                            if(response != null && response.data != null){
                                switch(response.statusCode){
                                    case 400:
                                        progressDialog.dismiss();
                                        json = new String(response.data);
                                        String trimmedString = null;
                                        try{
                                            JSONObject obj = new JSONObject(json);
                                            Log.d(TAG,obj.toString());
                                            if(obj.has("username")){
                                                if(obj.optString("username")!=null){
                                                    //.requestFocus();
                                                JSONArray array_object = obj.getJSONArray("username");
                                                for (int count = 0; count < array_object.length(); count++) {
                                                    Toast.makeText(getApplicationContext(),array_object.get(count).toString(),Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                            }
                                           else if(obj.has("password1")) {
                                                if (obj.optString("password1") != null) {
                                                    JSONArray array_object = obj.getJSONArray("password1");
                                                    for (int count = 0; count < array_object.length(); count++) {
                                                        Toast.makeText(getApplicationContext(), array_object.get(count).toString(), Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            }
                                           else if(obj.has("email")) {
                                                if (obj.optString("email") != null) {
                                                    JSONArray array_object = obj.getJSONArray("email");
                                                    for (int count = 0; count < array_object.length(); count++) {
                                                        Toast.makeText(getApplicationContext(), array_object.get(count).toString(), Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            }
                                        } catch(JSONException e){
                                            e.printStackTrace();
                                        }
                                        break;
                                    default:

                                        break;


                                }
                                //Additional cases
                            }
                            if( error instanceof NetworkError) {
                            } else if( error instanceof ClientError) {
                            } else if( error instanceof ServerError) {
                            } else if( error instanceof AuthFailureError) {
                            } else if( error instanceof ParseError) {
                            } else if( error instanceof NoConnectionError) {
                            } else if( error instanceof TimeoutError) {
                            }
                        }
                    }) {
                        /**
                         * Passing some request headers
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json;");
                            return headers;
                        }
                    };

                    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                            9000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MyVolleySingleton.getInstance().addToRequestQueue(jsonObjReq);
                }

            }
        });



    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }


    /**
     * Shows the progress UI and hides the login form.
     */

    public void process_login_facebook(){
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.d(TAG,AccessToken.getCurrentAccessToken().getToken());

            JSONObject js = new JSONObject();

            try {
                js.put("access_token", AccessToken.getCurrentAccessToken().getToken());
            }catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AHR_LOGIN_FACEBOOK_JSON_API_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("Response",response.toString());
                                JSONObject object = response;
                                if (object.has("key")){
                                    String key = object.optString("key");
                                    Log.d(TAG,key);
                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("access_token", key).commit();
                                    getuserprofile(key);
                                    if(intent_from == null){
                                        Intent myIntent = new Intent(getApplicationContext(), UbicaTuAsientoActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(myIntent);
                                    }
                                    else{
                                        if(intent_from.equalsIgnoreCase(INTENT_FROM_CONF)){
                                            Intent myIntent = new Intent(getApplicationContext(), UbicaTuAsientoActivity.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(myIntent);
                                        }
                                        else{
                                            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(myIntent);
                                        }
                                    }

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Login error key facebook", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String json = null;
                    NetworkResponse response = error.networkResponse;

                    if(response != null && response.data != null){
                        switch(response.statusCode){
                            case 400:
                                json = new String(response.data);
                                String trimmedString = null;
                                try{
                                    JSONObject obj = new JSONObject(json);
                                    Log.d(TAG,obj.toString());
                                    if(obj.has("non_field_errors")){
                                        if(obj.optString("non_field_errors")!=null){
                                            JSONArray array_object = obj.getJSONArray("non_field_errors");
                                            for (int count = 0; count < array_object.length(); count++) {
                                                Toast.makeText(getApplicationContext(),array_object.get(count).toString(),Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    }

                                } catch(JSONException e){
                                    e.printStackTrace();
                                }
                                break;
                            default:

                                break;


                        }
                        //Additional cases
                    }
                    //alert con error
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json;");
                    return headers;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    9000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyVolleySingleton.getInstance().addToRequestQueue(jsonObjReq);
        }
        else{
            Log.d(TAG,"No token");
        }
    }
    public void getuserprofile(String token) {
        final String Ukey = token;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AHR_USER_PROFILE_JSON_API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response",response.toString());
                            JSONObject object = response;
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("profile_id", object.optString("profile_id")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("username", object.optString("username")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("email", object.optString("email")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("userid", object.optString("id")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("zone", object.optString("zone")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("grada", object.optString("grada")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("section", object.optString("section")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("fila", object.optString("fila")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("seat", object.optString("seat")).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("speed_lover", object.optString("speed_lover")).commit();
                            Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                            startActivity(myIntent);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                NetworkResponse response = error.networkResponse;

                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            json = new String(response.data);
                            try{
                                JSONObject obj = new JSONObject(json);
                                Log.d(TAG,obj.toString());
                                if(obj.has("detail")){
                                    if(obj.optString("detail")!=null){
                                        Toast.makeText(getApplicationContext(),obj.optString("detail"),Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                            break;
                        default:

                            break;
                    }
                    //Additional cases
                }

            }
        }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json;");
                headers.put("Authorization", "Token "+ Ukey);

                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyVolleySingleton.getInstance().addToRequestQueue(jsonObjReq);
    }

}