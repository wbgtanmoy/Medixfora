package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.adpaters.PatientAppointmentAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.PatientAppointmentItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientAppointmentFragment extends Fragment {



    private RecyclerView recyclerView;
    PatientAppointmentAdapter adapter;
    private static String[] titles = null;
    TextView patientappt_datefrom,patientappt_dateto;

    List<PatientAppointmentItem> dataS = new ArrayList<>();

    Context context;
    String category_id="0",company_id="0";
    String name="",stype="category";

    ImageView norecordfound,patientappt_datefrom_image,patientappt_dateto_image;
    RadioGroup patientappt_details_tabs;

    public PatientAppointmentFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView= inflater.inflate(R.layout.fragment_patient_appointment, container, false);
        context=getActivity();

        patientappt_details_tabs = (RadioGroup) rootView.findViewById(R.id.patientappt_details_tabs);
        patientappt_details_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_hosp_overview) {
                    Log.d("@ hosp det:","overview");
                    setTab(1);
                } else  if (checkedId == R.id.rb_hosp_procedure) {
                    Log.d("@ hosp det:", "procedure");
                    setTab(2);
                }
            }
        });

        norecordfound = (ImageView) rootView.findViewById(R.id.norecordfound_patientappt);
        norecordfound.setVisibility(View.GONE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.patientappt_List);
        titles=getResources().getStringArray(R.array.appointment_types_arrays);
        dataS = new ArrayList<>();
        return rootView;
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_patient_appointment);

        dataS.clear();
        setTab(1);
    }


    private void setTab(Integer tab)
    {
        norecordfound.setVisibility(View.GONE);
        getPatientAppintments(tab);
    }

    private void getPatientAppintments(Integer tab){

        dataS.clear();
        String url="";
        url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_appoinments";

        if(tab==1)
            url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_appoinments";
        else if(tab==2)
            url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_previous_appoinments";

        String token= NetworkIOConstant.CS_Token.TOKEN;

        AppPreferences pref;
        JSONObject patient;
        String user_id="0";
        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            user_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device , 2 is IOS ---
        postDataParams.put("patientID", user_id); //

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ checkout params", " checkout "+ postJson.toString());

        //if(true){return; }

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(context, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

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

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));
                    JSONArray data_arr = obj.getJSONArray("result");

                    String doctor_appointment_date,doctor_name,email,address,country_name,city_name,item_price;
                    String postcode,contact_number,degree,designation,doctor_profile_image,week_day,checkup_start_time,checkup_end_time;

                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);

                            doctor_appointment_date = doc.getString("doctor_appointment_date");
                            doctor_name = doc.getString("doctor_name");
                            email = doc.getString("email");
                            address = doc.getString("address");
                            country_name = doc.getString("country_name");
                            city_name = doc.getString("city_name");
                            postcode = doc.getString("postcode");
                            contact_number =doc.getString("contact_number");
                            degree = doc.getString("degree");
                            designation = doc.getString("designation");
                            doctor_profile_image =  replaceString(doc.getString("doctor_profile_image"));
                            week_day = doc.getString("week_day");
                            checkup_start_time = doc.getString("checkup_start_time");
                            checkup_end_time = doc.getString("checkup_end_time");
                            item_price = doc.getString("item_price");

                            Log.d("@ doc",doctor_name + "on  "+doctor_appointment_date);

                            PatientAppointmentItem navItem = new PatientAppointmentItem();

                            navItem.setDoctor_appointment_date(doctor_appointment_date);
                            navItem.setDoctor_name(doctor_name);
                            navItem.setEmail(email);
                            navItem.setAddress(address);
                            navItem.setCountry_name(country_name);
                            navItem.setCity_name(city_name);
                            navItem.setPostcode(postcode);
                            navItem.setContact_number(contact_number);
                            navItem.setDegree(degree);
                            navItem.setDesignation(designation);
                            navItem.setDoctor_profile_image(doctor_profile_image);
                            navItem.setWeek_day(week_day);
                            navItem.setCheckup_start_time(checkup_start_time);
                            navItem.setCheckup_end_time(checkup_end_time);
                            navItem.setItem_price(item_price);


                            dataS.add(navItem);

                        }

                        //-----------------fill adapter only after network call finished------
                        adapter = new PatientAppointmentAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((PatientAppointmentAdapter) adapter).setOnItemClickListener(new PatientAppointmentAdapter.MyClickListener()
                                   {
                                       @Override
                                       public void onItemClick(int position, View v)
                                       {
                                           PatientAppointmentItem s=dataS.get(position);
                                           Log.i("", " Clicked on Item " + s.getDoctor_name()+"::" +s.getDoctor_appointment_date() );
                                           //Toast.makeText(getContext(),"Choosed  "+ s.getDoctor_name()+" On " + s.getDoctor_appointment_date(),Toast.LENGTH_SHORT).show();
                                       }
                                   }
                        );
                        //------------------------


                    }
                    else
                    {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Error :"+err, Toast.LENGTH_SHORT).show();
                                //new CustomToastAlertDialog((MainActivity)context,"Medixfora: Patient appointment Failed",err).show();
                                Log.e("@ Error","Fetching Patient appointment Failed:"+err);
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

    public  void setData() {
        List<PatientAppointmentItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < 20; i++) {
            PatientAppointmentItem navItem = new PatientAppointmentItem();
            navItem.setDoctor_name("xxxxx");
            dataS.add(navItem);

        }

    }


}
