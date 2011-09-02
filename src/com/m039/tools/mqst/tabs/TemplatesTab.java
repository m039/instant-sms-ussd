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
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.util.Log;
import android.widget.Button;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

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
    private static ItemListener mItemListener       = new ItemListener();
    private static final int ADD_ITEM_REQUEST       = 0;

    // menu
    
    @Override
    public boolean      onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        return true;
    }

    @Override
    public boolean      onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        case R.id.menu_add_item:
            startActivityForResult(new Intent(this, CreationTab.class),
                                   ADD_ITEM_REQUEST);
            break;
        default:
            break;
        }
        
        return true;
    }

    @Override
    protected void      onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case ADD_ITEM_REQUEST:
            if (resultCode == RESULT_OK) {
                updateAdapter();
            }

            break;
        default:
            break;
        }

    }

    
    // context menu

    @Override
    public void         onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo minfo) {
        super.onCreateContextMenu(menu, v, minfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.template_menu, menu);
    }

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

                v.setOnCreateContextMenuListener(null);
            }

            InstantItem item = getItem(position);

            if (item != null) {
                TextView help = (TextView) v.findViewById(R.id.template_item_help);
                TextView type = (TextView) v.findViewById(R.id.template_type);
                TextView hint = (TextView) v.findViewById(R.id.template_hint);

                if (help != null) {
                    help.setText(item.getHelp());
                }

                if (type != null) {
                    type.setText(item.getType());
                }

                if (hint != null) {
                    String h = item.getHint();

                    // strip the hint
                    
                    if (h.length() > 33) {
                        h = h.substring(0, 33) + "...";
                    }

                    hint.setText(h);
                }

                // setting callback for send button

                Button btn = (Button) v.findViewById(R.id.template_send_button);
                btn.setOnClickListener(new ButtonListener(item));
            }

            return v;
        }
    }

    private class ButtonListener
        implements OnClickListener {
        private final InstantItem mInstantItem;

        public ButtonListener(InstantItem item) {
            mInstantItem = item;
        }

        public void onClick(View v) {
            mInstantItem.send(TemplatesTab.this);
        }
    }

    static private class ItemListener
        implements OnClickListener {
        public void onClick(View v) {
            Log.d(TAG, "Item is clicked");
        }
    }

    private void    updateAdapter() {
        ItemFactory ifactory = ItemFactory.parseTemplates(this);
        setListAdapter(new TemplatesAdapter(this, R.layout.template_item, ifactory.getItems()));
    }

    @Override
    public void     onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = getListView();
        
        lv.setItemsCanFocus(true);

        updateAdapter();

        // register context menu
        registerForContextMenu(lv);
    }
}
