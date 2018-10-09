package com.brandhype.medixfora.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.interfaces.FragmentLifecycle;
import com.brandhype.medixfora.models.AppointmentItem;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientinfoFragment extends Fragment implements FragmentLifecycle {

    private static final String TAG = PatientinfoFragment.class.getSimpleName();


    Context context;
    View v;
    EditText patientinfo_name_edt,patientinfo_mobile_edt,patientinfo_email_edt;
    RadioGroup genderRadioGroup;
    Spinner patientinfo_appttype_sp;

    TextView patientinfo_dt_tv;

    public String doctor_id,booking_date,slotid,book_datetime;

    AppPreferences pref;
    JSONObject patient;

    AppointmentItem appointmentItem;

    Button doctor_book_nxt2;
    String appt_type[]={"Physical Appt (Rs 1200/-).","Through Call (Rs 500/-)","Through Video (Rs 800/-)","Through Chat (Rs 400/-)","Through Email (free)"};
    String appt_fees[]={"1200","500","800","400","0"};


    public PatientinfoFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context=getActivity();
        v = inflater.inflate(R.layout.fragment_patientinfo, container, false);

        patientinfo_name_edt = (EditText) v.findViewById(R.id.patientinfo_name_edt);
        patientinfo_mobile_edt = (EditText) v.findViewById(R.id.patientinfo_mobile_edt);
        patientinfo_email_edt = (EditText) v.findViewById(R.id.patientinfo_email_edt);

        patientinfo_dt_tv = (TextView) v.findViewById(R.id.patientinfo_dt_tv);

        patientinfo_appttype_sp = (Spinner) v.findViewById(R.id.patientinfo_appttype_sp);
        ArrayAdapter<String> adapterapptytype = new ArrayAdapter<String>(context, R.layout.simple_spinner,R.id.tv_appttype, appt_type);
        patientinfo_appttype_sp.setAdapter(adapterapptytype);

        doctor_book_nxt2 = (Button) v.findViewById(R.id.doctor_book_nxt2);
        doctor_book_nxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity act = (Activity) context;
                    ViewPager viewPager = (ViewPager) act.findViewById(R.id.pager);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        });




        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));

            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;

            patient_id=patient.getString("id");
            patient_first_name=patient.getString("first_name");
            if(! commonutils.isNullOrBlank(patient.getString("last_name")))
                patient_last_name=patient.getString("last_name");
            else
                patient_last_name="";
            patient_email=patient.getString("email");
            patient_contact=patient.getString("contact_number");
            //patient_gender=patient.getString("gender");

            if(!patient_first_name.equals(""))
            {
                patientinfo_name_edt.setText(patient_first_name+" "+patient_last_name);
                patientinfo_name_edt.requestFocus();
            }
            if(!patient_email.equals(""))
            {
                patientinfo_email_edt.setText(patient_email);
            }
            if(!patient_contact.equals(""))
            {
                patientinfo_mobile_edt.setText(patient_contact);
            }


        }catch (Exception e){}

        return v;
    }

    @Override
    public void onPause() {
        //Log.e("DEBUG", "OnPause of loginFragment");
        super.onPause();
    }

    @Override
    public void onPauseFragment(Context context) {
        Log.i(TAG, "onPauseFragment()");
        //Toast.makeText(context, "onPauseFragment():" + TAG, Toast.LENGTH_SHORT).show();

        try {

        Activity v=(Activity)context;

        EditText patientinfo_name_edt2 = (EditText) v.findViewById(R.id.patientinfo_name_edt);
        EditText patientinfo_mobile_edt2 = (EditText) v.findViewById(R.id.patientinfo_mobile_edt);
        EditText patientinfo_email_edt2 = (EditText) v.findViewById(R.id.patientinfo_email_edt);
        EditText patientinfo_health_edt2 = (EditText) v.findViewById(R.id.patientinfo_health_edt);
        Spinner  patientinfo_appttype_sp2=(Spinner) v.findViewById(R.id.patientinfo_appttype_sp);


        AppointmentItem appointmentItem2=new AppointmentItem();

        AppPreferences pref = new AppPreferences(context);//localstorage

        JSONObject patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
        String patient_id=patient.getString("id");

        String doctor_id2 = pref.LoadPreferences(AppPreferences.Storage.DOCTOR_ID.name());
        String booking_date2 = pref.LoadPreferences(AppPreferences.Storage.BOOKING_DATE.name());
        String slotid2 = pref.LoadPreferences(AppPreferences.Storage.SLOT_ID.name());
        String book_datetime2=pref.LoadPreferences(AppPreferences.Storage.SLOT_DATETIME.name());

        appointmentItem2.setDoctor_id(doctor_id2);
        appointmentItem2.setAppt_date(booking_date2);
        appointmentItem2.setSlotID(slotid2);

        appointmentItem2.setPatient_id(""+patient_id);
        appointmentItem2.setPatient_name(""+patientinfo_name_edt2.getText());
        appointmentItem2.setPatient_email(""+patientinfo_email_edt2.getText());
        appointmentItem2.setPatient_contact(""+patientinfo_mobile_edt2.getText());
        appointmentItem2.setPatient_health(""+patientinfo_health_edt2.getText());


        Log.d("# call type:",""+patientinfo_appttype_sp2.getSelectedItemPosition());

        String appt_fees2[]={"1200","500","800","400","0"};
        String fees=appt_fees2[patientinfo_appttype_sp2.getSelectedItemPosition()];
        appointmentItem2.setFees(""+fees);

        //String[] dtime = book_datetime2.split("\\s+");
        //appointmentItem2.setDispdate(dtime[0]);
        //appointmentItem2.setStart_time(dtime[2]);


        Gson gson = new Gson();
        String gsonobj=gson.toJson(appointmentItem2);

        pref.SavePreferences(AppPreferences.Storage.APPOINTMENTDATA.name(),gsonobj);

        }catch(Exception e){ e.printStackTrace();}

    }

    @Override
    public void onResumeFragment(Context context) {
        Activity v=(Activity)context;
        Log.i(TAG, "onResumeFragment()");

        //Toast.makeText(context, "onResumeFragment():" + TAG, Toast.LENGTH_SHORT).show();

        AppPreferences pref2 = new AppPreferences(context);

        doctor_id = pref2.LoadPreferences(AppPreferences.Storage.DOCTOR_ID.name());
        booking_date = pref2.LoadPreferences(AppPreferences.Storage.BOOKING_DATE.name());
        slotid = pref2.LoadPreferences(AppPreferences.Storage.SLOT_ID.name());
        book_datetime=pref2.LoadPreferences(AppPreferences.Storage.SLOT_DATETIME.name());


        if(slotid.equals("0"))
        {
            Toast.makeText(context, "You have'nt selected correct timing. Please move to appointment tab and select available timing.", Toast.LENGTH_LONG).show();
        }
        TextView patientinfo_dt_tv2 = (TextView) v.findViewById(R.id.patientinfo_dt_tv);
        patientinfo_dt_tv2.setText(book_datetime);

    }
}
