package com.brandhype.medixfora.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.adpaters.DoctorAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.DoctorItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.hideKeyboard2;


public class DoctorListFragment extends Fragment {


    private RecyclerView recyclerView;
    DoctorAdapter adapter;
    private static String[] titles = null;
    TextView doc_search_view;
    ImageView norecordfound;

    List<DoctorItem> dataS = new ArrayList<>();

    Context context;
    String speciality_id="0",speciality_name="",hospital_id,hospital_name;
    String stype="doctor";

    public DoctorListFragment() {
        // Required empty public constructor
        //titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getActivity();
        try {

            stype = getArguments().getString("stype");

            if(stype.equals("speciality")) {
                speciality_id = getArguments().getString("speciality_id");
                speciality_name = getArguments().getString("speciality_name");
                Log.d("@ Speciality id : ",speciality_id);
            }
            else if(stype.equals("hospital"))
            {
                hospital_id = getArguments().getString("hospital_id");
                hospital_name = getArguments().getString("hospital_name");
                Log.d("@ Hospital id : ",hospital_id);
            }


            // drawer labels
            //titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
            dataS = new ArrayList<>();

            //getDoctorFromServer();
        }catch (Exception e){e.printStackTrace(); stype="doctor";}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_doctor_list, container, false);


        norecordfound = (ImageView) rootView.findViewById(R.id.norecordfound);
        norecordfound.setVisibility(View.GONE);

        doc_search_view = (EditText) rootView.findViewById(R.id.doc_search_view);
        doc_search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = doc_search_view.getText().toString().toLowerCase();
                Log.i("@ filter", " filtering" );
                try {
                    adapter.filter(text);
                }catch(Exception e){e.printStackTrace();}
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


        //-- this for the menu
        recyclerView = (RecyclerView) rootView.findViewById(R.id.doctor_List);

        //adapter = new DoctorAdapter(getActivity(), getSpeciality());

