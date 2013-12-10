/** UssdMsg.java ---
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
public class UssdMsg extends Msg {

    public UssdMsg(String desc, String address, boolean isShowWarning) {
        super(desc, address, isShowWarning);
    }

    @Override
    public String getType() {
        return TYPE_USSD;
    }

    //
    // Parcelable
    //
    
    private UssdMsg(Parcel in) {
        readFromParcelAux(in);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(Parcel out, int flags) {
        writeToParcelAux(out, flags);
    }
    
    public static final Parcelable.Creator<UssdMsg> CREATOR = new Parcelable.Creator<UssdMsg>() {
        public UssdMsg createFromParcel(Parcel in) {
            return new UssdMsg(in);
        }
        
        public UssdMsg[] newArray(int size) {
            return new UssdMsg[size];
        }
    };

} // UssdMsg
