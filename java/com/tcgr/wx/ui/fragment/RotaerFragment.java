package com.tcgr.wx.ui.fragment;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tcgr.wx.R;
import com.tcgr.wx.core.AbstractResponseHandler;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.core.db.DbSchema;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.parser.RotaerParser;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.tcgr.wx.core.Async.get;

/**
 * Rotaer
 */
public class RotaerFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private RotaerParser parser;
    private TextView rotaerContent;
    private SearchView searchView;
    private String lastQuery;

    public RotaerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.rotaer));
        return inflater.inflate(R.layout.fragment_rotaer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(rotaerContent == null) {
            rotaerContent = (TextView) view.findViewById(R.id.rotaer_content);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setOnSearchClickListener(this);
            searchView.setOnQueryTextListener(this);
            searchView.setSubmitButtonEnabled(true);
            searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        }

        if (parser != null) {
            String urlCarta = parser.getUrlCartaVfr();
            if (urlCarta != null) {
                WebView webView = new WebView(getContext());
                webView.loadUrl(urlCarta);

                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setView(webView);
                dialog.setNegativeButton(getString(R.string.fechar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                MenuItem map = menu.findItem(R.id.action_rotaer_map);
                map.setVisible(true);
                map.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        dialog.show();
                        return true;
                    }
                });
            }
        }

        if ((rotaerContent != null) && (rotaerContent.getText().length() > 0)) {
            menu.findItem(R.id.save).setEnabled(true);
            menu.findItem(R.id.share).setEnabled(true);
        } else {
            menu.findItem(R.id.save).setEnabled(false);
            menu.findItem(R.id.share).setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchview_rotaer, menu);
        menu.add(0, R.id.save, 0, R.string.salvar);
        menu.add(0, R.id.delete, 1, R.string.excluir);
        menu.add(0, R.id.share, 2, R.string.share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                String data = Html.toHtml(rotaerContent.getEditableText());
                if (data.length() > 0) {
                    final ContentValues contentValues = new ContentValues();
                    contentValues.put(DbSchema.RotaerTable.Cols.ICAO, lastQuery);
                    contentValues.put(DbSchema.RotaerTable.Cols.CONTENT, data);
                    try {
                        getContext().getContentResolver().insert(DbSchema.RotaerTable.CONTENT_URI, contentValues);
                        Util.alert(getContext(), getResources().getString(R.string.inserido, lastQuery));
                    } catch (SQLiteConstraintException e) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(getResources().getString(R.string.existente, lastQuery))
                                .setPositiveButton(getResources().getString(R.string.sim), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        contentValues.remove(DbSchema.RotaerTable.Cols.ICAO);
                                        getContext().getContentResolver().update(DbSchema.RotaerTable.CONTENT_URI, contentValues, DbSchema.RotaerTable.Cols.ICAO + " = ? ", new String[]{lastQuery});
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
                Cursor c = getContext().getContentResolver().query(DbSchema.RotaerTable.CONTENT_URI, new String[]{DbSchema.RotaerTable.Cols.ICAO}, null, null, null);
                final String[] arrList = Util.cursorToArray(c);
                if (arrList.length > 0) {
                    final List<String> deleteList = new ArrayList<>();
                    AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                            .setTitle(getResources().getString(R.string.rotaer))
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
                                        getContext().getContentResolver().delete(DbSchema.RotaerTable.CONTENT_URI, DbSchema.RotaerTable.Cols.ICAO, deleteList.toArray(new String[deleteList.size()]));
                                    }
                                }
                            })
                            .create();
                    dialog.show();
                } else {
                    Util.alert(getContext(), getResources().getString(R.string.nao_existe));
                }
                break;

            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, rotaerContent.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.title_activity_main)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.matches(Constants.REGEX_ICAO_BRASIL)) {
            lastQuery = query;
            if (((App) getActivity().getApplication()).isOnline()) {
                parser = new RotaerParser();
                String url = String.format(Constants.AIS_WEB_ROTAER, query);
                get(url, new MyResponseHandler(parser));
            }
        } else {
            Util.alert(getContext(), getString(R.string.verifique_formato));
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if (!((App) getActivity().getApplication()).isOnline()) {
            searchView.clearFocus();
            searchView.setIconified(true);
            Cursor c = getContext().getContentResolver().query(DbSchema.RotaerTable.CONTENT_URI, new String[]{DbSchema.RotaerTable.Cols.ICAO}, null, null, null);
            final String[] arrList = Util.cursorToArray(c);
            if (arrList.length > 0) {
                AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                        .setTitle(getString(R.string.notam))
                        .setItems(arrList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String icao = arrList[which];
                                Cursor cursor = getContext().getContentResolver().query(DbSchema.RotaerTable.CONTENT_URI, new String[]{DbSchema.RotaerTable.Cols.CONTENT}, DbSchema.RotaerTable.Cols.ICAO + "=?", new String[]{icao}, null);
                                if (cursor != null) {
                                    if (cursor.moveToFirst()) {
                                        rotaerContent.setText(Html.fromHtml(cursor.getString(0)));
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

    private class MyResponseHandler extends AbstractResponseHandler {

        public MyResponseHandler(DefaultHandler handler) {
            super(handler, getContext());
        }

        @Override
        public void onSuccess(int i, Header[] headers, DefaultHandler handler) {
            progressDialog.dismiss();

            String rawData = parser.getList();
            if (rawData != null) {
                rotaerContent.setText(Html.fromHtml(rawData), TextView.BufferType.EDITABLE);
                getActivity().invalidateOptionsMenu();
            } else {
                Util.alert(getContext(), getResources().getString(R.string.nao_encontrado));
            }
        }
    }
}