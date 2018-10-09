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
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.adpaters.MessageAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.MessageItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryReplyFragment extends Fragment {

    Context context;
    TextView query_reply_txt;

    List<MessageItem> dataS = new ArrayList<>();

    private RecyclerView recyclerView;
    AppPreferences pref;
    JSONObject patient;

    MessageAdapter adapter;

    boolean loading=false;
    int previousTotal=0;

    ImageView norecordfound_query_reply;

    public QueryReplyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_query_reply, container, false);
        context=getActivity();

       /* query_reply_txt=(TextView) rootView.findViewById(R.id.query_reply_txt);

        try {
            String msg = getArguments().getString("fcm_message");
            query_reply_txt.setText(msg);
            Log.d("@ fcm message  : ",msg);
            Toast.makeText(context,"Admin replied "+msg,Toast.LENGTH_SHORT).show();
        }catch (Exception e){e.printStackTrace();}*/

        norecordfound_query_reply = (ImageView) rootView.findViewById(R.id.norecordfound_query_reply);
        norecordfound_query_reply.setVisibility(View.GONE);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.notification_List);
        dataS = new ArrayList<>();

        return rootView;
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Unread Notifications");
        //Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show();
        dataS.clear();

       getUnreadMessagesFromServer();

       markMessagesReadOnServer();

    }



    private void getUnreadMessagesFromServer()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/health_tips/get_health_tips_quires_list/apitoken/1edc0ae98198866510bce219d5115b72/patient_id/22

        String user_id="0";
        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            patient_id=patient.getString("id");
            user_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url="";

        url = NetworkIOConstant.CS_APIUrls.BASE_URL + "health_tips/get_health_tips_quires_list/apitoken/"+ token+ "/patient_id/"+ user_id ;

        Log.d("@ server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@  server response ","getUnreadMessagesFromServer "+result.toString());

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

                    String id,patient_id,health_query,query_reply,submitted_datetime,is_viewed,formated_datetime;
                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_query_reply.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }


                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            patient_id = data_arr.getJSONObject(i).getString("patient_id");
                            health_query = data_arr.getJSONObject(i).getString("health_query");
                            query_reply = data_arr.getJSONObject(i).getString("query_reply");
                            submitted_datetime = data_arr.getJSONObject(i).getString("submitted_datetime");
                            is_viewed = data_arr.getJSONObject(i).getString("is_viewed");
                            formated_datetime = data_arr.getJSONObject(i).getString("formated_datetime");

                            MessageItem navItem = new MessageItem();
                            navItem.setId(id);
                            navItem.setQuestion("Query. "+health_query);
                            navItem.setAnsweer("Reply: "+query_reply);
                            navItem.setDtime(formated_datetime);

                            Log.d("@ q  ",health_query);
                            dataS.add(navItem);
                        }

                        //-----------------fill adapter only after network call finished------
                        adapter = new MessageAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        final LinearLayoutManager mLayoutManager=new  LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);




                        ((MessageAdapter) adapter).setOnItemClickListener(new
                          MessageAdapter.MyClickListener()
                           {
                               @Override
                               public void onItemClick(int position, View v)
                               {
                                   MessageItem s=dataS.get(position);
                                   Log.i("", " Clicked on Item " + position +"Choosed id "+s.getId()+"::" +s.getAnsweer());
                                   Toast.makeText(getContext(),"Ans "+s.getAnsweer(),Toast.LENGTH_SHORT).show();
                               }
                           }
                        );


                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);


                                int visibleItemCount = recyclerView.getChildCount();
                                int totalItemCount = mLayoutManager.getItemCount();
                                int firstVisibleItemIndex = mLayoutManager.findFirstVisibleItemPosition();
                                Log.d("@ scroll", "T "+totalItemCount+" V- "+visibleItemCount+" <= "+firstVisibleItemIndex);

                                //synchronizew loading state when item count changes
                                if (loading) {
                                    if (totalItemCount > previousTotal) {
                                        loading = false;
                                        previousTotal = totalItemCount;
                                    }
                                }
                                if (!loading) {
                                    if ((totalItemCount - visibleItemCount) <= firstVisibleItemIndex) {
                                        // Loading NOT in progress and end of list has been reached
                                        // also triggered if not enough items to fill the screen
                                        // if you start loading
                                        //Toast.makeText(context, "End Query", Toast.LENGTH_SHORT).show();
                                        Log.d("@ recycleview ", "End Of List Reached");
                                        loading = true;
                                        // ****here hit api andd after getting data , make loading =false

                                    } else if (firstVisibleItemIndex == 0) {
                                        // top of list reached
                                        // if you start loading
                                        //Toast.makeText(context, "Top of list", Toast.LENGTH_SHORT).show();
                                        //loading = true;
                                    }
                                }
                            }

                        });
                        //------------------------
                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Query fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Queryfetching failed"+err);
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


    private void markMessagesReadOnServer()
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
        String url="";

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device , 2 is IOS ---
        postDataParams.put("patient_id", user_id);

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params", " markMessagesReadOnServer "+ postJson.toString());

        //http://brandhypedigital.in/demo/medixfora/restapi/health_tips/mark_all_notifications_as_read/apitoken/1edc0ae98198866510bce219d5115b72/patient_id/22
        //url = NetworkIOConstant.CS_APIUrls.BASE_URL + "health_tips/mark_all_notifications_as_read/apitoken/"+ token+ "/patient_id/"+ user_id ;
        url = NetworkIOConstant.CS_APIUrls.BASE_URL + "health_tips/mark_all_notifications_as_read" ;

        Log.d("@ server url : ",url);

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(context,postDataParams, 0) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@ server response ", "markMessagesReadOnServer "+result.toString());

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

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        Log.e("@ Success "," markMessagesReadOnServer "+err);
                    }
                    else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, ":"+err, Toast.LENGTH_SHORT).show();
                                Log.e("@ Error","markMessagesReadOnServer "+err);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (noProgressDialog == 1) {
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

}
