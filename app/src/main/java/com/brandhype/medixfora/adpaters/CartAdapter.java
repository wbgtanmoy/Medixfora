package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.fragments.CartFragment;
import com.brandhype.medixfora.fragments.RescheduleDoctApptFragment;
import com.brandhype.medixfora.models.CartItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    List<CartItem> data = Collections.emptyList();
    private ArrayList<CartItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

    private int[] myImageList = new int[]{ R.drawable.home  };

    public CartAdapter(Context context, List<CartItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<CartItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cart_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CartItem current = data.get(position);

        String itemType=current.getItemtype();

        Log.d("@ item type ","onBindViewHolder"+ current.getItemtype());

        holder.cart_price.setVisibility(View.VISIBLE);
        holder.appt_date.setVisibility(View.VISIBLE);
        holder.cart_buttons_mid.setVisibility(View.VISIBLE);
        holder.speciality_names.setVisibility(View.VISIBLE);
        holder.hospital_name.setVisibility(View.VISIBLE);
        holder.cart_price.setVisibility(View.VISIBLE);
        holder.cart_buttons_left.setVisibility(View.VISIBLE);
        holder.reschedule.setVisibility(View.VISIBLE);//cart_reschedule_txt
        holder.cart_reschedule_txt.setVisibility(View.VISIBLE);

        if(itemType.equals("doct"))
        {
            //------------------Docotor Item------------------------
            holder.item_name.setText(current.getItem_name());
            holder.cart_price.setText("Rs "+current.getPrice()+"/-");


            if (!current.getSpeciality_names().equals(""))
                holder.speciality_names.setText("" + current.getSpeciality_names());
            else
                holder.speciality_names.setVisibility(View.GONE);

            if (!current.getHospital_name().equals(""))
                holder.hospital_name.setText("" + current.getHospital_name());
            else
                holder.hospital_name.setVisibility(View.GONE);

            //--attachment--
            if (current.getAttachment_link().equals(""))
                holder.cart_buttons_left.setVisibility(View.GONE);
            else
                holder.cart_buttons_left.setVisibility(View.VISIBLE);


            try
            {
                SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                Date strDateN = sdf2.parse(current.getAppt_date()+ " "+ current.getCheckup_start_time());

                SimpleDateFormat dformat = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat iPformat = new SimpleDateFormat("yyyy-MM-dd");
                Date ipDate = iPformat.parse(current.getAppt_date());
                String display_date = dformat.format(ipDate);

                Date today=new Date();

                Log.d("@ Cart "," Doctor Appointment Validity checking :" + today + "---" +strDateN+ "-");

                if( today.after(strDateN))
                {
                        //---------booking expired without confirmation----
                        holder.appt_date.setBackgroundColor(Color.CYAN);
                        Log.d("@ Cart "," Doctor Appointment Validity Expired :" + today + "---" +strDateN+ "-");
                        holder.appt_date.setText("EXPIRED: "+ display_date + " At " + current.getCheckup_start_time());
                }
                else
                {
                    Integer no_of_patient_tosee=Integer.parseInt(current.getAppoint_number_of_patient_updated());
                    Integer noofpatient_appointed=Integer.parseInt(current.getNo_of_patient_appointed());
                    if( noofpatient_appointed >= no_of_patient_tosee){
                        holder.appt_date.setBackgroundColor(Color.YELLOW);
                        holder.appt_date.setText("ALL BOOKED : "+ display_date);// + " At " + current.getCheckup_start_time());
                    }
                    else {
                        holder.appt_date.setText(display_date + " At " + current.getCheckup_start_time());
                    }
                }


            }catch (Exception e){e.printStackTrace();}


            //holder.menuicon.setImageResource(myImageList[0]);
            try {
                if (!current.getDoctor_profile_image().equals("")) {
                    Picasso.with(context)
                            .load(current.getDoctor_profile_image())
                            .placeholder(R.drawable.no_image_cart)
                            .error(R.drawable.no_image_cart)
                            .into(holder.menuicon);
                }
            }catch (Exception e){e.printStackTrace();}

            holder.attach.setTag(current);
            holder.remove.setTag(current);
            holder.reschedule.setTag(current);

            holder.cart_buttons_mid.setVisibility(View.GONE);
        }

        if(itemType.equals("itm"))
        {
            //------------------Other Item------------------------
            holder.item_name.setText(current.getItem_name());
            holder.qty.setText(current.getQty());
            holder.cart_price.setText("Rs "+ current.getSubtotal() +"/-");

            if (!current.getCategory_names().equals(""))
                holder.speciality_names.setText("" + current.getCategory_names());
            else
                holder.speciality_names.setVisibility(View.GONE);

            if (!current.getCompany_name().equals(""))
                holder.hospital_name.setText("" + current.getCompany_name());
            else
                holder.hospital_name.setVisibility(View.GONE);

            holder.appt_date.setVisibility(View.GONE);;
            holder.appt_date.setVisibility(View.GONE);
            holder.reschedule.setVisibility(View.GONE);
            holder.cart_reschedule_txt.setVisibility(View.GONE);

            holder.cart_buttons_mid.setVisibility(View.VISIBLE);
            holder.cart_buttons_left.setVisibility(View.GONE);

            //holder.menuicon.setImageResource(myImageList[0]);
            try{
            Picasso.with(context)
                    .load(current.getItem_image_file_path())
                    .placeholder(R.drawable.no_image_cart)
                    .error(R.drawable.no_image_cart)
                    .into(holder.menuicon);

            }catch (Exception e){e.printStackTrace();}

            holder.remove.setTag(current);
            holder.plus.setTag(current);
            holder.minus.setTag(current);
        }

        if(itemType.equals("MP"))
        {
            //------------prescription--------------------
            holder.item_name.setText(current.getItem_name());

            holder.appt_date.setVisibility(View.GONE);
            holder.cart_buttons_mid.setVisibility(View.GONE);
            holder.speciality_names.setVisibility(View.GONE);
            holder.hospital_name.setVisibility(View.GONE);
            holder.reschedule.setVisibility(View.GONE);

            holder.cart_buttons_mid.setVisibility(View.GONE);
            holder.cart_buttons_left.setVisibility(View.VISIBLE);

            holder.cart_price.setVisibility(View.GONE);

            if (current.getAttachment_link().equals(""))
                holder.cart_buttons_left.setVisibility(View.GONE);
            else
                holder.cart_buttons_left.setVisibility(View.VISIBLE);

            holder.menuicon.getLayoutParams().height = 80;
            holder.menuicon.getLayoutParams().width = 80;
            //holder.menuicon.setLayoutParams(new LinearLayout.LayoutParams(40,40));
            holder.menuicon.requestLayout();

            try{
            Picasso.with(context)
                    .load(R.drawable.prescription_def)
                    .placeholder(R.drawable.prescription_def)
                    .error(R.drawable.prescription_def)
                    .into(holder.menuicon);
            }catch (Exception e){e.printStackTrace();}


            holder.remove.setTag(current);
            holder.attach.setTag(current);

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        /*
        String rowid,id,itemtype,item_name,price,qty,category_names,company_name,item_image_file,item_image_file_path;
        String speciality_names,hospital_name,doctor_profile_image,doctor_profile_image_path,doctor_id,week_day,checkup_start_time,checkup_end_time;
        String appoint_number_of_patient,subtotal,patient_name,patient_email,patient_contact,appt_date;
        */

        TextView item_name,speciality_names,hospital_name,appt_date,cart_price,cart_attachment_txt,qty,cart_reschedule_txt;
        ImageView menuicon;
        ImageView attach,remove;
        ImageView plus,minus;
        LinearLayout cart_buttons_mid,cart_buttons_left;
        ImageView reschedule;

        public MyViewHolder(View itemView) {
            super(itemView);

            menuicon = (ImageView) itemView.findViewById(R.id.cart_imageViewIcon);

            item_name = (TextView) itemView.findViewById(R.id.cart_name);
            speciality_names = (TextView) itemView.findViewById(R.id.cart_desig);
            hospital_name = (TextView) itemView.findViewById(R.id.cart_address);

            appt_date = (TextView) itemView.findViewById(R.id.cart_appttime);
            cart_price = (TextView) itemView.findViewById(R.id.cart_price);
            cart_attachment_txt = (TextView) itemView.findViewById(R.id.cart_attachment_txt);


            attach = (ImageView) itemView.findViewById(R.id.cart_attachment);
            remove = (ImageView) itemView.findViewById(R.id.cart_remove);
            reschedule = (ImageView) itemView.findViewById(R.id.cart_reschedule);
            cart_reschedule_txt = (TextView) itemView.findViewById(R.id.cart_reschedule_txt);

            plus = (ImageView) itemView.findViewById(R.id.cart_plus);
            minus = (ImageView) itemView.findViewById(R.id.cart_minus);
            qty = (TextView) itemView.findViewById(R.id.cart_itemcount_txt);

            cart_buttons_mid = (LinearLayout) itemView.findViewById(R.id.cart_buttons_mid);
            cart_buttons_left = (LinearLayout) itemView.findViewById(R.id.cart_buttons_left);


            itemView.setOnClickListener(this);

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem current=(CartItem)v.getTag();
                    //Toast.makeText(context,"Remove :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,"Remove: Under Development",Toast.LENGTH_SHORT).show();
                    //removeCart(current);
                    removeCardConfirm(current);

                }
            });

            attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem current=(CartItem)v.getTag();
                    //Toast.makeText(context,"Attach :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,"Attach: Under Development",Toast.LENGTH_SHORT).show();
                    String attachemtnLink=current.getAttachment_link();
                    viewFile(attachemtnLink);
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem current=(CartItem)v.getTag();
                    //Toast.makeText(context,"Increase Qty :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,"Increase Qty: Under Development",Toast.LENGTH_SHORT).show();
                    String qty=""+(Integer.parseInt(current.getQty())+1);
                    updateCart(current,qty);

                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem current=(CartItem)v.getTag();
                    //Toast.makeText(context,"Decrease Qty :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,"Decrease Qty: Under Development",Toast.LENGTH_SHORT).show();
                    Integer qtyint=Integer.parseInt(current.getQty());
                    if(qtyint>1){
                        String qty=""+(qtyint-1);
                        updateCart(current,qty);
                    }
                    else{
                        Toast.makeText(context,"Qty cannot be Zero. Please click 'remove' to delete item.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            reschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem current=(CartItem)v.getTag();
                    //Toast.makeText(context,"Remove :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,"Reschedule: Under Development",Toast.LENGTH_SHORT).show();
                    Log.d("@ rescheduling :",current.toString());

                    Bundle bundle=new Bundle();
                    bundle.putSerializable("cartitem", current);
                    commonutils.setFragmentPatient((MainActivity)context,new RescheduleDoctApptFragment(),bundle,R.string.title_reschedule,true);

                }
            });
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();
        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);
            //Log.i("@ filter", "No text ");
        }
        else
        {
            //Log.i("@ filter", "arraylist size >> "+arraylist.size());
            for (int i=0;i<arraylist.size();i++)
            {
                CartItem wp = arraylist.get(i);
                //Log.i("@ filter", "searching "+ wp.getTitle());

                if (wp.getItem_name().toLowerCase().contains(charText))
                {
                    //Log.i("@ filter", " matched" + wp.getTitle());
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeCardConfirm(final CartItem cartItem)
    {
        //------------------------Dialog---------------------------------------------
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog);

        TextView txt_main = (TextView) alertDialog.findViewById(R.id.txt_main);
        String itemname="";
        if(cartItem.getItem_name().length()>20)
            itemname=cartItem.getItem_name().substring(0,20)+"..";
        else
            itemname=cartItem.getItem_name();
        txt_main.setText("Sure to delete Item "+itemname +" from cart ?.");
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
                //**************perform delete operation************
                removeCart(cartItem);
                //**************
            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
        //----------------------------------------------------------------------
    }

    public void removeCart(CartItem cartItem)
    {
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

        item_id=cartItem.getId();
        item_type=cartItem.getItemtype();

        //http://brandhypedigital.in/demo/medixfora/restapi/custom_cart/delete_cart_item
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/delete_cart_item";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("user_id", user_id);
        postDataParams.put("item_id", item_id);
        postDataParams.put("itemtype", item_type);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params","Delete Cart "+postJson.toString());

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
                                Toast.makeText(context, "Cart Deleted ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Cart Deleted");

                                //CartFragment instanceFragment =(CartFragment)getFragmentManager().findFragmentById(R.id.idFragment);
                                //------------------reloads fragment--------------------
                                Fragment fragment = new CartFragment();
                                if (fragment != null) {
                                    FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    //fragmentTransaction.addToBackStack(null); //**
                                    fragmentTransaction.commit();
                                    // set the toolbar title
                                    ((MainActivity)context).getSupportActionBar().setTitle(R.string.cart);
                                }

                            }
                        });

                    } else {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Cart Deletion failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Cart Deletion failed:"+err);
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

    public void updateCart(CartItem cartItem,String item_qty)
    {
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

        item_id=cartItem.getId();
        item_type=cartItem.getItemtype();

        //http://brandhypedigital.in/demo/medixfora/restapi/custom_cart/delete_cart_item
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "custom_cart/update_cart_item";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("user_id", user_id);
        postDataParams.put("item_id", item_id);
        postDataParams.put("itemtype", item_type);
        postDataParams.put("item_qty", item_qty);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@ Post params","Delete Cart "+postJson.toString());

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
                                Toast.makeText(context, "Cart Updated ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Cart Updated");

                                //CartFragment instanceFragment =(CartFragment)getFragmentManager().findFragmentById(R.id.idFragment);
                                //------------------reloads fragment--------------------
                                Fragment fragment = new CartFragment();
                                if (fragment != null) {
                                    FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    //fragmentTransaction.addToBackStack(null); //**
                                    fragmentTransaction.commit();
                                    // set the toolbar title
                                    ((MainActivity)context).getSupportActionBar().setTitle(R.string.cart);
                                }

                            }
                        });

                    } else {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Cart Updated failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Cart Updated failed:"+err);
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


    public void viewFile(String url)
    {
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(url));
        Log.d("@ FILE attachment","File extension: "+fileExt(url) + "MIMETYPE: "+ mimeType +"Url: "+url);
        newIntent.setDataAndType(Uri.parse(url),mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (Exception e) {
            Toast.makeText(context, "No Application installed for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }

            return ext.toLowerCase();

        }
    }
}
