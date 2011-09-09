package com.m039.mqst.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.m039.mqst.items.InstantUssd;
import com.m039.mqst.items.InstantSms;
import com.m039.mqst.items.InstantItem;

import com.m039.mqst.ItemFactory;
import com.m039.mqst.R;
import android.widget.CheckBox;

/**
 * Describe class CreationTab here.
 *
 *
 * Created: Fri Sep  2 13:03:08 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class AddActivity extends Activity {
    static private final int TYPE_SMS       = 0;
    static private final int TYPE_USSD      = 1;

    static private final String[] mTypes    = {"sms", "ussd"};
    static private View mSmsLayout;
    static private View mTypeLayout;
    static private ViewGroup.LayoutParams mLayoutParams;

    static {
        mLayoutParams = new LinearLayout.LayoutParams(-1, -1, 1);
    }

    // on spinner listener

    private AdapterView.OnItemSelectedListener mTypeSelectListener = new AdapterView.OnItemSelectedListener()  {
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                ViewGroup vg = (ViewGroup) findViewById(R.id.add_activity_layout);
                View v = null;

                if (position == TYPE_SMS) {
                    v = mSmsLayout;
                }

                if (position == TYPE_USSD) {
                    v = mTypeLayout;
                }

                if (v != null) {
                    vg.removeViewAt(0);
                    vg.addView(v, 0, mLayoutParams);
                }

            }

            public void onNothingSelected (AdapterView<?> parent) {
            }
        };

    // on buttons listener (name is taken from the xml file)

    public void         onButtonClick(View v) {
        int id = v.getId();

        switch (id) {
        case R.id.add_activity_add_button:
            InstantItem item = createInstantItem();

            if (item != null) {
                ItemFactory.getFactory().addItem(item);
            }

            setResult(RESULT_OK);
            break;
        case R.id.add_activity_cancel_button:
            setResult(RESULT_CANCELED);
            break;
        default:
            break;
        }

        finish();
    }


    @Override
    public void         onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        initLayouts();

        Spinner s = (Spinner) findViewById(R.id.add_activity_spinner);

        s.setAdapter(new ArrayAdapter<String>(this,
                                              R.layout.add_activity_spinner_item,
                                              mTypes));

        s.setOnItemSelectedListener(mTypeSelectListener);
    }


    private InstantItem    createInstantItem() {
        Spinner s = (Spinner) findViewById(R.id.add_activity_spinner);
        int position = s.getSelectedItemPosition();

        View layout = findViewById(R.id.add_activity_layout);

        InstantItem item = null;

        if (position == TYPE_SMS) {
            EditText ehelp  = (EditText) layout.findViewById(R.id.add_activity_etext_help);
            EditText eaddr  = (EditText) layout.findViewById(R.id.add_activity_etext_address);
            EditText etext  = (EditText) layout.findViewById(R.id.add_activity_etext_text);
            CheckBox cwarn  = (CheckBox) layout.findViewById(R.id.add_activity_cbox_warning);

            String help     = ehelp.getText().toString();
            String addr     = eaddr.getText().toString();
            String text     = etext.getText().toString();
            Boolean warn    = cwarn.isChecked();

            if (!help.equals("") &&
                !addr.equals("") &&
                !text.equals("")) {
                item = new InstantSms(help, addr, text, warn);
            }
        }

        if (position == TYPE_USSD) {
            EditText ehelp = (EditText) layout.findViewById(R.id.add_activity_etext_help);
            EditText etext = (EditText) layout.findViewById(R.id.add_activity_etext_text);

            String help = ehelp.getText().toString();
            String text = etext.getText().toString();

            if (!help.equals("") &&
                !text.equals("")) {
                item = new InstantUssd(help, text);
            }
        }

        return item;
    }

    private void        initLayouts() {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup vg = (ViewGroup) findViewById(R.id.add_activity_layout);
        View v;

        mSmsLayout = inflater.inflate(R.layout.add_activity_sms, null);
        mTypeLayout = inflater.inflate(R.layout.add_activity_ussd, null);

        // set default view
        vg.addView(mSmsLayout, 0, mLayoutParams);

        // adds buttons to layouts
        vg = (ViewGroup) mSmsLayout;
        v = inflater.inflate(R.layout.add_activity_buttons, null);
        vg.addView(v);

        vg = (ViewGroup) mTypeLayout;
        v = inflater.inflate(R.layout.add_activity_buttons, null);
        vg.addView(v);
    }

}
