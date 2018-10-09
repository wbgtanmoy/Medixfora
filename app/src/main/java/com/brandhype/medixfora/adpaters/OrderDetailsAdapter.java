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
import com.brandhype.medixfora.models.OrderDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    List<OrderDetails> data = Collections.emptyList();
    private ArrayList<OrderDetails> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;


    public OrderDetailsAdapter(Context context, List<OrderDetails> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<OrderDetails>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_details_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderDetails current = data.get(position);

        if(current.getItem_name().toString().length()> 100)
            holder.orderItemDetails_itemname.setText(current.getItem_name().substring(0,80)+"...");
        else
            holder.orderItemDetails_itemname.setText(current.getItem_name());

        holder.orderItemDetails_price.setText("Rs. " + current.getItem_price() + "/-");
        holder.orderItemDetails_total.setText("Rs. " + current.getLine_total() + "/-");
        holder.orderItemDetails_qty.setText(current.getItem_qty());

        try {
            if (!current.getItem_image_file().equals("")) {
                Picasso.with(context)
                        .load(current.getItem_image_file())
                        .placeholder(R.drawable.no_image_cart)
                        .error(R.drawable.no_image_cart)
                        .into(holder.item_imageViewIcon);
            }
        }catch (Exception e){e.printStackTrace();}


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        ImageView item_imageViewIcon;
        TextView orderItemDetails_itemname,orderItemDetails_price,orderItemDetails_total,orderItemDetails_qty;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderItemDetails_itemname = (TextView) itemView.findViewById(R.id.orderItemDetails_itemname);
            orderItemDetails_price = (TextView) itemView.findViewById(R.id.orderItemDetails_price);
            orderItemDetails_total = (TextView) itemView.findViewById(R.id.orderItemDetails_total);
            orderItemDetails_qty = (TextView) itemView.findViewById(R.id.orderItemDetails_qty);

            item_imageViewIcon = (ImageView) itemView.findViewById(R.id.item_imageViewIcon);

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
                OrderDetails wp = arraylist.get(i);
                if (wp.getItem_name().toLowerCase().contains(charText))
                {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }



}
