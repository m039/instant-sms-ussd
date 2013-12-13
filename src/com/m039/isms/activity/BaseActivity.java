/** BaseActivity.java --- 
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 * 
 */

package com.m039.isms.activity;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * 
 *
 * Created: 11/20/13
 *
 * @author Mozgin Dmitry
 * @version 
 * @since 
 */
public class BaseActivity extends Activity {

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    
} // BaseActivity
