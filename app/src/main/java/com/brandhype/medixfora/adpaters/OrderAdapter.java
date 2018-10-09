package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.fragments.OrderDetailsFragment;
import com.brandhype.medixfora.models.OrderItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    List<OrderItem> data = Collections.emptyList();
    private ArrayList<OrderItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;


    public OrderAdapter(Context context, List<OrderItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<OrderItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderItem current = data.get(position);

        holder.orderItem_number.setText(current.getOrder_number());

        try {
            SimpleDateFormat iformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date order_dt = iformat.parse(current.getOrder_date());
            SimpleDateFormat pformat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            holder.orderItem_date.setText(pformat.format(order_dt));
        }catch(Exception e){e.printStackTrace();}

        holder.orderItem_amount.setText("Rs. "+ current.getTotal_amount()+"/-");
        holder.orderItem_status.setText(current.getOrder_status());

        if(current.getOrder_status().toString().equals("PENDING")) {
            holder.orderItem_status.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.orderItem_status.setBackgroundResource(R.drawable.rounded_corner_red_button);
        }
        else {
            holder.orderItem_status.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.orderItem_status.setBackgroundResource(R.drawable.rounded_corner_green_button);
        }

        holder.orderItem_details_button.setTag(current);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView orderItem_number,orderItem_date,orderItem_amount,orderItem_status;
        Button orderItem_details_button;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderItem_number = (TextView) itemView.findViewById(R.id.orderItem_number);
            orderItem_date = (TextView) itemView.findViewById(R.id.orderItem_date);
            orderItem_amount = (TextView) itemView.findViewById(R.id.orderItem_amount);
            orderItem_status = (TextView) itemView.findViewById(R.id.orderItem_status);

            orderItem_details_button = (Button) itemView.findViewById(R.id.orderItem_details_button);

            itemView.setOnClickListener(this);

            orderItem_details_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderItem current=(OrderItem)v.getTag();
                    //Toast.makeText(context,"Order :"+ current.getOrder_number() + "at "+ current.getTotal_amount(),Toast.LENGTH_SHORT).show();

                    Bundle bundle=new Bundle();
                    bundle.putSerializable("orderItem", current);
                    commonutils.setFragmentPatient((MainActivity)context,new OrderDetailsFragment(),bundle,R.string.title_order_details,true);

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
                OrderItem wp = arraylist.get(i);


                if (wp.getOrder_number().toLowerCase().contains(charText))
                {

                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }



}
