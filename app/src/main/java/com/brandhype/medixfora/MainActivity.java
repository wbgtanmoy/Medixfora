package com.brandhype.medixfora;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.LoginPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.UploadCartItemAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.database.DataBaseHandler;
import com.brandhype.medixfora.fragments.AboutusFragment;
import com.brandhype.medixfora.fragments.AskMeAnyFragment;
import com.brandhype.medixfora.fragments.CareathomeListFragment;
import com.brandhype.medixfora.fragments.CartFragment;
import com.brandhype.medixfora.fragments.CompanyProfileFragment;
import com.brandhype.medixfora.fragments.ContactUsFragment;
import com.brandhype.medixfora.fragments.DashboardFragment;
import com.brandhype.medixfora.fragments.DiagonisticFragment;
import com.brandhype.medixfora.fragments.FeedbackFragment;
import com.brandhype.medixfora.fragments.FragmentDrawer;
import com.brandhype.medixfora.fragments.HealthtipsFragment;
import com.brandhype.medixfora.fragments.MedicaltourismFragment;
import com.brandhype.medixfora.fragments.MyPrescriptionFragment;
import com.brandhype.medixfora.fragments.MyRequestsFragment;
import com.brandhype.medixfora.fragments.PatientAppointmentFragment;
import com.brandhype.medixfora.fragments.PatientOrdersFragment;
import com.brandhype.medixfora.fragments.PatientProfileFragment;
import com.brandhype.medixfora.fragments.PharmacyListFragment;
import com.brandhype.medixfora.fragments.QueryReplyFragment;
import com.brandhype.medixfora.fragments.SeeksecondopinionFragment;
import com.brandhype.medixfora.fragments.SettingsFragment;
import com.brandhype.medixfora.fragments.TermsConditionFragment;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.AppointmentItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    public Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    //FloatingActionButton fab;
    DrawerLayout drawerLayout;

    private int hot_number = 0;
    private TextView ui_hot = null;

    TextView tv, tv2;
    ActionBarDrawerToggle mDrawerToggle;

    private static final int SDCARD_PERMISSION_FILE_DEF = 321;
    private static final int ACTIVITY_CHOOSE_FILE = 3;
    private static final int REQUEST_PHONE_CALL = 1;

    private static final String MEDIXFORA_PHONE_NO = "911244119596"; //to add+ at begining in final build

    Fragment fragment = null;

    public DataBaseHandler databaseHandler;
    FloatingActionButton floatHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }*/

        floatHome = (FloatingActionButton) findViewById(R.id.floatHome);
        floatHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        floatHome.setOnTouchListener(new View.OnTouchListener() {

            float dX;
            float dY;
            int lastAction;
            float startX;
            float startRawX;
            float distanceX;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = view.getX() - event.getRawX();
                        startRawX = event.getRawX();
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        Log.e("@ action", "Down");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        Log.e("@ action", "Move");
                        break;

                    case MotionEvent.ACTION_UP:
                        distanceX = event.getRawX() - startRawX;
                        if (Math.abs(distanceX) < 5) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //commonutils.setFragmentPatient(MainActivity.this,new DashboardFragment(),null,R.string.dashboard,true);
                            displayView(0);
                        }
                        if (lastAction == MotionEvent.ACTION_MOVE) {
                            //Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
                            //showQueryDialog();
                            Log.e("@ action", "up");
                        }
                        break;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        Log.e("@ action", "pressed");
                        //Toast.makeText(context, "pressed!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.m_logo_3);


        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, 0, 0);
        /*mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.drawable.nav_drawer, 0, 0) {
             public void onDrawerClosed(View view) {  }
             public void onDrawerOpened(View drawerView) {  }
        };*/

        drawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.d("BackStackCount:>>", "backstackcount: " + backStackEntryCount);

                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    mDrawerToggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.back2); //set your own
                    mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  on Back key Pressed
                            onBackPressed();
                        }
                    });
                } else {
                    //Toast.makeText(getApplicationContext(), "backstackcount: "+backStackEntryCount, Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    //mDrawerToggle.setHomeAsUpIndicator(R.drawable.nav_drawer);
                }
                mDrawerToggle.syncState();
            }
        });


        try {
            databaseHandler = DataBaseHandler.getInstance(this);
            databaseHandler.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCartCountFromServer();
        getNotificationCountFromServer();

        try {
            //---------when called from notification-----------
            String fcm_msg = getIntent().getExtras().getString("query_reply_text");
            Log.d("@ fcm message  : ", " mainactivity: " + fcm_msg);
            Fragment fragment = new QueryReplyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("fcm_message", fcm_msg);
            fragment.setArguments(bundle);
            commonutils.setFragmentPatient(this, fragment, bundle, R.string.title_query_reply, true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//on create

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //------------for cart icon-----------------------
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.actionbar_cart_icon);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        tv = (TextView) notifCount.findViewById(R.id.cart_count_txt);
        tv.setText("0");
        RelativeLayout cart_layout = (RelativeLayout) notifCount.findViewById(R.id.cart_layout);

        //-- ON CART ICON CLICK ON ACTIONBAR
        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Cart count :"+tv.getText().toString(), Toast.LENGTH_SHORT).show();


                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                Fragment fragment = new CartFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    //-------------clears fragments in stack
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//**
                    displayView(0);//resets to dashboard
                    ///-------------------------------------
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    getSupportActionBar().setTitle(R.string.title_cart);
                }
            }
        });

        //---------------ON NOTIFICATION ICON CLICK ON ACTIONBAR--------------------------
        MenuItem item2 = menu.findItem(R.id.action_tips_notify);
        MenuItemCompat.setActionView(item2, R.layout.actionbar_notification_icon);
        RelativeLayout notifCount2 = (RelativeLayout) MenuItemCompat.getActionView(item2);

        tv2 = (TextView) notifCount2.findViewById(R.id.notification_count_txt);
        tv2.setText("0");
        RelativeLayout notify_layout = (RelativeLayout) notifCount2.findViewById(R.id.notification_layout);

        notify_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Cart count :"+tv.getText().toString(), Toast.LENGTH_SHORT).show();
                Fragment fragment = new QueryReplyFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    //-------------clears fragments in stack
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);//**
                    displayView(0);//resets to dashboard
                    ///-------------------------------------
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    getSupportActionBar().setTitle(R.string.title_query_reply);
                }
            }
        });

        //return true;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
            patientLogout();
            return true;
        }

        if (id == R.id.action_home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            displayView(0);
            return true;
        }

        /*if(id == R.id.action_share){
            Toast.makeText(getApplicationContext(), "Sharing", Toast.LENGTH_SHORT).show();
            return true;
        }

        if(id == R.id.action_ratenow){
            Toast.makeText(getApplicationContext(), "Rating", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        /*if(id == R.id.action_cart){

            Toast.makeText(getApplicationContext(), "Cart clicked", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    private void patientLogout() {
        //------------------------Dialog---------------------------------------------

        final Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog);

        Button _cancel = (Button) alertDialog.findViewById(R.id.btn_no);
        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);

        _yes.setEnabled(true);
        _cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //--Do nothing
            }
        });

        _yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                //**************perform logout , clear prference ************
                LoginPreferences loginPrefs = new LoginPreferences();
                SharedPreferences login_shared_preferences = getSharedPreferences(loginPrefs.login_Pref_key, Context.MODE_PRIVATE);
                SharedPreferences.Editor login_status_editor;
                login_status_editor = login_shared_preferences.edit();

                login_status_editor.putBoolean(loginPrefs.login_Status, false).commit();
                login_status_editor.putString(loginPrefs.patient_id, "").commit();
                login_status_editor.putString(loginPrefs.first_name, "").commit();
                login_status_editor.putString(loginPrefs.last_name, "").commit();
                login_status_editor.putString(loginPrefs.email, "").commit();


                AppPreferences pref = new AppPreferences(getApplicationContext());
                pref.removePreferences(AppPreferences.Storage.PATIENTDATA.name());

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                //finish();
                ActivityCompat.finishAffinity(MainActivity.this);//closes all activity
                //**************
            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();

        //----------------------------------------------------------------------

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //-to show iocn with action bar in drop down menu
        //if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e(TAG, "onMenuOpened", e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        //-------------clears fragments in stack
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // --------------shows dashboard for patient---
        commonutils.setFragmentPatient(this, new DashboardFragment(), null, R.string.dashboard, true);
        displayView(position);
    }

    private void displayView(int position) {

        String title = getString(R.string.app_name);
        Bundle bundle;
        fragment = null;

        //Toast.makeText(this,"Position"+position,Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                fragment = new DashboardFragment();
                title = getString(R.string.title_dashboard);
                break;

            case 1:
                fragment = new AboutusFragment();
                title = getString(R.string.title_aboutus);
                break;

            case 2:
                //My profile
                //fragment = new MyprofileFragment();
                //fragment = new PatientEditprofileFragment();
                fragment = new PatientProfileFragment();
                title = getString(R.string.title_profile);
                break;

            case 3:
                //My Orders
                //Toast.makeText(this,"My Orders: Under development",Toast.LENGTH_SHORT).show();
                fragment = new PatientOrdersFragment();
                title = getString(R.string.title_order);
                break;

            case 4:
                //E prescription
                fragment = new MyPrescriptionFragment();
                title = getString(R.string.title_myprescription);
                break;

            case 5:
                //My Request for care-at-home
                fragment = new MyRequestsFragment();
                title = getString(R.string.title_myrequest);
                break;

            /*case 6:
                // My report
                Toast.makeText(this,"My Report: Under development",Toast.LENGTH_SHORT).show();
                break;*/

            case 6:
                //My Appointment
                fragment = new PatientAppointmentFragment();
                title = getString(R.string.title_patient_appointment);
                break;

            case 7:
                //contact us
                showContactDialog();
                /*fragment = new ContactUsFragment();
                title = getString(R.string.title_contactus);*/
                break;

                //----------------------------------------------------
            case 8:
                //fragment = new PharmacyFragment();
                fragment = new PharmacyListFragment();
                bundle = new Bundle();
                bundle.putString("listType", "category");
                fragment.setArguments(bundle);
                title = getString(R.string.title_pharmacy);
                break;

            case 9:
                fragment = new SeeksecondopinionFragment();
                title = getString(R.string.title_seeksecondopinion);
                break;

            case 10:
                fragment = new DiagonisticFragment();
                title = getString(R.string.title_diagonistic);
                break;

            case 11:
                //fragment = new CareathomeFragment();
                fragment = new CareathomeListFragment();
                bundle = new Bundle();
                bundle.putString("listType", "category");
                fragment.setArguments(bundle);
                title = getString(R.string.title_careathome);
                break;

            case 12:
                fragment = new HealthtipsFragment();
                title = getString(R.string.title_queries);
                break;

            case 13:
                fragment = new MedicaltourismFragment();
                title = getString(R.string.title_medicaltourism);
                break;

            //------------------------------------------------------------
            case 14:
                //logout
                patientLogout();
                break;

            case 15:
                //settings
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;

            case 16:
                //feedback
                fragment = new FeedbackFragment();
                title = getString(R.string.title_feedback);
                break;

            case 17:
                // Terms & Condition
                fragment = new TermsConditionFragment();
                title = getString(R.string.title_terms);
                break;

            case 18:
                // company profile
                fragment = new CompanyProfileFragment();
                title = getString(R.string.title_company_profile);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "Mainactivity: onActivityResult", Toast.LENGTH_SHORT).show();
        Log.d("@ Mainactivity", "*** onActivityResult *** " + "Result Code " + resultCode + "Request Code " + requestCode);
    }


    public void getCartCountFromServer() {
        AppPreferences pref;
        JSONObject patient;
        String user_id = "0";

        try {
            pref = new AppPreferences(getApplicationContext());
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id, patient_first_name, patient_last_name, patient_email, patient_contact, patient_gender;
            patient_id = patient.getString("id");
            user_id = patient.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/custom_cart/number_of_cart_items/apitoken/1edc0ae98198866510bce219d5115b72/uid/22
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/number_of_cart_items/apitoken/" + token + "/uid/" + user_id;
        Log.d("@ cart server url : ", url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(this, 0) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("@ NUMBER", " OF CART LIST response " + result.toString());

                if (result.equals("")) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MainActivity.this, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "Cart not found");
                        }
                    });
                    return;
                }

                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));
                    final String number_of_cart_items = resultJson.getString("number_of_cart_items");


                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(number_of_cart_items);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Cart  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error", "Cart list fetching failed" + err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);//parallel
            } else {
                submitAsync.execute(url);//serial
            }
        }
    }


    public void getNotificationCountFromServer() {
        AppPreferences pref;
        JSONObject patient;
        String user_id = "0";

        try {
            pref = new AppPreferences(getApplicationContext());
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id, patient_first_name, patient_last_name, patient_email, patient_contact, patient_gender;
            patient_id = patient.getString("id");
            user_id = patient.getString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = NetworkIOConstant.CS_Token.TOKEN;

        //http://brandhypedigital.in/demo/medixfora/restapi/health_tips/get_unread_notifications/apitoken/1edc0ae98198866510bce219d5115b72/patient_id/22
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "health_tips/get_unread_notifications/apitoken/" + token + "/patient_id/" + user_id;
        Log.d("@ notification url : ", url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(this, 0) { //0 for no progress bar
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("@ NUMBER", " of notification response " + result.toString());

                if (result.equals("")) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MainActivity.this, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "Notification not found");
                        }
                    });
                    return;
                }

                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));
                    final String number_of_notification_items = resultJson.getString("result");


                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("@ notification count ", number_of_notification_items);
                                tv2.setText(number_of_notification_items);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Cart  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error", "Notification list fetching failed" + err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);//parallel
            } else {
                submitAsync.execute(url);//serial
            }
        }
    }


    public void addItemToCartMain(final String itemid, final String qty, final String types, final String itemName) {
        //------------------------Dialog---------------------------------------------
        final Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog);

        TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);

        String itemname = "";
        if (itemName.length() > 20)
            itemname = itemName.substring(0, 20) + "..";
        else
            itemname = itemName;

        txt_main.setText("Sure to add  " + itemname + " Item to cart ?.");

        Button _cancel = (Button) alertDialog.findViewById(R.id.btn_no);
        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);

        _yes.setEnabled(true);
        _cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //--Do nothing
            }
        });
        _yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //**************perform cart add operation************
                addItemToCartMainFinal(itemid, qty, types);
                //****************************************************
            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
        //----------------------------------------------------------------------
    }

    public void addItemToCartMainFinal(String itemid, String qty, String types) {
        final Context context = this;

        String user_id = "0";
        AppPreferences pref3 = new AppPreferences(context);
        AppointmentItem appointmentItem3 = new AppointmentItem();

        AppPreferences pref;
        JSONObject patient;

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id, patient_first_name, patient_last_name, patient_email, patient_contact, patient_gender;
            user_id = patient.getString("id");

        } catch (Exception e) {
            e.printStackTrace();
        }


        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/add_to_cart/";
        //String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "my_cart/add_to_cart/";
        String token = NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("item_id", itemid);
        postDataParams.put("item_qty", qty);
        postDataParams.put("itemtype", types); // itm for others
        postDataParams.put("user_id", user_id);

        JSONObject postJson = new JSONObject(postDataParams);
        Log.d("@ hitting  at : ", "add to cart : " + url);
        Log.d("@ cart params ", "add to cart: " + postJson.toString());


        //UploadCartItemAsynctask(Context context, HashMap<String, String> map,String filename, String url, AsyncResponse delegate){

        UploadCartItemAsynctask uploadcartitemAsynctask = new UploadCartItemAsynctask(context, postDataParams, "", url, new AsyncResponse() {

            @Override
            public void processFinish(String result) {
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  add to cart", result);
                //--------------------------------------------------------
                try {
                    Log.d("@ server response ", result.toString());

                    if (result.equals("")) {
                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = commonutils.replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                Log.d("Success", "Item Added to Cart");


                                //------------------------Dialog---------------------------------------------
                                final Dialog alertDialog2 = new Dialog(MainActivity.this);
                                alertDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                alertDialog2.setCancelable(false);
                                alertDialog2.setContentView(R.layout.custom_alertdialog_cart);

                                TextView txt_main = (TextView) alertDialog2.findViewById(R.id.txt_main);
                                //txt_main.setText("Item added to cart.");

                                Button _cancel = (Button) alertDialog2.findViewById(R.id.btn_no);
                                Button _yes = (Button) alertDialog2.findViewById(R.id.btn_yes);

                                //_cancel.setText("Continue Shopping");
                                //_yes.setText("Checkout");

                                _yes.setEnabled(true);
                                _cancel.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        alertDialog2.dismiss();
                                        Fragment fragment = new CartFragment();
                                        if (fragment != null) {
                                            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container_body, fragment);
                                            //fragmentTransaction.addToBackStack(null); //**
                                            fragmentTransaction.commit();
                                            // set the toolbar title
                                            ((AppCompatActivity) context).getSupportActionBar().setTitle(R.string.cart);
                                        }
                                    }
                                });
                                _yes.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        alertDialog2.dismiss();

                                    }
                                });

                                if (alertDialog2 != null && !alertDialog2.isShowing())
                                    alertDialog2.show();
                                //----------------------------------------------------------------------


                            }
                        });

                    } else {
                        ((MainActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Add To Cart Failed:" + err, Toast.LENGTH_SHORT).show();
                                Log.e("Error", "Add To Cart Failed: " + err);
                            }
                        });

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }

                //-----------------------------------

            }
        });


        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                uploadcartitemAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                uploadcartitemAsynctask.execute();//serial
            }
        }
    }//addItemToCart


    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("BackStackCount:>>", "backstackcount: " + backStackEntryCount);
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            Log.v("MyApp", "onDestroy");
            databaseHandler.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showContactDialog() {
        //------------------------Dialog---------------------------------------------
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog_contactus);

        TextView txt_main = (TextView) dialog.findViewById(R.id.txt_main);

        Button btn_email = (Button) dialog.findViewById(R.id.btn_email);
        Button btn_call = (Button) dialog.findViewById(R.id.btn_call);
        Button btn_more = (Button) dialog.findViewById(R.id.btn_more);

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendEmail();
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isPermissionGranted()) {
                    //call_action();
                    showPhnDialog();
                }
            }
        });

        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                commonutils.setFragmentPatient(MainActivity.this, new ContactUsFragment(), null, R.string.title_contactus, true);
            }
        });


        ImageView btn_close = (ImageView) dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (dialog != null && !dialog.isShowing())
            dialog.show();
        //-----------------------------------------------------
    }


    private void showPhnDialog() {
        //------------------------Dialog---------------------------------------------
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog_call);

        TextView txt_main = (TextView) dialog.findViewById(R.id.txt_main);
        txt_main.setText(" Call to "+ MEDIXFORA_PHONE_NO);

        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                call_action();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        if (dialog != null && !dialog.isShowing())
            dialog.show();
        //-----------------------------------------------------
    }


    public boolean isPermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public void call_action() {
        String phnum = MEDIXFORA_PHONE_NO;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phnum));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {

            case REQUEST_PHONE_CALL:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    //call_action();
                    showPhnDialog();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //Show permission explanation dialog...
                        Toast.makeText(getApplicationContext(), "Should accept call permission to make a call.", Toast.LENGTH_SHORT).show();
                    }else{
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //So, disable that feature, or fall back to another situation...
                    }
                }
                return;
            }
        }
    }

    private void sendEmail()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@medixfora.com"});
        //intent.putExtra(Intent.EXTRA_CC, new String[]{"seesee@gmail.com"}); // if necessary
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Issue");
        intent.putExtra(Intent.EXTRA_TEXT   , "Please type issue here ...");
        intent.setType("message/rfc822");


        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
