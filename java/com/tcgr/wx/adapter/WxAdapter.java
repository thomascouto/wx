package com.tcgr.wx.adapter;

import com.tcgr.wx.adapter.viewholder.AbstractWeatherViewHolder;
import com.tcgr.wx.base.Weather;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.preference.SharedPreference;

import java.util.ArrayList;

/**
 * Classe WxAdapter
 * @see Adapter
 *
 * Created by thomas on 02/12/15.
 */
public abstract class WxAdapter<U extends Weather, T extends AbstractWeatherViewHolder> extends Adapter<T> {

    ArrayList<U> wx;
    SharedPreference pref;
    App app;
    boolean showStar;

    WxAdapter(ArrayList<U> wx, boolean showStar, App app) {
        super();
        this.wx = wx;
        this.app = app;
        this.showStar = showStar;
    }

    @Override
    public ArrayList<U> getList() {
        return wx;
    }

    public String getShareableAdapter() {
        String shareableContent = "";
        for(int i = 0; i < selectedItems.size(); i++) {
            int selected = selectedItems.keyAt(i);
            shareableContent = shareableContent + wx.get(selected).getRawText();
            if(i + 1 < selectedItems.size()) {
                shareableContent = shareableContent + "\n";
            }
        }
        return shareableContent;
    }

    public int getItemCount() {
        return (wx == null) ? 0 : wx.size();
    }
}