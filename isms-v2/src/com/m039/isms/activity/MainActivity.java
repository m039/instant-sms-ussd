/** MainActivity.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.activity;

import org.apache.commons.collections4.CollectionUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.m039.isms.C;
import com.m039.isms.Utils;
import com.m039.isms.db.DB;
import com.m039.mqst.InstantActivity;
import com.m039.mqst.ItemFactory;
import com.m039.mqst.R;

/**
 *
 *
 * Created: 11/20/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "m039-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        if (getAndroidApiVersion() >= 14) {
            launchNewActivity();
        } else {
            launchOldActivity();
        }
    }

    private int getAndroidApiVersion() {
        return Utils.getAndroidApiVersion();
    }

    private void launchNewActivity() {
        // Import old data
        SharedPreferences sp = getMainSharedPreferences();

        if (!sp.getBoolean(EXTRA_IS_IMPORTED_OLD_DATA, false)) {

            boolean success = true;

            if (hasOldData()) {
                success = importOldData();
            }

            sp.edit().putBoolean(EXTRA_IS_IMPORTED_OLD_DATA, success).commit();
        }

        // launch activity
        startActivity(new Intent(this, HeadActivity.class));
    }

    private void launchOldActivity() {
        startActivity(new Intent(this, InstantActivity.class));
    }

    private boolean hasOldData() {
        boolean result = false;

        result = CollectionUtils
            .isNotEmpty(ItemFactory
                     .getFactory(this)
                     .getItems());

        Log.d(TAG, "hasOldData: " + result);

        return result;
    }

    private boolean importOldData() {
        boolean result = false;

        result = DB
            .getInstance(this)
            .importOldData(ItemFactory.getFactory(this).getItems());

        Log.d(TAG, "importOldData: " + result);

        return result;
    }

    private SharedPreferences getMainSharedPreferences() {
        return getSharedPreferences(C.PACKAGE + ".sp.main", Context.MODE_PRIVATE);
    }

    public static final String EXTRA_IS_IMPORTED_OLD_DATA = C.PACKAGE + "extra.is_imported_old_data";


} // MainActivity
