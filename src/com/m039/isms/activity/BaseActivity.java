/** BaseActivity.java --- 
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 * 
 */

package com.m039.isms.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

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
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
        }

        super.onCreate(savedInstanceState);     
    }

} // BaseActivity
