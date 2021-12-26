package com.tcgr.wx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcgr.wx.R;
import com.tcgr.wx.base.Notam;

import java.util.ArrayList;

/**
 * Notam Adapter
 *
 * @see Adapter
 */
public class NotamAdapter extends Adapter<NotamAdapter.NotamViewHolder> {

    private ArrayList<Notam> notamList;

    public NotamAdapter(ArrayList<Notam> notamList) {
        super();
        this.notamList = notamList;
    }

    @Override
    public NotamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notam, parent, false);
        if (context == null) {
            context = parent.getContext();
        }
        return new NotamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotamViewHolder holder, int position) {
        String validade = context.getResources().getString(R.string.validade_notam, notamList.get(position).getValidade());
        holder.itemView.setSelected(selectedItems.get(position, false));
        holder.notamId.setText(notamList.get(position).getNotamCode());
        holder.cidadeUf.setText(notamList.get(position).getCidade());
        holder.validade.setText(validade);
        holder.notamContent.setText(notamList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return notamList.size();
    }

    @Override
    public ArrayList<Notam> getList() {
        return notamList;
    }

    /**
     * @return Lista de notam's em formato texto, para ser compartilhados entre outros aplicativos.
     */
    @Override
    public String getShareableAdapter() {
        String shareableContent = "";
        for (int i = 0; i < selectedItems.size(); i++) {
            int selected = selectedItems.keyAt(i);

            shareableContent = shareableContent + notamList.get(selected).getNotamCode() + "\n" +
                    notamList.get(selected).getCidade() + "\n" +
                    notamList.get(selected).getValidade() + "\n" +
                    notamList.get(selected).getContent();

            if(i + 1 < selectedItems.size()) {
                shareableContent = shareableContent + "\n-\n";
            }
        }
        return shareableContent;
    }

    class NotamViewHolder extends RecyclerView.ViewHolder {

        TextView notamId;
        TextView notamContent;
        TextView cidadeUf;
        TextView validade;

        public NotamViewHolder(View itemView) {
            super(itemView);
            this.notamId = (TextView) itemView.findViewById(R.id.notam_id);
            this.cidadeUf = (TextView) itemView.findViewById(R.id.notam_cidade_uf);
            this.validade = (TextView) itemView.findViewById(R.id.notam_validade);
            this.notamContent = (TextView) itemView.findViewById(R.id.notam_content);
        }
    }
}