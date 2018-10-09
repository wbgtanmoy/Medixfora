package com.brandhype.medixfora.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.CustomToastAlertDialog;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.adpaters.CartAdapter;
import com.brandhype.medixfora.asynctasks.GeneralAsynctask;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.CartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    Context context;
    View rootView;
    private RecyclerView recyclerView;
    CartAdapter adapter;

    List<CartItem> dataS = new ArrayList<>();
    ImageView norecordfound_cart;

    AppPreferences pref;
    JSONObject patient;

    Button cart_checkout_button;
    TextView attach_prescription_tv;

    String expired_doct_appt;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_cart, container, false);
        context=getActivity();
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        expired_doct_appt="";

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cart_List);
        norecordfound_cart = (ImageView) rootView.findViewById(R.id.norecordfound_cart);
        norecordfound_cart.setVisibility(View.GONE);

        cart_checkout_button = (Button) rootView.findViewById(R.id.cart_checkout_button);
        cart_checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "To Checkout", Toast.LENGTH_SHORT).show();
                //checkOutCart();
                askForCheckout();
            }
        });

        /*attach_prescription_tv = (TextView) rootView.findViewById(R.id.attach_prescription_tv);
        attach_prescription_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "To Attach Prescrition", Toast.LENGTH_SHORT).show();

                //------------------ fragment--------------------
                Fragment fragment = new PrescriptionFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    ((MainActivity)context).getSupportActionBar().setTitle(R.string.prescription);
                }
            }
        });*/

        return rootView;
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of cartfragment");
        super.onResume();
        ((MainActivity)context).getSupportActionBar().setTitle(R.string.title_cart);

        dataS.clear();
        //----gets cart list from server-------
        getCartFromServer();
        //----gets cartcount , method of activity
        ((MainActivity)context).getCartCountFromServer();

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("myjson.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getCartFromServer()
    {
        //http://brandhypedigital.in/demo/medixfora/restapi/my_cart/get_cart_items/apitoken/1edc0ae98198866510bce219d5115b72
        //String token= NetworkIOConstant.CS_Token.TOKEN;
        //String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "my_cart/get_cart_items/apitoken/"+token;
        expired_doct_appt="";

        String user_id="0";
        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            patient_id=patient.getString("id");
            user_id=patient.getString("id");
         }catch (Exception e){e.printStackTrace();}

        String token= NetworkIOConstant.CS_Token.TOKEN;
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/get_cart_items/apitoken/"+token+"/uid/"+user_id;
        Log.d("@ cart server url : ",url);

        GeneralAsynctask submitAsync = new GeneralAsynctask(getActivity(), 1) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);

                Log.d("@ CART LIST response ",result.toString());

                ///result=loadJSONFromAsset();//---test purpose, loading from asset*********
                // Log.d("@ asset json ",result.toString());

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

                    String rowid="0",id="0",itemtype="",item_name="",price="",qty="";
                    String category_names="",company_name="",item_image_file="",item_image_file_path="";
                    String speciality_names="",hospital_name="",doctor_profile_image="",doctor_profile_image_path="",doctor_id="";
                    String week_day="",checkup_start_time="",checkup_end_time="";
                    String appoint_number_of_patient="",subtotal="",patient_name="",patient_email="",patient_contact="",appt_date="";
                    String attachment_link="";
                    String appoint_number_of_patient_updated="0",no_of_patient_appointed="0";

                    JSONArray data_arr = resultJson.getJSONArray("cart_content");

                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        if(data_arr.length()<= 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(context, "No Record Found.", Toast.LENGTH_SHORT).show();
                                    norecordfound_cart.setVisibility(View.VISIBLE);
                                    return;
                                }
                            });
                        }

                        for (int i = 0; i < data_arr.length(); i++) {

                            JSONObject doc=data_arr.getJSONObject(i);
                            itemtype = doc.getString("itemtype");

                            if(itemtype.equals("itm")) {

                                    id = doc.getString("id");
                                    //rowid = doc.getString("rowid");
                                    item_name = doc.getString("item_name");
                                    price = doc.getString("price");
                                    qty = doc.getString("qty");

                                    category_names = doc.getString("category_names");
                                    company_name = doc.getString("company_name");
                                    item_image_file = doc.getString("item_image_file");
                                    item_image_file_path = replaceString(doc.getString("item_image_file_path"));
                                    subtotal = doc.getString("subtotal");

                                Log.d("@item name",item_name+ "image "+item_image_file_path);

                            }
                            else  if(itemtype.equals("doct"))
                            {
                                    id = doc.getString("id");
                                    //rowid = doc.getString("rowid");
                                    item_name = doc.getString("item_name");
                                    price = doc.getString("price");
                                    qty = doc.getString("qty");

                                    if( !doc.isNull("speciality_names") )
                                        speciality_names = doc.getString("speciality_names");

                                    hospital_name = doc.getString("hospital_name");
                                    doctor_profile_image = replaceString(doc.getString("doctor_profile_image"));
                                    doctor_profile_image_path = replaceString(doc.getString("doctor_profile_image_path"));
                                    doctor_id = doc.getString("doctor_id");
                                    week_day = doc.getString("week_day");
                                    checkup_start_time = doc.getString("checkup_start_time");
                                    checkup_end_time = doc.getString("checkup_end_time");
                                    appoint_number_of_patient = doc.getString("appoint_number_of_patient");
                                    subtotal = doc.getString("subtotal");
                                    patient_name = doc.getString("patient_name");
                                    patient_email = doc.getString("patient_email");
                                    patient_contact = doc.getString("patient_contact");
                                    appt_date = doc.getString("appt_date");
                                    attachment_link = replaceString(doc.getString("attachment_link"));
                                    appoint_number_of_patient_updated = doc.getString("appoint_number_of_patient_updated");
                                    no_of_patient_appointed = doc.getString("no_of_patient_appointed");

                                    isExpired(doc);

                                Log.d("@ doctor name",item_name+ "attachment "+attachment_link);

                            }else if(itemtype.equals("MP")) {

                                id = doc.getString("id");
                                item_name = doc.getString("item_name");
                                price = doc.getString("price");
                                qty = doc.getString("qty");
                                item_image_file = doc.getString("item_image_file");
                                item_image_file_path = replaceString(doc.getString("item_image_file_path"));
                                attachment_link = replaceString(doc.getString("item_image_file_path"));

                                Log.d("@ prescription name",item_name+ "image "+item_image_file_path);

                            }

                                CartItem navItem = new CartItem();

                                navItem.setId(id);
                                navItem.setRowid(rowid);
                                navItem.setItemtype(itemtype);
                                navItem.setItem_name(item_name);
                                navItem.setPrice(price);
                                navItem.setQty(qty);


                                navItem.setCategory_names(category_names);
                                navItem.setCompany_name(company_name);
                                navItem.setItem_image_file(item_image_file);
                                navItem.setItem_image_file_path(item_image_file_path);

                                navItem.setSpeciality_names(speciality_names);
                                navItem.setHospital_name(hospital_name);
                                navItem.setDoctor_profile_image(doctor_profile_image);
                                navItem.setDoctor_profile_image_path(doctor_profile_image_path);
                                navItem.setDoctor_id(doctor_id);
                                navItem.setWeek_day(week_day);
                                navItem.setCheckup_start_time(checkup_start_time);
                                navItem.setCheckup_end_time(checkup_end_time);
                                navItem.setAppoint_number_of_patient(appoint_number_of_patient);
                                navItem.setSubtotal(subtotal);
                                navItem.setPatient_name(patient_name);
                                navItem.setPatient_email(patient_email);
                                navItem.setPatient_contact(patient_contact);
                                navItem.setAppt_date(appt_date);
                                navItem.setAttachment_link(attachment_link);
                                navItem.setAppoint_number_of_patient_updated(appoint_number_of_patient_updated);
                                navItem.setNo_of_patient_appointed(no_of_patient_appointed);

                               dataS.add(navItem);

                            //Log.d("@ item type ",navItem.getItemtype());
                        }



                        //-----------------fill adapter only after network call finished------

                        adapter = new CartAdapter(getActivity(), dataS);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        ((CartAdapter) adapter).setOnItemClickListener(new
                                    CartAdapter.MyClickListener()
                                   {
                                       @Override
                                       public void onItemClick(int position, View v)
                                       {
                                           CartItem s=dataS.get(position);
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
                                Toast.makeText(context, "Cart  list fetching failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Cart list fetching failed"+err);
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



    private void askForCheckout()
    {
        //------------------------Dialog---------------------------------------------
        final Dialog alertDialog = new Dialog(getActivity());
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog);

        TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);

        txt_main.setText("Sure to Checkout ?");

        Button _cancel = (Button) alertDialog.findViewById(R.id.btn_no);
        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);

        _yes.setEnabled(true);
        _cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //--Do nothing
            }
        });
        _yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //**************final checkout ************
                checkOutCart();
                //****************************************************
            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
        //----------------------------------------------------------------------
    }


    private void checkOutCart()
    {
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/checkout";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        Log.d("@ hitting at ", ">"+ url);

        AppPreferences pref;
        JSONObject patient;
        String user_id="0";
        String item_id="0";
        String item_type="";
        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id=patient.getString("id");
            user_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();}

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device , 2 is IOS ---
        postDataParams.put("user_id", user_id); //
        postDataParams.put("expired_doct_appt_id", expired_doct_appt); // comma separated item id
        postDataParams.put("item_type", "doct");

        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ checkout params", " checkout "+ postJson.toString());

        //if(true){return; }

        GeneralAsynctaskPost submitAsync = new GeneralAsynctaskPost(context, postDataParams, 1) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {

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

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));

                    if (api_action_success.equals("1") && api_syntax_success.equals("1")) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Checkout Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Checkout","Checkout Success");
                            }
                        });

                        //-------------clears fragments in stack--------------
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        new commonutils().showMessageDialog((MainActivity)context,"Checkout Success . Thank You for Your Purchase.");
                        commonutils.setFragmentPatient((MainActivity)context,new DashboardFragment(),null,R.string.dashboard,true);
                    }
                    else
                    {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getBaseContext(), "Login Failed:"+err, Toast.LENGTH_SHORT).show();
                                new CustomToastAlertDialog((MainActivity)context,"Medixfora: Checkout Failed",err).show();
                                Log.e("Error","Login Failed:"+err);
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


    private void isExpired(JSONObject doc)
    {
        try {
            String item_id = doc.getString("id");
            String appoint_number_of_patient_updated = doc.getString("appoint_number_of_patient_updated");
            String no_of_patient_appointed = doc.getString("no_of_patient_appointed");

            SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            Date strDateN = sdf2.parse(doc.getString("appt_date")+ " "+ doc.getString("checkup_start_time"));


            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //Date strDate = sdf.parse(doc.getString("appt_date"));

            SimpleDateFormat dformat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat iPformat = new SimpleDateFormat("yyyy-MM-dd");
            Date ipDate = iPformat.parse(doc.getString("appt_date"));
            String display_date = dformat.format(ipDate);

            //String time = doc.getString("checkup_start_time");//current.getCheckup_start_time();
            //SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a");
            //SimpleDateFormat date24Format = new SimpleDateFormat("HH");
            //Log.d("@@ checkup start", " in 24 hrs format >> "+ date24Format.format(date12Format.parse(time)));
            //Integer st_hrs=Integer.parseInt(date24Format.format(date12Format.parse(time)));

            Date today=new Date();
            //int hours= st_hrs*3600*1000; // after 'hours' of day appointment not confirmed will be treated as expired
            //Date apptDateTime= new Date(strDate.getTime() + hours);

            if( today.after(strDateN)) {
                //---------booking expired without confirmation----
                Log.d("@ isExpired ?? "," Doctor Appointment Validity Expired");
                expired_doct_appt=item_id +","+ expired_doct_appt;
            }
            else
            {
                // -------check for unexpired overbooking -----------------
                Integer no_of_patient_tosee=Integer.parseInt(appoint_number_of_patient_updated);
                Integer noofpatient_appointed=Integer.parseInt(no_of_patient_appointed);

                if( noofpatient_appointed >= no_of_patient_tosee){
                    //--no more booking available-------- overbooked
                    Log.d("@ Cart ","Overbooked : Doctor Appointment Cannot be confirmed");
                    expired_doct_appt=item_id +","+ expired_doct_appt;
                }

            }



        }catch(Exception e){e.printStackTrace();}
    }
}
