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
import com.brandhype.medixfora.adpaters.DoctorAppointmentAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.DoctorAppointmentItem;

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
public class DoctorAppointmentFragment extends Fragment {



    private RecyclerView recyclerView;
    DoctorAppointmentAdapter adapter;
    private static String[] titles = null;
    TextView docappt_datefrom,docappt_dateto;

    List<DoctorAppointmentItem> dataS = new ArrayList<>();

    Context context;
    String category_id="0",company_id="0";
    String name="",stype="category";

    ImageView norecordfound,docappt_datefrom_image,docappt_dateto_image;
    RadioGroup doctappt_details_tabs;


    public DoctorAppointmentFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView= inflater.inflate(R.layout.fragment_doctor_appointment, container, false);
        context=getActivity();


        /*docappt_datefrom = (TextView) rootView.findViewById(R.id.docappt_datefrom);
        docappt_dateto = (TextView) rootView.findViewById(R.id.docappt_dateto);

        docappt_datefrom_image = (ImageView) rootView.findViewById(R.id.docappt_datefrom_image);
        docappt_datefrom_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = ""+String.valueOf(year) +"-"+String.valueOf(monthOfYear)
                                +"-"+String.valueOf(dayOfMonth);
                        docappt_datefrom.setText(date);

                    }
                }, yy, mm, dd);
                datePicker.show();

            }
        });

        docappt_dateto_image = (ImageView) rootView.findViewById(R.id.docappt_dateto_image);
        docappt_dateto_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = ""+String.valueOf(year) +"-"+String.valueOf(monthOfYear)
                                +"-"+String.valueOf(dayOfMonth);
                        docappt_dateto.setText(date);

                    }
                }, yy, mm, dd);
                datePicker.show();

            }
        });
    */


        doctappt_details_tabs = (RadioGroup) rootView.findViewById(R.id.doctappt_details_tabs);
        doctappt_details_tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_today) {
                    Log.d("@ hosp det:","overview");
                    setTab(1);
                } else  if (checkedId == R.id.rb_upcomming) {
                    Log.d("@ hosp det:", "procedure");
                    setTab(2);

                } else  if (checkedId == R.id.rb_past) {
                    Log.d("@ hosp det:", "procedure");
                    setTab(3);
                }
            }
        });

        norecordfound = (ImageView) rootView.findViewById(R.id.norecordfound_doctappt);
        norecordfound.setVisibility(View.GONE);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.doctappt_List);

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
        getDoctorAppintments(tab);
    }


    private void getDoctorAppintments(Integer tab){

        dataS.clear();
        String url="";
        url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_appoinments";
        String appointment_for_the_day="";//PAST / PRESENT / FUTURE

        if(tab==1)
            appointment_for_the_day="PRESENT";
        else if(tab==2)
            appointment_for_the_day="FUTURE";
        else if(tab==3)
            appointment_for_the_day="PAST";

        String token= NetworkIOConstant.CS_Token.TOKEN;

        AppPreferences pref;
        JSONObject patient;
        String user_id="0";
        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            user_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device , 2 is IOS ---
        postDataParams.put("doctor_id", user_id);
        postDataParams.put("appointment_for_the_day", appointment_for_the_day);

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



                    String doctor_appointment_date,checkup_start_time,checkup_end_time,first_name,last_name,address;
                    String city_name,country_name,postcode,contact_number,email,profile_image_file;
                    String doctor_appointment_format_date,item_price;

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
                            first_name = doc.getString("first_name");
                            last_name = doc.getString("last_name");
                            email = doc.getString("email");
                            address = doc.getString("address");
                            country_name = doc.getString("country_name");
                            city_name = doc.getString("city_name");
                            postcode = doc.getString("postcode");
                            contact_number =doc.getString("contact_number");
                            profile_image_file =  replaceString(doc.getString("profile_image_file"));
                            checkup_start_time = doc.getString("checkup_start_time");
                            checkup_end_time = doc.getString("checkup_end_time");
                            doctor_appointment_format_date= doc.getString("doctor_appointment_format_date");
                            item_price = doc.getString("item_price");

                            Log.d("@ doc",first_name + " "+last_name);

                            DoctorAppointmentItem navItem = new DoctorAppointmentItem();

                            navItem.setDoctor_appointment_date(doctor_appointment_date);
                            navItem.setFirst_name(first_name);
                            navItem.setLast_name(last_name);
                            navItem.setEmail(email);
                            navItem.setAddress(address);
                            navItem.setCountry_name(country_name);
                            navItem.setCity_name(city_name);
                            navItem.setPostcode(postcode);
                            navItem.setContact_number(contact_number);
                            navItem.setProfile_image_file(profile_image_file);
                            navItem.setCheckup_start_time(checkup_start_time);
                            navItem.setCheckup_end_time(checkup_end_time);
                            navItem.setDoctor_appointment_format_date(doctor_appointment_format_date);
                            navItem.setItem_price(item_price);

                            dataS.add(navItem);

                        }

                        //-----------------fill adapter only after network call finished------
                        adapter = new DoctorAppointmentAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((DoctorAppointmentAdapter) adapter).setOnItemClickListener(new DoctorAppointmentAdapter.MyClickListener()
                             {
                                 @Override
                                 public void onItemClick(int position, View v)
                                 {
                                     DoctorAppointmentItem s=dataS.get(position);
                                     Log.i("", " Clicked on Item " + s.getFirst_name()+" " +s.getLast_name() );
                                     //Toast.makeText(getContext(),"Choosed  "+  s.getFirst_name()+" " +s.getLast_name(),Toast.LENGTH_SHORT).show();
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




}
