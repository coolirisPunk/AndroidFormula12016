package com.punkmkt.formula12016.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.punkmkt.formula12016.R;
import com.punkmkt.formula12016.utils.NetworkUtils;

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
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //hide loading image
                v.findViewById(R.id.imageLoading).setVisibility(View.GONE);
                //show webview
                v.findViewById(R.id.webview).setVisibility(View.VISIBLE);
            }


        });
        if (NetworkUtils.haveNetworkConnection(getActivity().getApplicationContext())){
            mWebView.loadUrl(AHZ_URL_SOCIAL_HUB);
        }
        else{

        }

        return v;
    }

}
