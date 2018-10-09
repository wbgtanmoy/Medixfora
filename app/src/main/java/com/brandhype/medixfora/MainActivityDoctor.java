package com.brandhype.medixfora;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.LoginPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.fragments.DashboardFragmentDoctor;
import com.brandhype.medixfora.fragments.DoctorAppointmentFragment;
import com.brandhype.medixfora.fragments.DoctorProifileFragment;
import com.brandhype.medixfora.fragments.DoctorSettingsFragment;
import com.brandhype.medixfora.fragments.DoctorscheduleFragment;
import com.brandhype.medixfora.fragments.FragmentDrawerDoctor;

import java.lang.reflect.Method;

//import android.support.v4.app.ActionBarDrawerToggle;

public class MainActivityDoctor extends AppCompatActivity implements FragmentDrawerDoctor.FragmentDrawerListener {

    private static String TAG = MainActivityDoctor.class.getSimpleName();

    public Toolbar mToolbar;
    private FragmentDrawerDoctor drawerFragment;
    //FloatingActionButton fab;
    DrawerLayout drawerLayout;

    private int hot_number = 0;
    private TextView ui_hot = null;

    TextView tv;
    ActionBarDrawerToggle mDrawerToggle;

    private static final int SDCARD_PERMISSION_FILE_DEF=321;
    private static final int ACTIVITY_CHOOSE_FILE = 3;

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_doctor);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_doctor);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().hide(); //full screen for activity with action bar


        drawerFragment = (FragmentDrawerDoctor) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_doctor);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_doctor, (DrawerLayout) findViewById(R.id.drawer_layout_doctor), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_doctor);

        mDrawerToggle = new ActionBarDrawerToggle (this, drawerLayout, mToolbar, 0, 0);
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
                Log.d("BackStackCount:>>", "backstackcount: "+backStackEntryCount);

                if(getSupportFragmentManager().getBackStackEntryCount() > 1){

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
                }
                else {
                    //Toast.makeText(getApplicationContext(), "backstackcount: "+backStackEntryCount, Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    //mDrawerToggle.setHomeAsUpIndicator(R.drawable.nav_drawer);
                }
                mDrawerToggle.syncState();
            }
        });



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
        getMenuInflater().inflate(R.menu.menu_main_doctor, menu);

        /*MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.actionbar_notification_icon);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        tv = (TextView) notifCount.findViewById(R.id.cart_count_txt);
        tv.setText("0");
        RelativeLayout cart_layout= (RelativeLayout) notifCount.findViewById(R.id.cart_layout);

        cart_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Cart count :"+tv.getText().toString(), Toast.LENGTH_SHORT).show();

                Fragment fragment = new CartFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    getSupportActionBar().setTitle(R.string.cart);
                }
            }
        });*/

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
            logOutDoctor();
            return true;
        }

        if(id == R.id.action_share){
            Toast.makeText(getApplicationContext(), "Sharing", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        //-to show iocn with action bar in drop down menu
        //if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
        if(menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e(TAG, "onMenuOpened", e);
                }
                catch(Exception e){
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
        // --------------shows dashboard for doctor---
        commonutils.setFragment(this,new DashboardFragmentDoctor(),null,R.string.dashboard,true);
        displayView(position);
    }

    private void displayView(int position) {

        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new DashboardFragmentDoctor();
                title = getString(R.string.title_dashboard);
                break;
            case 1:
                fragment = new DoctorProifileFragment();
                title = getString(R.string.title_docprofile);
                break;
            case 2:
                fragment = new DoctorscheduleFragment();
                title = getString(R.string.title_schedule);
                break;
            case 3:
                fragment = new DoctorAppointmentFragment();
                title = getString(R.string.title_appointment);
                break;
            case 4:
                fragment = new DoctorSettingsFragment();
                title = getString(R.string.title_settings);
                 break;
            case 5:
                logOutDoctor();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body_doctor, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //Toast.makeText(this, "Mainactivity: onActivityResult", Toast.LENGTH_SHORT).show();
        Log.d("@ Mainactivity", "*** onActivityResult *** "+"Result Code "+resultCode+"Request Code "+requestCode);
    }




    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("BackStackCount:>>", "backstackcount: "+backStackEntryCount);
        if(getSupportFragmentManager().getBackStackEntryCount() <= 1){
           finish();
        }
        else{
            super.onBackPressed();
        }
    }


    private void logOutDoctor()
    {
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

                login_status_editor.putBoolean(loginPrefs.login_Status,false).commit();
                login_status_editor.putString(loginPrefs.doctor_id,"").commit();
                login_status_editor.putString(loginPrefs.doctor_name,"").commit();
                login_status_editor.putString(loginPrefs.doctor_email,"").commit();

                login_status_editor.putString(loginPrefs.login_type,"").commit();


                AppPreferences pref = new AppPreferences(getApplicationContext());
                pref.removePreferences(AppPreferences.Storage.DOCTORDATA.name());

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                //finish();
                ActivityCompat.finishAffinity(MainActivityDoctor.this);//closes all activity
                //**************
            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
    }
}
