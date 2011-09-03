package com.m039.mqst.items;

import android.content.Intent;
import android.net.Uri;
import android.content.Context;
import android.util.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * Describe class InstantUssd here.
 *
 *
 * Created: Wed Aug 31 21:56:15 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class InstantUssd extends InstantItem {
    private final String mText;

    public InstantUssd(String help, String text) {
        super(help);

        mText = text;
    }

    public String       getText() {
        return mText;
    }
    
    public String       getType() {
        return "ussd";
    }

    public String       getHint() {
        return "hint: " + mText;
    }

    public void         send(Context context) {
        try {
            String encodedHash = Uri.encode("#");
            String ussd = mText.replace("#", encodedHash);
        
            context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd)));
        } catch (Exception e) {
            Log.e("InstantItem", "send failed");            
        }
    }

    public Element      createElement(Document doc) {
        Element el = doc.createElement("item");

        el.setAttribute("help", getHelp());
        el.setAttribute("type", getType());
        el.setAttribute("text", getText());

        return el;
    }   
}
