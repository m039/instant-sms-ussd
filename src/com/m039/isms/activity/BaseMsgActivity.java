/** BaseMsgActivity.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.activity;

import com.m039.mqst.R;

/**
 *
 *
 * Created: 12/06/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class BaseMsgActivity extends BaseActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.head_activity_enter, R.anim.msg_activity_exit);
    }

} // BaseMsgActivity
