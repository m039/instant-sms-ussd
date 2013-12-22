/** GreetOldFriendDialogFragment.java ---
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
import android.content.SharedPreferences;
import android.os.Bundle;

import com.m039.mqst.R;


/**
 *
 *
 * Created: 12/20/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class GreetOldFriendDialogFragment extends DialogFragment {

    public static GreetOldFriendDialogFragment newInstance() {
        return new GreetOldFriendDialogFragment();
    }

    public static final String SP_NAME = "GreetOldFriendDialogFragment";
    public static final String EXTRA_SHOULD_SHOW = "com.m039.isms.fragment.extra.shold_show";

    public static void shouldShowGreet(Activity a) {
        a.getSharedPreferences(SP_NAME, 0)
            .edit()
            .putBoolean(EXTRA_SHOULD_SHOW, true)
            .commit();
    }

    public static void showGreetIfNeccesary(Activity a) {
        SharedPreferences sp = a.getSharedPreferences(SP_NAME, 0);
        if (sp.getBoolean(EXTRA_SHOULD_SHOW, false)) {
            new GreetOldFriendDialogFragment()
                .show(a.getFragmentManager(), SP_NAME);

            sp.edit().putBoolean(EXTRA_SHOULD_SHOW, false).commit();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.f_greet_old_friend__title);
        builder.setMessage(R.string.f_greet_old_friend__message);

        return builder.create();
    }

} // GreetOldFriendDialogFragment
