package com.tcgr.wx.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcgr.wx.R;
import com.tcgr.wx.base.Chart;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.parser.ChartParser;
import com.tcgr.wx.ui.activity.ChartsActivity;

import java.util.ArrayList;

/**
 * @see Chart
 * @see ChartParser
 *
 * Created by thomas on 28/04/16.
 */
public class ChartsAdapter extends Adapter<ChartsAdapter.ChartsViewHolder> {

    private ArrayList<Chart> arrayList;

    public ChartsAdapter(ArrayList<Chart> arrayList) {
        super();
        this.arrayList = arrayList;
    }

    @Override
    public ChartsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carta, parent, false);
        if(context == null) {
            context = parent.getContext();
        }
        return new ChartsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChartsViewHolder holder, int position) {
        holder.itemView.setSelected(selectedItems.get(position, false));
        holder.stationName.setText(arrayList.get(position).getIcao());
        holder.tipoCarta.setText(arrayList.get(position).getTipo());
        holder.dataCarta.setText(arrayList.get(position).getData());
        holder.descricao.setText(arrayList.get(position).getDescricao());
        holder.nomeCarta.setText(arrayList.get(position).getNomeCarta());
        holder.url = arrayList.get(position).getUrl();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ArrayList<Chart> getList() {
        return arrayList;
    }

    @Override
    public String getShareableAdapter() {
        String shareableContent = "";
        for(int i = 0; i < selectedItems.size(); i++) {
            int selected = selectedItems.keyAt(i);

            shareableContent = shareableContent +
                    arrayList.get(selected).getIcao() + " - " + arrayList.get(selected).getNomeCarta() + "\n" +
                    arrayList.get(selected).getData() + "\n" +
                    arrayList.get(selected).getUrl();

            if(i + 1 < selectedItems.size()) {
                shareableContent = shareableContent + "\n-\n";
            }
        }
        return shareableContent;
    }

    class ChartsViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView stationName, tipoCarta, nomeCarta, dataCarta, descricao;
        String url;

        public ChartsViewHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.stationName = (TextView) itemView.findViewById(R.id.station_name);
            this.tipoCarta = (TextView) itemView.findViewById(R.id.tipo_carta);
            this.nomeCarta = (TextView) itemView.findViewById(R.id.nome_carta);
            this.dataCarta = (TextView) itemView.findViewById(R.id.data_carta);
            this.descricao = (TextView) itemView.findViewById(R.id.descricao_carta);
            this.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChartsActivity.class);
                    intent.putExtra(Constants.ICAO, stationName.getText().toString());
                    intent.putExtra(Constants.PROCEDIMETO, nomeCarta.getText().toString());
                    intent.putExtra(Constants.URL, url);
                    context.startActivity(intent);
                }
            });
        }
    }
}