package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivityDoctor;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.asynctasks.UploadMultipartWithImageAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.AsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorSettingsFragment extends Fragment {


    Context context;
    EditText doc_settings_password,doc_settings_cpassword;

    Button doc_settings_button;


    public DoctorSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView;
        rootView= inflater.inflate(R.layout.fragment_doctor_settings, container, false);
        context=getActivity();

        doc_settings_password = (EditText) rootView.findViewById(R.id.doc_settings_password);
        doc_settings_cpassword = (EditText) rootView.findViewById(R.id.doc_settings_cpassword);

        doc_settings_button = (Button) rootView.findViewById(R.id.doc_settings_button);
        doc_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show();

                updateProfileDataToServer();
            }
        });


        return rootView;
    }

    private void updateProfileDataToServer()
    {
        AppPreferences pref;
        JSONObject doctor;
        String doctor_id="0";

        try {
            pref = new AppPreferences(context);
            doctor = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            doctor_id=doctor.getString("id");

        }catch (Exception e){e.printStackTrace();}


        String sdoc_settings_password,sdoc_settings_cpassword;

        sdoc_settings_password = doc_settings_password.getText().toString().trim();
        sdoc_settings_cpassword = doc_settings_cpassword.getText().toString().trim();

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/doctor/get_doctor_details/apitoken/1edc0ae98198866510bce219d5115b72/doctorID/2191
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/update_doctor_password";
        Log.d("@ doc prof url : ",url);


        String errormsg="";
        Integer errorcount=0;

        if(sdoc_settings_password.equals("")) {
            errormsg+=" Password Cannot be blank.\n";
            errorcount++;
        }
        if(sdoc_settings_cpassword.equals("")) {
            errormsg+=" Confirm password Cannot be blank.\n";
            errorcount++;
        }
        if(!sdoc_settings_password.equals(sdoc_settings_cpassword)){
            errormsg+=" Password And Confirm Password Mismatched.\n";
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


        postDataParams.put("doctor_id", doctor_id);
        postDataParams.put("password", sdoc_settings_password);
        postDataParams.put("confirm_password", sdoc_settings_cpassword);


        /*ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> files = new HashMap<String, String>();
        String Filepath="";
        if(!Filepath.equals(""))
        {
            files.put("Fileupload", Filepath);//"key,value
        }
        fileList.add(files);*/


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","update doc prof: "+ url);
        Log.d("@ uploaddoc params ", "update doc prof: "+ postJson.toString());


        UploadMultipartWithImageAsynctask updatePassAsynctask=new UploadMultipartWithImageAsynctask(context, postDataParams, null, url,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  S ", "server response "+ result);
                //--------------------------------------------------------
                try {
                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
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
                    //String is_validate = obj.getString("is_validate");

                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") )
                    {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Password Changed", Toast.LENGTH_SHORT).show();
                                Log.d("Updating", "Password Changed");
                                //---------------------------------------------------------------------
                            }
                        });//runonui

                        ((MainActivityDoctor)context).onBackPressed();
                    }
                    else
                    {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Updation Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Updation Failed: ");
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
        if (!ConnectionDetector.isConnectingToInternet(context))
        {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                updatePassAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                updatePassAsynctask.execute();//serial
            }
        }


    }

}
