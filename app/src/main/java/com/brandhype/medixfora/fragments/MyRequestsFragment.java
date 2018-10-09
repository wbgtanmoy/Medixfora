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

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.adpaters.RequestAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.database.DataBaseHandler;
import com.brandhype.medixfora.models.RequestItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyRequestsFragment extends Fragment {


    Context context;
    TextView query_reply_txt;

    List<RequestItem> dataS = new ArrayList<>();

    private RecyclerView recyclerView;
    AppPreferences pref;
    JSONObject patient;

    RequestAdapter adapter;

    boolean loading=false;
    int previousTotal=0;

    ImageView norecordfound_myrequest;
    DataBaseHandler databaseHandler;

    public MyRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_my_requests, container, false);
        context=getActivity();

        MainActivity activity = (MainActivity)getActivity();
        databaseHandler = activity.databaseHandler;


        norecordfound_myrequest = (ImageView) rootView.findViewById(R.id.norecordfound_myrequest);
        norecordfound_myrequest.setVisibility(View.GONE);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.myrequest_List);
        dataS = new ArrayList<>();

        return rootView;
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume ");
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_myrequest);
        //Toast.makeText(context, "Under development", Toast.LENGTH_SHORT).show();
        dataS.clear();

        getRequestsFromServer();

    }



    private void getRequestsFromServer()
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

        http://brandhypedigital.in/demo/medixfora/restapi/home_care/get_all_care_at_home_request/apitoken/1edc0ae98198866510bce219d5115b72/patient_id/22

        url = NetworkIOConstant.CS_APIUrls.BASE_URL + "home_care/get_all_care_at_home_request/apitoken/"+ token+ "/patient_id/"+ user_id ;

        Log.d("@ server url : ","getRequestsFromServer "+url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@  server response ","getRequestsFromServer "+result.toString());

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

                /*
                        id	"20"
                        patient_id	"22"
                        item_id	"20"
                        item_type	"CAH"
                        req_datetime	"2017-10-11"
                        req_address	"sdfdf"
                        req_msg	"dsfds"
                        submitted_datetime	"2017-10-11 06:20:13"
                        patient_name	"Mr. Priyam Ghosh"
                        item_name	"Ambulence Services"
                        short_desc	"Ambulence Services"
                        packaging	""
                        price	"300"
                        city	""
                        state	""
                        pincode	""
                        email	""
                        contact_number	""
                        item_image_file	""
                        status	"Y"
                        created_datetime	"2017-05-04 07:13:27"
                        is_deleted	"N"
                         */



                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    String id,patient_id,item_id,item_type,req_datetime,req_address,req_msg,submitted_datetime,patient_name,item_name,short_desc;
                    String price,item_image_file;
                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Request Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_myrequest.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            patient_id = data_arr.getJSONObject(i).getString("patient_id");
                            item_id = data_arr.getJSONObject(i).getString("item_id");
                            item_type = data_arr.getJSONObject(i).getString("item_type");
                            req_address = data_arr.getJSONObject(i).getString("req_address");
                            req_datetime = data_arr.getJSONObject(i).getString("req_datetime");
                            req_msg = data_arr.getJSONObject(i).getString("req_msg");
                            submitted_datetime = data_arr.getJSONObject(i).getString("submitted_datetime");
                            patient_name = data_arr.getJSONObject(i).getString("patient_name");
                            item_name = data_arr.getJSONObject(i).getString("item_name");
                            short_desc = data_arr.getJSONObject(i).getString("short_desc");
                            price = data_arr.getJSONObject(i).getString("price");
                            item_image_file = commonutils.replaceString(data_arr.getJSONObject(i).getString("item_image_file"));


                            RequestItem navItem = new RequestItem();
                            navItem.setId(id);
                            navItem.setPatient_id(patient_id);
                            navItem.setItem_id(item_id);
                            navItem.setItem_type(item_type);
                            navItem.setReq_address(req_address);
                            navItem.setReq_datetime(req_datetime);
                            navItem.setReq_msg(req_msg);
                            navItem.setSubmitted_datetime(submitted_datetime);
                            navItem.setPatient_name(patient_name);
                            navItem.setItem_name(item_name);
                            navItem.setShort_desc(short_desc);
                            navItem.setPrice(price);
                            navItem.setItem_image_file(item_image_file);

                            Log.d("@ request to medix   ",item_name + " image :"+ item_image_file);
                            dataS.add(navItem);
                        }

                        databaseHandler.deleteAllRequest();//deletes old data
                        databaseHandler.insertorupdateMyRequest(dataS);//inserts will new date
                        adapter = new RequestAdapter(context, databaseHandler.getRequestList()); // get data as list from DB & pass

                        //-----------------fill adapter only after network call finished------

                        //adapter = new RequestAdapter(context, dataS);
                        recyclerView.setAdapter(adapter);
                        final LinearLayoutManager mLayoutManager=new  LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(mLayoutManager);


                        ((RequestAdapter) adapter).setOnItemClickListener(new
                                  RequestAdapter.MyClickListener()
                                  {
                                      @Override
                                      public void onItemClick(int position, View v)
                                      {
                                          RequestItem s=dataS.get(position);
                                          Log.i("", " Clicked on Item " + position +"Choosed id "+s.getId()+"::" +s.getItem_name());
                                          Toast.makeText(getContext()," "+s.getItem_name(),Toast.LENGTH_SHORT).show();
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
                                //--uncomment for log
                                //Log.d("@ scroll", "T "+totalItemCount+" V- "+visibleItemCount+" <= "+firstVisibleItemIndex);

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
                                        // ****here hit api and after getting data , make loading =false

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
                                Toast.makeText(context, "getRequestsFromServer fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","getRequestsFromServer  failed"+err);
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
