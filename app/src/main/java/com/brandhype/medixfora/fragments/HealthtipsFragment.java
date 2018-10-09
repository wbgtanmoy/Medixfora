package com.brandhype.medixfora.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.adpaters.HealthtipsAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
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
public class HealthtipsFragment extends Fragment {


    Button healthtips_submit_btn,healthtips_cancel_btn;
    EditText healthtips_message_edt;

    Context context;

    String query_txt="",patient_id="0";

    AppPreferences pref;
    JSONObject patient;

    private RecyclerView recyclerView;
    HealthtipsAdapter adapter;

    ImageView norecordfound_healthtips;
    private static String[] titles = null;
    List<GeneralItem> dataS = new ArrayList<>();
    FloatingActionButton fab_askquery;

    float dX;
    float dY;
    int lastAction;

    //AlertDialog alertDialog ;
    Dialog alertDialog ;

    public HealthtipsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_healtips, container, false);
        context=getActivity();



        fab_askquery = (FloatingActionButton) rootView.findViewById(R.id.fab_askquery);
        fab_askquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //Toast.makeText(context, "Post New Query", Toast.LENGTH_LONG).show();
                //showQueryDialog();
                }
        });

        fab_askquery.setOnTouchListener(new View.OnTouchListener() {

            float startX;
            float startRawX;
            float distanceX;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = view.getX() - event.getRawX();
                        startRawX = event.getRawX();
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        Log.e("@ action","Down");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        Log.e("@ action","Move");
                        break;

                    case MotionEvent.ACTION_UP:
                        distanceX = event.getRawX()-startRawX;
                        if (Math.abs(distanceX)< 5){
                            showQueryDialog();
                        }
                        if (lastAction == MotionEvent.ACTION_MOVE)
                        {
                            //Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
                            //showQueryDialog();
                            Log.e("@ action","up");
                        }
                        break;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        Log.e("@ action","pressed");
                        //Toast.makeText(context, "pressed!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        norecordfound_healthtips = (ImageView) rootView.findViewById(R.id.norecordfound_healthtips);
        norecordfound_healthtips.setVisibility(View.GONE);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.healthtips_List);

        titles=getResources().getStringArray(R.array.appointment_types_arrays);
        dataS = new ArrayList<>();
        return rootView;
    }


    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();

        // Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show();
        dataS.clear();
        getHealthtipsFromServer();

    }

    private void getHealthtipsFromServer()
    {
        //http://brandhypedigital.in/demo/medixfora/restapi/health_tips/get_health_tips_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url="";
        url = NetworkIOConstant.CS_APIUrls.BASE_URL + "health_tips/get_health_tips_list/apitoken/"+ token;
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

                    String title,description,image_path;


                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_healthtips.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject obj=data_arr.getJSONObject(i);
                            title = obj.getString("title");
                            description = obj.getString("description");
                            image_path = obj.getString("image_path");

                            GeneralItem navItem = new GeneralItem();
                            navItem.setTitle(title);
                            navItem.setSubtitle(description);
                            navItem.setImage(image_path);

                            Log.d("@ tips title ",title+ " image_path "+ image_path);
                            dataS.add(navItem);

                        }

                        adapter = new HealthtipsAdapter(context, dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((HealthtipsAdapter) adapter).setOnItemClickListener(new
                                         HealthtipsAdapter.MyClickListener()
                                         {
                                             @Override
                                             public void onItemClick(int position, View v)
                                             {
                                                 GeneralItem s=dataS.get(position);
                                                 Log.i("", " Clicked on Item " + s.getTitle() );
                                                 //Toast.makeText(getContext(),"Choosed id "+s.getTitle(),Toast.LENGTH_SHORT).show();
                                             }
                                         }
                        );


                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Pharmacy list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Pharmacy list fetching failed"+err);
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
        List<GeneralItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < 20; i++) {
            GeneralItem navItem = new GeneralItem();
            navItem.setTitle("Title"+i);
            navItem.setSubtitle("sub"+i);
            navItem.setImage("");

            dataS.add(navItem);

        }

    }



    private void showConfirmDialog()
    {
        //------------------------Dialog---------------------------------------------
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        TextView txt_main = (TextView) dialog.findViewById(R.id.txt_main);

        txt_main.setText("Are you sure to post this query ?");

        Button _cancel = (Button) dialog.findViewById(R.id.btn_no);
        Button _yes = (Button) dialog.findViewById(R.id.btn_yes);
        _yes.setEnabled(true);


        _cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //--Do nothing
            }
        });

        _yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendQueryToserver();
            }
        });

        if (dialog != null && !dialog.isShowing())
            dialog.show();
        //----------------------------------------------------------------------
    }

    private void sendQueryToserver()
    {
        query_txt=healthtips_message_edt.getText().toString();

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();patient_id="0";}

        //http://brandhypedigital.in/demo/medixfora/restapi/patient/save_health_tips_request
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/save_health_tips_request/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("query", query_txt);
        postDataParams.put("user_id", patient_id);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ query Post params",postJson.toString());

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(context, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
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


                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Query Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Query Success");
                                healthtips_message_edt.setText("");
                            }
                        });

                        new commonutils().showOKDialog(context,"Thanks for your query. Our expert will revert you very soon.");
                        alertDialog.dismiss();

                    } else {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, " Query Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error"," Query Failed:"+err);
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

    private void showQueryDialog()
    {
        alertDialog = new Dialog(context,R.style.popup_dialog);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //** no title bar
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.dialog_healthtips_query);

        //alertDialog = new AlertDialog.Builder(context).create();
        //LayoutInflater inflater = ((MainActivity)context).getLayoutInflater();
        //View convertView = (View) inflater.inflate(R.layout.dialog_healthtips_query, null);
        //alertDialog.setView(convertView);

        alertDialog.setTitle("Send Query");
        alertDialog.setCancelable(false);
        alertDialog.show();

        healthtips_message_edt=(EditText) alertDialog.findViewById(R.id.healthtips_message_edt);

        healthtips_cancel_btn = (Button) alertDialog.findViewById(R.id.healthtips_cancel_btn);
        healthtips_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        healthtips_submit_btn = (Button) alertDialog.findViewById(R.id.healthtips_submit_btn);
        healthtips_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!healthtips_message_edt.getText().toString().trim().equals("")){
                    showConfirmDialog();
                }
                else {
                    Toast.makeText(context, "Message/Query cannot be blank.", Toast.LENGTH_LONG).show();
                    healthtips_message_edt.requestFocus();
                }
            }
        });
    }


}
