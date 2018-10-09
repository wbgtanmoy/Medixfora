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
import com.brandhype.medixfora.models.GeneralItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HealthtipsAdapter extends RecyclerView.Adapter<HealthtipsAdapter.MyViewHolder> {

    List<GeneralItem> data = Collections.emptyList();
    private ArrayList<GeneralItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

    private int[] myImageList = new int[]{ R.drawable.home, R.drawable.about_us, R.drawable.my_profile };

    public HealthtipsAdapter(Context context, List<GeneralItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<GeneralItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.healthtips_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GeneralItem current = data.get(position);

        holder.healthtips_title.setText(current.getTitle());
        holder.healthtips_description.setText(current.getSubtitle());

        try {
            if (!current.getImage().equals("")) {
                Picasso.with(context)
                        .load(current.getImage())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.no_image_cart)
                        .into(holder.healthtips_imageViewIcon);
            }
        }catch (Exception e){e.printStackTrace();}

        //holder.bookbtn.setTag(current);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        TextView healthtips_title,healthtips_description;
        ImageView healthtips_imageViewIcon;


        public MyViewHolder(View itemView) {
            super(itemView);
            healthtips_title = (TextView) itemView.findViewById(R.id.healthtips_title);
            healthtips_description = (TextView) itemView.findViewById(R.id.healthtips_description);
            healthtips_imageViewIcon = (ImageView) itemView.findViewById(R.id.healthtips_imageViewIcon);

            itemView.setOnClickListener(this);

            /*bookbtn = (Button) itemView.findViewById(R.id.medicine_book_button);
            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GeneralItem current=(GeneralItem)v.getTag();
                    Toast.makeText(context,"Medicine :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    ((MainActivity)context).addItemToCartMain(current.getId(),"1","itm",current.getItem_name());
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
                GeneralItem wp = arraylist.get(i);
                //Log.i("@ filter", "searching "+ wp.getTitle());

                if (wp.getTitle().toLowerCase().contains(charText))
                {
                    //Log.i("@ filter", " matched" + wp.getTitle());
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
