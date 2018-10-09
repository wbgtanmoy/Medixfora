package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.DoctorAppointmentItem;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.MyViewHolder> {

    List<DoctorAppointmentItem> data = Collections.emptyList();
    private ArrayList<DoctorAppointmentItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

    private int[] myImageList = new int[]{ R.drawable.home, R.drawable.about_us, R.drawable.my_profile };

    public DoctorAppointmentAdapter(Context context, List<DoctorAppointmentItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<DoctorAppointmentItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.doct_appt_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DoctorAppointmentItem current = data.get(position);

        String dt1[]=current.getDoctor_appointment_date().toString().split(",");
        String dtOrg=current.getDoctor_appointment_format_date();

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date appt_date = format.parse(dtOrg);
            Calendar myCal = new GregorianCalendar();
            myCal.setTime(appt_date);

            String day,month,monthTxt,year;

            day=""+myCal.get(Calendar.DAY_OF_MONTH);
            String dayName=myCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());//Locale.US);
            month=""+(myCal.get(Calendar.MONTH) + 1);
            year=""+ myCal.get(Calendar.YEAR);
            monthTxt=""+new DateFormatSymbols().getMonths()[myCal.get(Calendar.MONTH)];

            holder.docappt_cal_current_date.setText(day);
            holder.docappt_cal_current_day.setText(dayName+ "\n"+ monthTxt);

            Log.d("# Day: " ,day+"-"+ dayName +" Month: " + month +"-"+monthTxt+" Year: " + year );
            //holder.docappt_cal_current_date.setText(dt1[0].substring(0,8));
            //holder.docappt_cal_current_day.setText(dt1[1]);

        }catch (Exception e){e.printStackTrace();}

        holder.docappt_patient_name.setText(current.getFirst_name()+ " "+current.getLast_name());
        holder.docappt_patient_address.setText(current.getAddress());
        holder.docappt_patient_timings.setText(current.getCheckup_start_time()+ " - "+current.getCheckup_end_time());
        holder.docappt_patient_date.setText(current.getDoctor_appointment_date());
        holder.docappt_patient_fees.setText("Rs."+ current.getItem_price()+"/-");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView docappt_cal_current_date,docappt_cal_current_day,docappt_patient_name,docappt_patient_address,docappt_patient_timings;
        TextView docappt_patient_date,docappt_patient_fees;

        public MyViewHolder(View itemView) {
            super(itemView);

            docappt_cal_current_date = (TextView) itemView.findViewById(R.id.docappt_cal_current_date);
            docappt_cal_current_day = (TextView) itemView.findViewById(R.id.docappt_cal_current_day);

            docappt_patient_name = (TextView) itemView.findViewById(R.id.docappt_patient_name);
            docappt_patient_address = (TextView) itemView.findViewById(R.id.docappt_patient_address);
            docappt_patient_timings = (TextView) itemView.findViewById(R.id.docappt_patient_timings);
            docappt_patient_fees = (TextView) itemView.findViewById(R.id.docappt_patient_fees);
            docappt_patient_date = (TextView) itemView.findViewById(R.id.docappt_patient_date);

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
        }
        else
        {
            for (int i=0;i<arraylist.size();i++)
            {
                DoctorAppointmentItem wp = arraylist.get(i);
                if (wp.getFirst_name().toLowerCase().contains(charText))
                {

                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
