package com.brandhype.medixfora.adpaters;

/**
 * Created by Tanmoy Banerjee on 08-08-2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.DiagonisticItem;
//import com.codopoliz.medixfora.models.SpecialityItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiagonisticAdapter extends RecyclerView.Adapter<DiagonisticAdapter.MyViewHolder> {
    List<DiagonisticItem> data = Collections.emptyList();
    private ArrayList<DiagonisticItem> arraylist;

    private LayoutInflater inflater;
    private Context context;
    private static DiagonisticAdapter.MyClickListener myClickListener;

    private int[] myImageList = new int[]{

            R.drawable.diagnostic_ervices,
            //R.drawable.list_arrow,
            R.drawable.health_tips,
            R.drawable.home,
            R.drawable.about_us,
            R.drawable.my_profile,
            R.drawable.my_order,
            R.drawable.contact_us,
            R.drawable.pharmasy,
            R.drawable.seeksecondopinion,

            R.drawable.care_at_home,

            R.drawable.medical_tourism,
            R.drawable.logout,
            R.drawable.settings,
            R.drawable.feedback,
    };

    public DiagonisticAdapter(Context context, List<DiagonisticItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<DiagonisticItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public DiagonisticAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.diagonistic_row, parent, false);
        DiagonisticAdapter.MyViewHolder holder = new DiagonisticAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DiagonisticAdapter.MyViewHolder holder, int position) {
        DiagonisticItem current = data.get(position);
        holder.title.setText(current.getTitle());

        //holder.menuicon.setImageResource(R.drawable.pschyback);

        holder.menuicon.setImageResource(myImageList[0]);
        //holder.menuicon.setVisibility(View.GONE);

        /*if(!current.getImage_name().equals(""))
        {
            Picasso.with(context)
                .load(current.getSpeciality_image())
                .placeholder(R.drawable.pschyback)
                .error(R.drawable.pschyback)
                .into(holder.menuicon);
        }
        else
        {
            holder.menuicon.setImageResource(R.drawable.pschyback);
        }*/

        //holder.speciality_inner_menu.setBackground();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView menuicon;
        //RelativeLayout speciality_inner_menu;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.diagonistic_title);
            menuicon = (ImageView) itemView.findViewById(R.id.diagonistic_imageViewIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(DiagonisticAdapter.MyClickListener myClickListener) {
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
        } else {
            //Log.i("@ filter", "arraylist size >> "+arraylist.size());
            for (int i = 0; i < arraylist.size(); i++) {
                DiagonisticItem wp = arraylist.get(i);
                //Log.i("@ filter", "searching "+ wp.getTitle());

                if (wp.getTitle().toLowerCase().contains(charText)) {
                    //Log.i("@ filter", " matched" + wp.getTitle());
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}