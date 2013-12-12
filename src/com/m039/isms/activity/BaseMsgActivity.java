/** BaseMsgActivity.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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
public class BaseMsgActivity extends BaseActivity {

    protected Spinner mSpinner;
    protected View mSpinnerBg;

    protected ViewGroup mStub;
    protected ViewGroup mUssdStub = null;
    protected ViewGroup mSmsStub = null;

    static final protected int PICK_CONTACT_REQUEST = 1;
    static final protected boolean USE_COLORIZE = false; // experimental

    public static final String EXTRA_MSG = "com.m039.isms.activity.extra.msg";

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.head_activity_enter, R.anim.msg_activity_exit);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mStub = (ViewGroup) findViewById(R.id.stub);
        mSpinnerBg = findViewById(R.id.spinner_bg);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        if (mSpinner != null) {
            mSpinner.setOnItemSelectedListener(mOnSpinnerItemSelectedListener);
        }
    }

    protected void onStubSelected(ViewGroup stub) {
    }

    AdapterView.OnItemSelectedListener mOnSpinnerItemSelectedListener =
        new AdapterView.OnItemSelectedListener() {

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
                        onStubSelected(mUssdStub);                      

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
                        onStubSelected(mSmsStub);

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
        };

    View.OnClickListener mOnPickUserClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
                                       PICK_CONTACT_REQUEST);
            }
        };

    protected void setSelectedMsgType(final String msgType) {
        if (mSpinner != null) {
            int count = mSpinner.getCount();

            for(int i = 0; i < count; i++) {
                if (msgType.equalsIgnoreCase((String) mSpinner.getItemAtPosition(i))) {
                    mSpinner.setSelection(i);
                }
            }
        }
    }

    protected String getSelectedMsgType() {
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

    protected void setDescription(final String value) {
        final EditText description;

        if ( mStub != null &&
            (description = (EditText) mStub.findViewById(R.id.description)) != null) {

            description.setText(value);
        }
    }
    
    protected String getDescription() {
        EditText description;

        if (mStub != null &&
            (description = (EditText) mStub.findViewById(R.id.description)) != null) {

            return description.getText().toString();
        }

        return "";
    }

    protected void setMessage(String value) {
        EditText message;

        if (mStub != null &&
            (message = (EditText) mStub.findViewById(R.id.message)) != null) {

            message.setText(value);
        }
    }

    protected String getMessage() {
        EditText message;

        if (mStub != null &&
            (message = (EditText) mStub.findViewById(R.id.message)) != null) {

            return message.getText().toString();
        }

        return "";
    }

    protected void setAddress(String value) {
        EditText address;

        if (mStub != null &&
            (address = (EditText) mStub.findViewById(R.id.address)) != null) {

            address.setText(value);
        }
    }
    
    protected String getAddress() {
        EditText address;

        if (mStub != null &&
            (address = (EditText) mStub.findViewById(R.id.address)) != null) {

            return address.getText().toString();
        }

        return "";
    }

    protected void setIsShowWarning(boolean value) {
        CheckBox isShowWarning;

        if (mStub != null &&
            (isShowWarning = (CheckBox) mStub.findViewById(R.id.warning__cost_money__checkbox)) != null) {

            isShowWarning.setChecked(value);
        }
    }
    
    protected boolean getIsShowWarning() {
        CheckBox isShowWarning;

        if (mStub != null &&
            (isShowWarning = (CheckBox) mStub.findViewById(R.id.warning__cost_money__checkbox)) != null) {

            return isShowWarning.isChecked();
        }

        return false;
    }

} // BaseMsgActivity
