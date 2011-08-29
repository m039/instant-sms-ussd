package com.m039.tools.mqst;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;
import android.app.TabActivity;
import android.content.res.Resources;

public class InstantSMSActivity extends TabActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    }
}
