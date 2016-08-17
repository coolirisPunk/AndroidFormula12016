package com.punkmkt.formula12016.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.punkmkt.formula12016.R;

/**
 * Created by germanpunk on 17/08/16.
 */
public class SocialHubFragment extends Fragment {
    private WebView mWebView;
    private final String AHZ_URL_SOCIAL_HUB = "http://104.236.3.158:82/social-hub/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_social_hub, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview);
        mWebView.loadUrl(AHZ_URL_SOCIAL_HUB);
        return v;
    }

}
