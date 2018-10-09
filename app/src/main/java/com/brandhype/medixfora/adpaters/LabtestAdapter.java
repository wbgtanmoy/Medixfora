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

import com.brandhype.medixfora.MainActivity;
import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.LabtestItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LabtestAdapter extends RecyclerView.Adapter<LabtestAdapter.MyViewHolder> {

    List<LabtestItem> data = Collections.emptyList();
    private ArrayList<LabtestItem> arraylist;

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

    public LabtestAdapter(Context context, List<LabtestItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.arraylist = new ArrayList<LabtestItem>();
        this.arraylist.addAll(data);
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.labtest_row, parent, false);
         MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LabtestItem current = data.get(position);

        holder.labtest_name.setText(current.getItem_name());

        if(!current.getPrice().equals(""))
            holder.labtest_price.setText("Rs "+ current.getPrice()+"/-");
        else
            holder.labtest_price.setVisibility(View.GONE);

        if(!current.getCompany_name().equals("") && !current.getCompany_name().equals("null"))
            holder.labtest_company_name.setText(current.getCompany_name());
        else
            holder.labtest_company_name.setVisibility(View.GONE);


        //holder.menuicon.setImageResource(myImageList[0]);
        try {
        Picasso.with(context)
                .load(current.getItem_image_file_path())
                .placeholder(R.drawable.labtesting)
                .error(R.drawable.labtesting)
                .into(holder.menuicon);
        }catch(Exception e){e.printStackTrace();}


        holder.bookbtn.setTag(current);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView labtest_name,labtest_price,labtest_company_name;
        ImageView menuicon;
        Button bookbtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            labtest_name = (TextView) itemView.findViewById(R.id.labtest_name);
            labtest_price = (TextView) itemView.findViewById(R.id.labtest_price);
            labtest_company_name = (TextView) itemView.findViewById(R.id.labtest_company_name);


            menuicon = (ImageView) itemView.findViewById(R.id.labtest_imageViewIcon);
            bookbtn = (Button) itemView.findViewById(R.id.labtest_book_button);

            itemView.setOnClickListener(this);

            bookbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    LabtestItem current=(LabtestItem)v.getTag();

                    //Toast.makeText(context,"Labtest :"+ current.getId()+")"+ current.getItem_name() + "at "+ current.getPrice(),Toast.LENGTH_SHORT).show();
                    ((MainActivity)context).addItemToCartMain(current.getId(),"1","itm",current.getItem_name());


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
            //Log.i("@ filter", "No text ");
        }
        else
        {
            //Log.i("@ filter", "arraylist size >> "+arraylist.size());
            for (int i=0;i<arraylist.size();i++)
            {
                LabtestItem wp = arraylist.get(i);
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
