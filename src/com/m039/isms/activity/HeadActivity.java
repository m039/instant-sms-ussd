/** HeadActivity.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.m039.isms.fragment.MsgListFragment;
import com.m039.mqst.R;

/**
 *
 * Created: 11/28/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class HeadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_head);

        if (savedInstanceState == null) {
            if (findViewById(R.id.f_list) != null) {
                getFragmentManager()
                    .beginTransaction()
                    .add(R.id.f_list, MsgListFragment.newInstance())
                    .commit();
            }
        }
    }


} // HeadActivity
