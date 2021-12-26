package com.tcgr.wx.ui.fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.NotamAdapter;
import com.tcgr.wx.base.Notam;
import com.tcgr.wx.core.AbstractFragment;
import com.tcgr.wx.core.AbstractNotamSoapRequest;
import com.tcgr.wx.core.AbstractResponseHandler;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.core.db.DbSchema;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.interfaces.CountListener;
import com.tcgr.wx.parser.NotamIntlParser;
import com.tcgr.wx.parser.NotamParser;
import com.tcgr.wx.parser.basic.BasicParser;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cz.msebera.android.httpclient.Header;

import static com.tcgr.wx.core.Async.get;

/**
 * Notam Fragment
 * @see AbstractFragment
 */
public class NotamFragment extends AbstractFragment implements View.OnClickListener {

    private BasicParser<ArrayList<Notam>> notamParser;
    private CountListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.notam));
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if((notamParser != null) && (notamParser.getSize() > 0)) {
            menu.findItem(R.id.save).setEnabled(true);
        } else {
            menu.findItem(R.id.save).setEnabled(false);
        }

        searchView.setOnSearchClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(0, R.id.save, 0, R.string.salvar);
        menu.add(0, R.id.delete, 1, R.string.excluir);
    }

    /**
     * Exibe o pop-up para escolher o NOTAM, caso o APP esteja OFF-LINE.
     * @param view view
     */
    @Override
    public void onClick(View view) {
        if (!((App) getActivity().getApplication()).isOnline()) {
            searchView.clearFocus();
            searchView.setIconified(true);
            Cursor c = getContext().getContentResolver().query(DbSchema.NotamTable.CONTENT_URI, new String[]{DbSchema.NotamTable.Cols.ICAO}, null, null, null);
            final String[] arrList = Util.cursorToArray(c);
            if (arrList.length > 0) {
                AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                        .setTitle(getString(R.string.notam))
                        .setItems(arrList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String icao = arrList[which];
                                Cursor cursor = getContext().getContentResolver().query(DbSchema.NotamTable.CONTENT_URI, new String[]{DbSchema.NotamTable.Cols.CONTENT}, DbSchema.NotamTable.Cols.ICAO + "=?", new String[]{icao}, null);
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        ArrayList<Notam> list = Util.deserializeObject(cursor.getBlob(0));
                                        myAdapter = new NotamAdapter(list);
                                        recyclerView.setAdapter(myAdapter);
                                    }
                                    cursor.close();
                                }
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }

    /**
     * Menu suspenso.
     *
     * @param item Itens
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (((App) getActivity().getApplication()).isOnline()) {
                    final ContentValues contentValues = new ContentValues();
                    try {
                        byte[] bNotamList = Util.serializeObject(myAdapter.getList());
                        contentValues.put(DbSchema.NotamTable.Cols.ICAO, lastQuery);
                        contentValues.put(DbSchema.NotamTable.Cols.CONTENT, bNotamList);
                        getContext().getContentResolver().insert(DbSchema.NotamTable.CONTENT_URI, contentValues);
                        Util.alert(getContext(), getResources().getString(R.string.inserido, lastQuery));
                    } catch (SQLiteConstraintException e) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(getResources().getString(R.string.existente, lastQuery))
                                .setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        contentValues.remove(DbSchema.NotamTable.Cols.ICAO);
                                        getContext().getContentResolver().update(DbSchema.NotamTable.CONTENT_URI, contentValues,
                                                DbSchema.NotamTable.Cols.ICAO + " = ? ", new String[]{lastQuery});
                                        Util.alert(getContext(), getResources().getString(R.string.inserido, lastQuery));
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.nao), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                }
                break;

            case R.id.delete:
                Cursor c = getContext().getContentResolver().query(DbSchema.NotamTable.CONTENT_URI, new String[]{DbSchema.NotamTable.Cols.ICAO}, null, null, null);
                final String[] arrList = Util.cursorToArray(c);
                if (arrList.length > 0) {
                    final List<String> deleteList = new ArrayList<>();
                    AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                            .setTitle(getResources().getString(R.string.notam))
                            .setMultiChoiceItems(arrList, null, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    String item = arrList[which];
                                    if (isChecked) {
                                        deleteList.add(item);
                                    } else {
                                        deleteList.remove(item);
                                    }
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.excluir), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (deleteList.size() > 0) {
                                        getContext().getContentResolver().delete(DbSchema.NotamTable.CONTENT_URI,
                                                DbSchema.NotamTable.Cols.ICAO, deleteList.toArray(new String[deleteList.size()]));
                                    }
                                }
                            })
                            .create();
                    dialog.show();
                } else {
                    Util.alert(getContext(), getString(R.string.nao_existe));
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (((App) getActivity().getApplication()).isOnline()) {
            if (query.length() == 4) {
                lastQuery = query;
                if (query.matches(Constants.REGEX_ICAO_BRASIL)) {
                    //NOTAM AIS-WEB
                    notamParser = new NotamParser();
                    String url = String.format(Constants.AIS_WEB_NOTAM, query);
                    get(url, new MyResponseHandler(notamParser));
                } else {
                    //NOTAM INTL
                    notamParser = new NotamIntlParser();
                    new MySoapRequest().execute(query);
                }
            } else {
                //formato incorreto
                Util.alert(getContext(), getResources().getString(R.string.verifique_formato));
            }
        }
        return super.onQueryTextSubmit(query);
    }

    public void setListener(CountListener listener) {
        this.listener = listener;
    }

    private class MyResponseHandler extends AbstractResponseHandler {

        public MyResponseHandler(DefaultHandler handler) {
            super(handler, getContext());
        }

        @Override
        public void onSuccess(int i, Header[] headers, DefaultHandler handler) {
            progressDialog.dismiss();
            if(!canceled) {
                if (notamParser.getSize() == 0) {
                    Util.alert(getContext(), getResources().getString(R.string.nao_encontrado));
                } else {
                    getActivity().invalidateOptionsMenu();
                    myAdapter = new NotamAdapter(notamParser.getList());
                    listener.countItem(Constants.NOTAM_ID, notamParser.getSize());
                    recyclerView.setAdapter(myAdapter);
                }
            }
        }
    }

    private class MySoapRequest extends AbstractNotamSoapRequest {

        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getString(R.string.aguarde));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(new InputSource(new StringReader(result)), notamParser);
                if (notamParser.getSize() == 0) {
                    Util.alert(getContext(), getResources().getString(R.string.nao_encontrado));
                } else {
                    myAdapter = new NotamAdapter(notamParser.getList());
                    listener.countItem(Constants.NOTAM_ID, notamParser.getSize());
                    recyclerView.setAdapter(myAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}