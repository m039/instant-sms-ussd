/** DB.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.db;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.m039.isms.items.Msg;
import com.m039.isms.items.SmsMsg;
import com.m039.isms.items.UssdMsg;
import com.m039.mqst.items.InstantItem;
import com.m039.mqst.items.InstantSms;
import com.m039.mqst.items.InstantUssd;

/**
 *
 *
 * Created: 11/28/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class DB {

    public static final String TAG = "m039-DB";

    private static DB sDB = null;

    public static DB getInstance(Context ctx) {
        if (sDB == null) {
            sDB = new DB(ctx);
        }

        return sDB;
    }

    public class DBHelper extends SQLiteOpenHelper {
        public static final int DB_VERSION = 2;
        public static final String DB_NAME = "isms.db";

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Msg.SQL.TABLE + " (" +
                       Msg.SQL.Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                       Msg.SQL.Columns.TYPE + " TEXT," +
                       Msg.SQL.Columns.DESCRIPTION + " TEXT," +
                       Msg.SQL.Columns.MESSAGE + " TEXT," +
                       Msg.SQL.Columns.IS_SHOW_WARNING + " INTEGER, " +
                       Msg.SQL.Columns.ADDRESS + " TEXT" +
                       " )");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // unused
        }

    }

    DBHelper mDBHelper;

    private DB(Context ctx) {
        mDBHelper = new DBHelper(ctx);
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }

    public Msg getMsgOrNull(long id) {
        Msg msg = null;

        Cursor c = mDBHelper
            .getReadableDatabase()
            .query(Msg.SQL.TABLE,
                   null,
                   Msg.SQL.Columns.ID + " = ?",
                   new String[] { String.valueOf(id) },
                   null,
                   null,
                   null);

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    String type = c.getString(c.getColumnIndex(Msg.SQL.Columns.TYPE));
                    String description = c.getString(c.getColumnIndex(Msg.SQL.Columns.DESCRIPTION));
                    String address = c.getString(c.getColumnIndex(Msg.SQL.Columns.ADDRESS));
                    boolean isShowWarning = c.getInt(c.getColumnIndex(Msg.SQL.Columns.IS_SHOW_WARNING)) == 1;
                    String message = c.getString(c.getColumnIndex(Msg.SQL.Columns.MESSAGE));

                    if (Msg.TYPE_USSD.equals(type)) {
                        return new UssdMsg(description, address, isShowWarning);
                    } else if(Msg.TYPE_SMS.equals(type)) {
                        return new SmsMsg(description, address, isShowWarning, message);
                    }
                }
            } finally {
                c.close();
            }
        }

        return msg;
    }

    public static ContentValues values(Msg msg) {
        ContentValues values = new ContentValues();

        values.put(Msg.SQL.Columns.TYPE, msg.getType());
        values.put(Msg.SQL.Columns.DESCRIPTION, msg.getDescription());
        values.put(Msg.SQL.Columns.ADDRESS, msg.getAddress());
        values.put(Msg.SQL.Columns.IS_SHOW_WARNING, msg.isShowWarning() ? 1 : 0 );

        if (msg instanceof UssdMsg) {
        } else if(msg instanceof SmsMsg) {
            values.put(Msg.SQL.Columns.MESSAGE, ((SmsMsg) msg).getMessage());
        }

        return values;
    }

    public boolean importOldData(List<InstantItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            return true;
        }

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String type = "", description = "", message = "", address = "";
        boolean isShowWarning = false;

        for (InstantItem item : items) {

            description = item.getHelp();

            if (item instanceof InstantSms) {
                InstantSms isms = (InstantSms) item;

                type = Msg.TYPE_SMS;
                message = isms.getText();
                isShowWarning = Boolean.TRUE.equals(isms.getWarning());
                address = isms.getAddress();

            } else if(item instanceof InstantUssd) {
                InstantUssd iussd = (InstantUssd) item;

                type = Msg.TYPE_USSD;
                message = iussd.getText();
                isShowWarning = false;
                address = "";
            }

            if (!importOldInstantItem(db, values, type, description, message, address, isShowWarning)) {
                return false;
            }

        }

        return true;
    }

    private boolean importOldInstantItem(SQLiteDatabase db,
                                         ContentValues values,
                                         String type,
                                         String description,
                                         String message,
                                         String address,
                                         boolean isShowWarning) {
        Log.d(TAG, String.format("t: %s, d: %s, m: %s, a: %s, show: %s",
                                 type, description, message, address, isShowWarning));

        values.clear();

        values.put(Msg.SQL.Columns.TYPE, type);
        values.put(Msg.SQL.Columns.DESCRIPTION, description);
        values.put(Msg.SQL.Columns.MESSAGE, message);
        values.put(Msg.SQL.Columns.ADDRESS, address);
        values.put(Msg.SQL.Columns.IS_SHOW_WARNING, isShowWarning? 1 : 0 );

        return db.insert(Msg.SQL.TABLE, null, values) != -1L;
    }

} // DB
