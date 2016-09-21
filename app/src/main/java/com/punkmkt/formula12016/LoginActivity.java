package com.punkmkt.formula12016;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
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
import com.punkmkt.formula12016.adapters.ConfAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;
    String TAG = LoginActivity.class.getName();
    String AHR_LOGIN_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/login/";
    String AHR_LOGIN_FACEBOOK_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/facebook/";
    String AHR_USER_PROFILE_JSON_API_URL = "http://104.236.3.158:82/api/auth/rest-auth/user-profile/user/";

    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public Activity activity;
    String intent_from = null;
    String current_token = "";
    String INTENT_FROM_CONF = "CONF";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String shared_token = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("access_token", null);
        Intent intent = getIntent();
        String inf = intent.getStringExtra("intent_from");
        //Toast.makeText(getApplicationContext(), inf, Toast.LENGTH_SHORT).show();
        if(inf!="" && inf!=null){
            Log.d(TAG,"Intent from config");
            intent_from = inf;
        }
        if (shared_token!=null){
         //   Toast.makeText(getApplicationContext(), shared_token, Toast.LENGTH_SHORT).show();
            current_token = shared_token;
            //Intent intent_home = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(intent_home);
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());


        callbackManager = CallbackManager.Factory.create();

        activity = this;

        setContentView(R.layout.activity_login);
        disconnectFromFacebook();
        Button entrar = (Button) findViewById(R.id.entrar);
        TextView cancelar = (TextView) findViewById(R.id.cancelar_action);
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        //mUsernameView.setText("germanpunk");
        //mPasswordView.setText("cooliris");
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

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

        Button register_sign_in_button = (Button) findViewById(R.id.register_sign_in_button);
        TextView forgot_password = (TextView) findViewById(R.id.forgot_password);
        register_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(intent);
            }
        });

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

    private void attemptLogin() {

        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            JSONObject js = new JSONObject();

            try {
                js.put("username", username);
                js.put("password", password);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("json",js.toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    AHR_LOGIN_JSON_API_URL, js,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("Response",response.toString());
                                JSONObject object = response;
                                if (object.has("key")){
                                    showProgress(false);
                                    String key = object.optString("key");
                                    Log.d(TAG,key);
                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("access_token", key).commit();
                                    getuserprofile(key);

                                    if(intent_from == null){
                                        Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(myIntent);
                                    }
                                    else {
                                        if (intent_from.equalsIgnoreCase(INTENT_FROM_CONF)) {
                                            Intent myIntent = new Intent(getApplicationContext(), UbicaTuAsientoActivity.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(myIntent);
                                        } else {
                                            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(myIntent);
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Login error key basic login", Toast.LENGTH_SHORT).show();
                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                //alert con error
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                                showProgress(false);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("V", "Error: " + error.getMessage());
                    //alert con error
                    //mUsernameView.setError(getString(R.string.error_incorrect_password));

                    showProgress(false);
                    String json = null;
                    NetworkResponse response = error.networkResponse;

                    if(response != null && response.data != null){
                        switch(response.statusCode){
                            case 400:
                                json = new String(response.data);
                                try{
                                    JSONObject obj = new JSONObject(json);
                                    Log.d(TAG,obj.toString());
                                    if(obj.has("non_field_errors")){
                                        if(obj.optString("non_field_errors")!=null){
                                            JSONArray array_object = obj.getJSONArray("non_field_errors");
                                            for (int count = 0; count < array_object.length(); count++) {
                                                //Toast.makeText(getApplicationContext(),array_object.get(count).toString(),Toast.LENGTH_SHORT).show();
                                                mUsernameView.setError(array_object.get(count).toString());
                                                mUsernameView.requestFocus();
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
            //mAuthTask = new UserLoginTask(username, password);
            //mAuthTask.execute((Void) null);
        }
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

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
                                        Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
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
