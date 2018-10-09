package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivityDoctor;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.adpaters.HospitalSpecilaityListAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.UploadMultipartWithImageAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.GeneralItem;

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
public class DoctorEditHospitalFragment extends Fragment {

    String hospitals[]={"",""};
    String specialities[]={"",""};

    //List<String> speciality_stringlist = new ArrayList<String>();
    //List<String> hospital_stringlist = new ArrayList<>();

    //List<SpecialityItem> speciality_list = new ArrayList<>();
    List<GeneralItem> hospital_list = new ArrayList<>();

    Context context;
    RecyclerView mRecyclerView;
    HospitalSpecilaityListAdapter mAdapter;

    Button update_doc_hospital_btn;

    String doctor_hospitals[];

    public DoctorEditHospitalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_doctor_edit_hospital, container, false);
        context=getActivity();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.hospital_recycler_view);

        update_doc_hospital_btn = (Button) rootView.findViewById(R.id.update_doc_hospital_btn);
        update_doc_hospital_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = "",ids="";
                List<GeneralItem> stList = ((HospitalSpecilaityListAdapter) mAdapter).getList();

                for (int i = 0; i < stList.size(); i++) {
                    GeneralItem item = stList.get(i);
                    if (item.getSelected() == true) {
                        data = data + "\n" + item.getTitle().toString();
                        ids +=  item.getId().toString()+",";
                    }
                }
                //Toast.makeText(context, "Selected ids: " + ids, Toast.LENGTH_SHORT).show();
                Log.d("@ Selected ids: ",ids.toString());

                updateDoctorHospital(ids);
            }
        });
        return rootView;
    }


    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();
        //getHospitalsFromServer();
        getDoctorFromServer();
    }

    private void getHospitalsFromServer()
    {
        //http://brandhypedigital.in/demo/medixfora/restapi/hospital/get_hospital_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "hospital/get_hospital_list/apitoken/"+ token;
        Log.d("@ server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

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


                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    String hospital_name,id,is_active,logo_image_path,logo_image;


                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        Log.d("@ doctor  length ",""+data_arr.length());
                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        }

                        Log.d("@  existing spcl count:",""+doctor_hospitals.length);

                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            hospital_name = data_arr.getJSONObject(i).getString("hospital_name");
                            logo_image = data_arr.getJSONObject(i).getString("logo_image");
                            logo_image_path = data_arr.getJSONObject(i).getString("logo_image_path");
                            is_active = data_arr.getJSONObject(i).getString("is_active");

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                GeneralItem navItem = new GeneralItem();
                                navItem.setTitle(hospital_name);
                                navItem.setId(id);
                                navItem.setSelected(false);

                                for(int j=0;j<doctor_hospitals.length;j++)
                                {
                                    if (id.equals(doctor_hospitals[j]))
                                    {
                                        navItem.setSelected(true);
                                        Log.d("@ matched hospital:",hospital_name);
                                    }

                                }

                                hospital_list.add(navItem);
                                //hospital_stringlist.add(hospital_name);
                            }

                        }

                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                        mAdapter = new HospitalSpecilaityListAdapter(hospital_list);
                        mRecyclerView.setAdapter(mAdapter);


                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Specility list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Specility list fetching failed"+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    pdspinnerGeneral.dismiss();
                }

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } else {
            submitAsync.execute(url);
        }

    }


    private void getDoctorFromServer()
    {
        AppPreferences pref;
        JSONObject doctor;
        String doctor_id="0";

        try {
            pref = new AppPreferences(context);
            doctor = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            doctor_id=doctor.getString("id");

        }catch (Exception e){e.printStackTrace();}

        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/doctor/get_doctor_details/apitoken/1edc0ae98198866510bce219d5115b72/doctorID/2191
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_details/apitoken/"+token+"/doctorID/"+doctor_id;
        Log.d("@ doc server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(context, 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                Log.d("@ server"," response "+result.toString());

                if (result.equals("")) {

                    ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            Log.e("Error","Doctor data not found");
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
                    //String is_validate = resultJson.getString("is_validate");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1") )
                    {

                        JSONObject doctor = resultJson.getJSONObject("result");


                        String doctor_id,doctor_name,doctor_email,designation,doctor_fee,degree,address,contact_number;
                        String country_name,city_name,postcode,website_url;
                        String about_doctor,doctor_profile_image_path,sp_name,h_name,sp_id,h_id;

                        doctor_id=doctor.getString("id");
                        doctor_name=doctor.getString("doctor_name");
                        doctor_email=doctor.getString("email");
                        designation=doctor.getString("designation");
                        degree=doctor.getString("degree");
                        doctor_fee=doctor.getString("doctor_fee");

                        address=doctor.getString("address");
                        contact_number=doctor.getString("contact_number");
                        country_name=doctor.getString("country_name");
                        city_name=doctor.getString("city_name");
                        postcode=doctor.getString("postcode");
                        website_url=replaceString(doctor.getString("website_url"));
                        about_doctor=doctor.getString("about_doctor");

                        doctor_profile_image_path=replaceString(doctor.getString("doctor_profile_image_path"));
                        sp_id=doctor.getString("sp_id");
                        sp_name=doctor.getString("sp_name");
                        h_id=doctor.getString("h_id");
                        h_name=doctor.getString("h_name");

                        Log.d("@ Doctor info:"," name :"+ doctor_name+" image url:  "+doctor_profile_image_path+ " hp id:"+h_id);

                        doctor_hospitals=h_id.split(",");

                        getHospitalsFromServer();


                    }
                    else
                    {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Cart  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor fetching failed"+err);
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


    private void updateDoctorHospital(String h_id)
    {
        AppPreferences pref;
        JSONObject doctor;
        String doctor_id="0";

        try {
            pref = new AppPreferences(context);
            doctor = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            doctor_id=doctor.getString("id");

        }catch (Exception e){e.printStackTrace();}



        String token= NetworkIOConstant.CS_Token.TOKEN;
        //http://brandhypedigital.in/demo/medixfora/restapi/doctor/get_doctor_details/apitoken/1edc0ae98198866510bce219d5115b72/doctorID/2191
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/update_doctor_hospital";
        Log.d("@ doc prof url : ",url);


        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("doctor_id", doctor_id);
        postDataParams.put("h_id", h_id);

        /*String Filepath="";
        ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> files = new HashMap<String, String>();
        if(!Filepath.equals("")){
            files.put("Fileupload", Filepath);//"key,value
        }
        fileList.add(files);*/


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ hitting  at : ","update hospital: "+ url);
        Log.d("@ uploaddoc params ", "update hospital: "+ postJson.toString());


        UploadMultipartWithImageAsynctask itemAsynctask=new UploadMultipartWithImageAsynctask(context, postDataParams, null, url,new AsyncResponse(){

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
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Updating Hospital Completed", Toast.LENGTH_SHORT).show();
                                Log.d("Updating", "Updating hospital Completed");
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
                itemAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                itemAsynctask.execute();//serial
            }
        }


    }


}
