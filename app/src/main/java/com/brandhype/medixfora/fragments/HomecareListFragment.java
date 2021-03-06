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
import com.brandhype.medixfora.adpaters.HomecareAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.HomecareItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomecareListFragment extends Fragment {


    private RecyclerView recyclerView;
    HomecareAdapter adapter;
    private static String[] titles = null;
    TextView homecare_search_view;

    List<HomecareItem> dataS = new ArrayList<>();

    Context context;
    String category_id="0",company_id="0";
    String name="",stype="category";


    ImageView norecordfound_homecare;

    public HomecareListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getActivity();

        try {

            stype = getArguments().getString("stype");
            if(stype.equals("category"))
                category_id = getArguments().getString("category_id");
            else if(stype.equals("company"))
                company_id = getArguments().getString("company_id");

            name = getArguments().getString("name");
            Log.d("@ category_id  : ",category_id);

            dataS = new ArrayList<>();


        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_homecare_list, container, false);
        View rootView = inflater.inflate(R.layout.fragment_homecare_list, container, false);
        context=getActivity();


        norecordfound_homecare = (ImageView) rootView.findViewById(R.id.norecordfound_homecare);
        norecordfound_homecare.setVisibility(View.GONE);

        homecare_search_view = (EditText) rootView.findViewById(R.id.homecare_search_view);
        homecare_search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = homecare_search_view.getText().toString().toLowerCase();
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


        recyclerView = (RecyclerView) rootView.findViewById(R.id.homecare_List);
        return rootView;
    }


    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();

        dataS.clear();
        if(stype.equals("category"))
            getHomecareFromServerByCategory();
        else if(stype.equals("company"))
            getHomecareFromServerByCompany();
    }

    public static List<HomecareItem> getSpeciality() {
        List<HomecareItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            HomecareItem navItem = new HomecareItem();
            navItem.setItem_name(titles[i]);
            data.add(navItem);
        }
        return data;
    }


    private void getHomecareFromServerByCategory()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/home_care/get_home_care_list/

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "home_care/get_home_care_list/";

        Log.d("@ homecare ser url : ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("category_id", category_id);


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
                    String id;
                    String item_type;
                    String item_name;
                    String short_desc;
                    String price;
                    String status;
                    String category_id;
                    String category_name;
                    String image_name;
                    String item_image_file_path;
                    String company_name;

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_homecare.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }


                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);

                            id = doc.getString("id");

                            item_type = doc.getString("item_type");
                            item_name = doc.getString("item_name");
                            short_desc = doc.getString("short_desc");
                            price = doc.getString("price");
                            category_id = doc.getString("cat_id");
                            category_name = doc.getString("cat_name");
                            image_name = doc.getString("item_image_file");
                            //item_image_file_path = replaceString(doc.getString("item_image_file_path"));
                            is_active = doc.getString("status");
                            company_name = doc.getString("dir_name");


                            Log.d("@ homecare name",item_name);

                            if (is_active.equals("Y") || is_active.equals("y")) {

                                HomecareItem navItem = new HomecareItem();

                                navItem.setId(id);
                                navItem.setItem_name(item_name);
                                navItem.setShort_desc(short_desc);
                                navItem.setPrice(price);
                                navItem.setCategory_id(category_id);
                                navItem.setCategory_name(category_name);
                                navItem.setCompany_name(company_name);
                                navItem.setImage_name(image_name);
                                //navItem.setItem_image_file_path(item_image_file_path);

                                dataS.add(navItem);
                            }

                        }



                        //-----------------fill adapter only after network call finished------

                        adapter = new HomecareAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((HomecareAdapter) adapter).setOnItemClickListener(new
                              HomecareAdapter.MyClickListener()
                              {
                                  @Override
                                  public void onItemClick(int position, View v)
                                  {
                                      HomecareItem s=dataS.get(position);
                                      Log.i("", " Clicked on Item " + s.getId()+"::" +s.getItem_name() );
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


    private void getHomecareFromServerByCompany()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/home_care/get_home_care_list/

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "home_care/get_home_care_list/";

        Log.d("@ homecare  ser url: ",url);

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---
        postDataParams.put("company_id", company_id);


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
                    String id;
                    String item_type;
                    String item_name;
                    String short_desc;
                    String price;
                    String status;
                    String category_id;
                    String category_name;
                    String image_name;
                    String item_image_file_path;
                    String company_name;

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_homecare.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }


                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);

                            id = doc.getString("id");

                            item_type = doc.getString("item_type");
                            item_name = doc.getString("item_name");
                            short_desc = doc.getString("short_desc");
                            price = doc.getString("price");
                            category_id = doc.getString("cat_id");
                            category_name = doc.getString("cat_name");
                            image_name = doc.getString("item_image_file");
                            //item_image_file_path = replaceString(doc.getString("item_image_file_path"));
                            is_active = doc.getString("status");
                            company_name = doc.getString("dir_name");


                            Log.d("@ homecare name",item_name);

                            if (is_active.equals("Y") || is_active.equals("y")) {

                                HomecareItem navItem = new HomecareItem();

                                navItem.setId(id);
                                navItem.setItem_name(item_name);
                                navItem.setShort_desc(short_desc);
                                navItem.setPrice(price);
                                navItem.setCategory_id(category_id);
                                navItem.setCategory_name(category_name);
                                navItem.setCompany_name(company_name);
                                navItem.setImage_name(image_name);
                                //navItem.setItem_image_file_path(item_image_file_path);

                                dataS.add(navItem);
                            }

                        }



                        //-----------------fill adapter only after network call finished------

                        adapter = new HomecareAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((HomecareAdapter) adapter).setOnItemClickListener(new
                              HomecareAdapter.MyClickListener()
                              {
                                  @Override
                                  public void onItemClick(int position, View v)
                                  {
                                      HomecareItem s=dataS.get(position);
                                      Log.i("", " Clicked on Item " + s.getId()+"::" +s.getItem_name() );
                                      Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getItem_name(),Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "homecare list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","homecare list fetching failed"+err);
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
