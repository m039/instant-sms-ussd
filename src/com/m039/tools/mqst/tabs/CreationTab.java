package com.m039.tools.mqst.tabs;

import com.m039.tools.mqst.InstantItem;
import com.m039.tools.mqst.InstantUssd;
import com.m039.tools.mqst.InstantSms;
import com.m039.tools.mqst.ItemFactory;

import android.app.Activity;
import android.os.Bundle;
import com.m039.tools.mqst.R;
import android.widget.SpinnerAdapter;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.EditText;
import com.m039.tools.mqst.InstantUssd;
import com.m039.tools.mqst.R.layout;

/**
 * Describe class CreationTab here.
 *
 *
 * Created: Fri Sep  2 13:03:08 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class CreationTab extends Activity {
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
                ViewGroup vg = (ViewGroup) findViewById(R.id.creation_tab_layout);
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
        case R.id.creation_tab_add_button:
            createInstantItem();
            setResult(RESULT_OK);
            break;
        case R.id.creation_tab_cancel_button:
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
        setContentView(R.layout.creation_tab);

        initLayouts();
        // initButtons();

        Spinner s = (Spinner) findViewById(R.id.creation_tab_spinner);

        s.setAdapter(new ArrayAdapter<String>(this,
                                              R.layout.creation_tab_spinner_item,
                                              mTypes));

        s.setOnItemSelectedListener(mTypeSelectListener);
    }

    
    private void        createInstantItem() {
        Spinner s = (Spinner) findViewById(R.id.creation_tab_spinner);
        int position = s.getSelectedItemPosition();

        View layout = findViewById(R.id.creation_tab_layout);
        
        InstantItem item = null;

        if (position == TYPE_SMS) {
            EditText ehelp = (EditText) layout.findViewById(R.id.creation_tab_etext_help);
            EditText eaddr = (EditText) layout.findViewById(R.id.creation_tab_etext_text);
            EditText etext = (EditText) layout.findViewById(R.id.creation_tab_etext_text);

            String help = ehelp.getText().toString();
            String addr = eaddr.getText().toString();
            String text = etext.getText().toString();

            if (!help.equals("") &&
                !addr.equals("") &&
                !text.equals("")) {
                item = new InstantSms(help, addr, text);
            }
        }

        if (position == TYPE_USSD) {
            EditText ehelp = (EditText) layout.findViewById(R.id.creation_tab_etext_help);          
            EditText etext = (EditText) layout.findViewById(R.id.creation_tab_etext_text);

            String help = ehelp.getText().toString();
            String text = etext.getText().toString();

            if (!help.equals("") &&
                !text.equals("")) {
                item = new InstantUssd(help, text);
            }
        }

        if (item != null) {
            ItemFactory.addItem(this, item);
        }
    }

    private void        initLayouts() {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup vg = (ViewGroup) findViewById(R.id.creation_tab_layout);
        View v;

        mSmsLayout = inflater.inflate(R.layout.creation_tab_sms, null);
        mTypeLayout = inflater.inflate(R.layout.creation_tab_ussd, null);

        // set default view
        vg.addView(mSmsLayout, 0, mLayoutParams);

        // adds buttons to layouts
        vg = (ViewGroup) mSmsLayout;
        v = inflater.inflate(R.layout.creation_tab_buttons, null);
        vg.addView(v);

        vg = (ViewGroup) mTypeLayout;
        v = inflater.inflate(R.layout.creation_tab_buttons, null);      
        vg.addView(v);
    }

    // /**
    //  * Call after initLayout otherwise error!
    //  */
    // private void        initButtons() {
    //     Button add = (Button) findViewById(R.id.creation_tab_add_button);
    //     Button cancel = (Button) findViewById(R.id.creation_tab_cancel_button);

    //     add.setOnClickListener(mOnClickListener);
    //     cancel.setOnClickListener(mOnClickListener);
    // }

}
