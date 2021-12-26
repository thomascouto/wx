package com.tcgr.wx.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcgr.wx.R;
import com.tcgr.wx.core.preference.SharedPreference;

/**
 *
 * Created by thomas on 12/06/16.
 */
public abstract class AbstractWeatherViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    public TextView stationId;
    public TextView rawMessage;
    public TextView elevation;
    public ImageView star;

    public AbstractWeatherViewHolder(View itemView, final SharedPreference pref) {
        super(itemView);
        this.cv = (CardView) itemView.findViewById(R.id.card_view);
        this.stationId = (TextView) itemView.findViewById(R.id.station_name);
        this.rawMessage = (TextView) itemView.findViewById(R.id.raw_message);
        this.elevation = (TextView) itemView.findViewById(R.id.elevation);
        this.star = (ImageView) itemView.findViewById(R.id.station_star);
        this.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pref.toggleFavorite(stationId.getText().toString())) {
                    star.setImageResource(R.drawable.ic_star_black_24dp);
                } else {
                    star.setImageResource(R.drawable.ic_star_border_black_24dp);
                }
            }
        });
    }
}