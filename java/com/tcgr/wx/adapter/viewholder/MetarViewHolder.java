package com.tcgr.wx.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcgr.wx.R;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.preference.SharedPreference;
import com.tcgr.wx.ui.activity.RecyclerViewActivity;

/**
 *
 * Created by thomas on 12/06/16.
 */
public class MetarViewHolder extends AbstractWeatherViewHolder {

    /**
     * ImageView skyCondition;
     * this.skyCondition = (ImageView) itemView.findViewById(R.id.sky_condition_image_view);
     * <sky_condition sky_cover="FEW" cloud_base_ft_agl="2500"/>
     */

    public TextView flightCategory;
    public ImageView star;
    public ImageView wxString;
    public ImageView wxString1;

    public MetarViewHolder(View itemView, final Context context, final SharedPreference pref, boolean showStar, final App app) {
        super(itemView, pref);
        this.flightCategory = (TextView) itemView.findViewById(R.id.flight_category);
        this.star = (ImageView) itemView.findViewById(R.id.station_star);
        this.wxString = (ImageView) itemView.findViewById(R.id.wx_string);
        this.wxString1 = (ImageView) itemView.findViewById(R.id.wx_string_1);
        if (showStar) {
            star.setVisibility(View.VISIBLE);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(app.isOnline()) {
                        Intent intent = new Intent(context, RecyclerViewActivity.class);
                        String stationName = stationId.getText().toString();
                        intent.putExtra(Constants.ICAO, stationName);
                        intent.putExtra(Constants.ID, Constants.METAR_ID);
                        context.startActivity(intent);
                    } else {
                        Util.alert(context, context.getResources().getString(R.string.verifique_conexao));
                    }
                }
            });
        }
    }
}