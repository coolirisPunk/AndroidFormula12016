package com.punkmkt.formula12016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class SocialHubActivity extends AppCompatActivity {
    private WebView mWebView;
    private final String AHZ_URL_SOCIAL_HUB = "http://104.236.3.158:82/social-hub/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_hub);
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.loadUrl(AHZ_URL_SOCIAL_HUB);
    }
}
