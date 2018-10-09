package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.brandhype.medixfora.adpaters.PharmacyAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.PharmacyItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class PharmacyListFragment extends Fragment {


    //Context context;
    String listtype="None";

    private RecyclerView recyclerView;
    PharmacyAdapter adapter;
    private static String[] titles = null;
    TextView search_view;
    ImageView norecordfound_pharmacy;

    List<PharmacyItem> dataS = new ArrayList<>();

    Context context;

    public PharmacyListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pharmacy_list, container, false);

        context=getActivity();

        View rootView = inflater.inflate(R.layout.fragment_pharmacy_list, container, false);
        try {

            listtype = getArguments().getString("listType");
            Log.d("@ listtype : ",listtype);

        }catch (Exception e){e.printStackTrace();}

        dataS= new ArrayList<>();

        norecordfound_pharmacy = (ImageView) rootView.findViewById(R.id.norecordfound_pharmacy);
        norecordfound_pharmacy.setVisibility(View.GONE);

        search_view = (EditText) rootView.findViewById(R.id.pharmacy_search_view);
        search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = search_view.getText().toString().toLowerCase();
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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.pharmacy_List);

        return rootView;
    }


    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_pharmacy);

        dataS.clear();

       /* if(listtype.equals("category"))
            getPharmacyByCategoryFromServer();
        else
            getPharmacyByCompanyFromServer();*/

        getPharmacyByCategoryFromServer();
    }

    public static List<PharmacyItem> getSpeciality() {
        List<PharmacyItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            PharmacyItem navItem = new PharmacyItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    private void getPharmacyByCategoryFromServer()
    {
        //http://brandhypedigital.in/demo/medixfora/restapi/medicine/get_medicine_category_list/apitoken/1edc0ae98198866510bce219d5115b72

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url="";

        url = NetworkIOConstant.CS_APIUrls.BASE_URL + "medicine/get_medicine_category_list/apitoken/"+ token;

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


                /*
                {"id":"22","category_type":"M",
                "category_name":"Medical Equipments",
                "created_datetime":"2017-04-20 10:52:32",
                "status":"Y",
                "is_deleted":"N"}
                */

                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    String category_name,id,is_active,category_image,image_file_path;


                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_pharmacy.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        PharmacyItem navItem_head = new PharmacyItem();
                        navItem_head.setTitle("Prescription Medicine");
                        navItem_head.setId("100000");
                        navItem_head.set_Image("");
                        dataS.add(navItem_head);

                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            category_name = data_arr.getJSONObject(i).getString("category_name");
                            image_file_path = data_arr.getJSONObject(i).getString("image_file_path");
                            //image_file_name = data_arr.getJSONObject(i).getString("image_file_name");
                            is_active = data_arr.getJSONObject(i).getString("status");

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                PharmacyItem navItem = new PharmacyItem();
                                navItem.setTitle(category_name);
                                navItem.setId(id);
                                navItem.set_Image(image_file_path);
                                //navItem.setImage_name(image_file_name);
                                Log.d("@ category name ",category_name);
                                dataS.add(navItem);
                            }

                        }

                        //adapter.notifyDataSetChanged();

                        //-----------------fill adapter only after network call finished------
                        adapter = new PharmacyAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        //RecyclerView.LayoutManager lm = new GridLayoutManager(context,2);
                        //recyclerView.setLayoutManager(lm);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                        ((PharmacyAdapter) adapter).setOnItemClickListener(new
                               PharmacyAdapter.MyClickListener()
                                 {
                                     @Override
                                     public void onItemClick(int position, View v)
                                     {

                                         PharmacyItem s=dataS.get(position);
                                         Log.i("", " Clicked on Item " + position +"Choosed id "+s.getId()+"::" +s.getTitle());
                                         //Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getTitle(),Toast.LENGTH_SHORT).show();

                                         String selected_id=s.getId();
                                         if(selected_id.equals("100000"))
                                         {
                                             //------------------ Prescription--------------------
                                             Fragment fragment = new PrescriptionFragment();
                                             if (fragment != null) {
                                                 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                 fragmentTransaction.replace(R.id.container_body, fragment);
                                                 fragmentTransaction.addToBackStack(null);
                                                 fragmentTransaction.commit();
                                                 // set the toolbar title
                                                 ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.prescription);
                                             }
                                         }
                                         else
                                         {
                                             //--------------MedicineList----------------------
                                             Fragment fragment = new MedicineListFragment();
                                             Bundle bundle = new Bundle();
                                             bundle.putString("category_id", s.getId());
                                             bundle.putString("name", s.getTitle());
                                             bundle.putString("stype", "category");

                                             fragment.setArguments(bundle);

                                             if (fragment != null) {
                                                 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                 fragmentTransaction.replace(R.id.container_body, fragment);
                                                 fragmentTransaction.addToBackStack(null);
                                                 fragmentTransaction.commit();
                                                 // set the toolbar title
                                                 ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(s.getTitle());
                                                 //((AppCompatActivity)context).getSupportActionBar().setTitle(Html.fromHtml("<small>"+s.getTitle()+"</small>"));
                                             }

                                         }
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
                                Toast.makeText(context, "Pharmacy list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Pharmacy list fetching failed"+err);
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



    private void getPharmacyByCompanyFromServer()
    {
        //http://brandhypedigital.in/demo/medixfora/restapi/medicine/get_medicine_category_list/apitoken/1edc0ae98198866510bce219d5115b72
        //http://brandhypedigital.in/demo/medixfora/restapi/medicine/get_medicine_directory_list/apitoken/1edc0ae98198866510bce219d5115b72

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url="";

        url= NetworkIOConstant.CS_APIUrls.BASE_URL + "medicine/get_medicine_directory_list/apitoken/"+ token;

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


                /*
                {"id":"14","item_name":"Ranbaxy","short_desc":"Ranbaxy",
                "item_type":"MD","city":"Kolkata","state":"WB",
                "pincode":"700078","email":"ran45@gmail.com",
                "contact_number":"784555555","status":"Y","is_deleted":"N",
                "category_name":" Cat1,Anesthetics"}
                */

                try {

                    JSONObject resultJson = new JSONObject(result);

                    String api_action_success = resultJson.getString("api_action_success");
                    String api_syntax_success = resultJson.getString("api_syntax_success");
                    final String err = replaceString(resultJson.getString("api_action_message"));

                    String item_name,id,is_active,category_image,image_file_name;


                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_pharmacy.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            item_name = data_arr.getJSONObject(i).getString("item_name");
                            //category_name = data_arr.getJSONObject(i).getString("category_name");
                            //category_image = data_arr.getJSONObject(i).getString("image_path");
                            //image_file_name = data_arr.getJSONObject(i).getString("image_file_name");
                            is_active = data_arr.getJSONObject(i).getString("status");

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                PharmacyItem navItem = new PharmacyItem();
                                navItem.setTitle(item_name);
                                navItem.setId(id);
                                //navItem.setSpeciality_image(speciality_image);
                                //navItem.setImage_name(image_file_name);
                                Log.d("@ company name ",item_name);
                                dataS.add(navItem);
                            }

                        }

                        //adapter.notifyDataSetChanged();

                        //-----------------fill adapter only after network call finished------
                        adapter = new PharmacyAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        //RecyclerView.LayoutManager lm = new GridLayoutManager(context,2);
                        //recyclerView.setLayoutManager(lm);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


                        ((PharmacyAdapter) adapter).setOnItemClickListener(new
                               PharmacyAdapter.MyClickListener()
                               {
                                   @Override
                                   public void onItemClick(int position, View v)
                                   {
                                       Log.i("", " Clicked on Item " + position);
                                       PharmacyItem s=dataS.get(position);
                                       //Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getTitle(),Toast.LENGTH_SHORT).show();

                                       Fragment fragment = new MedicineListFragment();
                                       Bundle bundle=new Bundle();
                                       bundle.putString("company_id", s.getId());
                                       bundle.putString("name", s.getTitle());
                                       bundle.putString("stype", "company");
                                       fragment.setArguments(bundle);

                                       if (fragment != null) {
                                           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                           fragmentTransaction.replace(R.id.container_body, fragment);

                                           fragmentTransaction.addToBackStack(null);

                                           fragmentTransaction.commit();

                                           // set the toolbar title
                                           ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(s.getTitle());
                                           //((AppCompatActivity)context).getSupportActionBar().setTitle(Html.fromHtml("<small>"+s.getTitle()+"</small>"));
                                       }
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
                                Toast.makeText(context, "Pharmacy list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Pharmacy list fetching failed"+err);
                            }
                        });
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

                pdspinnerGeneral.dismiss();


            }

        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            submitAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } else {
            submitAsync.execute(url);
        }

    }


}
