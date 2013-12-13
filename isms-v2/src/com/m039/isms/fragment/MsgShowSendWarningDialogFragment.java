/** MsgShowSendWarningDialogFragment.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.m039.isms.items.Msg;
import com.m039.mqst.R;

/**
 *
 *
 * Created: 12/13/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class MsgShowSendWarningDialogFragment extends DialogFragment {

    public static final String EXTRA_MSG = "extra.msg";

    public interface OnSendMsgListener {
        public void onSendMsg(Msg msg);
    }

    public static MsgShowSendWarningDialogFragment newInstance(Msg msg) {
        MsgShowSendWarningDialogFragment df = new MsgShowSendWarningDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_MSG, msg);
        df.setArguments(args);

        return df;
    }

     @Override
     public Dialog onCreateDialog(Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         builder.setMessage(R.string.f_msg_show_send_warning_dialog__title)
             .setPositiveButton(R.string.f_msg_show_send_warning_dialog__positive_button,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Activity a = getActivity();
                                        if (a instanceof OnSendMsgListener) {
                                            ((OnSendMsgListener) a).onSendMsg(getMsg());
                                        }
                                    }
                                })
             .setNegativeButton(R.string.f_msg_show_send_warning_dialog__negative_button,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
         return builder.create();
     }

    Msg getMsg() {
        return (Msg) getArguments().getParcelable(EXTRA_MSG);
    }


} // MsgShowSendWarningDialogFragment
