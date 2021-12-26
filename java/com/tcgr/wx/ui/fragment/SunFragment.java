package com.tcgr.wx.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.tcgr.wx.R;
import com.tcgr.wx.adapter.SunRiseAdapter;
import com.tcgr.wx.base.SunRise;
import com.tcgr.wx.core.AbstractFragment;
import com.tcgr.wx.core.AbstractResponseHandler;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.parser.SunRiseParser;
import com.tcgr.wx.parser.basic.BasicParser;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static com.tcgr.wx.core.Async.get;

/**
 * Created by thomas on 12/01/16.
 * @see AbstractFragment
 */
public class SunFragment extends AbstractFragment implements DatePickerDialog.OnDateSetListener {

    private BasicParser<ArrayList<SunRise>> myParser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.sunrise_sunset));
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (((App) getActivity().getApplication()).isOnline()) {
            if (query.matches(Constants.REGEX_ICAO_BRASIL)) {
                lastQuery = query;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        SunFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        } else {
            //off-line
            Snackbar.make(getActivity().findViewById(R.id.frame_container), getResources().getString(R.string.verifique_conexao), Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        monthOfYear++;
        monthOfYearEnd++;
        String dtInicialAux = year + "-" + String.format(Constants.BRAZIL, "%02d", monthOfYear) + "-" + String.format(Constants.BRAZIL, "%02d", dayOfMonth);
        String dtFinalAux = null;

        if ((year <= yearEnd) && (monthOfYear <= monthOfYearEnd)) {
            if (dayOfMonth <= dayOfMonthEnd) {
                dtFinalAux = yearEnd + "-" + String.format(Constants.BRAZIL, "%02d", monthOfYearEnd) + "-" + String.format(Constants.BRAZIL, "%02d", dayOfMonthEnd);
            } else {
                dtFinalAux = dtInicialAux;
            }
        }

        String url = String.format(Constants.AIS_WEB_SUN, searchView.getQuery(), dtInicialAux,
                ((dtFinalAux != null) && !dtFinalAux.equals(dtInicialAux)) ? dtFinalAux : "");

        myParser = new SunRiseParser();
        get(url, new MyResponseHandler(myParser));
    }

    private class MyResponseHandler extends AbstractResponseHandler {

        public MyResponseHandler(DefaultHandler handler) {
            super(handler, getContext());
        }

        @Override
        public void onSuccess(int i, Header[] headers, DefaultHandler handler) {
            progressDialog.dismiss();
            myAdapter = new SunRiseAdapter(myParser.getList());
            recyclerView.setAdapter(myAdapter);
        }
    }
}