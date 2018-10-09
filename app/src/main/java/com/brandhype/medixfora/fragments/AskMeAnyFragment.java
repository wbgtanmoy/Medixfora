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

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.CustomToastAlertDialog;
import com.brandhype.medixfora.asynctasks.UploadMultipartWithImageAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.AsyncResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.brandhype.medixfora.Utils.commonutils.isValidEmail;
import static com.brandhype.medixfora.Utils.commonutils.replaceString;


public class AskMeAnyFragment extends Fragment {

    Context context;

    EditText askmeany_name,askmeany_email,askmeany_phone,askmeany_message;
    Button askmeany_skip_button,askmeany_submit_button;

    String saskmeany_name,saskmeany_email,saskmeany_phone,saskmeany_message;

    public AskMeAnyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView=inflater.inflate(R.layout.fragment_ask_me_any, container, false);
        context=getActivity();


        askmeany_name = (EditText) rootView.findViewById(R.id.askmeany_name);
        askmeany_email = (EditText) rootView.findViewById(R.id.askmeany_email);
        askmeany_phone = (EditText) rootView.findViewById(R.id.askmeany_phone);
        askmeany_message = (EditText) rootView.findViewById(R.id.askmeany_message);

        askmeany_skip_button = (Button) rootView.findViewById(R.id.askmeany_skip_button);
        askmeany_submit_button = (Button) rootView.findViewById(R.id.askmeany_submit_button);

        askmeany_skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).onBackPressed();
            }
        });

        askmeany_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataToServer();
            }
        });

        return rootView;
    }



    private void updateDataToServer()
    {
        AppPreferences pref;
        JSONObject patient;
        String patient_id="0";

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}

        saskmeany_name = askmeany_name.getText().toString().trim();
        saskmeany_email = askmeany_email.getText().toString().trim();
        saskmeany_phone = askmeany_phone.getText().toString().trim();
        saskmeany_message = askmeany_message.getText().toString().trim();


        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "contact/askme";
        Log.d("@ ask me any : ",url);

        String errormsg="";
        Integer errorcount=0;


        if(saskmeany_name.equals("")) {
            errormsg+=" Name Cannot be blank.\n";
            errorcount++;
        }

        if(saskmeany_email.equals("")) {
            errormsg+=" Email Cannot be blank.\n";
            errorcount++;
        }

        if(!isValidEmail(saskmeany_email)) {
            errormsg+=" Invalid email.\n";
            errorcount++;
        }

        if(saskmeany_phone.equals("")) {
            errormsg+=" Phone Cannot be blank.\n";
            errorcount++;
        }
        if(saskmeany_message.equals("")) {
            errormsg+=" Message Cannot be blank.\n";
            errorcount++;
        }


        if(errorcount>0)
        {
            //Toast.makeText(context, ""+errormsg, Toast.LENGTH_SHORT).show();
            new CustomToastAlertDialog(getActivity(),"Medixfora",""+errormsg ).show();
            return;
        }

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("patient_id", patient_id);
        postDataParams.put("name", saskmeany_name);
        postDataParams.put("email", saskmeany_email);
        postDataParams.put("phone", saskmeany_phone);
        postDataParams.put("message", saskmeany_message);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : "," ask me any : "+ url);
        Log.d("@ ask me any ", " ask me any: "+ postJson.toString());

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
                                //Toast.makeText(context, "Query Submitted Successfully", Toast.LENGTH_SHORT).show();

                                new CustomToastAlertDialog(getActivity(),"Medixfora","Your information has been sent to admin successfully." ).show();
                                Log.d("@ Ask me any ", "Query Submitted Successfully");
                                //---------------------------------------------------------------------
                            }
                        });


                        ((MainActivity)context).onBackPressed();
                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Submission Failed: "+err, Toast.LENGTH_SHORT).show();
                                Log.d("@ Error","Submission  Failed: ");
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
