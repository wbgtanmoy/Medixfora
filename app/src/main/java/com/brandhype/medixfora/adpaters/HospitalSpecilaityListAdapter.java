package com.brandhype.medixfora.adpaters;

/**
 * Created by Tanmoy Banerjee on 11-09-2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brandhype.medixfora.R;
import com.brandhype.medixfora.models.GeneralItem;

import java.util.List;

public class HospitalSpecilaityListAdapter extends RecyclerView.Adapter<HospitalSpecilaityListAdapter.ViewHolder> {

    private List<GeneralItem> stList;

    public HospitalSpecilaityListAdapter(List<GeneralItem> items) {
        this.stList = items;
    }

    // Create new views
    @Override
    public HospitalSpecilaityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate( R.layout.hospital_cardview_row, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.tvName.setText(stList.get(position).getTitle());
        viewHolder.chkSelected.setChecked(stList.get(position).getSelected());

        viewHolder.chkSelected.setTag(stList.get(position));


        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                GeneralItem hosp = (GeneralItem) cb.getTag();
                hosp.setSelected(cb.isChecked());
                stList.get(pos).setSelected(cb.isChecked());
                //Toast.makeText(  v.getContext(),""+ hosp.getTitle()+ " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return stList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public CheckBox chkSelected;

        public GeneralItem item;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);
        }

    }

    // method to access in activity after updating selection
    public List<GeneralItem> getList() {
        return stList;
    }

    public void setList(List<GeneralItem> rlist) {
        stList=rlist;
        notifyDataSetChanged();
    }

}