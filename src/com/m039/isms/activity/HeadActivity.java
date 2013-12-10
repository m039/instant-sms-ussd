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
import android.widget.Toast;

import com.m039.isms.fragment.MsgListFragment;
import com.m039.isms.items.Msg;
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

    static final int REQUEST_CREATE_MSG = 1;
    static final int REQUEST_EDIT_MSG = 2;

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
        startActivityForResult(new Intent(this, CreateMsgActivity.class), REQUEST_CREATE_MSG);
        overridePendingTransition(R.anim.msg_activity_enter, R.anim.head_activity_exit);
    }

    @Override
    protected void      onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case REQUEST_CREATE_MSG:

                Msg msg = (Msg) data.getParcelableExtra(CreateMsgActivity.EXTRA_MSG);
                
                Toast.makeText(this, "Created: " + msg, Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
            }

        }
    }

} // HeadActivity
