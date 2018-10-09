package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.PatientAppointmentItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.MyViewHolder> {

    List<PatientAppointmentItem> data = Collections.emptyList();
    private ArrayList<PatientAppointmentItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

    private int[] myImageList = new int[]{ R.drawable.home, R.drawable.about_us, R.drawable.my_profile };

    public PatientAppointmentAdapter(Context context, List<PatientAppointmentItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<PatientAppointmentItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.patient_appt_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        PatientAppointmentItem current = data.get(position);
        holder.patientappt_docname.setText(current.getDoctor_name());

        if(!current.getDesignation().toString().equals(""))
        {holder.patientappt_docdesig.setVisibility(View.VISIBLE);
            holder.patientappt_docdesig.setText(current.getDesignation());}
        else
            holder.patientappt_docdesig.setVisibility(View.GONE);

        holder.patientappt_docfees.setText("Rs. "+ current.getItem_price()+ "/-");
        holder.patientappt_date.setText(current.getDoctor_appointment_date() + " at "+current.getCheckup_start_time() );

        try {
            if (!current.getDoctor_profile_image().equals("")) {
                Picasso.with(context)
                        .load(current.getDoctor_profile_image())
                        .placeholder(R.drawable.no_image_cart)
                        .error(R.drawable.no_image_cart)
                        .into(holder.docimage);
            }
        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView patientappt_docname,patientappt_docdesig,patientappt_date,patientappt_docfees;
        ImageView docimage;

        public MyViewHolder(View itemView) {
            super(itemView);
            patientappt_docname = (TextView) itemView.findViewById(R.id.patientappt_docname);
            patientappt_docdesig = (TextView) itemView.findViewById(R.id.patientappt_docdesig);
            patientappt_date = (TextView) itemView.findViewById(R.id.patientappt_date);
            patientappt_docfees = (TextView) itemView.findViewById(R.id.patientappt_docfees);
            docimage = (ImageView) itemView.findViewById(R.id.patientappt_docimage);
            itemView.setOnClickListener(this);

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
                PatientAppointmentItem wp = arraylist.get(i);
                //Log.i("@ filter", "searching "+ wp.getTitle());

                if (wp.getDoctor_name().toLowerCase().contains(charText))
                {
                    //Log.i("@ filter", " matched" + wp.getTitle());
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
