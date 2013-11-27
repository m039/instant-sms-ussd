/** Msg.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.items;

import android.provider.BaseColumns;

/**
 *
 *
 * Created: 11/28/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public abstract class Msg {

    public interface SQL {
        public static final String TABLE = "msg";

        public interface Columns {
            public static final String ID = BaseColumns._ID;
            public static final String TYPE = "type";
            public static final String DESCRIPTION = "description";
            public static final String MESSAGE = "message";
            public static final String IS_SHOW_WARNING = "is_show_warning";
            public static final String ADDRESS = "address";
        }
    }

    public static final String TYPE_USSD = "ussd";
    public static final String TYPE_SMS = "sms";

    protected final String mDescription;
    protected final String mMessage;
    protected final boolean mIsShowWarning;

    public String getDescription() {
        return mDescription;
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isShowWarning() {
        return mIsShowWarning;
    }

    public Msg(String description,
               String message,
               boolean isShowWarning) {
        mDescription = description;
        mMessage = message;
        mIsShowWarning = isShowWarning;
    }

    abstract public String getType();

} // Msg
