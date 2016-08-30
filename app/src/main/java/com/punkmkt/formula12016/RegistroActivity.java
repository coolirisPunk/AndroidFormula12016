package com.punkmkt.formula12016;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.log4j.chainsaw.Main;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;
    String AHR_REGISTER_URL = "http://104.236.3.158:82/api/auth/rest-auth/registration/";
    String TAG = RegistroActivity.class.getName();
    String intent_from = null;
    private CoordinatorLayout coordinatorLayout;
    String current_token = "";
    String INTENT_FROM_CONF = "CONF";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        String shared_token = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("access_token", null);
        Intent intent = getIntent();
        String inf = intent.getStringExtra("intent_from");
        if(inf!=""){
            Log.d(TAG,"Intent from config");
            intent_from = inf;
        }
        if (shared_token!=null){
            Toast.makeText(getApplicationContext(), shared_token, Toast.LENGTH_SHORT).show();
            current_token = shared_token;
            Intent intent_home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent_home);
        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        Button cancelar = (Button) findViewById(R.id.cancelar_action);
        Button guardar_cambios_action = (Button) findViewById(R.id.guardar_cambios_action);
        final EditText form_name = (EditText) findViewById(R.id.form_name);
        final EditText form_password = (EditText) findViewById(R.id.form_password);
        final EditText form_password_match = (EditText) findViewById(R.id.form_password_match);
        final EditText form_email = (EditText) findViewById(R.id.form_email);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,loginResult.toString());
                GraphRequest request = new GraphRequest(
                        loginResult.getAccessToken(),
                        "/"+loginResult.getAccessToken().getUserId(),
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                                try {
                                    JSONObject object = response.getJSONObject();
                                    Log.d(TAG,object.toString());
                                    if(object.has("name") && !object.optString("name").equals("null")){
                                        form_name.setText(object.optString("name"));
                                    }
                                    if(object.has("email") && !object.optString("email").equals("null")){
                                        form_email.setText(object.optString("email"));
                                    }
                                } catch (FacebookException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,age_range,link,gender,birthday,email,locale");
                request.setParameters(parameters);
                request.executeAsync();
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
                                        Log.d("Response",response.toString());
                                        JSONObject object = response;
                                        if (object.has("key")){
                                            String key = object.optString("key");
                                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putString("access_token", key).commit();
                                            if(intent_from.equals(INTENT_FROM_CONF)){
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
                                        else {
                                            Toast.makeText(getApplicationContext(), "Error al registrarse.", Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("V", "Error: " + error.getMessage());
                            //alert con error
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
                }                 // There was an error; don't attempt login and focus the first
                    // form field with an error.

                // If there is a validation error, display the error
              //  if (validationError) {
                    //Toast.makeText(getApplicationContext(), validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    //.show();
                   // Snackbar snackbar = Snackbar
                      //      .make(coordinatorLayout, validationErrorMessage.toString(), Snackbar.LENGTH_LONG);
                  //  snackbar.setActionTextColor(Color.RED);

                  //  snackbar.show();
                    //Crouton.makeText(getActivity(), validationErrorMessage.toString(), Style.ALERT).show();
                 //   return;
               // }
              //  else{

                //}

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
        return password.length() > 4;
    }
}
