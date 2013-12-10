/** SmsMsg.java ---
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

/**
 *
 *
 * Created: 11/28/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class SmsMsg extends Msg {

    private final String mMessage;

    public SmsMsg(String desc,
                  String address,
                  boolean isShowWarning,
                  String message) {
        super(desc, address, isShowWarning);

        mMessage = message;
    }

    @Override
    public String getType() {
        return TYPE_SMS;
    }

    public String getMessage() {
        return mMessage;
    }

    //
    // Parcelable
    //
    
    private SmsMsg(Parcel in) {
        readFromParcelAux(in);

        mMessage = in.readString();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(Parcel out, int flags) {
        writeToParcelAux(out, flags);

        out.writeString(mMessage);
    }
    
    public static final Parcelable.Creator<SmsMsg> CREATOR = new Parcelable.Creator<SmsMsg>() {
        public SmsMsg createFromParcel(Parcel in) {
            return new SmsMsg(in);
        }
        
        public SmsMsg[] newArray(int size) {
            return new SmsMsg[size];
        }
    };


} // SmsMsg
