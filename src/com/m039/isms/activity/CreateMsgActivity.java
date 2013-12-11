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
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
public class CreateMsgActivity extends BaseMsgActivity {

    public static final String TAG = "m039-CreateMsgActivity";
    public static final String EXTRA_MSG = "com.m039.isms.activity.extra.msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_create_msg);
    }

    Spinner mSpinner;
    View mSpinnerBg;

    ViewGroup mStub;
    ViewGroup mUssdStub = null;
    ViewGroup mSmsStub = null;

    static final boolean USE_COLORIZE = false; // experimental
    static final int PICK_CONTACT_REQUEST = 1;

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mStub = (ViewGroup) findViewById(R.id.stub);
        mSpinnerBg = findViewById(R.id.spinner_bg);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        if (mSpinner != null) {
            mSpinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected (AdapterView<?> parent) {
                    }

                    @Override
                    public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                        if (mStub != null) {
                            mStub.removeAllViews();

                            String type = (String) parent.getAdapter().getItem(position);

                            LayoutInflater li = getLayoutInflater();

                            if (Msg.TYPE_USSD.equalsIgnoreCase(type)) {
                                if (mUssdStub == null) {
                                    mUssdStub =
                                        (ViewGroup) li.inflate(R.layout.i_create_msg__holder__ussd, mStub, false);

                                    setOnPickUserClickListener(mUssdStub);
                                }

                                mStub.addView(mUssdStub);

                                if (mSpinnerBg != null && USE_COLORIZE) {
                                    mSpinnerBg.setBackgroundResource(R.drawable.a_create_msg__spinner_bg__blue);
                                }

                            } else if(Msg.TYPE_SMS.equalsIgnoreCase(type)) {
                                if (mSmsStub == null) {
                                    mSmsStub =
                                        (ViewGroup) li.inflate(R.layout.i_create_msg__holder__sms, mStub, false);

                                    setOnPickUserClickListener(mSmsStub);
                                }

                                mStub.addView(mSmsStub);

                                if (mSpinnerBg != null  && USE_COLORIZE) {
                                    mSpinnerBg.setBackgroundResource(R.drawable.a_create_msg__spinner_bg__green);
                                }
                            }
                        }
                    }

                    void setOnPickUserClickListener(View stub) {
                        View pickUser = stub.findViewById(R.id.pick_user);
                        if (pickUser != null) {
                            pickUser.setOnClickListener(mOnPickUserClickListener);
                        }
                    }
                });
        }

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
                    msg = createSmsMsg();
                } else if(Msg.TYPE_USSD.equals(type)) {
                    msg = createUssdMsg();
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

    View.OnClickListener mOnPickUserClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
                                       PICK_CONTACT_REQUEST);
            }
        };

    private String getSelectedMsgType() {
        if (mSpinner != null) {
            String type = (String) mSpinner.getSelectedItem();

            if (Msg.TYPE_SMS.equalsIgnoreCase(type)) {
                return Msg.TYPE_SMS;
            } else if(Msg.TYPE_USSD.equalsIgnoreCase(type)) {
                return Msg.TYPE_USSD;
            }
        }

        return Msg.TYPE_NONE;
    }

    private String getDescription() {
        EditText description;

        if (mStub != null &&
            (description = (EditText) mStub.findViewById(R.id.description)) != null) {

            return description.getText().toString();
        }

        return "";
    }

    private String getMessage() {
        EditText message;

        if (mStub != null &&
            (message = (EditText) mStub.findViewById(R.id.message)) != null) {

            return message.getText().toString();
        }

        return "";
    }

    private String getAddress() {
        EditText address;

        if (mStub != null &&
            (address = (EditText) mStub.findViewById(R.id.address)) != null) {

            return address.getText().toString();
        }

        return "";
    }

    private boolean getIsShowWarning() {
        CheckBox isShowWarning;

        if (mStub != null &&
            (isShowWarning = (CheckBox) mStub.findViewById(R.id.warning__cost_money__checkbox)) != null) {

            return isShowWarning.isChecked();
        }

        return false;
    }

    @Override
    protected void      onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case PICK_CONTACT_REQUEST:
            if (resultCode == RESULT_OK) {
                Cursor cur = managedQuery(data.getData(), null, null, null, null);

                if (cur != null) {
                    try {
                        if (cur.moveToFirst()) {
                            String contactId = cur
                                .getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            int hasPhone = cur
                                .getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                            if (hasPhone == 1) {

                                Cursor phones = getContentResolver()
                                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                           null,
                                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                           new String[] { contactId },
                                           null);

                                if (phones != null) {
                                    try {
                                        if (phones.moveToFirst()) {
                                            onUserPickResult(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                                        }

                                    } finally {
                                        phones.close();
                                    }
                                }

                            } else {
                                Toast.makeText(this, R.string.contact_hasnt_phone, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (IllegalArgumentException  e) {
                        Log.e(TAG, e.getMessage());
                    } finally {
                        stopManagingCursor(cur); // necessary to close cursor
                        cur.close();
                    }
                }
            }
            break;
        default:
            break;
        }
    }

    private void onUserPickResult(String phone) {
        EditText address;
        if (phone != null &&
            mStub != null &&
            (address = (EditText) mStub.findViewById(R.id.address)) != null) {
            address.setText(phone);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSpinner = null;
        mStub = null;
        mUssdStub = null;
        mSmsStub = null;
    }

} // CreateMsgActivity
