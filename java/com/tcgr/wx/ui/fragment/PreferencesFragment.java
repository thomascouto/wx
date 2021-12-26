package com.tcgr.wx.ui.fragment;

import android.os.Bundle;
import me.philio.preferencecompatextended.PreferenceFragmentCompat;

import com.tcgr.wx.R;

/**
 * Fragment Preference
 *
 * Created by thomas on 30/04/16.
 */
public class PreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        getActivity().setTitle(getString(R.string.config));
        addPreferencesFromResource(R.xml.preferences);
    }
}