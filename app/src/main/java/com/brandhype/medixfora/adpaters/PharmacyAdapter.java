package com.brandhype.medixfora.adpaters;

/**
 * Created by Tanmoy on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.PharmacyItem;
import com.squareup.picasso.Picasso;
//import com.codopoliz.medixfora.models.SpecialityItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.MyViewHolder> {
    List<PharmacyItem> data = Collections.emptyList();
    private ArrayList<PharmacyItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static MyClickListener myClickListener;

    private int[] myImageList = new int[]{
                                        R.drawable.health_tips,
                                        //R.drawable.list_arrow,
                                        R.drawable.home,
                                        R.drawable.about_us,
                                        R.drawable.my_profile,
                                        R.drawable.my_order,
                                        R.drawable.contact_us,
                                        R.drawable.pharmasy,
                                        R.drawable.seeksecondopinion,
                                        R.drawable.diagnostic_ervices,
                                        R.drawable.care_at_home,

                                        R.drawable.medical_tourism,
                                        R.drawable.logout,
                                        R.drawable.settings,
                                        R.drawable.feedback,
                                        };

    public PharmacyAdapter(Context context, List<PharmacyItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<PharmacyItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pharmacy_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PharmacyItem current = data.get(position);
        holder.title.setText(current.getTitle());

        //holder.menuicon.setImageResource(R.drawable.pschyback);

        //holder.menuicon.setImageResource(myImageList[0]);
        //holder.menuicon.setVisibility(View.GONE); //to comment



        try {
            if (!current.get_Image().equals("")) {
                Picasso.with(context)
                        .load(current.get_Image())
                        .placeholder(R.drawable.loading60)
                        .error(R.drawable.health_tips)
                        .into(holder.menuicon);
            } else {
                holder.menuicon.setImageResource(R.drawable.health_tips);
            }
        }catch(Exception e){holder.menuicon.setImageResource(R.drawable.health_tips);}

        try {
            //--- for first static list item: prescription medicine --
            if (current.getId().equals("100000")) {
                Picasso.with(context)
                        .load(R.drawable.pres)
                        .placeholder(R.drawable.loading60)
                        .error(R.drawable.health_tips)
                        .into(holder.menuicon);
            }
        }catch(Exception e){}

        //holder.speciality_inner_menu.setBackground();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView title;
        ImageView menuicon;
        //RelativeLayout speciality_inner_menu;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.pharmacy_title);
            menuicon = (ImageView) itemView.findViewById(R.id.pharmacy_imageViewIcon);
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
                PharmacyItem wp = arraylist.get(i);
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
