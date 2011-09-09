package com.m039.mqst.items;

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

    public InstantSms(String help, String address, String text) {
        super(help);

        mAddress = address;
        mText = text;
    }

    public String       getAddress() {
        return mAddress;
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

    public void         send(Context context) {
        if (mReceiver == null) {
            mReceiver = new BroadcastReceiver() {
                    // almost taken from API DEMOS
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String message = null;
                        boolean error = true;

                        switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            message = "The SMS has been sent";
                            error = false;
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            message = "Error: The SMS hasn't been sent";
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            message = "Error: No service.";
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            message = "Error: Null PDU.";
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            message = "Error: Radio off.";
                            break;
                        }

                        Toast.makeText(context,
                                       message,
                                       error? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                    }
                };

            context.registerReceiver(mReceiver, new IntentFilter(ACTION_SMS_SENT));
        }

        try {
            SmsManager sms          = SmsManager.getDefault();
            PendingIntent pintent   = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_SMS_SENT), 0);

            sms.sendTextMessage(mAddress, null, mText, pintent, null);

            Toast.makeText(context, "Sending SMS", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("InstantItem", "send failed");
        }
    }

    public Element      createElement(Document doc) {
        Element el = doc.createElement("item");

        el.setAttribute("help", getHelp());
        el.setAttribute("type", getType());
        el.setAttribute("address", getAddress());
        el.setAttribute("text", getText());

        return el;
    }
}
