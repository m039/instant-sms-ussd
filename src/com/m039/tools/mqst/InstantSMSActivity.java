package com.m039.tools.mqst;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;
import com.m039.tools.mqst.tabs.TemplatesTab;
import com.m039.tools.mqst.tabs.CreationTab;
import android.view.Menu;
import android.view.MenuInflater;

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
                  .setContent(new Intent(this, TemplatesTab.class)));

        th.addTab(th.newTabSpec("settings")
                  .setIndicator("", res.getDrawable(android.R.drawable.ic_menu_preferences))
                  .setContent(R.id.status_tv));

        th.setCurrentTab(0);

        updateTextView();
        parseXmlFile();
    }

    // for debugging purpose

    private void        setText(String text) {
        TextView tv = (TextView) findViewById(R.id.status_tv);
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

    private void        parseXmlFile() {
        ItemFactory ifactory = ItemFactory.getFactory(this);

        setText("The size is " + ifactory.getItems().size() + "\n");

    }
}
