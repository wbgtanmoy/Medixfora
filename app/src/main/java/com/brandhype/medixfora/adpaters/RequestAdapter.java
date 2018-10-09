package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.RequestItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    List<RequestItem> data = Collections.emptyList();
    private ArrayList<RequestItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;



    public RequestAdapter(Context context, List<RequestItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<RequestItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.request_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RequestItem current = data.get(position);

        holder.item_name.setText(current.getItem_name());
        holder.req_msg.setText(current.getReq_msg());
        holder.req_datetime.setText(current.getReq_datetime());
        holder.request_address.setText(current.getReq_address());

        try {
            Picasso.with(context)
                    .load(current.getItem_image_file())
                    .placeholder(R.drawable.careathom)
                    .error(R.drawable.careathom)
                    .into(holder.icon);
        }catch(Exception e){e.printStackTrace();}

        //holder.bookbtn.setTag(current);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView req_msg,req_datetime,item_name,short_desc,price,item_image_file,request_address;
        ImageView icon;
        Button bookbtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            item_name = (TextView) itemView.findViewById(R.id.request_itemname);
            req_msg = (TextView) itemView.findViewById(R.id.request_msg);
            req_datetime = (TextView) itemView.findViewById(R.id.request_datetime);
            request_address = (TextView) itemView.findViewById(R.id.request_address);

            icon = (ImageView) itemView.findViewById(R.id.request_imageViewIcon);

            itemView.setOnClickListener(this);

            /*bookbtn = (Button) itemView.findViewById(R.id.homecare_book_button);
            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestItem current=(RequestItem)v.getTag();
                }
            });*/


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
                RequestItem wp = arraylist.get(i);
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

}
