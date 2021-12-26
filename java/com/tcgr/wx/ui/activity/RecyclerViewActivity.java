package com.tcgr.wx.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.MetarAdapter;
import com.tcgr.wx.adapter.TafAdapter;
import com.tcgr.wx.adapter.WxAdapter;
import com.tcgr.wx.core.AbstractResponseHandler;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.preference.SharedPreference;
import com.tcgr.wx.parser.WxMetarParser;
import com.tcgr.wx.parser.WxTafParser;

import org.xml.sax.helpers.DefaultHandler;

import cz.msebera.android.httpclient.Header;

import static com.tcgr.wx.core.Async.get;

/**
 * Metar e TAF extendidos por 'X' horas.
 * @author Thomas Couto
 */
public class RecyclerViewActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, ActionMode.Callback {

    private GestureDetectorCompat gestureDetector;
    private ActionMode mActionMode;
    private RecyclerView recyclerView;
    private WxAdapter wxAdapter;
    private WxMetarParser wxMetarParser;
    private WxTafParser wxTafParser;
    private MyResponseHandler handler;
    private String query;
    private int qtd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyler_view);
        Bundle extras = getIntent().getExtras();
        String requestIcao = extras.getString(Constants.ICAO);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnItemTouchListener(this);
        gestureDetector = new GestureDetectorCompat(this, new RecyclerViewOnGestureListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreference sharedPreference = new SharedPreference(getApplicationContext());

        switch (extras.getInt(Constants.ID)) {
            case Constants.METAR_ID:
                wxMetarParser = new WxMetarParser();
                handler = new MyResponseHandler(wxMetarParser);
                setTitle(getResources().getString(R.string.metar) + " " + requestIcao);
                qtd = sharedPreference.getValue(getString(R.string.prefs_metar_key));
                query = String.format(Constants.ADDS_METAR_TIME, requestIcao, qtd);
                break;

            case Constants.TAF_ID:
                wxTafParser = new WxTafParser();
                handler = new MyResponseHandler(wxTafParser);
                setTitle(getResources().getString(R.string.taf) + " " + requestIcao);
                qtd = sharedPreference.getValue(getString(R.string.prefs_taf_key));
                query = String.format(Constants.ADDS_TAF_TIME, requestIcao, qtd);
                break;
        }

        if(savedInstanceState == null) {
            get(query, handler);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu_share, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
           switch (item.getItemId()) {
            case R.id.action_mode_share:
                String shareableAdapter = wxAdapter.getShareableAdapter();
                Intent sendIntent = new Intent();
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareableAdapter);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.title_activity_main)));
                break;
        }
        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        wxAdapter.clearSelections();
    }

    private void myToggleSelection(int idx) {
        wxAdapter.toggleSelection(idx);
        int count = wxAdapter.getSelectedItemCount();

        if (count == 0) {
            mActionMode.finish();
            return;
        }

        String title = getResources().getQuantityString(R.plurals.selected_count, count, count);
        mActionMode.setTitle(title);
    }

    private class RecyclerViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public void onLongPress(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if(mActionMode == null) {
                mActionMode = startSupportActionMode(RecyclerViewActivity.this);
            }
            int idx = recyclerView.getChildAdapterPosition(view);
            myToggleSelection(idx);
        }
    }

    private class MyResponseHandler extends AbstractResponseHandler {

        public MyResponseHandler(DefaultHandler handler) {
            super(handler, RecyclerViewActivity.this);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, DefaultHandler defaultHandler) {
            progressDialog.dismiss();

            if(wxMetarParser != null) {
                wxAdapter = new MetarAdapter(wxMetarParser.getList(), false);
            } else {
                wxAdapter = new TafAdapter(wxTafParser.getList(), false);
            }

            recyclerView.setAdapter(wxAdapter);
            String text = String.format(getString(R.string.registros_encontrados), wxAdapter.getItemCount(), qtd);
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }
}