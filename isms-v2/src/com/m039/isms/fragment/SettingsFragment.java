/** SettingsFragment.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.m039.isms.C;
import com.m039.mqst.R;

/**
 *
 *
 * Created: 12/19/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class SettingsFragment extends PreferenceFragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference p = findPreference(C.Preferences.Key.ABOUT);
        if (p != null) {
            p.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick (Preference preference) {
                        Toast.makeText(getActivity(), "On About click", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
        }
    }

} // SettingsFragment
