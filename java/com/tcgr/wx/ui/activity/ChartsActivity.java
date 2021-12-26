package com.tcgr.wx.ui.activity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.tcgr.wx.R;
import com.tcgr.wx.adapter.ChartsAdapter;
import com.tcgr.wx.base.Chart;
import com.tcgr.wx.core.De;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.parser.ChartParser;

/**
 * @see Chart
 * @see ChartsAdapter
 * @see ChartParser
 * <p>
 * Created by Thomas on 28/04/16.
 */
public class ChartsActivity extends AppCompatActivity {

    private String url, procedimento, requestIcao;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        requestIcao = extras.getString(Constants.ICAO);
        url = extras.getString(Constants.URL);
        procedimento = extras.getString(Constants.PROCEDIMETO);
        setTitle(requestIcao + " " + procedimento);

        webView = (WebView) findViewById(R.id.web_view);
        if (webView != null) {
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                    setProgress(progress * 1000);
                }
            });
            webView.loadUrl(Constants.GOOGLE_PDF + url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.charts_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            String fileName = "[" + requestIcao + "]" + procedimento.replace(" ", "_").concat(".pdf");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
        } catch (Exception e) {
            De.bug("Sem permissao para salvar...");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.clearCache(true);
        finish();
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
}