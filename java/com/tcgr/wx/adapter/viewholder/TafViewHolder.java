package com.tcgr.wx.adapter.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tcgr.wx.R;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.preference.SharedPreference;
import com.tcgr.wx.ui.activity.RecyclerViewActivity;

/**
 * View Holder
 * Created by thomas on 12/06/16.
 */
public class TafViewHolder extends AbstractWeatherViewHolder {
    public TafViewHolder(View itemView, final Context context, final SharedPreference pref, boolean showStar, final App app) {
        super(itemView, pref);
        if (showStar) {
            star.setVisibility(View.VISIBLE);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (app.isOnline()) {
                        Intent intent = new Intent(context, RecyclerViewActivity.class);
                        String stationName = stationId.getText().toString();
                        intent.putExtra(Constants.ICAO, stationName);
                        intent.putExtra(Constants.ID, Constants.TAF_ID);
                        context.startActivity(intent);
                    } else {
                        Util.alert(context, context.getResources().getString(R.string.verifique_conexao));
                    }
                }
            });
        }
    }
}