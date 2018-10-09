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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.adpaters.CustomPrescriptionListAdapterN;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.PrescriptionItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPrescriptionFragment extends Fragment {


    Context context;
    View rootView;

    List<PrescriptionItem> dataS = new ArrayList<>();

    AppPreferences pref;
    JSONObject patient;

    ImageView norecordfound_prescription;
    ListView oldPrescriptions;

    CustomPrescriptionListAdapterN adapter;;

    public MyPrescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_my_prescription, container, false);
        context=getActivity();

        norecordfound_prescription = (ImageView) rootView.findViewById(R.id.norecordfound_prescription);
        oldPrescriptions = (ListView) rootView.findViewById(R.id.oldPrescriptions);

        return rootView;
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_myprescription);

        dataS.clear();

        getPrescriptionFromServer();
    }

    private void getPrescriptionFromServer()
    {
        String user_id="0";
        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            patient_id=patient.getString("id");
            user_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}


        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_uploaded_prescription/patientID/"+user_id+"/apitoken/"+token;

        Log.d("@ old pres url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@ presc response ",result.toString());

                ///result=loadJSONFromAsset();//---test purpose, loading from asset*********
                // Log.d("@ asset json ",result.toString());

                if (result.equals("")) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
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

                    String id="0",title="",patient_id="",upload_filename="",prescription_file_path="";

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_prescription.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        String names[] = new String[data_arr.length()];

                        for (int i = 0; i < data_arr.length(); i++)
                        {
                            JSONObject doc=data_arr.getJSONObject(i);
                            id = doc.getString("id");
                            title = doc.getString("prescription_title");
                            patient_id = doc.getString("patient_id");
                            upload_filename = doc.getString("upload_filename");
                            prescription_file_path = replaceString(doc.getString("prescription_file_path"));

                            Log.d("@ pres name:",id+ " title: "+title+ "file:"+prescription_file_path);

                            PrescriptionItem navItem = new PrescriptionItem();
                            navItem.setId(id);
                            navItem.setTitle(title);
                            navItem.setPatient_id(patient_id);
                            navItem.setUpload_filename(upload_filename);
                            navItem.setPrescription_file_path(prescription_file_path);

                            dataS.add(navItem);
                            names[i]=title;
                        }


                        adapter=new CustomPrescriptionListAdapterN(context, names,dataS );
                        oldPrescriptions.setAdapter( adapter );
                        oldPrescriptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(context, "choosed "+dataS.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Prescription fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Prescription fetching failed"+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                pdspinnerGeneral.dismiss();
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
