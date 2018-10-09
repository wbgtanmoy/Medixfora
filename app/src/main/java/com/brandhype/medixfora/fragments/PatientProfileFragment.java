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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientProfileFragment extends Fragment {


    Context context;
    String Filepath="";


    TextView patient_prof_name,patient_prof_title,patient_prof_firstname,patient_prof_lastname,patient_prof_address,patient_prof_country;
    TextView patient_prof_citytown,patient_prof_postcode,patient_prof_contact,patient_prof_about,patient_prof_email;

    Button patient_prof_button;
    ImageView patient_prof_image,patient_profile_edit;



    public PatientProfileFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView=inflater.inflate(R.layout.fragment_patient_profile, container, false);;
        context=getActivity();

        patient_prof_name = (TextView) rootView.findViewById(R.id.patient_prof_name);
        patient_prof_image = (ImageView) rootView.findViewById(R.id.patient_prof_image);
        patient_profile_edit = (ImageView) rootView.findViewById(R.id.patient_profile_edit);
        patient_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonutils.setFragmentPatient(getActivity(),new PatientEditprofileFragment(),null,R.string.title_edit_profile,true);
            }
        });


        patient_prof_address = (TextView) rootView.findViewById(R.id.patient_prof_address);
        patient_prof_country = (TextView) rootView.findViewById(R.id.patient_prof_country);
        patient_prof_citytown = (TextView) rootView.findViewById(R.id.patient_prof_citytown);
        patient_prof_postcode = (TextView) rootView.findViewById(R.id.patient_prof_postcode);
        patient_prof_contact = (TextView) rootView.findViewById(R.id.patient_prof_contact);
        patient_prof_email = (TextView) rootView.findViewById(R.id.patient_prof_email);

        patient_prof_button = (Button) rootView.findViewById(R.id.patient_prof_button);
       /* patient_prof_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonutils.setFragmentPatient(getActivity(),new PatientEditprofileFragment(),null,R.string.title_edit_profile,true);
            }
        });*/

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPatientFromServer();
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_profile);
    }



    private void getPatientFromServer()
    {
        AppPreferences pref;
        JSONObject patient;
        String patient_id="0";

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");

        }catch (Exception e){e.printStackTrace();}

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/patient/get_patient_details/apitoken/1edc0ae98198866510bce219d5115b72/patientID/2191
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_details/apitoken/"+token+"/patientID/"+patient_id;
        Log.d("@ doc server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(context, 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("@ server"," response "+result.toString());

                if (result.equals("")) {

                    ((MainActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            Log.e("Error","Patient data not found");
                        }
                    });

                    if (noProgressDialog == 1) {
                        pdspinnerGeneral.dismiss();
                    }
                    return;
                }

                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") )
                    {

                        JSONObject patient = resultJson.getJSONObject("result");

                        String title_prefix,patient_firstname,patient_lastname,address,contact_number,email;
                        String country_name,city_name,postcode;
                        String profile_image_path;

                        /*if(!isNullOrBlank(patient.getString("prefix_title")))
                            title_prefix=patient.getString("prefix_title");
                        else
                            title_prefix="";*/

                        patient_firstname=patient.getString("first_name");

                        /*if(!isNullOrBlank(patient.getString("last_name")))
                            patient_lastname=patient.getString("last_name");
                        else
                            patient_lastname="";*/

                        address=patient.getString("address");
                        city_name=patient.getString("city_name");
                        country_name=patient.getString("country_name");
                        contact_number=patient.getString("contact_number");
                        postcode=patient.getString("postcode");
                        email=patient.getString("email");
                        profile_image_path=replaceString(patient.getString("profile_image_path"));

                        Log.d("@ Patient info:"," name :"+ patient_firstname+" image url:  "+profile_image_path);

                        //patient_prof_name.setText(title_prefix+ " "+ patient_firstname + " "+ patient_lastname);
                        patient_prof_name.setText(patient_firstname );

                        patient_prof_address.setText(address);
                        patient_prof_citytown.setText(city_name);
                        patient_prof_country.setText(country_name);
                        patient_prof_contact.setText(contact_number);
                        patient_prof_postcode.setText(postcode);
                        patient_prof_email.setText(email);


                        try {
                            Picasso.with(context)
                                    .load(profile_image_path)
                                    .placeholder(R.drawable.profilepic1)
                                    .error(R.drawable.default_avatar)
                                    .into(patient_prof_image);
                        }catch(Exception e){ e.printStackTrace();}

                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Error:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Patient details fetching failed"+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
                finally {
                    if (noProgressDialog == 1) {
                        pdspinnerGeneral.dismiss();
                    }
                }

            }
        };

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(context))
        {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
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


}
