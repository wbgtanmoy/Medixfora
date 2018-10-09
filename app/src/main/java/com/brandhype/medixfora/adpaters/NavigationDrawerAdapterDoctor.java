package com.brandhype.medixfora.adpaters;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.NavDrawerItem;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapterDoctor extends RecyclerView.Adapter<NavigationDrawerAdapterDoctor.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private int[] myImageList = new int[]{
                                        R.drawable.home,
                                        R.drawable.my_profile,
                                        R.drawable.about_us,
                                        R.drawable.my_order,
                                        R.drawable.settings,
                                        R.drawable.logout,
                                        R.drawable.contact_us,
                                        R.drawable.pharmasy,
                                        R.drawable.seeksecondopinion,
                                        R.drawable.diagnostic_ervices,
                                        R.drawable.care_at_home,
                                        R.drawable.health_tips,
                                        R.drawable.medical_tourism,
                                        R.drawable.feedback,
                                        };

    public NavigationDrawerAdapterDoctor(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.menuicon.setImageResource(myImageList[position]);
        /*if(position==4 || position==10)
        {
            holder.separator.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        View separator;
        ImageView menuicon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            separator = (View) itemView.findViewById(R.id.separator);
            menuicon = (ImageView) itemView.findViewById(R.id.imageViewIcon);
        }
    }
}
