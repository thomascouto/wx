package com.tcgr.wx.ui.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.MetarAdapter;
import com.tcgr.wx.adapter.MyCursorAdapter;
import com.tcgr.wx.core.AbstractResponseHandler;
import com.tcgr.wx.core.AbstractWeatherFragment;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.core.db.DbSchema;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.parser.WxMetarParser;

import org.xml.sax.helpers.DefaultHandler;

import cz.msebera.android.httpclient.Header;

import static com.tcgr.wx.core.Async.get;

/**
 * MetarFragment
 *
 * @see com.tcgr.wx.core.AbstractWeatherFragment
 * @see com.tcgr.wx.core.AbstractFragment
 * @author thomas on 02/04/16.
 */
public class MetarFragment extends AbstractWeatherFragment {

    private WxMetarParser myParser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.metar));
        return inflater.inflate(R.layout.fragment_metar_taf, container, false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        String[] cols = new String[]{Constants.DB_ID, DbSchema.MetarSearchTable.Cols.ICAO};
        cursorAdapter = new MyCursorAdapter(getContext(), cols, new int[]{R.id.search_view_text_view});
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (((App) getActivity().getApplication()).isOnline()) {
            String url = String.format(Constants.ADDS_METAR, query);
            myParser = new WxMetarParser();
            get(url, new MyResponseHandler(myParser));
            lastQuery = query;
        } else {
            Snackbar.make(getActivity().findViewById(R.id.frame_container), getResources().getString(R.string.verifique_conexao), Snackbar.LENGTH_LONG).show();
        }
        return super.onQueryTextSubmit(query);
    }

    @Override
    public void onRefresh() {
        if ((lastQuery != null) && (!lastQuery.equals(""))) {
            String query = String.format(Constants.ADDS_METAR, lastQuery);
            myParser = new WxMetarParser();
            get(query, new MyResponseHandler(myParser));
        }
        swipeLayout.setRefreshing(false);
    }

    private class MyResponseHandler extends AbstractResponseHandler {

        public MyResponseHandler(DefaultHandler handler) {
            super(handler, getContext());
        }

        @Override
        public void onSuccess(int i, Header[] headers, DefaultHandler handler) {
            progressDialog.dismiss();
            if(!canceled) {
                if(myParser.getSize() == 0) {
                    Util.alert(getContext(), getResources().getString(R.string.nao_encontrado));
                } else {
                    myAdapter = new MetarAdapter(myParser.getList(), true, (App)MetarFragment.this.getActivity().getApplication());
                    listener.countItem(Constants.METAR_ID, myParser.getSize());
                    recyclerView.setAdapter(myAdapter);

                    //Insere o registro encontrado no Banco METAR
                    ContentValues cv = new ContentValues();
                    cv.put(DbSchema.MetarSearchTable.Cols.ICAO, lastQuery);
                    getContext().getContentResolver().insert(DbSchema.MetarSearchTable.CONTENT_URI, cv);
                }
            }
        }
    }
}