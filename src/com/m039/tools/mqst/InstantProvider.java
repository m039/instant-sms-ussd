package com.m039.tools.mqst;

import com.m039.tools.mqst.items.InstantItem;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.appwidget.AppWidgetProvider;
import android.text.Html;
import android.content.ComponentName;

/**
 * Describe class InstantProvider here.
 *
 *
 * Created: Mon Aug 29 20:07:05 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class InstantProvider extends AppWidgetProvider {
    private static final String         TAG                 = "MDBWidget";
    private static final String         ACTION_INIT_WIDGET  = "com.m039.tools.mqst.INIT_WIDGET";
    private static InstantItem          mSelectedItem;

    @Override
    public void                 onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        if (ACTION_INIT_WIDGET.equals(action)) {
            RemoteViews rview = getViews(context);

            initWidget(context, rview);
            refreshWidget(context, rview);
        }
    }

    @Override
    public void                 onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews rview = getViews(context);

        for (int appWidgetId: appWidgetIds) {
            updateWidgetClick(context, rview);

            appWidgetManager.updateAppWidget(appWidgetId, rview);
        }

        // init widget

        context.sendBroadcast(new Intent(ACTION_INIT_WIDGET));
    }

    // private

    private void                initWidget(Context context, RemoteViews rview) {
        ItemFactory ifactory = ItemFactory.getFactory(context);
        String top, bottom;

        mSelectedItem = ifactory.getSelectedItem();

        if (mSelectedItem == null) {
            top = "Instant";
            bottom = "<font color='#CCCCCC'><i>SMS|USSD</i></font>";
        } else {
            top = mSelectedItem.getHelp();
            bottom = "";
        }

        rview.setCharSequence(R.id.instant_provider_button_top, "setText",
                              Html.fromHtml(top));
        rview.setCharSequence(R.id.instant_provider_button_bottom, "setText",
                              Html.fromHtml(bottom));
    }

    private void                refreshWidget(Context context, RemoteViews rview) {
        ComponentName       widget  = new ComponentName(context.getPackageName(),
                                                        getClass().getName());
        AppWidgetManager    manager = AppWidgetManager.getInstance(context);

        for (int id: manager.getAppWidgetIds(widget)) {
            manager.updateAppWidget(id, rview);
        }
    }

    private RemoteViews         getViews(Context context) {
        RemoteViews rview;

        rview = new RemoteViews(context.getPackageName(), R.layout.instant_provider);

        return rview;
    }

    private void                updateWidgetClick(Context context, RemoteViews rview) {
        Intent intent           = new Intent(context, InstantActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent pintent   = PendingIntent.getActivity(context,
                                                            0,
                                                            intent,
                                                            PendingIntent.FLAG_UPDATE_CURRENT);

        rview.setOnClickPendingIntent(R.id.instant_provider_button_top, pintent);
        rview.setOnClickPendingIntent(R.id.instant_provider_button_bottom, pintent);
    }
}
