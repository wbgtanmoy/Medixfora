package com.brandhype.medixfora;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.brandhype.medixfora.Utils.LoginPreferences;
import com.brandhype.medixfora.marshmallowpermission.MarshMallowPermissions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.google.android.gms.ads.MobileAds;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {





    private static final int SPLASHSCREEN_DELAY = 2000; // millisecond
    MarshMallowPermissions marshMallowPermissions;

    SharedPreferences sharedPreferences1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Log.d("@ SPLASH ACTIVITY  ", ".............. splash activity");







        final LoginPreferences loginPrefs = new LoginPreferences();
        sharedPreferences1 = getSharedPreferences(loginPrefs.login_Pref_key, Context.MODE_PRIVATE);

        if (sharedPreferences1.getBoolean(LoginPreferences.login_Status, false)) {

            String login_type=sharedPreferences1.getString(LoginPreferences.login_type, "");

                if(login_type.equals("Patient")) {

                    /*try {
                        //---------if FCM notification is received--------------
                        Log.d("@ FCM splash", "Splashactivity : App is in Background");
                        Log.d("@ FCM splash", "fcm_title " + getIntent().getExtras().get("post_title"));
                        Log.d("@ FCM splash", "fcm_id " + getIntent().getExtras().get("post_id"));
                        Log.d("@ FCM splash", "fcm_msg " + getIntent().getExtras().get("post_msg"));

                        Intent intent = new Intent(this, MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("query_reply_text", getIntent().getExtras().get("post_msg").toString());
                        intent.putExtras(b);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        return;

                    }catch(Exception e){ e.printStackTrace();}*/


                    String uname = sharedPreferences1.getString(LoginPreferences.first_name, "");
                    String user_id = sharedPreferences1.getString(LoginPreferences.patient_id, "");
                    Toast.makeText(getBaseContext(), "Welcome: " + uname, Toast.LENGTH_SHORT).show();
                    Log.d("@ user ", uname + " id " + user_id);
                    Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main_intent);
                    finish();

                }else if(login_type.equals("Doctor")){

                    String uname = sharedPreferences1.getString(LoginPreferences.doctor_name, "");
                    String user_id = sharedPreferences1.getString(LoginPreferences.doctor_id, "");
                    Toast.makeText(getBaseContext(), "Welcome Doctor: " + uname, Toast.LENGTH_SHORT).show();
                    Log.d("@ user ", uname + " id " + user_id);
                    Intent main_intent = new Intent(getApplicationContext(), MainActivityDoctor.class);
                    startActivity(main_intent);
                    finish();
                }

        } else {

            marshMallowPermissions = new MarshMallowPermissions(this);
            checkNecessaryPermissions();
            //navigateToApp();
        }

    }

    private void checkNecessaryPermissions() {
        if (marshMallowPermissions.checkPermissionForFineLocation()) {
            //do your task--------------
            Log.d("Permission:","Fine location permission is already given");
            navigateToApp();
        } else {
            Log.d("Permission:","requesting Fine location permission");
            marshMallowPermissions.requestPermissionForFineLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MarshMallowPermissions.FINE_LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Permission:","Fine location permission aquired");
                    navigateToApp();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public void navigateToApp()
    {
        //--small to big animation & reverse----
        ImageView imageView = (ImageView) findViewById(R.id.login_image);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
        imageView.startAnimation(animation);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 2000 ms */

                Intent login_intent = new Intent(getApplicationContext(), BannerActivityNew.class);
                //Intent login_intent = new Intent(getApplicationContext(), BannerActivity.class);
                //Intent login_intent = new Intent(getApplicationContext(), FeatureActivity.class);
                startActivity(login_intent);
                finish();
            }
        }, SPLASHSCREEN_DELAY);

    }



}
