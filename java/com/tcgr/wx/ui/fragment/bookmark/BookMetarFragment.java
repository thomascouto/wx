package com.tcgr.wx.ui.fragment.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.MetarAdapter;
import com.tcgr.wx.core.AbstractResponseHandler;
import com.tcgr.wx.core.AbstractWeatherFragment;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.preference.SharedPreference;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.parser.WxMetarParser;

import org.xml.sax.helpers.DefaultHandler;

import cz.msebera.android.httpclient.Header;

import static com.tcgr.wx.core.Async.get;

/**
 * Utilizado pelo ViewPager
 * Created by thomas on 28/04/16.
 */
public class BookMetarFragment extends AbstractWeatherFragment {

    private WxMetarParser myParser = new WxMetarParser();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_metar_taf, container, false);
        update();
        return view;
    }

    @Override
    public void onRefresh() {
        if(((App)getActivity().getApplication()).isOnline()) {
            update();
        }
        swipeLayout.setRefreshing(false);
    }

    private void update() {
        String query = new SharedPreference(getContext(), Constants.SHARED_METAR).getLinearArray();
        if ((query != null) && (query.length() > 0)) {
            String url = String.format(Constants.ADDS_METAR, query);
            get(url, new MyResponseHandler(myParser));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {}

    @Override
    public void onPrepareOptionsMenu(Menu menu) {}

    private class MyResponseHandler extends AbstractResponseHandler {

        public MyResponseHandler(DefaultHandler handler) {
            super(handler, getContext());
        }

        @Override
        public void onSuccess(int i, Header[] headers, DefaultHandler handler) {
            progressDialog.dismiss();
            myAdapter = new MetarAdapter(myParser.getList(), true, (App)BookMetarFragment.this.getActivity().getApplication());
            recyclerView.setAdapter(myAdapter);
        }

        @Override
        public void onFailure(int i, Header[] headers, DefaultHandler defaultHandler) {
            progressDialog.dismiss();
        }
    }
}