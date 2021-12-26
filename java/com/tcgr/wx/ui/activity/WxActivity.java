package com.tcgr.wx.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.tcgr.wx.R;
import com.tcgr.wx.core.App;
import com.tcgr.wx.core.db.DatabaseManager;
import com.tcgr.wx.core.interfaces.Constants;
import com.tcgr.wx.core.interfaces.CountListener;
import com.tcgr.wx.ui.fragment.ChartsFragment;
import com.tcgr.wx.ui.fragment.MetarFragment;
import com.tcgr.wx.ui.fragment.NotamFragment;
import com.tcgr.wx.ui.fragment.PreferencesFragment;
import com.tcgr.wx.ui.fragment.RotaerFragment;
import com.tcgr.wx.ui.fragment.SunFragment;
import com.tcgr.wx.ui.fragment.TabsFragment;
import com.tcgr.wx.ui.fragment.TafFragment;

import io.fabric.sdk.android.Fabric;

/**
 * Main Activity.
 *
 * @author thomas
 * @see AppCompatActivity
 */
public class WxActivity extends AppCompatActivity implements CountListener, Drawer.OnDrawerItemClickListener {

    private ChartsFragment chartsFragment;
    private MetarFragment metarFragment;
    private TafFragment tafFragment;
    private NotamFragment notamFragment;
    private SunFragment sunFragment;
    private RotaerFragment rotaerFragment;
    private App application;
    private AccountHeader headerResult;
    private Drawer result;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        setContentView(R.layout.activity_sample_dark_toolbar_new);

        //Loading default values for first launch...
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        application = ((App) getApplication());
        DatabaseManager.initializeInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final IProfile profile = new ProfileDrawerItem().withName(getResources().getString(R.string.title_activity_main));

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.radialbackground)
                .addProfiles(profile)
                .withCompactStyle(true)
                .withOnAccountHeaderProfileImageListener(listener)
                .withSavedInstance(savedInstanceState)
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        assert toolbar != null;
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withHasStableIds(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.home)).withIcon(R.drawable.ic_home_black_24dp).withIdentifier(Constants.HOME_ID).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.metar).withIcon(R.drawable.ic_cloud_queue_black_24dp).withIdentifier(Constants.METAR_ID).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.taf).withIcon(R.drawable.ic_cloud_queue_black_24dp).withIdentifier(Constants.TAF_ID).withSelectable(true),
                        new SectionDrawerItem().withName(R.string.ad),
                        new PrimaryDrawerItem().withName(R.string.notam).withIcon(R.drawable.ic_playlist_add_check_black_24dp).withIdentifier(Constants.NOTAM_ID).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.rotaer).withIcon(R.drawable.ic_content_copy_black_24dp).withIdentifier(Constants.ROTAER_ID).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.cartas).withIcon(R.drawable.ic_map_black_24dp).withIdentifier(Constants.CHARTS_ID).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.sunrise_sunset).withIcon(R.drawable.ic_brightness_4_black_24dp).withIdentifier(Constants.SUNSET_ID).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.fpl).withIcon(R.drawable.ic_assignment_black_24dp).withIdentifier(Constants.FPLAN_ID).withSelectable(false),
                        new SectionDrawerItem().withName(R.string.config),
                        new PrimaryDrawerItem().withName(R.string.config).withIcon(R.drawable.ic_settings_black_24dp).withIdentifier(Constants.CONFIG_ID).withSelectable(true),
                        new SectionDrawerItem().withName(R.string.contato),
                        new PrimaryDrawerItem().withName(R.string.email).withIcon(R.drawable.ic_email_black_24dp).withIdentifier(Constants.MAIL_ID).withSelectable(false))
                .withSavedInstance(savedInstanceState)
                .withOnDrawerItemClickListener(this)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new TabsFragment()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void countItem(int id, int count) {
        result.updateBadge(id, new StringHolder(count + ""));
    }

    private AccountHeader.OnAccountHeaderProfileImageListener listener = new AccountHeader.OnAccountHeaderProfileImageListener() {
        @Override
        public boolean onProfileImageClick(View view, IProfile profile, boolean current) {

            Intent i = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(i);
            return true;
        }

        @Override
        public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
            return false;
        }
    };

    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.action_search);
        if (menuItem != null) {
            if (application.isOnline()) {
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            } else {
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            boolean onLine = (netInfo != null) && netInfo.isConnectedOrConnecting();
            application.setIsOnline(onLine);

            if (menuItem != null) {
                if (onLine) {
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                } else {
                    menuItem.collapseActionView();
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                }
            }
        }
    };

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        Fragment mFragment = null;
        int selectedItem = (int) drawerItem.getIdentifier();

        switch (selectedItem) {
            case Constants.HOME_ID:
                mFragment = new TabsFragment();
                break;

            case Constants.METAR_ID:
                if (metarFragment == null) {
                    metarFragment = new MetarFragment();
                    metarFragment.setListener(WxActivity.this);
                }

                mFragment = metarFragment;
                break;

            case Constants.TAF_ID:
                if (tafFragment == null) {
                    tafFragment = new TafFragment();
                    tafFragment.setListener(WxActivity.this);
                }

                mFragment = tafFragment;
                break;

            case Constants.NOTAM_ID:
                if (notamFragment == null) {
                    notamFragment = new NotamFragment();
                    notamFragment.setListener(WxActivity.this);
                }

                mFragment = notamFragment;
                break;

            case Constants.ROTAER_ID:
                mFragment = (rotaerFragment == null) ? rotaerFragment = new RotaerFragment() : rotaerFragment;
                break;

            case Constants.CHARTS_ID:
                if (chartsFragment == null) {
                    chartsFragment = new ChartsFragment();
                    chartsFragment.setListener(WxActivity.this);
                }
                mFragment = chartsFragment;
                break;

            case Constants.SUNSET_ID:
                mFragment = (sunFragment == null) ? sunFragment = new SunFragment() : sunFragment;
                break;

            case Constants.CONFIG_ID:
                mFragment = new PreferencesFragment();
                break;

            case Constants.MAIL_ID:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + getString(R.string.myMail)));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_activity_main));
                startActivity(intent);
                break;

            default:
                Toast.makeText(getApplicationContext(), getString(R.string.breve), Toast.LENGTH_LONG).show();
                break;
        }

            if (mFragment != null) {
                FragmentManager mFragmentManager = getSupportFragmentManager();
                mFragmentManager.beginTransaction().replace(R.id.frame_container, mFragment).commit();
            }

        return false;
    }
}