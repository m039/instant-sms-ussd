/** BaseApplication.java --- 
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 * 
 */

package com.m039.isms.app;

import android.app.Application;
import android.preference.PreferenceManager;

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
public class BaseApplication extends Application {

    public static final String TAG = "m039-BaseApplication";
    
    @Override
    public void onCreate() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
    
} // BaseApplication
