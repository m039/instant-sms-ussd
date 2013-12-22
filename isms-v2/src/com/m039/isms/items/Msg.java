/** Msg.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.items;

import android.os.Parcel;
import android.os.Parcelable;
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
public abstract class Msg implements Parcelable {

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

    public static final String TYPE_NONE = "";
    public static final String TYPE_USSD = "ussd";
    public static final String TYPE_SMS = "sms";

    protected String mDescription;
    protected String mAddress;
    protected boolean mIsShowWarning;

    public String getDescription() {
        return mDescription;
    }

    public String getAddress() {
        return mAddress;
    }

    public boolean isShowWarning() {
        return mIsShowWarning;
    }

    public Msg(String description,
               String address,
               boolean isShowWarning) {
        mDescription = description;
        mAddress = address;
        mIsShowWarning = isShowWarning;
    }

    protected Msg() {
    }

    abstract public String getType();

    @Override
    public String toString() {
        return String.format("{ class: %s, type: '%s', desc: '%s', addr: '%s', w: %s }",
                             Msg.class.getSimpleName(),
                             getType(),
                             getDescription(),
                             getAddress(),
                             mIsShowWarning);
    }

    //
    // Parcelable
    //

    protected void readFromParcelAux(Parcel in) {
        mDescription = in.readString();
        mAddress = in.readString();
        mIsShowWarning = in.readInt() == 1;
    }

    protected void writeToParcelAux(Parcel out, int flags) {
        out.writeString(mDescription);
        out.writeString(mAddress);
        out.writeInt(mIsShowWarning? 1 : 0);
    }

} // Msg
