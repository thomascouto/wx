package com.tcgr.wx.core.preference;import android.content.Context;import android.content.DialogInterface;import android.support.v7.app.AlertDialog;import android.support.v7.preference.DialogPreference;import android.util.AttributeSet;import android.widget.Toast;import com.tcgr.wx.R;import com.tcgr.wx.core.db.DbSchema;/** * * Created by thomas on 17/05/16. */public class RotaerDialogPreference extends DialogPreference {    public RotaerDialogPreference(Context context, AttributeSet attrs) {        super(context, attrs);    }    @Override    protected void onClick() {        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());        dialog.setTitle(getContext().getString(R.string.reset_app));        dialog.setMessage(getContext().getString(R.string.reset_rotaer));        dialog.setCancelable(true);        dialog.setPositiveButton(getContext().getString(R.string.sim), new DialogInterface.OnClickListener() {            @Override            public void onClick(DialogInterface dialog, int which) {                getContext().getContentResolver().delete(DbSchema.RotaerTable.CONTENT_URI, null, null);                Toast.makeText(getContext(), getContext().getString(R.string.sucesso), Toast.LENGTH_SHORT).show();            }        });        dialog.setNegativeButton(getContext().getString(R.string.nao), new DialogInterface.OnClickListener() {            @Override            public void onClick(DialogInterface dialog, int which) {                dialog.dismiss();            }        });        AlertDialog alertDialog = dialog.create();        alertDialog.show();    }}