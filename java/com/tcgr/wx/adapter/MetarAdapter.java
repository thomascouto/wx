package com.tcgr.wx.adapter;

import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.viewholder.MetarViewHolder;
import com.tcgr.wx.base.Metar;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.preference.SharedPreference;

import java.util.ArrayList;

/**
 * Metar Adapter
 * 
 * @see WxAdapter
 * @see Adapter
 * @author Thomas
 */
public class MetarAdapter extends WxAdapter<Metar, MetarViewHolder> {

    public MetarAdapter(ArrayList<Metar> wx, boolean showStar, App app) {
        super(wx, showStar, app);
    }

    public MetarAdapter(ArrayList<Metar> wx, boolean showStar) {
        super(wx, showStar, null);
    }

    @Override
    public MetarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_metar, parent, false);
        if (context == null) {
            context = parent.getContext();
            pref = new SharedPreference(context, Constants.SHARED_METAR);
        }
        return new MetarViewHolder(v, context, pref, showStar, app);
    }

    @Override
    public void onBindViewHolder(MetarViewHolder holder, int position) {
        String elevation = context.getString(R.string.elevation_ft, wx.get(position).getElevation());
        String category = (wx.get(position).getFlightCategory() == null) ? "" : wx.get(position).getFlightCategory();
        holder.itemView.setSelected(selectedItems.get(position, false));
        holder.stationId.setText(wx.get(position).getStationId());
        holder.elevation.setText(elevation);
        holder.flightCategory.setText(category);

        if(pref.stationExists(wx.get(position).getStationId())) {
            holder.star.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            holder.star.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

        switch (category) {
            case "VFR":
                holder.flightCategory.setTextColor(Color.BLUE);
                break;
            case "MVFR":
                holder.flightCategory.setTextColor(Color.GREEN);
                break;
            case "IFR":
                holder.flightCategory.setTextColor(Color.MAGENTA);
                break;
            case "LIFR":
                holder.flightCategory.setTextColor(Color.RED);
                break;
            default:
                break;
        }

        String sWx = (wx.get(position)).getWxString();
        if (sWx != null) {
            if (sWx.contains(" ")) {
                //tem um espaço e branco, realizar os checks para selecinar a imagem correta.
                int[] arrWx = checkWx(sWx);
                holder.wxString.setImageResource(arrWx[0]);
                if (arrWx.length > 1) {
                    holder.wxString1.setImageResource(arrWx[1]);
                }
            } else {
                //so há uma imagem a ser inserida.
                holder.wxString.setImageResource(wxSymbol(sWx));
            }
        } else {
            holder.wxString.setImageDrawable(null);
            holder.wxString1.setImageDrawable(null);
        }

        String metarType;
        if ((wx.get(position)).getMetarType().equals("SPECI")) {
            metarType = "<font color=red><b><i>SPECI </i></b></font> ";
        } else {
            metarType = "<b>METAR</b> ";
        }

        holder.rawMessage.setText(Html.fromHtml(metarType.concat((wx.get(position)).getRawText())));
    }

    /**
     * @param wxString ...
     * @return um array de String.
     */
    private int[] checkWx(String wxString) {

        int sCheckWx = wxSymbol(wxString);
        if (sCheckWx == -1) {
            String[] arrWxString = wxString.split("\\s");
            int[] newResult = new int[arrWxString.length];

            for (int i = 0; i < arrWxString.length; i++) {
                newResult[i] = wxSymbol(arrWxString[i]);
            }
            return newResult;
        }
        return new int[]{sCheckWx};
    }

    private int wxSymbol(String wxString) {
        int result;

        switch (wxString) {
            case "FU":
            case "VA":
                result = R.drawable.fu;
                break;

            case "HZ":
                result = R.drawable.hz;
                break;

            case "DU":
                result = R.drawable.du;
                break;

            case "SA":
            case "BLSA":
            case "VCBLSA":
            case "BLDU":
            case "VCBLDU":
            case "BLPY":
                result = R.drawable.sabldu;
                break;

            case "PO":
            case "VCPO":
                result = R.drawable.po;
                break;

            case "VCSS":
            case "VCDS":
                result = R.drawable.vcss;
                break;

            case "BR":
                result = R.drawable.br;
                break;

            case "MIFG":
                result = R.drawable.mifg;
                break;

            case "VCTS":
                result = R.drawable.vcts;
                break;

            case "VIRGA":
                result = R.drawable.virga;
                break;

            case "VCSH":
                result = R.drawable.vcsh;
                break;

            case "TS":
                result = R.drawable.ts;
                break;

            case "SQ":
                result = R.drawable.sq;
                break;

            case "FC":
            case "+FC":
                result = R.drawable.fc_heavy_fc;
                break;

            case "SS":
            case "DS":
            case "DRSA":
            case "DRDU":
                result = R.drawable.ssds;
                break;

            case "+SS":
            case "+DS":
                result = R.drawable.heavy_ss_heavy_ds;
                break;

            case "BLSN":
            case "VCBLSN":
                result = R.drawable.blsn;
                break;

            case "DRSN":
                result = R.drawable.drsn;
                break;

            case "VCFG":
                result = R.drawable.vcfg;
                break;

            case "BCFG":
                result = R.drawable.bcfg;
                break;

            case "PRFG":
                result = R.drawable.prfg;
                break;

            case "FG":
                result = R.drawable.fg;
                break;

            case "FZFG":
                result = R.drawable.fzfg;
                break;

            case "-DZ":
                result = R.drawable.light_dz;
                break;

            case "DZ":
                result = R.drawable.dz;
                break;

            case "+DZ":
                result = R.drawable.heavy_dz;
                break;

            case "-FZDZ":
                result = R.drawable.light_fzdz;
                break;

            case "FZDZ":
            case "+FZDZ":
                result = R.drawable.fzdz_heavy_fzdz;
                break;

            case "DZ -RA":
            case "-DZ RA":
            case "-DZ -RA":
                result = R.drawable.dz_light_ra;
                break;

            case "DZ RA":
            case "+DZ RA":
            case "DZ +RA":
            case "+DZ +RA":
                result = R.drawable.dzra;
                break;

            case "+RA":
                result = R.drawable.heavy_ra;
                break;

            case "RA":
                result = R.drawable.ra;
                break;

            case "-RA":
                result = R.drawable.light_ra;
                break;

            case "-FZRA":
                result = R.drawable.light_fzra;
                break;

            case "FZRA":
            case "+FZRA":
                result = R.drawable.fzra;
                break;

            case "-RA -SN":
            case "-RA SN":
            case "-DZ -SN":
            case "-DZ SN":
                result = R.drawable.light_ra_light_sn;
                break;

            case "RA SN":
            case "DZ SN":
            case "+RA SN":
            case "+DZ SN":
            case "RA +SN":
            case "DZ +SN":
            case "+RA +SN":
            case "+DZ +SN":
                result = R.drawable.rasn;
                break;

            case "-SN":
                result = R.drawable.light_sn;
                break;

            case "SN":
                result = R.drawable.sn;
                break;

            case "+SN":
                result = R.drawable.heavy_sn;
                break;

            case "UP":
                result = R.drawable.up;
                break;

            case "SG":
                result = R.drawable.sg;
                break;

            case "IC":
                result = R.drawable.ic;
                break;

            case "PL":
            case "PE":
            case "SHPL":
            case "SHPE":
                result = R.drawable.plpe;
                break;

            case "-SH":
            case "-SHRA":
                result = R.drawable.light_sh_light_shra;
                break;

            case "SH":
            case "+SH":
            case "SHRA":
            case "+SHRA":
                result = R.drawable.sh_heavy_sh;
                break;

            case "-SHRA SN":
            case "-SHSN RA":
            case "-SHRA -SN":
            case "-SHSN -RA":
                result = R.drawable.light_shrasn;
                break;

            case "SHRA SN":
            case "SHSN RA":
            case "+SHRA SN":
            case "+SHSN RA":
                result = R.drawable.shrasn;
                break;

            case "-SHSN":
                result = R.drawable.light_shsn;
                break;

            case "SHSN":
            case "+SHSN":
                result = R.drawable.shsn;
                break;

            case "-GS":
            case "-SHGS":
                result = R.drawable.light_gs;
                break;

            case "GS":
            case "SHGS":
            case "+GS":
            case "+SHGS":
                result = R.drawable.gs;
                break;

            case "-GR":
            case "-SHGR":
                result = R.drawable.light_gr_light_shgr;
                break;

            case "GR":
            case "SHGR":
            case "+GR":
            case "+SHGR":
                result = R.drawable.gr;
                break;

            case "TSRA":
            case "TSSN":
            case "TSPL":
            case "-TSRA":
                result = R.drawable.tsra;
                break;

            case "TSGR":
            case "TSGS":
                result = R.drawable.tsgr;
                break;

            case "+TSRA":
            case "+TSSN":
            case "+TSPL":
                result = R.drawable.heavy_tsra;
                break;

            case "TSSA":
            case "TSDU":
            case "TSDS":
                result = R.drawable.any_ts;
                break;

            case "+TSGS":
            case "+TSGR":
                result = R.drawable.heavy_tsgs;
                break;

            default:
                result = -1;
                break;
        }
        return result;
    }
}