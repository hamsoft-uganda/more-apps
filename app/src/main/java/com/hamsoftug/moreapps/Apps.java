package com.hamsoftug.moreapps;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Apps extends AppCompatActivity {

    private WebView webView = null;
    private String url = "https://play.google.com/store/search?q=hamsoft%20uganda%20(hamsoftug.com)";
    private String loaded_title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = (WebView) findViewById(R.id.web_view);

        webView.setWebChromeClient(new CustomWebChromeClient());

        webView.setWebViewClient(new CustomWebView());

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if(isNetworkStatusAvialable(getApplicationContext())) {
            webView.loadUrl(url);
        } else {
            Snackbar.make(webView,"No internet connection", Snackbar.LENGTH_INDEFINITE).show();
        }

    }

    class CustomWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            //Make the bar disappear after URL is loaded, and changes string to Loading...
            Apps.this.setTitle("Please wait.. . "+newProgress+"%");
            Apps.this.setProgress(newProgress * 100); //Make the bar disappear after URL is loaded

            // Return the app name after finish loading
            if(newProgress == 100) {
                String tt = getResources().getString(R.string.app_name);
                if(loaded_title.length()<1) {
                    Apps.this.setTitle(tt);
                } else {
                    Apps.this.setTitle(loaded_title);
                }
            }

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            loaded_title = title;
        }
    }

    class CustomWebView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(isNetworkStatusAvialable(getApplicationContext())) {

                if (Uri.parse(url).getHost().contains("hamsoft")) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    finish();
                    return true;
                }
            } else {
                Snackbar.make(webView,"No internet connection",Snackbar.LENGTH_INDEFINITE).show();
                return true;
            }

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            view.loadUrl("about:blank");
            Snackbar.make(webView,description,Snackbar.LENGTH_INDEFINITE).show();
        }

    }

    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                if(netInfos.isConnected())
                    return true;
        }
        return false;
    }

}
