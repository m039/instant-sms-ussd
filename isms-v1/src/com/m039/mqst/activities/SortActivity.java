package com.m039.mqst.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.m039.mqst.R;

/**
 * Describe class SettingsActivity here.
 *
 *
 * Created: Thu Sep 29 18:32:08 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class SortActivity extends PreferenceActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.sort_preferences);
    }
}
