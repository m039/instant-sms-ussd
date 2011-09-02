package com.m039.tools.mqst;

import android.content.Context;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

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
    
    public String               getHelp() {
        return mHelp;
    }

    public void                 setHelp(String help) {
        mHelp = help;
    }

    abstract public String      getHint();
    abstract public String      getType();
    abstract public void        send(Context context);
    abstract public Element     createElement(Document doc);
}
