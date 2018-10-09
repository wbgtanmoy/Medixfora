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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivityDoctor;
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
public class DoctorProifileFragment extends Fragment {


    Context context;

    ImageView doctor_prof_image,doc_profile_edit,doc_profile_hosp_edit,doc_profile_speciality_edit;
    TextView doctor_prof_name,doctor_prof_desig,doctor_prof_address;
    TextView doc_profile_email_val,doc_profile_phone_val,doc_profile_fees_val,doc_profile_about_val;
    TextView doc_profile_hospitals,doc_profile_speciality;

    public DoctorProifileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView=inflater.inflate(R.layout.fragment_doctor_proifile, container, false);;
        context=getActivity();

        doctor_prof_name = (TextView) rootView.findViewById(R.id.doctor_prof_name);
        doctor_prof_desig = (TextView) rootView.findViewById(R.id.doctor_prof_desig);
        doctor_prof_address = (TextView) rootView.findViewById(R.id.doctor_prof_address);

        doc_profile_email_val = (TextView) rootView.findViewById(R.id.doc_profile_email_val);
        doc_profile_phone_val = (TextView) rootView.findViewById(R.id.doc_profile_phone_val);
        doc_profile_fees_val = (TextView) rootView.findViewById(R.id.doc_profile_fees_val);
        doc_profile_about_val = (TextView) rootView.findViewById(R.id.doc_profile_about_val);

        doc_profile_hospitals = (TextView) rootView.findViewById(R.id.doc_profile_hospitals);
        doc_profile_speciality = (TextView) rootView.findViewById(R.id.doc_profile_speciality);

        doctor_prof_image = (ImageView) rootView.findViewById(R.id.doctor_prof_image);

        doc_profile_hosp_edit = (ImageView) rootView.findViewById(R.id.doc_profile_hosp_edit);
        doc_profile_hosp_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"edit profile",Toast.LENGTH_SHORT).show();
                commonutils.setFragment(getActivity(),new DoctorEditHospitalFragment(),null,R.string.title_edit_hospital,true);
            }
        });

        doc_profile_speciality_edit = (ImageView) rootView.findViewById(R.id.doc_profile_speciality_edit);
        doc_profile_speciality_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"edit profile",Toast.LENGTH_SHORT).show();
                commonutils.setFragment(getActivity(),new DoctorEditSpecialityFragment(),null,R.string.title_edit_speciality,true);
            }
        });

        doc_profile_edit = (ImageView) rootView.findViewById(R.id.doc_profile_edit);
        doc_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"edit profile",Toast.LENGTH_SHORT).show();
                commonutils.setFragment(getActivity(),new DoctorEditprofileFragment(),null,R.string.title_edit_docprofile,true);
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();
        getDoctorFromServer();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_docprofile);
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
                        String about_doctor,doctor_profile_image_path,sp_name,h_name;

                        doctor_id=doctor.getString("id");
                        doctor_name=doctor.getString("doctor_name");
                        doctor_email=doctor.getString("email");
                        designation=doctor.getString("designation");
                        degree=doctor.getString("degree");
                        doctor_fee=doctor.getString("doctor_fee");
                        address=doctor.getString("address");
                        contact_number=doctor.getString("contact_number");
                        about_doctor=doctor.getString("about_doctor");
                        doctor_profile_image_path=replaceString(doctor.getString("doctor_profile_image_path"));

                        if(! commonutils.isNullOrBlank(doctor.getString("sp_name")))
                            sp_name=doctor.getString("sp_name");
                        else
                            sp_name="";

                        if(! commonutils.isNullOrBlank(doctor.getString("h_name")))
                            h_name=doctor.getString("h_name");
                        else
                            h_name="";


                        Log.d("@ Doctor info:"," name :"+ doctor_name+" image url:  "+doctor_profile_image_path);

                        doctor_prof_name.setText(doctor_name);
                        doctor_prof_desig.setText(degree+ "\n"+ designation);
                        doctor_prof_address.setText(address);

                        doc_profile_email_val.setText(doctor_email);
                        doc_profile_phone_val.setText(contact_number);
                        doc_profile_fees_val.setText("Rs "+doctor_fee +"/-");
                        doc_profile_about_val.setText(about_doctor);

                        doc_profile_hospitals.setText(h_name);
                        doc_profile_speciality.setText(sp_name);

                        try {
                            Picasso.with(context)
                                    .load(doctor_profile_image_path)
                                    .placeholder(R.drawable.profilepic1)
                                    .error(R.drawable.default_avatar)
                                    .into(doctor_prof_image);
                        }catch(Exception e){ e.printStackTrace();}

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

}
