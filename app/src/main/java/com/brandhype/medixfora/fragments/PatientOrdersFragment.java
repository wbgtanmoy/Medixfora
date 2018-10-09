package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.adpaters.OrderAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.OrderDetails;
import com.brandhype.medixfora.models.OrderItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    OrderAdapter adapter;
    private static String[] titles = null;
    TextView patientOrder_search_view;

    List<OrderItem> dataS = new ArrayList<>();

    Context context;
    String category_id="0",company_id="0";
    String name="",stype="category";

    ImageView norecordfound_patientOrder;

    public PatientOrdersFragment() {
        // Required empty public constructor
        context=getActivity();
        try {
           dataS = new ArrayList<>();
        }catch (Exception e){e.printStackTrace();}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_patient_orders, container, false);

        context=getActivity();

        norecordfound_patientOrder = (ImageView) rootView.findViewById(R.id.norecordfound_patientOrder);
        norecordfound_patientOrder.setVisibility(View.GONE);

        patientOrder_search_view = (EditText) rootView.findViewById(R.id.patientOrder_search_view);
        patientOrder_search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = patientOrder_search_view.getText().toString().toLowerCase();
                Log.i("@ filter", " filtering" );
                try {
                    adapter.filter(text);
                }catch(Exception e){e.printStackTrace();}
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.patientOrder_List);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        dataS.clear();
        getpatientOrderFromServer();
    }

    private void getpatientOrderFromServer()
    {

        AppPreferences pref;
        JSONObject patient;
        String patient_id="0";

        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");

        }catch (Exception e){e.printStackTrace();}

        //http://brandhypedigital.in/demo/medixfora/restapi/patient/get_patient_order_items

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_orders";

        Log.d("@ med cat server url : ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("patientID", patient_id);

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ orderlist params", "  "+ postJson.toString());

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

                    String id,order_number,order_date,total_amount,transaction_id,is_payment_completed,order_status;
                    List<OrderDetails> order_details = new ArrayList<>();

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_patientOrder.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);

                            id = doc.getString("id");
                            order_number = doc.getString("order_number");
                            order_date = doc.getString("order_date");
                            total_amount = doc.getString("total_amount");
                            transaction_id = doc.getString("transaction_id");
                            is_payment_completed = doc.getString("is_payment_completed");
                            order_status = doc.getString("order_status");

                            Log.d("@ patientOrder ",order_number);

                            OrderItem navItem = new OrderItem();

                            navItem.setId(id);
                            navItem.setOrder_number(order_number);
                            navItem.setOrder_date(order_date);
                            navItem.setTotal_amount(total_amount);
                            navItem.setTransaction_id(transaction_id);
                            navItem.setIs_payment_completed(is_payment_completed);
                            navItem.setOrder_status(order_status);

                            dataS.add(navItem);

                        }

                        //-----------------fill adapter only after network call finished------

                        adapter = new OrderAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((OrderAdapter) adapter).setOnItemClickListener(new
                               OrderAdapter.MyClickListener()
                               {
                                   @Override
                                   public void onItemClick(int position, View v)
                                   {
                                       OrderItem s=dataS.get(position);
                                       Log.i("", " Clicked on Item " + s.getOrder_number()+"::" +s.getTotal_amount() );
                                       //Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getItem_name(),Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "Patient order  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Patient Order list fetching failed"+err);
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
