package com.m039.tools.mqst;

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
    
    InstantUssd(String help, String text) {
        super(help);

        mText = text;
    }   

    public String       getType() {
        return "ussd";
    }

    public String       getHint() {
        return "hint: " + mText;
    }
}
