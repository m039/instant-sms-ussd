/** MsgPickActionsDialogFragment.java ---
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
public class MsgPickActionsDialogFragment extends DialogFragment {

    public static final String EXTRA_MSG_ID = "extra.msg_id";

    public interface OnMsgPickActionsListener {
        public void onMsgPickActiondDelete(long msgId);
        public void onMsgPickActiondEdit(long msgId);
    }
    
    public static MsgPickActionsDialogFragment newInstance(long msgId) {
        MsgPickActionsDialogFragment df = new MsgPickActionsDialogFragment();

        Bundle args = new Bundle();
        args.putLong(EXTRA_MSG_ID, msgId);
        df.setArguments(args);

        return df;
    }

    private long getMsgId() {
        return getArguments().getLong(EXTRA_MSG_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
            .setTitle(R.string.f_msg_actions_dialog__title)
            .setItems(new String[] {
                    getString(R.string.f_msg_actions_dialog__action__edit),
                    getString(R.string.f_msg_actions_dialog__action__delete)
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity a = getActivity();

                        if (a instanceof OnMsgPickActionsListener) {
                            OnMsgPickActionsListener l = (OnMsgPickActionsListener) a;

                            if (which == 0) { // edit
                                l.onMsgPickActiondEdit(getMsgId());
                            }  else if (which == 1) { // delete
                                l.onMsgPickActiondDelete(getMsgId());
                            }
                        }
                    }
                })
            .create();
    }

} // MsgPickActionsDialogFragment