        return rootView;
    }


    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();

        hideKeyboard2(context);

        dataS.clear();

        if(stype.equals("speciality"))
            getDoctorBySpecialityFromServer();
        else if(stype.equals("hospital"))
            getDoctorByHospitalFromServer();
        else if(stype.equals("doctor"))
            getDoctorFromServer();
    }

    public static List<DoctorItem> getSpeciality() {
        List<DoctorItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            DoctorItem navItem = new DoctorItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }


    private void getDoctorFromServer()
    {
        //http://brandhypedigital.in/demo/medixfora/restapi/speciality/get_speciality_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_list/";

        Log.d("@ server url : ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        //postDataParams.put("speciality_id", "");
        //postDataParams.put("hospital_id", "");

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(getActivity(),postDataParams, 1) {

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

                    String is_active;
                    String id,name,city_name,doctor_fees,address,speciality_nam,speciality_ids,image_path, degree,designation,is_verified,about_doctor;

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        Log.d("@ doctor  length ",""+data_arr.length());
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
                            id = doc.getString("id");
                            speciality_nam = speciality_name;
                            speciality_ids=speciality_id;
                            name = doc.getString("doctor_name");

                            is_active = doc.getString("status");
                            address = doc.getString("address");
                            city_name = doc.getString("city_name");
                            degree = doc.getString("degree");
                            designation = doc.getString("designation");
                            is_verified=doc.getString("is_verified");
                            about_doctor = doc.getString("about_doctor");


                            image_path = replaceString(doc.getString("doctor_profile_image"));
                            Log.d("@ doctor  name ",name);

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                DoctorItem navItem = new DoctorItem();
                                navItem.setTitle(speciality_name);
                                navItem.setSpeciality_id(speciality_ids);
                                navItem.setSpeciality_name(speciality_nam);
                                navItem.setName(name);
                                navItem.setAddress(address);
                                navItem.setCity_name(city_name);
                                navItem.setImage(image_path);
                                navItem.setId(id);
                                navItem.setDegree(degree);
                                navItem.setDesignation(designation);
                                navItem.setIs_verified(is_verified);
                                navItem.setAbout_doctor(about_doctor);

                                Log.d("@ doctor  ",navItem.toString());

                                dataS.add(navItem);
                            }

                        }

                        //adapter.notifyDataSetChanged();

                        //-----------------fill adapter only after network call finished------

                        adapter = new DoctorAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((DoctorAdapter) adapter).setOnItemClickListener(new
                                                                                 DoctorAdapter.MyClickListener()
                                                                                 {
                                                                                     @Override
                                                                                     public void onItemClick(int position, View v)
                                                                                     {
                                                                                         DoctorItem s=dataS.get(position);
                                                                                         Log.i("", " Clicked on Item " + s.getId()+"::" +s.getName() );
                                                                                         //Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getName(),Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "Doctor list list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor list fetching failed"+err);
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

    private void getDoctorBySpecialityFromServer()
    {


        //http://brandhypedigital.in/demo/medixfora/restapi/speciality/get_speciality_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_list/";

        Log.d("@ server url : ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("speciality_id", speciality_id);
       //postDataParams.put("hospital_id", "1");

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(getActivity(),postDataParams, 1) {

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

                    String is_active;
                    String id,name,city_name,doctor_fees,address,speciality_nam,speciality_ids,image_path, degree,designation,is_verified,about_doctor;

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        Log.d("@ doctor  length ",""+data_arr.length());
                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }


                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);
                            id = doc.getString("id");
                            speciality_nam = speciality_name;
                            speciality_ids=speciality_id;
                            name = doc.getString("doctor_name");

                            is_active = doc.getString("status");
                            address = doc.getString("address");
                            city_name = doc.getString("city_name");
                            degree = doc.getString("degree");
                            designation = doc.getString("designation");
                            is_verified=doc.getString("is_verified");
                            about_doctor=doc.getString("about_doctor");

                            image_path = replaceString(doc.getString("doctor_profile_image"));
                            Log.d("@ doctor  name ",name);

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                DoctorItem navItem = new DoctorItem();
                                navItem.setTitle(speciality_name);
                                navItem.setSpeciality_id(speciality_ids);
                                navItem.setSpeciality_name(speciality_nam);
                                navItem.setName(name);
                                navItem.setAddress(address);
                                navItem.setCity_name(city_name);
                                navItem.setImage(image_path);
                                navItem.setId(id);
                                navItem.setDegree(degree);
                                navItem.setDesignation(designation);
                                navItem.setIs_verified(is_verified);
                                navItem.setAbout_doctor(about_doctor);

                                Log.d("@ doctor ", navItem.toString());

                                dataS.add(navItem);
                            }

                        }

                        //adapter.notifyDataSetChanged();

                        //-----------------fill adapter only after network call finished------

                        adapter = new DoctorAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((DoctorAdapter) adapter).setOnItemClickListener(new
                                     DoctorAdapter.MyClickListener()
                                     {
                                         @Override
                                         public void onItemClick(int position, View v)
                                         {
                                             DoctorItem s=dataS.get(position);
                                             Log.i("", " Clicked on Item " + s.getId()+"::" +s.getName() );
                                             //Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getName(),Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(context, "Doctor list list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor list fetching failed"+err);
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



    private void getDoctorByHospitalFromServer()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/speciality/get_speciality_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_list/";

        Log.d("@ server url : ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("hospital_id", hospital_id);
        //postDataParams.put("hospital_id", "1");

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(getActivity(),postDataParams, 1) {

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

                    String is_active;
                    String id,name,city_name,doctor_fees,address,speciality_nam,speciality_ids,image_path, degree,designation,is_verified,about_doctor;

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        Log.d("@ doctor  length ",""+data_arr.length());
                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);
                            id = doc.getString("id");
                            //speciality_nam = speciality_name;
                            //speciality_ids=speciality_id;
                            name = doc.getString("doctor_name");

                            is_active = doc.getString("status");
                            address = doc.getString("address");
                            city_name = doc.getString("city_name");
                            degree = doc.getString("degree");
                            designation = doc.getString("designation");
                            is_verified=doc.getString("is_verified");
                            about_doctor=doc.getString("about_doctor");

                            image_path = replaceString(""+doc.getString("doctor_profile_image"));
                            Log.d("@ doctor  name ",name);

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                DoctorItem navItem = new DoctorItem();
                                //navItem.setTitle(speciality_name);
                                //navItem.setSpeciality_id(speciality_ids);
                                //navItem.setSpeciality_name(speciality_nam);
                                navItem.setName(name);
                                navItem.setAddress(address);
                                navItem.setCity_name(city_name);
                                navItem.setImage(image_path);
                                navItem.setId(id);
                                navItem.setDegree(degree);
                                navItem.setDesignation(designation);
                                navItem.setIs_verified(is_verified);
                                navItem.setAbout_doctor(about_doctor);

                                Log.d("@ doctor   ", navItem.toString());

                                dataS.add(navItem);
                            }

                        }

                        //adapter.notifyDataSetChanged();

                        //-----------------fill adapter only after network call finished------

                        adapter = new DoctorAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((DoctorAdapter) adapter).setOnItemClickListener(new
                                 DoctorAdapter.MyClickListener()
                                 {
                                     @Override
                                     public void onItemClick(int position, View v)
                                     {
                                         DoctorItem s=dataS.get(position);
                                         Log.i("", " Clicked on Item " + s.getId()+"::" +s.getName() );
                                         //Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getName(),Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(context, "Doctor list list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor list fetching failed"+err);
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

    public String replaceString(String rtstr) {

        //rtstr = rtstr.replace("[", "");
        //rtstr = rtstr.replace("{", "");
        //rtstr = rtstr.replace("}", "");
        rtstr = rtstr.replace("\"", "");
        rtstr = rtstr.replace("\\/", "/");
        //rtstr = rtstr.replace("]", "");
        //rtstr = rtstr.replace(" ", "%20");
        return rtstr;
    }

}
