package com.punkmkt.formula12016;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotActivity extends AppCompatActivity {
    String AHR_FORGOT_PASSWORD = "http://104.236.3.158:82/api/auth/rest-auth/password/reset/";
    private EditText mEmailView;
    String TAG = ForgotActivity.class.getName();
    ProgressDialog progressDialog;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        Button saltar = (Button)findViewById(R.id.saltar);
        Button guardar_cambios_action = (Button)findViewById(R.id.guardar_cambios_action);
        mEmailView = (EditText) findViewById(R.id.form_email);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        saltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        guardar_cambios_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(ForgotActivity.this);
                progressDialog.setMessage("Cargando...");
                progressDialog.show();

                mEmailView.setError(null);
                final String email = mEmailView.getText().toString();
                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                }
                if(!isValidEmail(email)){
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                    cancel = true;
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    progressDialog.dismiss();
                    focusView.requestFocus();

                } else {
                    JSONObject js = new JSONObject();
                    try {
                        js.put("email", email);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            AHR_FORGOT_PASSWORD, js,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.d("Response",response.toString());
                                        JSONObject object = response;
                                        if (object.has("success")){
                                            Intent myIntent = new Intent(getApplicationContext(), ThanksActivity.class);
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(myIntent);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Error al intentar recuperar la contraseña.", Toast.LENGTH_SHORT).show();
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
            }
        });
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
