package com.m039.tools.mqst;

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
    
    InstantSms(String help, String address, String text) {
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
}
