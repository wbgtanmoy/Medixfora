package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.asynctasks.UploadMultipartWithImageAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.AsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    Context context;
    EditText patient_oldpass_edt,patient_newpass_edt,patient_newpass_edt2;
    Button patient_settings_button;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_settings, container, false);;
        context=getActivity();

        patient_oldpass_edt = (EditText) rootView.findViewById(R.id.patient_oldpass_edt);
        patient_newpass_edt = (EditText) rootView.findViewById(R.id.patient_newpass_edt);
        patient_newpass_edt2 = (EditText) rootView.findViewById(R.id.patient_newpass_edt2);

        patient_settings_button = (Button) rootView.findViewById(R.id.patient_settings_button);
        patient_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePasswordToServer();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_settings);
    }

    private void updatePasswordToServer()
    {
        AppPreferences pref;
        JSONObject patient;
        String patient_id="0";

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}


        String spatient_oldpass_edt="",spatient_newpass_edt="",spatient_newpass_edt2="";

        spatient_oldpass_edt = patient_oldpass_edt.getText().toString().trim();
        spatient_newpass_edt = patient_newpass_edt.getText().toString().trim();
        spatient_newpass_edt2 = patient_newpass_edt2.getText().toString().trim();


        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/update_patient_password";
        Log.d("@ patient prof url : ",url);

        String errormsg="";
        Integer errorcount=0;

        if(spatient_oldpass_edt.equals("")) {
            errormsg+=" Old Password Cannot be blank.\n";
            errorcount++;
        }
        if(spatient_newpass_edt.equals("")) {
            errormsg+=" New Password Cannot be blank.\n";
            errorcount++;
        }
        if(spatient_newpass_edt2.equals("")) {
            errormsg+=" Retype password Cannot be blank.\n";
            errorcount++;
        }
        if(!spatient_newpass_edt.equals(spatient_newpass_edt2)) {
            errormsg+=" Password And Retype Password mismatched.\n";
            errorcount++;
        }
        if(errorcount>0)
        {
            Toast.makeText(context, ""+errormsg, Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("patient_id", patient_id);
        postDataParams.put("old_password", spatient_oldpass_edt);
        postDataParams.put("new_password", spatient_newpass_edt);
        postDataParams.put("retype_password", spatient_newpass_edt2);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","update patient password : "+ url);
        Log.d("@ uploaddoc params ", "update patient password : "+ postJson.toString());

        UploadMultipartWithImageAsynctask uploadDocProfileAsynctask=new UploadMultipartWithImageAsynctask(context, postDataParams, null, url,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  S ", "server response "+ result);
                //--------------------------------------------------------
                try {
                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");
                    String is_validate = obj.getString("is_validate");

                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") && is_validate.equals("1"))
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Password Update Success", Toast.LENGTH_SHORT).show();
                                Log.d("Updating", "Update Success");
                                //---------------------------------------------------------------------
                            }
                        });//runonui

                        ((MainActivity)context).onBackPressed();
                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Password Updation Failed: "+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Updation Failed: ");
                            }
                        });

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally{

                }
                //-----------------------------------
            }
        });


        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(context))
        {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                uploadDocProfileAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                uploadDocProfileAsynctask.execute();//serial
            }
        }


    }

}
