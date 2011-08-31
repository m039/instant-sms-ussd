package com.m039.tools.mqst;

import org.xml.sax.helpers.DefaultHandler;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import android.util.Log;
import org.xml.sax.Attributes;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * Describe class ItemFactory here.
 *
 *
 * Created: Tue Aug 30 21:13:00 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class ItemFactory extends DefaultHandler {
    private final static String TAG         = "ItemFactory";
    private final List<InstantItem> mItems  = new ArrayList<InstantItem>();
    private InstantItem mSelectedItem;

    /**
     * Check if return value is null !
     */
    public static ItemFactory           parseTemplates(Context context) {
        AssetManager am         = context.getAssets();
        ItemFactory ifactory    = null;     

        try {
            InputStream is = am.open("sms_templates.xml");

            ifactory = ItemFactory.parse(is);

            is.close();
            
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return ifactory;
    }
    
    public static ItemFactory           parse(InputStream is) {
        ItemFactory ifactory        = new ItemFactory();
        SAXParserFactory factory    = SAXParserFactory.newInstance();
        SAXParser parser;

        try {

            parser = factory.newSAXParser();
            parser.parse(is, ifactory);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return ifactory;
    }

    
    /**
     * Check return for null
     */
    public InstantItem                  getSelectedItem() {
        return mSelectedItem;
    }

    public List<InstantItem>            getItems() {
        return mItems;
    }

    @Override
    public void                         startElement(String uri,
                                                     String name,
                                                     String qName,
                                                     Attributes atts) {
        InstantItem item = null;

        if (name.equals("item")) {
            try {
                String type = atts.getValue("type");
            
                if (type.equals("sms")) {
                    item = new InstantSms(atts.getValue("help"),
                                          atts.getValue("address"),
                                          atts.getValue("text"));
                
                    mItems.add(item);
                }

                if (type.equals("ussd")) {
                    item = new InstantUssd(atts.getValue("help"),
                                           atts.getValue("text"));
                
                    mItems.add(item);
                }

                if (atts.getValue("selected").equals("true")) {
                    mSelectedItem = item;
                }

            } catch (NullPointerException e) {
                // failed if getValue() doesn't find a attribute
            }
        }
    }
}
