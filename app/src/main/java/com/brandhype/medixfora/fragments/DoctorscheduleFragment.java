package com.brandhype.medixfora.fragments;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivityDoctor;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.ScheduleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorscheduleFragment extends Fragment implements View.OnClickListener {


    Context context;

    TextView docschedule_starttime_1,docschedule_starttime_2,docschedule_starttime_3,docschedule_starttime_4,docschedule_starttime_5,docschedule_starttime_6,docschedule_starttime_7;
    TextView docschedule_patientcount_1,docschedule_patientcount_2,docschedule_patientcount_3,docschedule_patientcount_4,docschedule_patientcount_5,docschedule_patientcount_6,docschedule_patientcount_7;
    ImageView doc_schedule_edit_1,doc_schedule_edit_2,doc_schedule_edit_3,doc_schedule_edit_4,doc_schedule_edit_5,doc_schedule_edit_6,doc_schedule_edit_7;


    String starttime,endtime,noofpatients;
    EditText docschedule_starttime_edt;
    EditText docschedule_endtime_edt,docschedule_noofpatient_edt;

    List<ScheduleItem> dataS = new ArrayList<>();

    public DoctorscheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        rootView=inflater.inflate(R.layout.fragment_doctorschedule, container, false);

        context=getActivity();

        docschedule_starttime_1 = (TextView) rootView.findViewById(R.id.docschedule_starttime_1);
        docschedule_starttime_2 = (TextView) rootView.findViewById(R.id.docschedule_starttime_2);
        docschedule_starttime_3 = (TextView) rootView.findViewById(R.id.docschedule_starttime_3);
        docschedule_starttime_4 = (TextView) rootView.findViewById(R.id.docschedule_starttime_4);
        docschedule_starttime_5 = (TextView) rootView.findViewById(R.id.docschedule_starttime_5);
        docschedule_starttime_6 = (TextView) rootView.findViewById(R.id.docschedule_starttime_6);
        docschedule_starttime_7 = (TextView) rootView.findViewById(R.id.docschedule_starttime_7);

        docschedule_patientcount_1 = (TextView) rootView.findViewById(R.id.docschedule_patientcount_1);
        docschedule_patientcount_2 = (TextView) rootView.findViewById(R.id.docschedule_patientcount_2);
        docschedule_patientcount_3 = (TextView) rootView.findViewById(R.id.docschedule_patientcount_3);
        docschedule_patientcount_4 = (TextView) rootView.findViewById(R.id.docschedule_patientcount_4);
        docschedule_patientcount_5 = (TextView) rootView.findViewById(R.id.docschedule_patientcount_5);
        docschedule_patientcount_6 = (TextView) rootView.findViewById(R.id.docschedule_patientcount_6);
        docschedule_patientcount_7 = (TextView) rootView.findViewById(R.id.docschedule_patientcount_7);

        doc_schedule_edit_1 = (ImageView) rootView.findViewById(R.id.doc_schedule_edit_1);
        doc_schedule_edit_2 = (ImageView) rootView.findViewById(R.id.doc_schedule_edit_2);
        doc_schedule_edit_3 = (ImageView) rootView.findViewById(R.id.doc_schedule_edit_3);
        doc_schedule_edit_4 = (ImageView) rootView.findViewById(R.id.doc_schedule_edit_4);
        doc_schedule_edit_5 = (ImageView) rootView.findViewById(R.id.doc_schedule_edit_5);
        doc_schedule_edit_6 = (ImageView) rootView.findViewById(R.id.doc_schedule_edit_6);
        doc_schedule_edit_7 = (ImageView) rootView.findViewById(R.id.doc_schedule_edit_7);

        doc_schedule_edit_1.setOnClickListener(this);
        doc_schedule_edit_2.setOnClickListener(this);
        doc_schedule_edit_3.setOnClickListener(this);
        doc_schedule_edit_4.setOnClickListener(this);
        doc_schedule_edit_5.setOnClickListener(this);
        doc_schedule_edit_6.setOnClickListener(this);
        doc_schedule_edit_7.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();
        dataS.clear();
        getDoctorSchedule();

    }

    @Override
    public void onClick(View view) {
        Integer rownum=0;
        switch(view.getId())
        {
            case R.id.doc_schedule_edit_1:
                rownum=1;
                break;
            case R.id.doc_schedule_edit_2:
                rownum=2;
                break;
            case R.id.doc_schedule_edit_3:
                rownum=3;
                break;
            case R.id.doc_schedule_edit_4:
                rownum=4;
                break;
            case R.id.doc_schedule_edit_5:
                rownum=5;
                break;
            case R.id.doc_schedule_edit_6:
                rownum=6;
                break;
            case R.id.doc_schedule_edit_7:
                rownum=7;
                break;
        }

        //Toast.makeText(context,"Edit "+rownum,Toast.LENGTH_SHORT).show();
        setDoctorTime(rownum);
    }



    private void getDoctorSchedule()
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
        //http://brandhypedigital.in/demo/medixfora/restapi/doctor/get_doctor_schedule/dID/2191/apitoken/1edc0ae98198866510bce219d5115b72
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/get_doctor_schedule/dID/"+doctor_id+"/apitoken/"+token+"";
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
                            Log.e("Error","Doctor schedule data not found");
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
                        JSONArray doctors = resultJson.getJSONArray("result");

                        String id,doctor_id,week_day,checkup_start_time,checkup_end_time,appoint_number_of_patient;

                        for (int i = 0; i < 7; i++) {
                            ScheduleItem navItem = new ScheduleItem();
                            navItem.setId("");
                            navItem.setDoctor_id("");
                            navItem.setWeek_day("");
                            navItem.setCheckup_start_time("");
                            navItem.setCheckup_end_time("");
                            navItem.setAppoint_number_of_patient("");
                            dataS.add(navItem);
                        }

                        for (int i = 0; i < doctors.length(); i++) {

                            JSONObject doc = doctors.getJSONObject(i);
                            id=doc.getString("id");
                            doctor_id=doc.getString("doctor_id");
                            week_day=doc.getString("week_day");
                            checkup_start_time=""+doc.getString("checkup_start_time");
                            checkup_end_time=""+doc.getString("checkup_end_time");
                            appoint_number_of_patient=doc.getString("appoint_number_of_patient");

                            Log.d("@ Doctor schedule:"," day :"+ week_day+" schedule:  "+checkup_start_time +"--"+checkup_end_time);

                            ScheduleItem navItem = new ScheduleItem();
                            navItem.setId(id);
                            navItem.setDoctor_id(doctor_id);
                            navItem.setWeek_day(week_day);
                            navItem.setCheckup_start_time(checkup_start_time);
                            navItem.setCheckup_end_time(checkup_end_time);
                            navItem.setAppoint_number_of_patient(appoint_number_of_patient);

                            //dataS.add(navItem);
                            dataS.set(Integer.parseInt(week_day)-1, navItem);//replace object at index i

                            setDisplaySchedule(doc);
                        }

                    }
                    else
                    {
                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Cart  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor schedule fetching failed"+err);
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


    public void setDisplaySchedule( JSONObject doc)
    {
        try{

            JSONObject doctor = doc;
            String week_day=doctor.getString("week_day");
            String appt_time=doctor.getString("checkup_start_time")+"--"+doctor.getString("checkup_end_time");
            String no_of_patient= doc.getString("appoint_number_of_patient");

            if(week_day.equals("1"))
            {
                docschedule_starttime_1.setText(appt_time);
                docschedule_patientcount_1.setText(no_of_patient);
            }
            else if(week_day.equals("2")){

                docschedule_starttime_2.setText(appt_time);
                docschedule_patientcount_2.setText(no_of_patient);
            }
            else if(week_day.equals("3")){

                docschedule_starttime_3.setText(appt_time);
                docschedule_patientcount_3.setText(no_of_patient);
            }
            else if(week_day.equals("4")){

                docschedule_starttime_4.setText(appt_time);
                docschedule_patientcount_4.setText(no_of_patient);
            }
            else if(week_day.equals("5")){

                docschedule_starttime_5.setText(appt_time);
                docschedule_patientcount_5.setText(no_of_patient);
            }
            else if(week_day.equals("6")){

                docschedule_starttime_6.setText(appt_time);
                docschedule_patientcount_6.setText(no_of_patient);
            }
            else if(week_day.equals("7")){

                docschedule_starttime_7.setText(appt_time);
                docschedule_patientcount_7.setText(no_of_patient);
            }
            else{

                docschedule_starttime_7.setText("--  --");
                docschedule_patientcount_7.setText("--");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void setDoctorTime(final Integer rownum)
    {
        //------------------------Dialog---------------------------------------------

        starttime="";
        endtime="";
        noofpatients="";

        if(dataS.size()>0) {
            ScheduleItem navItem = dataS.get(rownum - 1);
            starttime = "" + navItem.getCheckup_start_time();
            endtime = "" + navItem.getCheckup_end_time();
            noofpatients = "" + navItem.getAppoint_number_of_patient();
        }


        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.doctor_schedule_dialog);

        alertDialog.getWindow().getAttributes().width = ViewGroup.LayoutParams.FILL_PARENT ;

        ImageView docschedule_starttime_pick = (ImageView) alertDialog.findViewById(R.id.docschedule_starttime_pick);
        ImageView docschedule_endtime_pick = (ImageView) alertDialog.findViewById(R.id.docschedule_endtime_pick);
        Button docschedule_button = (Button) alertDialog.findViewById(R.id.docschedule_button);

        Button docschedule_cancel_button = (Button) alertDialog.findViewById(R.id.docschedule_cancel_button);
        Button docschedule_reset_button = (Button) alertDialog.findViewById(R.id.docschedule_reset_button);

        docschedule_starttime_edt=(EditText) alertDialog.findViewById(R.id.docschedule_starttime_edt);
        docschedule_starttime_edt.setKeyListener(null);
        docschedule_starttime_edt.setText(starttime);

        docschedule_endtime_edt=(EditText) alertDialog.findViewById(R.id.docschedule_endtime_edt);
        docschedule_endtime_edt.setKeyListener(null);
        docschedule_endtime_edt.setText(endtime);

        docschedule_noofpatient_edt=(EditText) alertDialog.findViewById(R.id.docschedule_noofpatient_edt);
        docschedule_noofpatient_edt.setText(noofpatients);


        docschedule_starttime_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertDialog.dismiss();
                //get start time

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY,selectedHour);
                        cal.set(Calendar.MINUTE,selectedMinute);
                        Format formatter;
                        formatter = new SimpleDateFormat("h:mm a");
                        String timetoshow= formatter.format(cal.getTime());

                        docschedule_starttime_edt.setText( timetoshow);
                        starttime=timetoshow;
                    }
                }, hour, minute, false);//Yes 12 hour time
                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
            }
        });

        docschedule_endtime_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //alertDialog.dismiss();
                //get end time
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY,selectedHour);
                        cal.set(Calendar.MINUTE,selectedMinute);
                        Format formatter;
                        formatter = new SimpleDateFormat("h:mm a");
                        String timetoshow= formatter.format(cal.getTime());

                        docschedule_endtime_edt.setText(timetoshow);
                        endtime=timetoshow;
                    }
                }, hour, minute, false);//Yes 12 hour time
                mTimePicker.setTitle("Select End Time");
                mTimePicker.show();
            }
        });

        docschedule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //save here
                noofpatients=docschedule_noofpatient_edt.getText().toString().trim();

                //--local validation
                String errormsg="";
                Integer errorcount=0;

                if(starttime.equals("")) {
                    errormsg+=" Start time cannot e blank.\n";
                    errorcount++;
                }
                if(endtime.equals("")) {
                    errormsg+=" End time cannot be blank.\n";
                    errorcount++;
                }
                if(noofpatients.equals("")) {
                    errormsg+=" Number of patient cannot be blank.\n";
                    errorcount++;
                }

                if(errorcount>0)
                {
                    Toast.makeText(context, ""+errormsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                alertDialog.dismiss();
                //updateSchedule(rownum,starttime,endtime,noofpatients);
                postDocscheduleToServer(starttime,endtime,noofpatients,rownum);
            }
        });

        docschedule_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        docschedule_reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                resetDoctorSchedule(rownum);
            }
        });


        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
    }




    private void resetDoctorSchedule(final Integer weekid)
    {
        AppPreferences pref;
        JSONObject doctor;
        String doctor_id="0";

        try {
            pref = new AppPreferences(context);
            doctor = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            doctor_id=doctor.getString("id");

        }catch (Exception e){e.printStackTrace();}


        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/reset_doctor_schedule/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("doctor_id", doctor_id);
        postDataParams.put("weekid", ""+weekid);

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params",postJson.toString());

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(context, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

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

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Doctor Schedule Reset ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Doctor Schedule Reset");


                            }
                        });

                        //updateUI(Integer rownum,String starttime,String endtime,String noofpatients)

                        updateUI(weekid, "--","--","--" );

                    } else {

                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Doctor Schedule Saving Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor Schedule Saving Failed:"+err);
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


    private void postDocscheduleToServer(final String starttime,final String endtime,final String noofpatients,final Integer weekid)
    {

        AppPreferences pref;
        JSONObject doctor;
        String doctor_id="0";

        try {
            pref = new AppPreferences(context);
            doctor = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.DOCTORDATA.name()));
            doctor_id=doctor.getString("id");

        }catch (Exception e){e.printStackTrace();}


        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "doctor/save_doctor_schedule/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("doctor_id", doctor_id);
        postDataParams.put("weekid", ""+weekid);
        postDataParams.put("appoint_number_of_patient", noofpatients);
        postDataParams.put("checkup_start_datetime", starttime);
        postDataParams.put("checkup_end_datetime", endtime);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params",postJson.toString());

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(context, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

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

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    //String is_validate = obj.getString("is_validate");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Doctor Schedule Saved ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Doctor Schedule Saved");


                            }
                        });

                        //updateUI(Integer rownum,String starttime,String endtime,String noofpatients)
                        updateUI(weekid, starttime,endtime,noofpatients );

                    } else {

                        ((MainActivityDoctor)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Doctor Schedule Saving Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Doctor Schedule Saving Failed:"+err);
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

    private void updateUI(Integer rownum,String starttime,String endtime,String noofpatients)
    {

        String separator="--";

        switch(rownum)
        {
            case 1:
                docschedule_starttime_1.setText(starttime+"--"+endtime);
                docschedule_patientcount_1.setText(noofpatients);

                break;
            case 2:
                docschedule_starttime_2.setText(starttime+"--"+endtime);
                docschedule_patientcount_2.setText(noofpatients);
                break;
            case 3:
                docschedule_starttime_3.setText(starttime+"--"+endtime);
                docschedule_patientcount_3.setText(noofpatients);
                break;
            case 4:
                docschedule_starttime_4.setText(starttime+"--"+endtime);
                docschedule_patientcount_4.setText(noofpatients);
                break;
            case 5:
                docschedule_starttime_5.setText(starttime+"--"+endtime);
                docschedule_patientcount_5.setText(noofpatients);
                break;
            case 6:
                docschedule_starttime_6.setText(starttime+"--"+endtime);
                docschedule_patientcount_6.setText(noofpatients);
                break;
            case 7:
                docschedule_starttime_7.setText(starttime+"--"+endtime);
                docschedule_patientcount_7.setText(noofpatients);
                break;
        }

        //====update display======
        dataS.clear();
        getDoctorSchedule();
    }


}
