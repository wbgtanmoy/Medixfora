package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.AppPreferences;
import com.brandhype.medixfora.fragments.AppointmentFragment;
import com.brandhype.medixfora.models.DoctorItem;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {
    List<DoctorItem> data = Collections.emptyList();
    private ArrayList<DoctorItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

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

    public DoctorAdapter(Context context, List<DoctorItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<DoctorItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doctor_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DoctorItem current = data.get(position);
        //holder.title.setText(current.getName());
        holder.name.setText(current.getName());

        if(!current.getAddress().equals(""))
            holder.address.setText(current.getAddress());
        else
            holder.address.setVisibility(View.GONE);

        if(!current.getCity_name().equals(""))
            holder.city_name.setText(current.getCity_name());
        else
            holder.city_name.setVisibility(View.GONE);

        if(!current.getDesignation().equals(""))
            holder.designation.setText(current.getDesignation());
        else
            holder.designation.setVisibility(View.GONE);

        if(current.getIs_verified().trim().equals("Y"))
        {
            holder.doctor_VerifyIcon.setVisibility(View.VISIBLE);
        }
        else
            holder.doctor_VerifyIcon.setVisibility(View.GONE);


        //holder.menuicon.setImageResource(myImageList[0]);
        if(!current.getImage().equals("")) {
            Picasso.with(context)
                    .load(current.getImage())
                    .placeholder(R.drawable.profilepic1)
                    .error(R.drawable.default_avatar)
                    .into(holder.menuicon);
        }


        holder.bookbtn.setTag(current);
        holder.doctor_info_button.setTag(current);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView title,name,city_name,address,designation;
        ImageView menuicon,doctor_VerifyIcon;
        Button bookbtn,doctor_info_button;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.doctor_name);
            city_name = (TextView) itemView.findViewById(R.id.doctor_city);
            address = (TextView) itemView.findViewById(R.id.doctor_address);
            //speciality_name = (TextView) itemView.findViewById(R.id.doctor_spname);
            //degree = (TextView) itemView.findViewById(R.id.doctor_degree);
            designation = (TextView) itemView.findViewById(R.id.doctor_designation);

            menuicon = (ImageView) itemView.findViewById(R.id.doctor_imageViewIcon);
            doctor_VerifyIcon = (ImageView) itemView.findViewById(R.id.doctor_VerifyIcon);

            bookbtn = (Button) itemView.findViewById(R.id.doctor_book_button);
            doctor_info_button = (Button) itemView.findViewById(R.id.doctor_info_button);

            itemView.setOnClickListener(this);

            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context,"Doctor ID :"+v.getTag().toString(),Toast.LENGTH_SHORT).show();
                    DoctorItem current=(DoctorItem)v.getTag();

                    //--storing the doc id in shared--
                    AppPreferences pref = new AppPreferences(context);
                    pref.SavePreferences(AppPreferences.Storage.DOCTOR_ID.name(),current.getId());
                    pref.SavePreferences(AppPreferences.Storage.DOCTOR_NAME.name(),current.getName());

                    Fragment fragment = new AppointmentFragment();
                    Bundle bundle=new Bundle();

                    Gson gson = new Gson();
                    String gsonobj=gson.toJson(current);
                    bundle.putString("doctor", gsonobj);
                    fragment.setArguments(bundle);

                     if (fragment != null) {
                         FragmentManager fragmentManager =((AppCompatActivity) context).getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         fragmentTransaction.replace(R.id.container_body, fragment);
                         fragmentTransaction.addToBackStack(null);
                         fragmentTransaction.commit();

                         // set the toolbar title
                         ((AppCompatActivity)context).getSupportActionBar().setTitle(R.string.appointment);
                         //((AppCompatActivity)context).getSupportActionBar().setTitle(Html.fromHtml("<small>Appointment</small>"));
                     }
                }
            });

            doctor_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorItem current=(DoctorItem)v.getTag();
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



    private void showInfoDialog(DoctorItem current)
    {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.setContentView(R.layout.custom_alertdialog_doctorinfo);

        TextView txt_doctorname = (TextView) alertDialog.findViewById(R.id.txt_doctorname);
        TextView txt_doctorinfo = (TextView) alertDialog.findViewById(R.id.txt_doctorinfo);
        ImageView doctor_image = (ImageView) alertDialog.findViewById(R.id.doctor_image);

        txt_doctorname.setText(current.getName());
        txt_doctorinfo.setText(current.getAbout_doctor());

        if(!current.getImage().equals("")) {
        Picasso.with(context)
                .load(current.getImage())
                .placeholder(R.drawable.profilepic1)
                .error(R.drawable.default_avatar)
                .into(doctor_image);
        }


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
                DoctorItem wp = arraylist.get(i);

                if (wp.getName().toLowerCase().contains(charText))
                {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
