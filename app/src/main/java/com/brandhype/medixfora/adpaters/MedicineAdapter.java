package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.UploadCartItemAsynctask;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.fragments.CartFragment;
import com.brandhype.medixfora.interfaces.AsyncResponse;
import com.brandhype.medixfora.models.AppointmentItem;
import com.brandhype.medixfora.models.MedicineItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    List<MedicineItem> data = Collections.emptyList();
    private ArrayList<MedicineItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

    String desc;

    private int[] myImageList = new int[]{
                                        R.drawable.home,
                                        R.drawable.about_us,
                                        R.drawable.my_profile,
                                        R.drawable.my_order,
                                        R.drawable.contact_us,
                                        R.drawable.pharmasy,
                                        R.drawable.seeksecondopinion,
                                        R.drawable.diagnostic_ervices,
                                        R.drawable.care_at_home,
                                        R.drawable.health_tips,
                                        R.drawable.medical_tourism,
                                        R.drawable.logout,
                                        R.drawable.settings,
                                        R.drawable.feedback,
                                        };

    public MedicineAdapter(Context context, List<MedicineItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<MedicineItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.medicine_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MedicineItem current = data.get(position);

        holder.medicine_name.setText(current.getItem_name());
        holder.medicine_price.setVisibility(View.VISIBLE);
        holder.offer_price.setVisibility(View.VISIBLE);

        if(!current.getPrice().equals("")) {
            holder.medicine_price.setText("Rs " + current.getPrice() + "/-");
            holder.medicine_price.setPaintFlags(holder.medicine_price.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
        else
            holder.medicine_price.setVisibility(View.GONE);


        if(!current.getOffer_price().equals("0")) {
            holder.offer_price.setText("Rs " + current.getOffer_price() + "/-");
            holder.medicine_price.setPaintFlags(holder.medicine_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
            holder.offer_price.setVisibility(View.GONE);

        if(!current.getCompany_name().equals("") && !current.getCompany_name().equals("null"))
            holder.medicine_company_name.setText(current.getCompany_name());
        else
            holder.medicine_company_name.setVisibility(View.GONE);



        /*if(!current.getShort_desc().equals("") && !current.getShort_desc().equals("null")) {
            desc = current.getShort_desc().toString().trim();
            if (desc.length() > 120)
                holder.medicine_desc.setText(desc.substring(0,120));
            else
                holder.medicine_desc.setText(desc);
        }
        else {
            holder.medicine_desc.setVisibility(View.GONE);
        }*/

        holder.medicine_desc.setVisibility(View.GONE);


        //holder.menuicon.setImageResource(myImageList[0]);
        try{
        Picasso.with(context)
                .load(current.getItem_image_file_path())
                .placeholder(R.drawable.no_image_cart)
                .error(R.drawable.no_image_cart)
                .into(holder.menuicon);
        }catch(Exception e){e.printStackTrace();}


        holder.bookbtn.setTag(current);
        holder.info.setTag(current);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView medicine_name,medicine_price,offer_price,medicine_company_name,medicine_desc;
        ImageView menuicon;
        Button bookbtn,info;

        public MyViewHolder(View itemView) {
            super(itemView);
            medicine_name = (TextView) itemView.findViewById(R.id.medicine_name);
            medicine_price = (TextView) itemView.findViewById(R.id.medicine_price);
            offer_price = (TextView) itemView.findViewById(R.id.medicine_offer_price);
            medicine_company_name = (TextView) itemView.findViewById(R.id.medicine_company_name);
            medicine_desc = (TextView) itemView.findViewById(R.id.medicine_desc);


            menuicon = (ImageView) itemView.findViewById(R.id.medicine_imageViewIcon);
            bookbtn = (Button) itemView.findViewById(R.id.medicine_book_button);
            info = (Button) itemView.findViewById(R.id.medicine_info_button);

            itemView.setOnClickListener(this);

            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicineItem current=(MedicineItem)v.getTag();
                    //Toast.makeText(context,"Medicine :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    ((MainActivity)context).addItemToCartMain(current.getId(),"1","itm",current.getItem_name());
                }
            });

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicineItem current=(MedicineItem)v.getTag();
                    Log.d("@ med ",current.toString());
                    showInfoDialog(current);
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
        }
        else
        {
            for (int i=0;i<arraylist.size();i++)
            {
                MedicineItem wp = arraylist.get(i);
                if (wp.getItem_name().toLowerCase().contains(charText))
                {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }


    private void showInfoDialog(MedicineItem current)
    {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog_medicineinfo);

        TextView txt_medicinename = (TextView) alertDialog.findViewById(R.id.txt_medicinename);
        TextView txt_medicineinfo = (TextView) alertDialog.findViewById(R.id.txt_medicineinfo);
        ImageView medicine_image = (ImageView) alertDialog.findViewById(R.id.medicine_image);
        TextView txt_medicineprice = (TextView) alertDialog.findViewById(R.id.txt_medicineprice);
        TextView txt_medicineofferprice = (TextView) alertDialog.findViewById(R.id.txt_medicineofferprice);

        txt_medicinename.setText(current.getItem_name());
        txt_medicineinfo.setText(current.getShort_desc());

        if(!current.getPrice().equals(""))
            txt_medicineprice.setText("Price: Rs. " + current.getPrice() + "/-");
        else
            txt_medicineprice.setVisibility(View.GONE);

        if(!current.getOffer_price().equals("0")) {
            txt_medicineprice.setText("Price: Rs. " + current.getPrice() + "/-");
            txt_medicineprice.setPaintFlags(txt_medicineofferprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            txt_medicineofferprice.setVisibility(View.GONE);
        }
        txt_medicineofferprice.setText("Offer: Rs. "+current.getOffer_price()+ "/-");


        try{
            Picasso.with(context)
                    .load(current.getItem_image_file_path())
                    .placeholder(R.drawable.no_image_cart)
                    .error(R.drawable.no_image_cart)
                    .into(medicine_image);
        }catch(Exception e){e.printStackTrace();}


        Button _yes = (Button) alertDialog.findViewById(R.id.btn_yes);

        _yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        if (alertDialog != null && !alertDialog.isShowing())
            alertDialog.show();
    }


    private void addItemToCart(String itemid,String qty,String types)
    {

        String user_id="0";
        AppPreferences pref3 = new AppPreferences(context);
        AppointmentItem appointmentItem3=new AppointmentItem();

        AppPreferences pref;
        JSONObject patient;

        try{

            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            String patient_id,patient_first_name,patient_last_name,patient_email,patient_contact,patient_gender;
            user_id=patient.getString("id");

        }catch(Exception e){e.printStackTrace();}


        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "my_cart/add_to_cart/";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---

        postDataParams.put("item_id", itemid);
        postDataParams.put("item_qty", qty);
        postDataParams.put("itemtype", types); // itm for others
        postDataParams.put("user_id", user_id);


        //UploadCartItemAsynctask(Context context, HashMap<String, String> map,String filename, String url, AsyncResponse delegate){

        UploadCartItemAsynctask uploadcartitemAsynctask=new UploadCartItemAsynctask(context,postDataParams,"",url,new AsyncResponse(){

            @Override
            public void processFinish(String result){
                //Toast.makeText(getActivity().getApplicationContext(),result ,Toast.LENGTH_LONG).show();
                Log.d("@  add to cart", result);
                //--------------------------------------------------------
                try {
                    Log.d("@ server response ",result.toString());

                    if (result.equals("")) {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( context, "Blank Response From server", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject obj = new JSONObject(result);

                    String api_action_success = obj.getString("api_action_success");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = commonutils.replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1")  && api_syntax_success.equals("1"))
                    {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Item Added to Cart");

                                Fragment fragment = new CartFragment();
                                if (fragment != null) {
                                    FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    //fragmentTransaction.addToBackStack(null); /*//**
                                    fragmentTransaction.commit();
                                    // set the toolbar title
                                    ((AppCompatActivity)context).getSupportActionBar().setTitle(R.string.cart);
                                }
                            }
                        });

                    }
                    else
                    {
                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Add To Cart Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error","Add To Cart Failed: "+err);
                            }
                        });

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }
                //-----------------------------------
            }
        });

        //----checks internet connection before post , & then check build version
        if (!ConnectionDetector.isConnectingToInternet(context))
        {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                uploadcartitemAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);//parallel
            } else {
                uploadcartitemAsynctask.execute();//serial
            }
        }
    }//addItemToCart


}
