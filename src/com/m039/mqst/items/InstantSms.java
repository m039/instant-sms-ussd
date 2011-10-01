package com.m039.mqst.items;

import com.m039.mqst.R;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;
import android.util.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import android.content.BroadcastReceiver;
import android.app.Activity;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Describe class InstantSms here.
 *
 *
 * Created: Tue Aug 30 21:03:06 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class InstantSms extends InstantItem {
    private static final String         ACTION_SMS_SENT = "com.m039.mqst.items.SMS_SENT";

    private static BroadcastReceiver    mReceiver;
    private final String                mAddress;
    private final String                mText;
    private final Boolean               mWarning;

    public InstantSms(String help,
                      String address,
                      String text,
                      Boolean warning) {
        super(help);

        mAddress = address;
        mText = text;
        mWarning = warning;
    }

    public String       getAddress() {
        return mAddress;
    }

    public Boolean      getWarning() {
        return mWarning;
    }

    public String       getText() {
        return mText;
    }

    public String       getType() {
        return "sms";
    }

    public String       getHint() {
        return "addr: " + mAddress + " hint: " + mText;
    }

    private Context     mContext;
    
    public void         send(Context context) {
        mContext = context;
        
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                    // almost taken from API DEMOS
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int message = R.string.sms_sent_ok;
                        boolean error = true;

                        switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            message = R.string.sms_sent_ok;
                            error = false;
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            message = R.string.sms_sent_generic_failure;
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            message = R.string.sms_sent_no_service;
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            message = R.string.sms_sent_null_pdu;
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            message = R.string.sms_sent_radio_off;
                            break;
                        }

                        Toast.makeText(context,
                                       message,
                                       error? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                    }
                };

            mContext.registerReceiver(mReceiver, new IntentFilter(ACTION_SMS_SENT));
        }

        if (getWarning()) {
            checkUserDesire();
        } else {
            sendSMS();
        }
    }

    private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    sendSMS();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
                }
            }
        };

    private void        sendSMS() {
        try {
            SmsManager sms          = SmsManager.getDefault();
            PendingIntent pintent   = PendingIntent.getBroadcast(mContext,
                                                                 0,
                                                                 new Intent(ACTION_SMS_SENT),
                                                                 PendingIntent.FLAG_ONE_SHOT);

            sms.sendTextMessage(mAddress, null, mText, pintent, null);

            Toast.makeText(mContext, R.string.sms_sending, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("InstantItem", "send failed");
        }   
    }

    private void     checkUserDesire() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.sms_sending_warning)
            .setPositiveButton(R.string.yes_button, mDialogListener)
            .setNegativeButton(R.string.no_button, mDialogListener).show();

    }

    public Element      createElement(Document doc) {
        Element el = doc.createElement("item");

        el.setAttribute("help", getHelp());
        el.setAttribute("type", getType());
        el.setAttribute("address", getAddress());
        el.setAttribute("text", getText());
        el.setAttribute("warning", getWarning().toString());

        return el;
    }
}
