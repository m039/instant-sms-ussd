/** SettingsFragment.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.m039.isms.C;
import com.m039.isms.activity.AboutActivity;
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
                        Activity a = getActivity();
                        if (a != null) {
                            a.startActivity(new Intent(a, AboutActivity.class));
                        }
                        
                        return true;
                    }
                });
        }
    }

} // SettingsFragment
