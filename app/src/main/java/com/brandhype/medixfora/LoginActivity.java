package com.brandhype.medixfora;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.CustomToastAlertDialog;
import com.brandhype.medixfora.Utils.LoginPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.brandhype.medixfora.Utils.commonutils.isNullOrBlank;
import static com.brandhype.medixfora.Utils.commonutils.replaceString;

public class LoginActivity extends Activity {

    EditText login_contact,login_pass;
    Button login_button;
    TextView bottom_login_button_text2,bottom_login_forgotpass_txt;
    String slogin_contact,slogin_pass;

    RadioGroup radioGroup;
    String stype;

    SharedPreferences login_shared_preferences;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        login_contact = (EditText) findViewById(R.id.login_contact);
        login_pass = (EditText) findViewById(R.id.login_pass);
        login_button = (Button) findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "log");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Login");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
                mFirebaseAnalytics.setUserId("10001");
                mFirebaseAnalytics.setUserProperty("Food", "Chocolate");*/

                checkPatientDoctor();
                //performLoginInTask();
            }
        });

        bottom_login_forgotpass_txt = (TextView) findViewById(R.id.bottom_login_forgotpass_txt);
        bottom_login_forgotpass_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEmailDialog();
            }
        });

        bottom_login_button_text2 = (TextView) findViewById(R.id.bottom_login_button_text2);
        bottom_login_button_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performRegistration();
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.docpayRadioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.docpay_doctor1) {
                    //do work when radioButton1 is active
                } else  if (checkedId == R.id.docpay_patient1) {
                    //do work when radioButton2 is active
                }
            }
        });
    }


    private void checkPatientDoctor()
    {
        slogin_contact=login_contact.getText().toString();
        slogin_pass=login_pass.getText().toString();

        String errormsg="";
        Integer errorcount=0;

        if(slogin_contact.equals("")) {
            errormsg+=" Please provide your phone number.\n";
            errorcount++;
        }
        /*if(!commonutils.isValidEmail(slogin_contact)) {
            errormsg+=" Wrong email format.\n";
            errorcount++;
        }*/

        if(slogin_pass.equals("")) {
            errormsg+=" Password cannot be blank.\n";
            errorcount++;
        }
        if(errorcount>0)
        {
            //Toast.makeText(getBaseContext(), ""+errormsg, Toast.LENGTH_SHORT).show();
            new CustomToastAlertDialog(this,"Medixfora",errormsg).show();
            return;
        }

        int radioButtonID = radioGroup.getCheckedRadioButtonId();// find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) findViewById(radioButtonID);

        if(radioButton.getText().toString().equals("Doctor"))
            stype="Doctor";
        else
            stype="Patient";

        //Toast.makeText(getBaseContext(), "Login Type: "+stype, Toast.LENGTH_SHORT).show();
        Log.d("@ login",""+stype);


        if(stype.equals("Patient"))
        {
            performPatientLoginInTask();
        }
        else
        {
            performDoctorLoginTask();
        }
    }

    private void performRegistration()
    {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    private void performPatientLoginInTask()
    {
        slogin_contact=login_contact.getText().toString();
        slogin_pass=login_pass.getText().toString();

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/check_login_data/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        //---------------Retrieve user fcm registrationid----------------------
        AppPreferences pref = new AppPreferences(this);
        String registration_id = pref.LoadPreferences(AppPreferences.Storage.DEVICE_FCMID.name());


        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---


        postDataParams.put("contact", slogin_contact);
        postDataParams.put("password", slogin_pass);

        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device , 2 is IOS ---
        postDataParams.put("registration_id", registration_id);


        Log.d("@ hitting at", url);
        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params", " login "+ postJson.toString());


        //if(true){return;}

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(this, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (noProgressDialog == 1) {
                            pdspinnerGeneral.dismiss();
                        }
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String is_validate = obj.getString("is_validate");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1") && is_validate.equals("1") && api_syntax_success.equals("1")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getBaseContext(), "Login Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Login Success");
                            }
                        });


                        JSONObject patient = obj.getJSONObject("patient_details");
                        String patient_id,patient_first_name,patient_last_name,patient_email;

                        patient_id=patient.getString("id");
                        patient_first_name=patient.getString("first_name");
                        if(!isNullOrBlank(patient.getString("last_name").toString()))
                            patient_last_name=patient.getString("last_name");
                        else
                            patient_last_name="";
                        patient_email=patient.getString("email");

                        Toast.makeText(getBaseContext(), "Welcome :"+patient_first_name+" "+patient_last_name, Toast.LENGTH_SHORT).show();


                        LoginPreferences loginPrefs = new LoginPreferences();
                        login_shared_preferences = getSharedPreferences(loginPrefs.login_Pref_key, Context.MODE_PRIVATE);
                        SharedPreferences.Editor login_status_editor;
                        login_status_editor = login_shared_preferences.edit();

                        login_status_editor.putBoolean(loginPrefs.login_Status,true).commit();
                        login_status_editor.putString(loginPrefs.patient_id,patient_id).commit();
                        login_status_editor.putString(loginPrefs.first_name,patient_first_name).commit();
                        login_status_editor.putString(loginPrefs.last_name,patient_last_name).commit();
                        login_status_editor.putString(loginPrefs.email,patient_email).commit();

                        login_status_editor.putString(loginPrefs.login_type,"Patient").commit();

                        AppPreferences pref = new AppPreferences(getApplicationContext());
                        pref.SavePreferences(AppPreferences.Storage.PATIENTDATA.name(),patient.toString());

                        Log.d("@ Patient profile:",patient.toString());

                        Intent main_intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main_intent);
                        finish();
                    }
                    else
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getBaseContext(), "Login Failed:"+err, Toast.LENGTH_SHORT).show();
                                new CustomToastAlertDialog(LoginActivity.this,"Medixfora: Login Failed",err).show();
                                Log.e("Error","Login Failed:"+err);
                            }
                        });

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (noProgressDialog == 1) {
                        pdspinnerGeneral.dismiss();
                    }
                }

            }

        };

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(this))
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);//parallel
            } else {
                submitAsync.execute(url);//serial
            }
        }

    }

    private void performDoctorLoginTask()
    {
        slogin_contact=login_contact.getText().toString();
        slogin_pass=login_pass.getText().toString();

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/check_login_data/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("contact", slogin_contact);
        postDataParams.put("password", slogin_pass);

        Log.d("@ hitting at", url);
        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params",postJson.toString());

        //if(true){return;}

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(this, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (noProgressDialog == 1) {
                            pdspinnerGeneral.dismiss();
                        }
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String is_validate = obj.getString("is_validate");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1") && is_validate.equals("1") && api_syntax_success.equals("1")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getBaseContext(), "Login Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Login Success");


                            }
                        });

                        JSONObject doctor = obj.getJSONObject("doctor_details");

                        //String patient_id,patient_first_name,patient_last_name,patient_email;
                        String doctor_id,doctor_name,doctor_email;

                        doctor_id=doctor.getString("id");
                        doctor_name=doctor.getString("doctor_name");
                        doctor_email=doctor.getString("email");


                        Toast.makeText(getBaseContext(), "Welcome :"+doctor_name, Toast.LENGTH_SHORT).show();


                        LoginPreferences loginPrefs = new LoginPreferences();
                        login_shared_preferences = getSharedPreferences(loginPrefs.login_Pref_key, Context.MODE_PRIVATE);
                        SharedPreferences.Editor login_status_editor;
                        login_status_editor = login_shared_preferences.edit();

                        login_status_editor.putBoolean(loginPrefs.login_Status,true).commit();
                        login_status_editor.putString(loginPrefs.doctor_id,doctor_id).commit();
                        login_status_editor.putString(loginPrefs.doctor_name,doctor_name).commit();
                        login_status_editor.putString(loginPrefs.doctor_email,doctor_email).commit();

                        login_status_editor.putString(loginPrefs.login_type,"Doctor").commit();

                        AppPreferences pref = new AppPreferences(getApplicationContext());
                        pref.SavePreferences(AppPreferences.Storage.DOCTORDATA.name(),doctor.toString());

                        Log.d("@ Doctor profile:",doctor.toString());

                        Intent main_intent = new Intent(getApplicationContext(), MainActivityDoctor.class);
                        startActivity(main_intent);
                        finish();
                    }
                    else
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CustomToastAlertDialog(LoginActivity.this,"Medixfora: Login Failed",err).show();
                                //Toast.makeText(getBaseContext(), "Login Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Login Failed:"+err);
                            }
                        });

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (noProgressDialog == 1) {
                        pdspinnerGeneral.dismiss();
                    }
                }

            }

        };

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(this))
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);//parallel
            } else {
                submitAsync.execute(url);//serial
            }
        }

    }




    private void showEmailDialog()
    {

        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(radioButtonID);

        if(radioButton.getText().toString().equals("Doctor"))
            stype="Doctor";
        else
            stype="Patient";

        final Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog_forgotpass);

        final EditText forgotpass_email=(EditText) alertDialog.findViewById(R.id.forgotpass_email);
        Button _cancel = (Button) alertDialog.findViewById(R.id.btn_no);
        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);


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

                String eml= forgotpass_email.getText().toString().trim();
                if(!eml.equals("") && commonutils.isValidEmail(eml)) {
                    sendToForgotPassword(eml,stype);
                    alertDialog.dismiss();
                }
                else if(eml.equals("") ){
                    Toast.makeText(LoginActivity.this,"Email cannot be blank.",Toast.LENGTH_LONG).show();
                }
                else if(!commonutils.isValidEmail(eml)) {
                    Toast.makeText(LoginActivity.this,"Invalid email.",Toast.LENGTH_LONG).show();
                }

            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();

    }

    public void sendToForgotPassword(String email, String login_type)
    {
        //Toast.makeText(LoginActivity.this,""+ login_type+ "'s Email: "+email,Toast.LENGTH_LONG).show();
        //http://medixfora.com.md-in-64.webhostbox.net/restapi/forgot_password/manage_forgot_password

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "forgot_password/manage_forgot_password";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device , 2 is IOS ---

        postDataParams.put("email", email);

        if(login_type.equals("Doctor"))
            postDataParams.put("user_type", "doctor");
        if(login_type.equals("Patient"))
            postDataParams.put("user_type", "patient");

        Log.d("@ hitting at", url);
        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params", " login "+ postJson.toString());

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(this, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (noProgressDialog == 1) {
                            pdspinnerGeneral.dismiss();
                        }
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 Log.d("Success","Password reset  Success");
                                //new CustomToastAlertDialog(LoginActivity.this,"A reset link has been mailed to your registered email.",err).show();
                            }
                        });
                        //------------------------Dialog---------------------------------------------
                        final Dialog dialog = new Dialog(LoginActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_alertdialog_ok);

                        TextView txt_main = (TextView) dialog.findViewById(R.id.txt_main);
                        txt_main.setText(err);
                        //txt_main.setText("A reset link has been mailed to your registered email. Please open your mail and reset your password.");

                        Button _yes = (Button) dialog.findViewById(R.id.btn_yes);
                        _yes.setEnabled(true);
                        _yes.setText("OK");

                        _yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        if (dialog != null && !dialog.isShowing())
                            dialog.show();
                        //-----------------------------------------------------

                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getBaseContext(), "Login Failed:"+err, Toast.LENGTH_SHORT).show();
                                new CustomToastAlertDialog(LoginActivity.this,"Password reset failed. ",err).show();
                                Log.e("Error","Password reset failed :"+err);
                            }
                        });
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (noProgressDialog == 1) {
                        pdspinnerGeneral.dismiss();
                    }
                }
            }
        };

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(this))
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);//parallel
            } else {
                submitAsync.execute(url);//serial
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
