package com.brandhype.medixfora;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.CustomToastAlertDialog;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.UserRegistrationData;
import com.dpizarro.pinview.library.PinView;
import com.dpizarro.pinview.library.PinViewSettings;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Random;
import static com.brandhype.medixfora.Utils.commonutils.replaceString;

public class OTPActivity extends Activity {


    Button otp_button,otp_resend_button;
    ImageView otp_resend_image;

    String verifyPin="0",enteredOTP;
    PinView pinView;
    TextView otp_entered_txt,otp_contactno_txt;

    AppPreferences pref;
    JSONObject user;

    //String user_id_reg="0";
    String user_phone="0";
    UserRegistrationData userRegistrationData;
    String stype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        otp_entered_txt = (TextView)findViewById(R.id.otp_entered_txt);
        otp_contactno_txt = (TextView)findViewById(R.id.otp_contactno_txt);


        otp_button = (Button)findViewById(R.id.otp_button);
        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });

        otp_resend_image = (ImageView) findViewById(R.id.otp_resend_image);
        otp_resend_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP();
            }
        });

        otp_resend_button = (Button)findViewById(R.id.otp_resend_button);
        otp_resend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Random random = new Random();
        verifyPin = String.format("%04d", random.nextInt(10000));

        pinView = (PinView) findViewById(R.id.pinView);

        PinViewSettings pinViewSettings = new PinViewSettings.Builder()
                //.withPinTitles(titlesAux)
                .withMaskPassword(true)
                .withDeleteOnClick(true)
                .withKeyboardMandatory(false)
                .withSplit(null)
                .withNumberPinBoxes(4)
                .withNativePinBox(false)
                .build();

        pinView.setSettings(pinViewSettings);

        pinView.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, final String pinResults) {
                if (completed) {

                    showResult(pinResults);
                }
            }
        });

        try {
            userRegistrationData=(UserRegistrationData) getIntent().getSerializableExtra("userRegistrationDataKey");
            otp_contactno_txt.setText(userRegistrationData.getPhone());
            Log.d("@ user reg data "," "+userRegistrationData.toString() );
        }catch (Exception e){e.printStackTrace();}

        //askServerToSendOTP_alt();
        askServerToSendOTP();
    }


    private void verifyOTP()
    {
        if(verifyPin.equals(enteredOTP))
        {
            //Toast.makeText(getApplicationContext(),"OTP verified.", Toast.LENGTH_SHORT).show();
            //---------once OTP is verified-----
            Log.d("@ activating "," user phone "+userRegistrationData.getPhone() );
            //register user;
            stype=userRegistrationData.getType();
            registerUserByType(stype);

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Wrong OTP", Toast.LENGTH_LONG).show();
            pinView.clear();
        }
    }

    private void resendOTP()
    {
        Random random = new Random();
        verifyPin = String.format("%04d", random.nextInt(10000));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pinView.clear();
            }
        }, 3000);

        //askServerToSendOTP_alt();
        askServerToSendOTP();
    }

    private void showResult(final String pinResults) {

        //Toast.makeText(getApplicationContext(),"PIN:"+ pinResults,Toast.LENGTH_LONG).show();
        Log.d("@ ","PIN:"+ pinResults);
        otp_entered_txt.setText(pinResults);
        enteredOTP=pinResults;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //pinView.clear();
                //pinView.clearFocus();
               commonutils.hideKeyboard2(OTPActivity.this);
            }
        }, 1000);
    }


    private void askServerToSendOTP_alt()
    {
        Log.d("@ Medixfora", "Please enter" + " " +verifyPin + " "+ "in the OTP box.");
        //new CustomToastAlertDialog(this,"IHH", getResources().getString(R.string.otp_enter_msg1) + " " +verifyPin + " "+ getResources().getString(R.string.otp_enter_msg2)).show();
        //------------------------Dialog---------------------------------------------

        final Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog_ok);

        TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);
        txt_main.setText( "Please enter" + " " +verifyPin + " "+ "in the OTP box.");
        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);

        _yes.setEnabled(true);
        _yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
           }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();

        //----------------------------------------------------------------------
    }

    private void askServerToSendOTP()
    {
        //http://medixfora.com.md-in-64.webhostbox.net/restapi/patient/send_otp
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL +  "patient/send_otp";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("otp", verifyPin);// 4 digit random number,this digit/pin  will be sent to the user phone in SMS ,who inturn input it the PIN field
        postDataParams.put("contact", userRegistrationData.getPhone());// phone

        Log.d("@ hitting at ","for OTP "+url);

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
                    //String is_validate = obj.getString("is_validate");
                    String api_syntax_success = obj.getString("api_syntax_success");
                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1") && api_syntax_success.equals("1"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CustomToastAlertDialog(OTPActivity.this,"Medixfora","You will receive OTP shortly.").show();
                                //Toast.makeText(getBaseContext(), "You will receive OTP shortly.", Toast.LENGTH_LONG).show();
                                Log.d("Success","OTP Success");

                            }
                        });

                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //new CustomToastAlertDialog(LoginActivity.this,"IHH, Login Failed :",err).show();
                                //Toast.makeText(getBaseContext(), "Doctor Registration Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error"," OTP sending  Failed:"+err);
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


    private void activateUser()
    {

        String user_id_reg="0";

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL +  "ACTIVATEUSER_URL";
        String token= NetworkIOConstant.CS_Token.TOKEN;
        Log.d("@ hitting at ",""+url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("id", user_id_reg); //  user ID got during registration

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
                    //String is_validate = obj.getString("is_validate");
                    String api_syntax_success = obj.getString("api_syntax_success");
                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getBaseContext(), "Your Account Activated.", Toast.LENGTH_SHORT).show();
                                Log.d("@ Success","Activation  Success");

                            }
                        });

                        //---------------------------------------------------
                        final Dialog alertDialog = new Dialog(OTPActivity.this);
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setCancelable(false);
                        alertDialog.setContentView(R.layout.custom_alertdialog_ok);

                        TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);
                        txt_main.setText(R.string.otp_account_activate_success);



                        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);
                        _yes.setEnabled(true);
                        _yes.setText("OK");
                        _yes.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                Intent main_intent = new Intent(OTPActivity.this, LoginActivity.class);
                                startActivity(main_intent);
                                finish();
                            }
                        });

                        if (alertDialog != null && !alertDialog.isShowing())
                            alertDialog.show();
                        //------------------------------------------------------------

                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //new CustomToastAlertDialog(LoginActivity.this,"IHH, Login Failed :",err).show();
                                //Toast.makeText(getBaseContext(), "Doctor Registration Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("@ Error"," Activation Failed, contact admin "+err);
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



    private void registerUserByType(String stype)
    {
        Log.d("@ registering  "," for "+ stype );
        if(stype.equals("Patient"))
        {
            performPatientSignInTask();
        }
        else
        {
            performDoctorSignInTask();
        }
    }


    private void performDoctorSignInTask()
    {

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/save_registration_data/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        Log.d("@ hitting at ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("doctor_name", userRegistrationData.getName());
        postDataParams.put("email", userRegistrationData.getEmail());
        postDataParams.put("contact", userRegistrationData.getPhone());
        postDataParams.put("password", userRegistrationData.getPassword());
        postDataParams.put("conf_password", userRegistrationData.getPassword());

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ doc Post params",postJson.toString());

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
                                //Toast.makeText(getBaseContext(), "Doctor Registration Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Doctor Registration Success");
                                showActivationProcessToUser();

                                //Intent main_intent = new Intent(getApplicationContext(), LoginActivity.class);
                                ///startActivity(main_intent);
                                //finish();
                                //ActivityCompat.finishAffinity(OTPActivity.this);//closes all activity
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CustomToastAlertDialog(OTPActivity.this,"Medixfora: Registration Failed",err).show();
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

        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/save_registration_data/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        Log.d("@ hitting at ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---


        String str = userRegistrationData.getName();
        String first_name="",last_name="";

        String trimmed = str.trim();
        int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;

        if(words>1) {
            String[] splitStr = str.split("\\s+");

            if (splitStr[0] != null || splitStr[0] != "")
                first_name = splitStr[0];
            if (splitStr[1] != null || splitStr[1] != "")
                last_name = splitStr[words-1];
        }
        else
        {
            first_name=trimmed;last_name="";
        }


        //postDataParams.put("title_prefix", "");
        postDataParams.put("first_name", userRegistrationData.getName());
        //postDataParams.put("last_name", last_name);
        postDataParams.put("email", userRegistrationData.getEmail());
        postDataParams.put("contact", userRegistrationData.getPhone());
        postDataParams.put("password", userRegistrationData.getPassword());
        postDataParams.put("conf_password", userRegistrationData.getPassword());


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
                                //Toast.makeText(getBaseContext(), "Registration Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Registration Success");
                                showActivationProcessToUser();

                                //Intent main_intent = new Intent(getApplicationContext(), LoginActivity.class);
                                //startActivity(main_intent);
                                //finish();
                                //ActivityCompat.finishAffinity(OTPActivity.this);//closes all activity

                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new CustomToastAlertDialog(OTPActivity.this,"Medixfora: Registration Failed",err).show();
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

    private void showActivationProcessToUser()
    {
        //---------------------------------------------------
        final Dialog alertDialog = new Dialog(OTPActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog_ok);

        TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);
        txt_main.setText(R.string.otp_account_activation_process);


        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);
        _yes.setEnabled(true);
        _yes.setText("OK");
        _yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent main_intent = new Intent(OTPActivity.this, LoginActivity.class);
                startActivity(main_intent);
                //finish();
                ActivityCompat.finishAffinity(OTPActivity.this);//closes all activity
            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
        //------------------------------------------------------------
    }
}
