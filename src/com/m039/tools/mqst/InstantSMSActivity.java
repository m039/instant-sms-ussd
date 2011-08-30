package com.m039.tools.mqst;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;
import android.app.TabActivity;
import android.content.res.Resources;
import java.io.IOException;
import java.io.InputStream;
import android.widget.TextView;
import java.io.BufferedReader;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.InputStreamReader;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import org.xml.sax.Attributes;

public class InstantSMSActivity extends TabActivity
{
    private static final String TAG = "InstantSMS";

    /** Called when the activity is first created. */
    @Override
    public void         onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();

        TabHost th = getTabHost();

        th.addTab(th.newTabSpec("templates")
                  .setIndicator("", res.getDrawable(android.R.drawable.ic_menu_send))
                  .setContent(R.id.log_tv));

        th.addTab(th.newTabSpec("settings")
                  .setIndicator("", res.getDrawable(android.R.drawable.ic_menu_preferences))
                  .setContent(R.id.status_tv));

        th.setCurrentTab(0);

        updateTextView();
        parseXmlFile();
    }
    
    // for debugging purpose

    private void        setText(String text) {
        TextView tv = (TextView) findViewById(R.id.log_tv);
        tv.setText(text);       
    }

    private void        updateTextView() {
        AssetManager am = getAssets();

        try {
            InputStream in = am.open("sms_templates.xml");

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder sb = new StringBuilder();

            String line = reader.readLine();

            do {
                sb.append(line + "\n");
            } while ((line = reader.readLine()) != null);

            setText(sb.toString());

            reader.close();
        } catch (IOException e) {
            Log.d(TAG, "Something is going wrong!\n" + e.getMessage());
        }
    }

    private class MyAppHandler extends DefaultHandler {
        public StringBuilder mSBuilder = new StringBuilder();
        
        public void     startElement(String uri, String name, String qName, Attributes atts) {
            mSBuilder.append("[" + uri + "] [" + name + "] [" + qName + "]" + "\n");
            
            if (name.equals("item")) {
                mSBuilder.append("help = " + atts.getValue("help") + "\n");
            }
        }
        
        public void     endDocument() {
            setText(mSBuilder.toString());
        }
    }

    private void        parseXmlFile() {
        SAXParser parser;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        DefaultHandler handler = new MyAppHandler();

        AssetManager am = getAssets();

        try {
            InputStream is = am.open("sms_templates.xml");
            
            parser = factory.newSAXParser();
            parser.parse(is, handler);

            is.close();
            
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
