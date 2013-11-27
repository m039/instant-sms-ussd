/** UssdMsg.java ---
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
public class UssdMsg extends Msg {

    public UssdMsg(String desc, String message, boolean isShowWarning) {
        super(desc, message, isShowWarning);
    }

    @Override
    public String getType() {
        return TYPE_USSD;
    }

} // UssdMsg
