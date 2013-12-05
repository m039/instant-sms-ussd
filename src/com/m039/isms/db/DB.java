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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.m039.isms.items.Msg;
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
            // db.execSQL("DROP TABLE IF EXISTS " + Msg.SQL.TABLE);
            // onCreate(db);

            throw new RuntimeException();
        }

    }

    DBHelper mDBHelper;

    private DB(Context ctx) {
        mDBHelper = new DBHelper(ctx);
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
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
