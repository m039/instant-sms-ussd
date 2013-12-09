/** HeadActivity.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.head_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_new:
            onActionNewClicked();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void onActionNewClicked() {
        startActivity(new Intent(this, CreateMsgActivity.class));
        overridePendingTransition(R.anim.msg_activity_enter, R.anim.head_activity_exit);
    }

} // HeadActivity
