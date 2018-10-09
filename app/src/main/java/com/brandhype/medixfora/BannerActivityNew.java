package com.brandhype.medixfora;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class BannerActivityNew extends AppCompatActivity {


    private InterstitialAd mInterstitialAd;

    AdView  mAdView ;


    Button skip;
    WebView htmlWebView ;
    WebSettings webSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_new);


        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        /*MobileAds.initialize(this, "ca-app-pub-8045437937582584~8963720063");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");--def
        banner add: ca-app-pub-3940256099942544/6300978111 -def
        */

        /*MobileAds.initialize(this, "ca-app-pub-8045437937582584~8963720063");
        //Interstitials
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8045437937582584/9967069725");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("@ AdMob", "The interstitial wasn't loaded yet.");
                }
            }
        });*/

        //---------------------banner add
       /* mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/


        skip = (Button) findViewById(R.id.btnSkip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent _intent = new Intent(getActivity(), SignupActivity.class);
                Intent _intent = new Intent(BannerActivityNew.this, LoginActivity.class);
                startActivity(_intent);
                finish();

            }
        });

        htmlWebView = (WebView)findViewById(R.id.webView);
        htmlWebView.setWebViewClient(new CustomWebViewClient());
        webSetting = htmlWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDisplayZoomControls(true);

        //http://brandhypedigital.in/demo/medixfora/home/slider_banner
        htmlWebView.loadUrl("http://brandhypedigital.in/demo/medixfora/home/slider_banner");

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
    }

}
