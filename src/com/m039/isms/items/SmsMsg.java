/** SmsMsg.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.items;

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

    private final String mAddress;

    public SmsMsg(String desc,
                  String message,
                  boolean isShowWarning,
                  String address) {
        super(desc, message, isShowWarning);

        mAddress = address;
    }

    @Override
    public String getType() {
        return TYPE_SMS;
    }

    public String getAddress() {
        return mAddress;
    }


} // SmsMsg
