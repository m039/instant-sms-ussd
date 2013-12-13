/** CreateMsgActivity.java ---
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
import android.view.View;
import android.widget.Toast;

import com.m039.isms.items.Msg;
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
public class CreateMsgActivity extends BaseMsgActivity {

    public static final String TAG = "m039-CreateMsgActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_create_msg);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        // just for fun
        View buttons = findViewById(R.id.buttons);
        if (buttons != null) {
            for (Object pair[] : new Object[][] {
                    new Object[] {
                        buttons.findViewById(R.id.cancel), mOnCancelClickListener
                    },
                    new Object[] {
                        buttons.findViewById(R.id.add), mOnAddClickListener
                    }
                }) {
                View button = (View) pair[0];
                if (button != null) {
                    button.setOnClickListener((View.OnClickListener) pair[1]);
                }
            }
        }
    }

    View.OnClickListener mOnCancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

    View.OnClickListener mOnAddClickListener = new View.OnClickListener() {
            int onCreateMsgErrorString = R.string.a_create_msg__error__cannot_create_msg;

            @Override
            public void onClick(View v) {
                Msg msg = null;

                String type = getSelectedMsgType();

                if (Msg.TYPE_SMS.equals(type)) {
                    msg = createSmsMsgOrNull();
                } else if(Msg.TYPE_USSD.equals(type)) {
                    msg = createUssdMsgOrNull();
                }

                if (msg == null) {
                    Toast.makeText(v.getContext(), onCreateMsgErrorString, Toast.LENGTH_SHORT).show();
                } else {
                    Intent data = new Intent();

                    data.putExtra(EXTRA_MSG, msg);

                    setResult(RESULT_OK, data);
                    onBackPressed();
                }
            }
        };

} // CreateMsgActivity
