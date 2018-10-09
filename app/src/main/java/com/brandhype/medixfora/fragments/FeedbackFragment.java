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
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.MainActivityDoctor;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
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
public class FeedbackFragment extends Fragment {


    Button feedback_submit_btn;
    EditText feedback_firstname,feedback_email,feedback_edt;
    String sfeedback_firstname,sfeedback_email,sfeedback_edt;
    Context context;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =inflater.inflate(R.layout.fragment_feedback, container, false);
        context=getActivity();

        feedback_firstname=(EditText) rootView.findViewById(R.id.feedback_firstname);
        //feedback_lastname=(EditText) rootView.findViewById(R.id.feedback_lastname);
        feedback_email=(EditText) rootView.findViewById(R.id.feedback_email);
        feedback_edt=(EditText) rootView.findViewById(R.id.feedback_edt);

        feedback_submit_btn = (Button) rootView.findViewById(R.id.feedback_submit_btn);
        feedback_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,"Under Development",Toast.LENGTH_SHORT).show();
                updateFeedbackToServer();
            }
        });

        AppPreferences pref;
        JSONObject patient;
        String patient_id,patient_first_name,patient_last_name,patient_email,profile_image_path;
        try {
            pref = new AppPreferences(getActivity());
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");
            patient_first_name=patient.getString("first_name");
            //patient_last_name=patient.getString("last_name");
            patient_email=patient.getString("email");

            feedback_firstname.setText(patient_first_name);
            //feedback_lastname.setText(patient_last_name);
            feedback_email.setText(patient_email);

        }catch (Exception e){e.printStackTrace();}

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        // set the toolbar title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_feedback);
    }


    private void updateFeedbackToServer()
    {
        AppPreferences pref;
        JSONObject patient;
        String patient_id="0";

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}

        sfeedback_firstname = feedback_firstname.getText().toString().trim();
        //sfeedback_lastname = feedback_lastname.getText().toString().trim();
        sfeedback_email = feedback_email.getText().toString().trim();
        sfeedback_edt=feedback_edt.getText().toString().trim();

        String errormsg="";
        Integer errorcount=0;

        if(sfeedback_firstname.equals("")) {
            errormsg+=" First Name Cannot be blank.\n";
            errorcount++;
        }
        if(sfeedback_edt.equals("")) {
            errormsg+=" Please give your feed back in the message box.\n";
            errorcount++;
        }
       /* if(sfeedback_lastname.equals("")) {
            errormsg+=" Last Name Cannot be blank.\n";
            errorcount++;
        }*/
        if(!commonutils.isValidEmail(sfeedback_email)) {
            errormsg+=" Invalid Email.\n";
            errorcount++;
        }
        if(errorcount>0)
        {
            Toast.makeText(context, ""+errormsg, Toast.LENGTH_SHORT).show();
            return;
        }

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://medixfora.com.md-in-64.webhostbox.net/restapi/feedback/add
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "feedback/add";
        Log.d("@ patient feedbk url : ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("patient_id", patient_id);
        postDataParams.put("name", sfeedback_firstname);
        //postDataParams.put("last_name", sfeedback_lastname);
        postDataParams.put("email", sfeedback_email);
        postDataParams.put("message", sfeedback_edt);

       /* ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> files = new HashMap<String, String>();
        if(!Filepath.equals("")){
            files.put("Fileupload", Filepath);//"key,value
        }
        fileList.add(files);*/


        UploadMultipartWithImageAsynctask uploadDocProfileAsynctask=new UploadMultipartWithImageAsynctask(context, postDataParams, null, url,new AsyncResponse(){

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

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1"))
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Your feedback has been sent to admin successfully", Toast.LENGTH_SHORT).show();
                                Log.d("Feedback", "Feedback Success");
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
                                Toast.makeText(context, "Feedback Failed: "+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Feedback Failed: ");
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
