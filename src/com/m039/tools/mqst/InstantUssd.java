package com.m039.tools.mqst;

import android.content.Intent;
import android.net.Uri;
import android.content.Context;

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

    public String       getType() {
        return "ussd";
    }

    public String       getHint() {
        return "hint: *#" + mText + "#";
    }

    public void         send(Context context) {
        String encodedHash = Uri.encode("#");
        String ussd = "*" + encodedHash + mText + encodedHash;
        
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussd)));
    }
}
