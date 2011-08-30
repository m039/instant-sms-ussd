package com.m039.tools.mqst;

/**
 * Describe class InstantItem here.
 *
 *
 * Created: Tue Aug 30 21:02:22 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
abstract public class InstantItem {
    private String mHelp;

    public InstantItem(String help) {
        setHelp(help);
    }
    
    public String getHelp() {
        return mHelp;
    }

    public void setHelp(String help) {
        mHelp = help;
    }
}
