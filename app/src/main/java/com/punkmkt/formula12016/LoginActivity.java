package com.punkmkt.formula12016;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton loginButton;
    String TAG = LoginActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess");
                Log.d(TAG,loginResult.getAccessToken().toString());
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
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Login error"+exception.getMessage().toString(), Toast.LENGTH_SHORT).show();

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
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
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
}
