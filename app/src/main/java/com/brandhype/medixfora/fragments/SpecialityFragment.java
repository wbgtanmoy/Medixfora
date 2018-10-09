package com.brandhype.medixfora.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.adpaters.SpecialityAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.models.SpecialityItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;
import static com.brandhype.medixfora.Utils.commonutils.toTitleCase;


public class SpecialityFragment extends Fragment {


    private RecyclerView recyclerView;
    SpecialityAdapter adapter;
    //HospitalAdapter adapter2;
    private static String[] titles = null;
    TextView search_view;

    List<SpecialityItem> dataS = new ArrayList<>();

    Context context;

    String listtype="speciality";

    public SpecialityFragment() {
        // Required empty public constructor
        //titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=getActivity();
        // drawer labels
        //titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        dataS= new ArrayList<>();
        //getSpecialityFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_speciality, container, false);


        search_view = (EditText) rootView.findViewById(R.id.search_view);
        search_view.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = search_view.getText().toString().toLowerCase();
                Log.d("@ filter", " filtering" );
                try {
                    if(adapter != null)
                        adapter.filter(text);
                }catch(Exception e){e.printStackTrace();}
                /*try {
                    if(adapter2 != null)
                        adapter2.filter(text);
                }catch(Exception e){e.printStackTrace();}*/
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


        try {

            listtype = getArguments().getString("listType");
            Log.d("@ listtype : ",listtype);

        }catch (Exception e){e.printStackTrace();}


        recyclerView = (RecyclerView) rootView.findViewById(R.id.speciality_List);

        //adapter = new SpecialityAdapter(getActivity(), getSpeciality());

        return rootView;
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of LoginFragment");
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(toTitleCase(listtype.toString()));
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Html.fromHtml("<small>Speciality</small>"));

        dataS.clear();

        if(listtype.equals("speciality"))
            getSpecialityFromServer();
        else if(listtype.equals("hospital"))
            getHospitalsFromServer();
    }


    public static List<SpecialityItem> getSpeciality() {
        List<SpecialityItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            SpecialityItem navItem = new SpecialityItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }


    //--------------get specialities from server----------

    private void getSpecialityFromServer()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/speciality/get_speciality_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "speciality/get_speciality_list/apitoken/"+ token;

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

                    String speciality_name,id,is_active,speciality_image,image_file_name;

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    Log.d("@ speciality  length ",""+data_arr.length());
                    if(data_arr.length()<= 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });
                    }

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            speciality_name = data_arr.getJSONObject(i).getString("speciality_name");
                            speciality_image = data_arr.getJSONObject(i).getString("image_path");
                            image_file_name = data_arr.getJSONObject(i).getString("image_file_name");
                            is_active = data_arr.getJSONObject(i).getString("is_active");

                            if (is_active.equals("Y") || is_active.equals("y")) {
                                SpecialityItem navItem = new SpecialityItem();
                                navItem.setTitle(speciality_name);
                                navItem.setId(id);
                                navItem.setSpeciality_image(speciality_image);
                                navItem.setImage_name(image_file_name);
                                Log.d("@ speciality name ",speciality_name);
                                dataS.add(navItem);
                            }

                        }

                        //adapter.notifyDataSetChanged();

                        //-----------------fill adapter only after network call finished------
                        adapter = new SpecialityAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        RecyclerView.LayoutManager lm = new GridLayoutManager(context,2);
                        recyclerView.setLayoutManager(lm);
                        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((SpecialityAdapter) adapter).setOnItemClickListener(new
                             SpecialityAdapter.MyClickListener()
                             {
                                 @Override
                                 public void onItemClick(int position, View v)
                                 {
                                     Log.d("@ speciality", " Clicked on Item " + position);
                                     SpecialityItem s=dataS.get(position);
                                     //Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getTitle(),Toast.LENGTH_SHORT).show();

                                     Fragment fragment = new DoctorListFragment();
                                     Bundle bundle=new Bundle();
                                     bundle.putString("speciality_id", s.getId());
                                     bundle.putString("speciality_name", s.getTitle());
                                     bundle.putString("stype", "speciality");

                                     fragment.setArguments(bundle);
                                     if (fragment != null) {
                                         FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                         fragmentTransaction.replace(R.id.container_body, fragment);
                                         //fragmentTransaction.add(R.id.container_body, fragment);
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
                                Toast.makeText(context, "Specility list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Specility list fetching failed"+err);
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


    //--------------get hospital from server----------

    private void getHospitalsFromServer()
    {

        //http://brandhypedigital.in/demo/medixfora/restapi/hospital/get_hospital_list/apitoken/1edc0ae98198866510bce219d5115b72
        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "hospital/get_hospital_list/apitoken/"+ token;

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

                    String hospital_name,id,is_active,logo_image_path,logo_image;

                    JSONArray data_arr = resultJson.getJSONArray("result");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {


                        Log.d("@ hospital  length ", ""+data_arr.length());
                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            id = data_arr.getJSONObject(i).getString("id");
                            hospital_name = data_arr.getJSONObject(i).getString("hospital_name");
                            logo_image = data_arr.getJSONObject(i).getString("logo_image");
                            logo_image_path = data_arr.getJSONObject(i).getString("logo_image_path");
                            is_active = data_arr.getJSONObject(i).getString("is_active");


                            // ------- reused specialityitem for hospital for siplicity -----
                            if (is_active.equals("Y") || is_active.equals("y")) {
                                SpecialityItem navItem = new SpecialityItem();
                                navItem.setTitle(hospital_name);
                                navItem.setId(id);
                                navItem.setSpeciality_image(logo_image_path);
                                navItem.setImage_name(logo_image);
                                Log.d("@ speciality name ",hospital_name);
                                dataS.add(navItem);
                            }

                        }

                        //adapter.notifyDataSetChanged();

                        //-----------------fill adapter only after network call finished------
                        adapter = new SpecialityAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        //adapter2 = new HospitalAdapter(getActivity(), dataS);//new
                        //recyclerView.setAdapter(adapter2);
                        RecyclerView.LayoutManager lm = new GridLayoutManager(context,2);//changed frm 2
                        recyclerView.setLayoutManager(lm);
                        recyclerView.setBackgroundColor(ContextCompat.getColor(context,R.color.grey));//new
                        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        ((SpecialityAdapter) adapter).setOnItemClickListener(new
                                SpecialityAdapter.MyClickListener()
                                {
                        /*((HospitalAdapter) adapter2).setOnItemClickListener(new
                                 HospitalAdapter.MyClickListener()
                                 {*/
                                     @Override
                                     public void onItemClick(int position, View v)
                                     {
                                         Log.d("@ hosp", " Clicked on Item " + position);
                                         SpecialityItem s=dataS.get(position);
                                        // Toast.makeText(getContext(),"Choosed id "+s.getId()+"::" +s.getTitle(),Toast.LENGTH_SHORT).show();

                                         Fragment fragment = new DoctorListFragment();
                                         Bundle bundle=new Bundle();
                                         bundle.putString("hospital_id", s.getId());
                                         bundle.putString("hospital_name", s.getTitle());
                                         bundle.putString("stype", "hospital");

                                         fragment.setArguments(bundle);
                                         if (fragment != null) {
                                             FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                             fragmentTransaction.replace(R.id.container_body, fragment);
                                             //fragmentTransaction.add(R.id.container_body, fragment);
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
                                Toast.makeText(context, "List fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","hosp list fetching failed"+err);
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
