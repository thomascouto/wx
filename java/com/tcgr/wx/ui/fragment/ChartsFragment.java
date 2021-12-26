package com.tcgr.wx.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.ChartsAdapter;
import com.tcgr.wx.core.AbstractFragment;
import com.tcgr.wx.core.AbstractResponseHandler;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.Util;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.interfaces.CountListener;
import com.tcgr.wx.parser.ChartParser;

import org.xml.sax.helpers.DefaultHandler;

import cz.msebera.android.httpclient.Header;

import static com.tcgr.wx.core.Async.get;

/**
 * Fragment Cartas.
 *
 * @see AbstractFragment
 * <p>
 * Cartas Disponíveis (AIS-WEB):
 * ADC / ARC / IAC / PDC / SID / STAR / VAC
 * <p>
 * Created by thomas on 03/04/16.
 */
public class ChartsFragment extends AbstractFragment /*implements ActivityCompat.OnRequestPermissionsResultCallback*/ {

    private ChartParser cartaParser;
    private CountListener listener;
    private View myView;
    private Snackbar snackbar;

    public ChartsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        getActivity().setTitle(getString(R.string.cartas));
        myView = view.findViewById(R.id.recycler_view_layout);
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (((App) getActivity().getApplication()).isOnline()) {
            if ((query.length() == 4) && (query.matches(Constants.REGEX_ICAO_BRASIL))) {
                cartaParser = new ChartParser();
                String url = String.format(Constants.AIS_WEB_CARTAS, query);
                get(url, new MyResponseHandler(cartaParser));
            } else {
                Util.alert(getContext(), getResources().getString(R.string.verifique_formato));
            }
        }
        return super.onQueryTextSubmit(query);
    }

    @Override
    public void onResume() {
        if(Build.VERSION.SDK_INT >= 23) {
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(ChartsFragment.this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    snackbar = Snackbar.make(myView, R.string.permissao_salvar, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(ChartsFragment.this.getActivity(),
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            Constants.REQUEST_EXTERNAL);
                                }
                            });
                    snackbar.show();
                } else {
                    ActivityCompat.requestPermissions(ChartsFragment.this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_EXTERNAL);
                }
            }
        }
        super.onStart();
    }

    @Override
    public void onPause() {
        if(snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
        super.onPause();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case Constants.REQUEST_EXTERNAL:
//                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    snackbar = Snackbar.make(myView, R.string.sucesso, Snackbar.LENGTH_SHORT);
//                    snackbar.show();
//
//                    requestPermissions();
//                }
//                break;
//
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

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
            if (cartaParser.getSize() == 0) {
                Util.alert(getContext(), getResources().getString(R.string.nao_encontrado));
            } else {
                myAdapter = new ChartsAdapter(cartaParser.getList());
                listener.countItem(Constants.CHARTS_ID, cartaParser.getSize());
                recyclerView.setAdapter(myAdapter);
            }
        }
    }
}