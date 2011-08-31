package com.m039.tools.mqst.tabs;

import com.m039.tools.mqst.R;
import com.m039.tools.mqst.InstantItem;
import com.m039.tools.mqst.ItemFactory;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import java.util.List;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;
import android.util.Log;
import android.widget.Button;

/**
 * Describe class TemplatesListView here.
 *
 *
 * Created: Wed Aug 31 16:58:01 2011
 *
 * @author <a href="mailto:flam44@gmail.com">Mozgin Dmitry</a>
 * @version 1.0
 */
public class TemplatesTab extends ListActivity {
    private static final String TAG                 = "m039";
    private static ButtonListener mButtonListener   = new ButtonListener();
    
    private class TemplatesAdapter extends ArrayAdapter<InstantItem> {
        public TemplatesAdapter(Context context, int id, List<InstantItem> objs) {
            super(context, id, objs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;

                if (v == null) {
                    LayoutInflater li = (LayoutInflater) getLayoutInflater();
                    v = li.inflate(R.layout.template_item, null);

                    v.setClickable(true);
                    v.setFocusable(true);
                    v.setBackgroundResource(android.R.drawable.menuitem_background);

                    v.setOnClickListener(new ItemListener(position));
                }

                InstantItem item = getItem(position);

                if (item != null) {
                        TextView help = (TextView) v.findViewById(R.id.template_item_help);
                        TextView type = (TextView) v.findViewById(R.id.template_type);
                        TextView hint = (TextView) v.findViewById(R.id.template_hint);

                        Button btn = (Button) v.findViewById(R.id.template_send_button);
                        btn.setOnClickListener(mButtonListener);

                        if (help != null) {
                            help.setText(item.getHelp());
                        }

                        if (type != null) {
                            type.setText(item.getType());
                        }

                        if (hint != null) {
                            hint.setText(item.getHint());
                        }
                }

                return v;
        }
    }

    private class ItemListener
        implements OnClickListener {
        private final int mPosition;

        public ItemListener(int position) {
            mPosition = position;
        }
        
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "I'm click listener #" + mPosition, Toast.LENGTH_SHORT).show();
        }
    }
    
    static private class ButtonListener
        implements OnClickListener {
        public void onClick(View v) {
                Log.d(TAG, "Button is clicked");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = getListView();
        lv.setItemsCanFocus(true);

        ItemFactory ifactory = ItemFactory.parseTemplates(this);
        setListAdapter(new TemplatesAdapter(this, R.layout.template_item, ifactory.getItems()));
    }
}
