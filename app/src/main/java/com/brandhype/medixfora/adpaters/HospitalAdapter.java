package com.brandhype.medixfora.adpaters;

/**
 * Created by Tanmoy on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.Utils.commonutils;
import com.brandhype.medixfora.models.SpecialityItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {
    List<SpecialityItem> data = Collections.emptyList();
    private ArrayList<SpecialityItem> arraylist;

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

    public HospitalAdapter(Context context, List<SpecialityItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<SpecialityItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hospitals_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SpecialityItem current = data.get(position);
        String title;
        if(current.getTitle().length()>60)
            title=current.getTitle().substring(0,60)+"...";
        else
            title=current.getTitle();

        holder.title.setText(commonutils.toTitleCase(title));

        //holder.menuicon.setImageResource(R.drawable.pschyback);
        //holder.menuicon.setImageResource(myImageList[0]);

        holder.pb.setVisibility(View.VISIBLE);

        if(!current.getImage_name().equals(""))
        {
            try{
            Picasso.with(context)
                .load(current.getSpeciality_image())
                //.placeholder(R.drawable.progress_animation)
                .placeholder(R.drawable.loading_blank)
                .error(R.drawable.no_image)
                .into(holder.menuicon, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.pb.setVisibility(View.GONE);
                    }
                });
            }catch(Exception e){e.printStackTrace();}
        }
        else
        {
            holder.menuicon.setImageResource(R.drawable.pschyback);
        }

        //holder.speciality_inner_menu.setBackground();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView title;
        ImageView menuicon;
        ProgressBar pb;
        //RelativeLayout speciality_inner_menu;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.speciality_title);
            //speciality_inner_menu = (RelativeLayout) itemView.findViewById(R.id.speciality_inner_menu);
            menuicon = (ImageView) itemView.findViewById(R.id.speciality_imageViewIcon);
            pb = (ProgressBar) itemView.findViewById(R.id.speciality_progress);
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
            Log.d("@ filter", "No text ");
        }
        else
        {
            //Log.d("@ filter", "arraylist size >> "+arraylist.size());
            for (int i=0;i<arraylist.size();i++)
            {
                SpecialityItem wp = arraylist.get(i);
                Log.d("@ filter", "searching "+ wp.getTitle());

                if (wp.getTitle().toLowerCase().contains(charText))
                {
                    Log.d("@ filter", " matched" + wp.getTitle());
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
