package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.NetworkIOConstants.NetworkIOConstant;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.asynctasks.GeneralAsynctaskPost;
import com.brandhype.medixfora.connectionDetector.ConnectionDetector;
import com.brandhype.medixfora.models.HomecareItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.brandhype.medixfora.Utils.commonutils.replaceString;


public class HomecareAdapter extends RecyclerView.Adapter<HomecareAdapter.MyViewHolder> {

    List<HomecareItem> data = Collections.emptyList();
    private ArrayList<HomecareItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

    AlertDialog alertDialog ;

    AppPreferences pref;
    JSONObject patient;

    String patient_id;
    String strDate="";
    //EditText careathome_dtime_edt,careathome_address_edt,careathome_message_edt;

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

    public HomecareAdapter(Context context, List<HomecareItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<HomecareItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.homecare_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomecareItem current = data.get(position);

        holder.homecare_name.setText(current.getItem_name());

        if(!current.getPrice().equals(""))
            holder.homecare_price.setText("Rs "+ current.getPrice()+"/-");
        else
            holder.homecare_price.setVisibility(View.GONE);

        if(!current.getCompany_name().equals("") && !current.getCompany_name().equals("null"))
            holder.homecare_company_name.setText(current.getCompany_name());
        else
            holder.homecare_company_name.setVisibility(View.GONE);


        //holder.menuicon.setImageResource(myImageList[0]);
        try {
            Picasso.with(context)
                    .load(current.getItem_image_file_path())
                    .placeholder(R.drawable.careathom)
                    .error(R.drawable.careathom)
                    .into(holder.menuicon);
        }catch(Exception e){e.printStackTrace();}


        holder.bookbtn.setTag(current);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView homecare_name,homecare_price,homecare_company_name;
        ImageView menuicon;
        Button bookbtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            homecare_name = (TextView) itemView.findViewById(R.id.homecare_name);
            homecare_price = (TextView) itemView.findViewById(R.id.homecare_price);
            homecare_company_name = (TextView) itemView.findViewById(R.id.homecare_company_name);


            menuicon = (ImageView) itemView.findViewById(R.id.homecare_imageViewIcon);
            bookbtn = (Button) itemView.findViewById(R.id.homecare_book_button);

            itemView.setOnClickListener(this);

            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HomecareItem current=(HomecareItem)v.getTag();
                   // Toast.makeText(context,"homecare :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    //((MainActivity)context).addItemToCartMain(current.getId(),"1","itm",current.getItem_name());
                    //---requests callback from company representative----------
                    requestCallback(current);
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
                HomecareItem wp = arraylist.get(i);
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

    private void requestCallback(HomecareItem homecareItem)
    {
        final String item_id=homecareItem.getId();

        alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = ((MainActivity)context).getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.fragment_careathome_info, null);

        alertDialog.setView(convertView);
        alertDialog.setTitle("Enter your Information below");
        alertDialog.setCancelable(false);
        alertDialog.show();

        final EditText careathome_dtime_edt=(EditText) convertView.findViewById(R.id.careathome_dtime_edt);
        careathome_dtime_edt.setKeyListener(null);
        final EditText careathome_address_edt=(EditText) convertView.findViewById(R.id.careathome_address_edt);
        final EditText careathome_message_edt=(EditText) convertView.findViewById(R.id.careathome_message_edt);

        final ImageView careuphome_dt_image=(ImageView) convertView.findViewById(R.id.careuphome_dt_image);
        careuphome_dt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);

                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(year, monthOfYear, dayOfMonth, 0, 0);
                        strDate = format.format(c.getTime());
                        //String date = ""+String.valueOf(year) +"-"+String.valueOf(monthOfYear)+"-"+String.valueOf(dayOfMonth);
                        //careathome_dtime_edt.setText(strDate);

                        //-------------------
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY,selectedHour);
                                cal.set(Calendar.MINUTE,selectedMinute);
                                Format formatter;
                                formatter = new SimpleDateFormat("h:mm a");
                                String timetoshow= formatter.format(cal.getTime());

                                //strDate += " at " + selectedHour + ":" + selectedMinute +" hours";

                                strDate+= " at "+timetoshow;
                                careathome_dtime_edt.setText( strDate);
                            }
                        }, hour, minute, false);//true for  24 hour time, false for 12hr
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();

                        //-------------------

                    }
                }, yy, mm, dd);
                datePicker.show();

            }
        });


        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String todays_date = df.format(Calendar.getInstance().getTime());
        careathome_dtime_edt.setText(todays_date);

        Button submit = (Button) convertView.findViewById(R.id.careat_home_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,"Representative will call you back",Toast.LENGTH_SHORT).show();

                // to call api to send mail to the customer then show this dialog
                //showDialog();
                String dtime=""+careathome_dtime_edt.getText();
                String address=""+careathome_address_edt.getText();
                String message=""+careathome_message_edt.getText();

                String errormsg="";
                Integer errorcount=0;

                if(address.equals("")) {
                    errormsg+="Address Cannot be blank.\n";
                    errorcount++;
                }

                if(message.equals("")) {
                    errormsg+="Message Cannot be blank.\n";
                    errorcount++;
                }

                if(errorcount>0)
                {
                    Toast.makeText(context, ""+errormsg, Toast.LENGTH_LONG).show();
                    return;
                }
                alertDialog.dismiss();

                sendCallRequestToserver(dtime,address,message,item_id);
            }
        });

        Button cancel = (Button) convertView.findViewById(R.id.careat_home_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
              }
        });


    }


    private void sendCallRequestToserver(String dtime,String address,String message,String item_id)
    {



        try {
            pref = new AppPreferences(context);
            patient = new JSONObject(pref.LoadPreferences(AppPreferences.Storage.PATIENTDATA.name()));
            patient_id=patient.getString("id");
        }catch (Exception e){e.printStackTrace();patient_id="0";}

        //http://brandhypedigital.in/demo/medixfora/restapi/home_care/save_care_at_home_request
        String url = NetworkIOConstant.CS_APIUrls.BASE_URL + "home_care/save_care_at_home_request";
        String token= NetworkIOConstant.CS_Token.TOKEN;

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("apitoken", token);//--security feature---
        postDataParams.put("mode", NetworkIOConstant.CS_Type.device_type);//-- 1 is Android device ---


        postDataParams.put("user_id", patient_id);
        postDataParams.put("item_id", item_id);
        postDataParams.put("item_type", "CAH");
        postDataParams.put("req_datetime", dtime);
        postDataParams.put("req_address", address);
        postDataParams.put("req_msg", message);


        JSONObject postJson=new JSONObject(postDataParams);
        Log.d("@  Post params",postJson.toString());

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
                    //String is_validate = obj.getString("is_validate");
                    String api_syntax_success = obj.getString("api_syntax_success");

                    final String err = replaceString(obj.getString("api_action_message"));


                    if (api_action_success.equals("1")  && api_syntax_success.equals("1")) {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(context, "Query Success ", Toast.LENGTH_SHORT).show();
                                Log.d("Success","Query Success");
                            }
                        });


                        new commonutils().showOKDialog(context,"Our expert will revert you very soon.");

                    } else {

                        ((MainActivity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, " Query Failed:"+err, Toast.LENGTH_SHORT).show();
                                Log.e("Error"," Query Failed:"+err);
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


}
