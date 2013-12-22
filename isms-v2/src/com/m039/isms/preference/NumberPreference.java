/** NumberPreference.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

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
public class NumberPreference extends DialogPreference {

     public NumberPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.p_number);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    private int mCurrentValue = DEFAULT_VALUE;
    public static final int DEFAULT_VALUE = 1;


    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mCurrentValue = this.getPersistedInt(DEFAULT_VALUE);
        } else {
            mCurrentValue = (Integer) defaultValue;
            persistInt(mCurrentValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected View onCreateDialogView() {
        View view = super.onCreateDialogView();

        NumberPicker np = (NumberPicker) view.findViewById(R.id.number_picker);
        if (np != null) {
            np.setMinValue(1);
            np.setMaxValue(100);
            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange (NumberPicker picker, int oldVal, int newVal) {
                        mCurrentValue = newVal;
                    }
                });
            np.setValue(mCurrentValue);
        }

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mCurrentValue);
        }
    }

    public static final String EXTRA_OLD = "extra.old";
    public static final String EXTRA_VALUE = "extra.value";


    @Override
    protected void onRestoreInstanceState (Parcelable state) {
        Bundle in = (Bundle) state;

        super.onRestoreInstanceState(in.getParcelable(EXTRA_OLD));

        mCurrentValue = in.getInt(EXTRA_VALUE, DEFAULT_VALUE);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle out = new Bundle();

        out.putParcelable(EXTRA_OLD, super.onSaveInstanceState());
        out.putInt(EXTRA_VALUE, mCurrentValue);

        return out;
    }

} // NumberPreference
