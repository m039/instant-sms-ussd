package com.m039.mqst.items;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;
import android.util.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

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
        try {
            SmsManager sms = SmsManager.getDefault();

            sms.sendTextMessage(mAddress, null, mText, null, null);
        
            Toast.makeText(context, "sending sms", Toast.LENGTH_SHORT).show();
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
