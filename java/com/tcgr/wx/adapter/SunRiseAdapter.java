package com.tcgr.wx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcgr.wx.R;
import com.tcgr.wx.base.SunRise;

import java.util.ArrayList;

/**
 * SunRise Adapter
 * @see Adapter
 */
public class SunRiseAdapter extends Adapter<SunRiseAdapter.SunRiseViewHolder> {

    private ArrayList<SunRise> sunList;

    public SunRiseAdapter(ArrayList<SunRise> sunList) {
        super();
        this.sunList = sunList;
    }

    @Override
    public SunRiseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sun, parent, false);
        if(context == null) {
            context = parent.getContext();
        }
        return new SunRiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SunRiseViewHolder holder, int position) {
        holder.itemView.setSelected(selectedItems.get(position, false));
        holder.sunRise.setText(sunList.get(position).getSunRise());
        holder.sunSet.setText(sunList.get(position).getSunSet());
        holder.date.setText(sunList.get(position).getDayMonth());
        holder.year.setText(sunList.get(position).getYear());
    }

    @Override
    public ArrayList<SunRise> getList() {
        return sunList;
    }

    @Override
    public String getShareableAdapter() {
        String shareableContent = "";
        for(int i = 0; i < selectedItems.size(); i++) {
            int selected = selectedItems.keyAt(i);

            SunRise s = sunList.get(selected);
            String temp = "["+s.getDayMonth()+"/"+s.getYear()+"]" + " SR - " + sunList.get(selected).getSunRise() +
                    " | SS - " + sunList.get(selected).getSunSet();

            shareableContent = shareableContent + temp;

            if(i + 1 < selectedItems.size()) {
                shareableContent = shareableContent + "\n";
            }
        }
        return shareableContent;
    }

    @Override
    public int getItemCount() {
        return sunList.size();
    }

    class SunRiseViewHolder extends RecyclerView.ViewHolder {

        TextView sunRise;
        TextView sunSet;
        TextView date;
        TextView year;

        public SunRiseViewHolder(View itemView) {
            super(itemView);
            this.sunRise = (TextView) itemView.findViewById(R.id.sunrise_text);
            this.sunSet = (TextView) itemView.findViewById(R.id.sunset_text);
            this.date = (TextView) itemView.findViewById(R.id.sun_date);
            this.year = (TextView) itemView.findViewById(R.id.sun_year);
        }
    }
}