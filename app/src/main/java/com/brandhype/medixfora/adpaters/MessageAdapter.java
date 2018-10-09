package com.brandhype.medixfora.adpaters;

/**
 * Created by tanmoy on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.MessageItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    List<MessageItem> data = Collections.emptyList();
    private ArrayList<MessageItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;



    public MessageAdapter(Context context, List<MessageItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<MessageItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageItem current = data.get(position);

        holder.message_qs.setText(current.getQuestion());
        holder.message_ans.setText(current.getAnsweer());
        holder.message_time.setText(current.getDtime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView message_qs,message_ans,message_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            message_qs = (TextView) itemView.findViewById(R.id.message_qs);
            message_ans = (TextView) itemView.findViewById(R.id.message_ans);
            message_time = (TextView) itemView.findViewById(R.id.message_time);

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
                MessageItem wp = arraylist.get(i);
                //Log.i("@ filter", "searching "+ wp.getTitle());

                if (wp.getQuestion().toLowerCase().contains(charText))
                {
                    //Log.i("@ filter", " matched" + wp.getTitle());
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
