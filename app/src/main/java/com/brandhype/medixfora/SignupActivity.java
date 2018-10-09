package com.brandhype.medixfora;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.Utils.CustomToastAlertDialog;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.UserRegistrationData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

public class SignupActivity extends AppCompatActivity {

    Button signupBtn;
    EditText signu_username,signu_email,signu_contact,signu_pass,signu_confpass,signu_contact_stdcode;
    TextView bottom_signu_button_text2;
    String ssignu_username,ssignu_email,ssignu_contact,ssignu_pass,ssignu_confpass, stype;

    UserRegistrationData userRegistrationData;

    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        signu_username = (EditText) findViewById(R.id.signu_username);
        signu_email = (EditText) findViewById(R.id.signu_email);
        signu_contact = (EditText) findViewById(R.id.signu_contact);
        signu_contact_stdcode = (EditText) findViewById(R.id.signu_contact_stdcode);
        signu_contact_stdcode.setText(null);


        signu_confpass = (EditText) findViewById(R.id.signu_confpass);
        signu_pass = (EditText) findViewById(R.id.signu_pass);


        signupBtn = (Button) findViewById(R.id.signu_button);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performCheck();
                //performSignInTask();
            }
        });

        bottom_signu_button_text2 = (TextView) findViewById(R.id.bottom_signu_button_text2);
        bottom_signu_button_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.docpayRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.docpay_doctor) {
                    //do work when radioButton1 is active
                } else  if (checkedId == R.id.docpay_patient) {
                    //do work when radioButton2 is active
                }
            }
        });

        userRegistrationData=new UserRegistrationData();
    }

    private void performLogin()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void performCheck()
    {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();// find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) findViewById(radioButtonID);

        if(radioButton.getText().toString().equals("Doctor"))
            stype="Doctor";
        else
            stype="Patient";

        ssignu_username=signu_username.getText().toString();
        ssignu_email=signu_email.getText().toString();
        ssignu_contact=signu_contact.getText().toString();
        ssignu_pass=signu_pass.getText().toString();
        ssignu_confpass=signu_confpass.getText().toString();

        String errormsg="";
        Integer errorcount=0;

        if(ssignu_username.equals("")) {
            errormsg+=" Name Cannot be blank.\n";
            errorcount++;
        }
        if(ssignu_email.equals("")) {
            errormsg+=" Email Cannot be blank.\n";
            errorcount++;
        }
        if(!commonutils.isValidEmail(ssignu_email)) {
            errormsg+="Wrong email format.\n";
            errorcount++;
        }
        if(ssignu_contact.equals("")) {
            errormsg+=" Mobile number Cannot be blank.\n";
            errorcount++;
        }
        if(ssignu_pass.equals("")) {
            errormsg+=" Password Cannot be blank.\n";
            errorcount++;
        }

        if(ssignu_pass.length()<6 || ssignu_pass.length()>15 ) {
            errormsg+="Password length must be between 6 to 25 characters.\n";
            errorcount++;
        }
        if(!ssignu_pass.equals(ssignu_confpass)) {
            errormsg+=" Password & Confirm password mismatched.\n";
            errorcount++;
        }
        if(errorcount>0)
        {
            new CustomToastAlertDialog(SignupActivity.this,"Medixfora",errormsg).show();
            //Toast.makeText(getBaseContext(), ""+errormsg, Toast.LENGTH_SHORT).show();
            return;
        }

        userRegistrationData.setType(stype);
        userRegistrationData.setName(ssignu_username);
        userRegistrationData.setEmail(ssignu_email);
        userRegistrationData.setPassword(ssignu_confpass);
        userRegistrationData.setPhone(ssignu_contact);


        Intent main_intent = new Intent(getApplicationContext(), OTPActivity.class);
        main_intent.putExtra("userRegistrationDataKey",userRegistrationData);
        startActivity(main_intent);
        //finish();


        /*Toast.makeText(getBaseContext(), "Login Type: "+stype, Toast.LENGTH_SHORT).show();
        if(stype.equals("Patient"))
        {
            performPatientSignInTask();
        }
        else
        {
            performDoctorSignInTask();
        }*/
    }



    private void performDoctorSignInTask()
    {
        //Toast.makeText(getBaseContext(), "Doctor Signup is under development."+stype, Toast.LENGTH_SHORT).show();
        ssignu_username=signu_username.getText().toString();
        ssignu_email=signu_email.getText().toString();
        ssignu_contact=signu_contact.getText().toString();
        ssignu_pass=signu_pass.getText().toString();
        ssignu_confpass=signu_confpass.getText().toString();

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/save_registration_data/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("doctor_name", ssignu_username);
        postDataParams.put("email", ssignu_email);
        postDataParams.put("contact", ssignu_contact);
        postDataParams.put("password", ssignu_pass);
        postDataParams.put("conf_password", ssignu_confpass);

        String errormsg="";
        Integer errorcount=0;

        if(ssignu_username.equals("")) {
            errormsg+=" Name Cannot be blank.\n";
            errorcount++;
        }
        if(ssignu_email.equals("")) {
            errormsg+=" Email Cannot be blank.\n";
            errorcount++;
        }
        if(!commonutils.isValidEmail(ssignu_email)) {
            errormsg+="Wrong email format.\n";
            errorcount++;
        }
        if(ssignu_contact.equals("")) {
            errormsg+=" Mobile number Cannot be blank.\n";
            errorcount++;
        }
        if(ssignu_pass.equals("")) {
            errormsg+=" Password Cannot be blank.\n";
            errorcount++;
        }
        if(!ssignu_pass.equals(ssignu_confpass)) {
            errormsg+=" Password & Confirm password mismatched.\n";
            errorcount++;
        }
        if(errorcount>0)
        {
            new CustomToastAlertDialog(SignupActivity.this,"Medixfora",errormsg).show();
            Toast.makeText(getBaseContext(), ""+errormsg, Toast.LENGTH_SHORT).show();
            return;
        }


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params",postJson.toString());

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
                                Toast.makeText(getBaseContext(), "Doctor Registration Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Doctor Registration Success");

                                Intent main_intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(main_intent);
                                finish();
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CustomToastAlertDialog(SignupActivity.this,"Medixfora: Registration Failed",err).show();
                                //Toast.makeText(getBaseContext(), "Doctor Registration Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor Registration Failed:"+err);
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


    private void performPatientSignInTask()
    {
        ssignu_username=signu_username.getText().toString();
        ssignu_email=signu_email.getText().toString();
        ssignu_contact=signu_contact.getText().toString();
        ssignu_pass=signu_pass.getText().toString();
        ssignu_confpass=signu_confpass.getText().toString();


        String errormsg="";
        Integer errorcount=0;

        if(ssignu_username.equals("")) {
            errormsg+=" Name Cannot be blank.\n";
            errorcount++;
        }
        if(ssignu_email.equals("")) {
            errormsg+=" Email Cannot be blank.\n";
            errorcount++;
        }
        if(ssignu_contact.equals("")) {
            errormsg+=" Mobile number Cannot be blank.\n";
            errorcount++;
        }
        if(!commonutils.isValidEmail(ssignu_email)) {
            errormsg+="Wrong email format.\n";
            errorcount++;
        }
        if(ssignu_pass.equals("")) {
            errormsg+=" Password Cannot be blank.\n";
            errorcount++;
        }
        if(!ssignu_pass.equals(ssignu_confpass)) {
            errormsg+="Password & Confirm password mismatched.\n";
            errorcount++;
        }
        if(errorcount>0)
        {
            new CustomToastAlertDialog(SignupActivity.this,"Medixfora",errormsg).show();
            //Toast.makeText(getBaseContext(), ""+errormsg, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/save_registration_data/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---


        String str = ssignu_username;
        String first_name="",last_name="";

        String trimmed = str.trim();
        int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;

        if(words>1) {
            String[] splitStr = str.split("\\s+");

            if (splitStr[0] != null || splitStr[0] != "")
                first_name = splitStr[0];
            if (splitStr[1] != null || splitStr[1] != "")
                last_name = splitStr[1];
        }
        else
        {
            first_name=trimmed;last_name="";
        }


        postDataParams.put("title_prefix", "Mr");
        postDataParams.put("first_name", first_name);
        postDataParams.put("last_name", last_name);
        postDataParams.put("email", ssignu_email);
        postDataParams.put("contact", ssignu_contact);

        if(ssignu_pass.equals(ssignu_confpass)) {
            postDataParams.put("password", ssignu_pass);
            postDataParams.put("conf_password", ssignu_confpass);
        }else{
            Toast.makeText(getBaseContext(), "Password & Confirm password mismatched", Toast.LENGTH_SHORT).show();
            return;
        }


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params",postJson.toString());

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
                                Toast.makeText(getBaseContext(), "Registration Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Registration Success");

                                Intent main_intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(main_intent);
                                finish();

                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new CustomToastAlertDialog(SignupActivity.this,"Medixfora: Registration Failed",err).show();
                                //Toast.makeText(getBaseContext(), "Registration Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Registration Failed:"+err);
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
