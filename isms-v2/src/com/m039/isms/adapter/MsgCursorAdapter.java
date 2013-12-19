/** MsgCursorAdapter.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.m039.isms.C;
import com.m039.isms.graphics.drawable.TypeDrawable;
import com.m039.isms.items.Msg;
import com.m039.mqst.R;

/**
 *
 *
 * Created: 12/05/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class MsgCursorAdapter extends CursorAdapter {

    public MsgCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    static boolean CONSTANTS_INITIALIZED = false;
    static int COLOR_SMS = -1;
    static int COLOR_USSD = -1;

    static class Holder {
        TextView desc;
        TextView message;
        TextView addr;
        TextView type;
        TypeDrawable typeDrawable;
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        
        Holder h = (Holder) v.getTag();

        int columnIndex;

        String type = Msg.TYPE_NONE;

        if (h.type != null) {
            columnIndex = cursor.getColumnIndex(Msg.SQL.Columns.TYPE);
            if (columnIndex != -1) {
                type = cursor.getString(columnIndex);

                if (Msg.TYPE_SMS.equals(type)) {
                    h.typeDrawable.setBackgroundColor(COLOR_SMS);
                } else if(Msg.TYPE_USSD.equals(type)) {
                    h.typeDrawable.setBackgroundColor(COLOR_USSD);
                }

                h.type.setText(type);
            }
        }
        
        if (h.desc != null) {
            columnIndex = cursor.getColumnIndex(Msg.SQL.Columns.DESCRIPTION);
            if (columnIndex != -1) {
                h.desc.setText(cursor.getString(columnIndex));
            }
        }

        boolean isShowAddress = false;

        if (h.addr != null) {
            if (isShowAddress = sp.getBoolean(C.Preferences.IS_SHOW_ADDRESS, false)) {
                columnIndex = cursor.getColumnIndex(Msg.SQL.Columns.ADDRESS);
                if (columnIndex != -1) {
                    h.addr.setText(cursor.getString(columnIndex));
                    h.addr.setVisibility(View.VISIBLE);                 
                }               
            } else {
                h.addr.setVisibility(View.GONE);
            }
        }       

        if (h.message != null) {
            // in ussd msg message field is empty 
            if (Msg.TYPE_USSD.equals(type) && isShowAddress) {
                h.message.setVisibility(View.GONE);
            } else {
                h.message.setVisibility(View.VISIBLE);
            }
            
            columnIndex = cursor.getColumnIndex(Msg.SQL.Columns.MESSAGE);
            if (columnIndex != -1) {
                h.message.setText(cursor.getString(columnIndex));
            }
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // init constants
        if (!CONSTANTS_INITIALIZED) {
            Resources res = context.getResources();

            COLOR_SMS = res.getColor(R.color.e_msg__type__sms);
            COLOR_USSD = res.getColor(R.color.e_msg__type__ussd);

            CONSTANTS_INITIALIZED = true;
        }


        LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.e_msg, parent, false);
        Holder h = new Holder();

        h.desc = (TextView) v.findViewById(R.id.desc);
        h.message = (TextView) v.findViewById(R.id.message);
        h.type = (TextView) v.findViewById(R.id.type);
        h.addr = (TextView) v.findViewById(R.id.addr);

        if (h.type != null) {
            h.typeDrawable = new TypeDrawable(context);
            h.type.setBackgroundDrawable(h.typeDrawable);
        }

        v.setTag(h);

        return v;
    }

} // MsgCursorAdapter
