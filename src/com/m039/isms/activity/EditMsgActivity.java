/** EditMsgActivity.java ---
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
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.m039.isms.items.Msg;
import com.m039.isms.items.SmsMsg;
import com.m039.isms.items.UssdMsg;
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
public class EditMsgActivity extends BaseMsgActivity {

    public static final String EXTRA_MSG_ID = "com.m039.isms.activity.extra.msg_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_create_edit);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        setSelectedMsgType(getMsg().getType());

        View buttons = findViewById(R.id.buttons);
        if (buttons != null) {
            for (Object pair[] : new Object[][] {
                    new Object[] {
                        buttons.findViewById(R.id.cancel), mOnCancelClickListener
                    },
                    new Object[] {
                        buttons.findViewById(R.id.save), mOnSaveClickListener
                    }
                }) {
                View button = (View) pair[0];
                if (button != null) {
                    button.setOnClickListener((View.OnClickListener) pair[1]);
                }
            }
        }
    }

    boolean mInitializedFields = false;

    @Override
    public void onStubSelected(ViewGroup stub) {
        if (!mInitializedFields) {
            Msg msg = getMsg();

            setDescription(msg.getDescription());
            setAddress(msg.getAddress());
            setIsShowWarning(msg.isShowWarning());

            if (msg instanceof SmsMsg) {
                setMessage (((SmsMsg) msg).getMessage());
            }
            
            mInitializedFields = true;
        }
    }

    View.OnClickListener mOnCancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };

    View.OnClickListener mOnSaveClickListener = new View.OnClickListener() {
            int onCreateMsgErrorString = R.string.a_create_msg__error__cannot_create_msg;

            @Override
            public void onClick(View v) {
                Msg msg = null;

                String type = getSelectedMsgType();

                if (Msg.TYPE_SMS.equals(type)) {
                    msg = createSmsMsg();
                } else if(Msg.TYPE_USSD.equals(type)) {
                    msg = createUssdMsg();
                }

                if (msg == null) {
                    Toast.makeText(v.getContext(), onCreateMsgErrorString, Toast.LENGTH_SHORT).show();
                } else {
                    Intent data = new Intent();

                    data.putExtra(EXTRA_MSG, msg);
                    data.putExtra(EXTRA_MSG_ID, getMsgId());                   

                    setResult(RESULT_OK, data);
                    onBackPressed();
                }
            }

            UssdMsg createUssdMsg() {
                String description = getDescription();
                String address = getAddress();

                // mandatory fields
                for (String field: new String[] {
                        description, address
                    }) {
                    if (TextUtils.isEmpty(field)) {
                        return null;
                    }
                }

                boolean isShowWarning = getIsShowWarning();

                return new UssdMsg(description, address, isShowWarning);
            }

            SmsMsg createSmsMsg() {
                String description = getDescription();
                String address = getAddress();
                String message = getMessage();

                // mandatory fields
                for (String field: new String[] {
                        description, address, message
                    }) {
                    if (TextUtils.isEmpty(field)) {
                        return null;
                    }
                }

                boolean isShowWarning = getIsShowWarning();

                return new SmsMsg(description, address, isShowWarning, message);
            }
        };

    Msg getMsg() {
        return (Msg) getIntent().getExtras().getParcelable(EXTRA_MSG);
    }

    long getMsgId() {
        return getIntent().getExtras().getLong(EXTRA_MSG_ID);
    }

} // EditMsgActivity
