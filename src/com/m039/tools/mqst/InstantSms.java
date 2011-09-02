package com.m039.tools.mqst;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

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
    private final String mAddress;
    private final String mText;
    
    public InstantSms(String help, String address, String text) {
        super(help);

        mAddress = address;
        mText = text;
    }

    public String       getType() {
        return "sms";
    }

    public String       getHint() {
        return "addr: " + mAddress + " hint: " + mText;
    }

    public void         send(Context context) {
        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(mAddress, null, mText, null, null);
        
        Toast.makeText(context, "sending sms", Toast.LENGTH_SHORT).show();
    }
}
