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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.adpaters.OrderDetailsAdapter;
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

public class OrderDetailsFragment extends Fragment {


    OrderItem orderItem;
    String item_order_id="0";

    private RecyclerView recyclerView;
    OrderDetailsAdapter adapter;
    private static String[] titles = null;
    TextView orderItemDetails_heading;

    List<OrderDetails> dataS = new ArrayList<>();

    Context context;
    String category_id="0",company_id="0";
    String name="",stype="category";

    ImageView norecordfound_patientOrderDetails;


    public OrderDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_order_details, container, false);
        View rootView=inflater.inflate(R.layout.fragment_order_details, container, false);
        context=getActivity();

        try {

            orderItem= (OrderItem) getArguments().getSerializable("orderItem");
            item_order_id = orderItem.getId();

            AppPreferences pref;
            JSONObject patient;
            String user_id="0";
            try {
                pref = new AppPreferences(getActivity());
                patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
                user_id=patient.getString("id");
            }catch (Exception e){e.printStackTrace();}

            Log.d("@ appt item ",orderItem.toString());
            Log.d("@ appt USER ID ",user_id);

            Toast.makeText(context,"ORDER: "+ orderItem.getOrder_number() , Toast.LENGTH_SHORT).show();

        }catch (Exception e){e.printStackTrace();}

        norecordfound_patientOrderDetails = (ImageView) rootView.findViewById(R.id.norecordfound_patientOrderDetails);
        norecordfound_patientOrderDetails.setVisibility(View.GONE);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.patientOrderDetails_List);
        orderItemDetails_heading = (TextView) rootView.findViewById(R.id.orderItemDetails_heading);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        dataS.clear();
        orderItemDetails_heading.setText("Order No: "+orderItem.getOrder_number());
        getpatientOrderDetailsDetailsFromServer();
    }

    private void getpatientOrderDetailsDetailsFromServer()
    {


        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "patient/get_patient_order_items";

        Log.d("@ med cat server url : ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("item_order_id",  orderItem.getId());

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ order details params", "  "+ postJson.toString());

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

                    //String id,order_number,order_date,total_amount,transaction_id,is_payment_completed,order_status;
                    String item_qty,item_price,line_total,item_name,item_image_file,item_type;
                    List<OrderDetails> order_details = new ArrayList<>();

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_patientOrderDetails.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);

                            item_qty = doc.getString("item_qty");
                            item_price = doc.getString("item_price");
                            line_total = doc.getString("line_total");
                            item_name = doc.getString("item_name");
                            item_image_file = replaceString(doc.getString("item_image_file"));
                            item_type = doc.getString("item_type");

                            Log.d("@  Order Details ",item_name );

                            OrderDetails navItem = new OrderDetails();

                            navItem.setItem_qty(item_qty);
                            navItem.setItem_price(item_price);
                            navItem.setLine_total(line_total);
                            navItem.setItem_name(item_name);
                            navItem.setItem_image_file(item_image_file);
                            navItem.setItem_type(item_type);


                            dataS.add(navItem);

                        }

                        //-----------------fill adapter only after network call finished------

                        adapter = new OrderDetailsAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((OrderDetailsAdapter) adapter).setOnItemClickListener(new
                                    OrderDetailsAdapter.MyClickListener()
                                    {
                                        @Override
                                        public void onItemClick(int position, View v)
                                        {
                                            OrderDetails s=dataS.get(position);
                                            Log.d("@ item ", " Clicked " + s.getItem_name() );
                                            Toast.makeText(getContext(),""+s.getItem_name(),Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "Patient order details  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Patient Order Details list fetching failed"+err);
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
