package com.brandhype.medixfora;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.brandhype.medixfora.Utils.commonutils;

public class ChatActivity extends Activity {

    Context context;
    Button skip;
    WebView htmlWebView;
    WebSettings webSetting;
    String chatURL = "http://medixfora.com.md-in-64.webhostbox.net/home/account/tawk_to";
    ProgressDialog prDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        prDialog = new ProgressDialog(this);


        skip = (Button) findViewById(R.id.btnSkip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        htmlWebView = (WebView) findViewById(R.id.webView);
        htmlWebView.setWebViewClient(new CustomWebViewClient());
        webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);
        htmlWebView.loadUrl(chatURL);
    }

    private class CustomWebViewClient extends WebViewClient {

       /* @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }*/

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog = new ProgressDialog(ChatActivity.this);
            prDialog.setMessage("Please wait ...");
            prDialog.setCanceledOnTouchOutside(true);
            prDialog.show();
            commonutils.hideKeyboard2(ChatActivity.this);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (prDialog != null) {
                prDialog.dismiss();
            }

        }

    }



}