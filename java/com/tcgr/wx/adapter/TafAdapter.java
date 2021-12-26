package com.tcgr.wx.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.viewholder.TafViewHolder;
import com.tcgr.wx.base.Taf;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.preference.SharedPreference;

import java.util.ArrayList;

/**
 * TafAdapter
 *
 * @see WxAdapter
 * @see Adapter
 * @author Thomas
 */
public class TafAdapter extends WxAdapter<Taf, TafViewHolder> {

    public TafAdapter(ArrayList<Taf> wx, boolean showStar, App app) {
        super(wx, showStar, app);
    }

    public TafAdapter(ArrayList<Taf> wx, boolean showStar) {
        super(wx, showStar, null);
    }

    @Override
    public TafViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_taf, parent, false);
        if(context == null) {
            context = parent.getContext();
            pref = new SharedPreference(context, Constants.SHARED_TAF);
        }
        return new TafViewHolder(v, context, pref, showStar, app);
    }

    @Override
    public void onBindViewHolder(TafViewHolder holder, int position) {
        String elevation = context.getResources().getString(R.string.elevation_ft, wx.get(position).getElevation());
        String rawText = wx.get(position).getRawText();
        rawText = rawText.replace("TAF", "<b>TAF</b> ");
        rawText = rawText.replace("BECMG", "<br><b>BECMG</b> ");
        rawText = rawText.replace("PROB40", "<br><b>PROB40</b> ");
        rawText = rawText.replace("PROB30", "<br><b>PROB30</b> ");
        rawText = rawText.replace("TEMPO", "<br><b>TEMPO</b> ");

        if(pref.stationExists(wx.get(position).getStationId())) {
            holder.star.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            holder.star.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        holder.itemView.setSelected(selectedItems.get(position, false));
        holder.elevation.setText(elevation);
        holder.stationId.setText(wx.get(position).getStationId());
        holder.rawMessage.setText(Html.fromHtml(rawText));
    }
}