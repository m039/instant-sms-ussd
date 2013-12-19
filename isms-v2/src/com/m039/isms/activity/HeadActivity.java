/** HeadActivity.java ---
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
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.m039.isms.C;
import com.m039.isms.db.DB;
import com.m039.isms.fragment.GreetOldFriendDialogFragment;
import com.m039.isms.fragment.MsgListFragment;
import com.m039.isms.fragment.MsgPickActionsDialogFragment;
import com.m039.isms.fragment.MsgShowSendWarningDialogFragment;
import com.m039.isms.fragment.SettingsFragment;
import com.m039.isms.items.Msg;
import com.m039.isms.items.SmsMsg;
import com.m039.isms.items.UssdMsg;
import com.m039.mqst.R;
import com.m039.mqst.items.InstantSms;

/**
 *
 * Created: 11/28/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class HeadActivity extends BaseActivity
    implements MsgListFragment.OnMsgLongClickListener,
               MsgListFragment.OnMsgListItemClickListener,
               MsgPickActionsDialogFragment.OnMsgPickActionsListener,
               MsgShowSendWarningDialogFragment.OnSendMsgListener
{
    static final String TAG = "m039-HeadActivity";

    static final String ACTION_SMS_SENT = InstantSms.ACTION_SMS_SENT;

    static final int REQUEST_CREATE_MSG = 1;
    static final int REQUEST_EDIT_MSG = 2;

    DrawerLayout mDrawerLayout = null;
    ActionBarDrawerToggle mDrawerToggle = null;

    AdView mAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_head);

        if (savedInstanceState == null) {

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            // list
            if (findViewById(R.id.f_list) != null) {
                ft.add(R.id.f_list, MsgListFragment.newInstance());
            }

            // settings
            if (findViewById(R.id.f_drawer) != null) {
                ft.add(R.id.f_drawer, SettingsFragment.newInstance());
            }

            if (!ft.isEmpty()) {
                ft.commit();
            }
        }

        onCreateDrawer();
        onCreateAd();

        GreetOldFriendDialogFragment.showGreetIfNeccesary(this);
    }

    private void onCreateDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerToggle =
                new ActionBarDrawerToggle(
                                          this,
                                          mDrawerLayout,
                                          R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                                          R.string.a_head__action_bar__settings,  /* "open drawer" description */
                                          0  /* "close drawer" description */
                                          ) {

                    public void onDrawerClosed(View view) {
                        ActionBar ab = getActionBar();
                        if (ab != null) {
                            ab.setTitle(null);
                        }

                        HeadActivity.this.onDrawerClosed();
                    }

                    public void onDrawerOpened(View drawerView) {
                        ActionBar ab = getActionBar();
                        if (ab != null) {
                            ab.setTitle(R.string.a_head__action_bar__settings);
                        }
                    }
                };

            mDrawerLayout.setDrawerListener(mDrawerToggle);

            ActionBar ab = getActionBar();
            if (ab != null) {
                ab.setTitle(null);
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeButtonEnabled(true);
                ab.setDisplayShowTitleEnabled(true);
            }
        }
    }

    private void onCreateAd() {
        mAd = (AdView) findViewById(R.id.ad);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }

        syncAdState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        registerReceiver(mActionsSmsSentReceiver, new IntentFilter(ACTION_SMS_SENT));
    }

    @Override
    public void onStop() {
        super.onStop();

        unregisterReceiver(mActionsSmsSentReceiver);
    }

    @Override
    protected void onDestroy() {
        if (mAd != null) {
            mAd.destroy();
        }

        super.onDestroy();

        mAd = null;
        mDrawerLayout = null;
        mDrawerToggle = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.head_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
        case R.id.action_new:
            onActionNewOptionClicked();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void onActionNewOptionClicked() {
        startActivityForResult(new Intent(this, CreateMsgActivity.class), REQUEST_CREATE_MSG);
        overridePendingTransition(R.anim.msg_activity_enter, R.anim.head_activity_exit);
    }

    private void onDrawerClosed() {
        syncAdState();
        syncListState();
    }

    private void syncListState() {
        MsgListFragment mlf = getMsgListFragment();
        if (mlf != null) {
            mlf.requery();
        }
    }
    
    private void syncAdState() {
        if (mAd != null) {
            if (PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(C.Preferences.Key.IS_SHOW_BANNER, false)) {

                AdRequest adRequest = new AdRequest();
                adRequest.addTestDevice("2E3072A5A0253BC66B3E0B419F8D0E85");
                mAd.loadAd(adRequest);
                mAd.setVisibility(View.VISIBLE);

            } else {
                mAd.setVisibility(View.GONE);
            }
        }
    }

    //
    // MsgListFragment listeners
    //

    @Override
    public boolean onMsgLongClick (AdapterView<?> parent, View view, int position, long id) {
        MsgPickActionsDialogFragment
            .newInstance(id)
            .show(getFragmentManager(), "MsgPickActionsDialogFragment");

        return true;
    }

    @Override
    public void onMsgListItemClick(ListView l, View v, int position, long id) {
        Msg msg = DB.getInstance(this).getMsgOrNull(id);

        if (msg.isShowWarning()) {
            MsgShowSendWarningDialogFragment
                .newInstance(msg)
                .show(getFragmentManager(), "MsgShowSendWarningDialogFragment");

        } else {
            onSendMsg(msg);
        }
    }

    @Override
    public void onSendMsg(Msg msg) {
        if (msg instanceof UssdMsg) {
            sendUssd((UssdMsg) msg);
        } else if(msg instanceof SmsMsg) {
            sendSms((SmsMsg) msg);
        }
    }

    private void sendUssd(UssdMsg ussdMsg) {
        try {
            String encodedHash = Uri.encode("#");
            String ussd = ussdMsg.getAddress().replace("#", encodedHash);

            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd)));
        } catch (Exception e) {
            Log.e(TAG, "send failed");
        }
    }

    private void sendSms(SmsMsg smsMsg) {
        try {
            PendingIntent pintent = PendingIntent
                .getBroadcast(this,
                              0,
                              new Intent(ACTION_SMS_SENT),
                              PendingIntent.FLAG_ONE_SHOT);

            SmsManager
                .getDefault()
                .sendTextMessage(smsMsg.getAddress(), null, smsMsg.getMessage(), pintent, null);

            Toast.makeText(this, R.string.a_head__sms_sending, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("InstantItem", "send failed");
        }
    }

    //
    // MsgPickActionsDialogFragment listener
    //

    @Override
    public void onMsgPickActiondDelete(long msgId) {
        MsgListFragment mlf = getMsgListFragment();
        if (mlf != null) {
            mlf.deleteMsgForce(msgId);
        }
    }

    @Override
    public void onMsgPickActiondEdit(long msgId) {
        Intent intent = new Intent(this, EditMsgActivity.class);

        intent.putExtra(EditMsgActivity.EXTRA_MSG_ID, msgId);
        intent.putExtra(EditMsgActivity.EXTRA_MSG, DB.getInstance(this).getMsgOrNull(msgId));

        startActivityForResult(intent, REQUEST_EDIT_MSG);
        overridePendingTransition(R.anim.msg_activity_enter, R.anim.head_activity_exit);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case REQUEST_CREATE_MSG: {
                MsgListFragment mlf = getMsgListFragment();
                if (mlf != null) {
                    mlf.insertMsg((Msg) data.getParcelableExtra(CreateMsgActivity.EXTRA_MSG));
                }
            }
                break;

            case REQUEST_EDIT_MSG: {
                MsgListFragment mlf = getMsgListFragment();
                if (mlf != null) {
                    mlf.updateMsg(data.getLongExtra(EditMsgActivity.EXTRA_MSG_ID, -1L),
                                  (Msg) data.getParcelableExtra(EditMsgActivity.EXTRA_MSG));
                }
            }
                break;

            default:
                break;
            }
        }
    }

    BroadcastReceiver mActionsSmsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int message = R.string.a_head__sms_sent_ok;
                boolean error = true;

                switch (getResultCode()) {
                case Activity.RESULT_OK:
                    message = R.string.a_head__sms_sent_ok;
                    error = false;
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    message = R.string.a_head__sms_sent_generic_failure;
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    message = R.string.a_head__sms_sent_no_service;
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    message = R.string.a_head__sms_sent_null_pdu;
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    message = R.string.a_head__sms_sent_radio_off;
                    break;
                }

                Toast.makeText(context, message, error? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
            }

        };

    private MsgListFragment getMsgListFragment() {
        return (MsgListFragment) getFragmentManager().findFragmentById(R.id.f_list);
    }

} // HeadActivity
