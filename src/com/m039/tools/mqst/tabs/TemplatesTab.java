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
import android.widget.AdapterView.AdapterContextMenuInfo;

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
    private static final int ADD_ITEM_REQUEST       = 0;
    private static final int EDIT_ITEM_REQUEST      = 1;

    // for short clicks
    private final ItemListener mItemListener = new ItemListener();
    
    private boolean isItemsUpdated = false;

    @Override
    protected void         onPause() {
        super.onPause();

        if (isItemsUpdated == true) {
            ItemFactory.saveFactory(this);
            isItemsUpdated = false;

            Log.d(TAG, "Factory is saved");
        }
    }

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
            Intent intent = new Intent(this, CreationTab.class);            
            startActivityForResult(intent, ADD_ITEM_REQUEST);
            break;
        default:
            break;
        }

        return true;
    }

    @Override
    protected void      onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case EDIT_ITEM_REQUEST:         
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

    @Override
    public boolean      onContextItemSelected(MenuItem mitem) {
        super.onContextItemSelected(mitem);

        int id = mitem.getItemId();
        AdapterContextMenuInfo minfo = (AdapterContextMenuInfo) mitem.getMenuInfo();


        switch (id) {
        case R.id.template_menu_delete:
            ItemFactory.getFactory().removeItem(minfo.position);
            updateAdapter();
            break;
        case R.id.template_menu_edit:
            Intent intent = new Intent(this, EditionTab.class);
            intent.putExtra("item position", minfo.position);
            startActivityForResult(intent, EDIT_ITEM_REQUEST);
            break;
        default:
            break;
        }

        return true;
    }

    // class for listview adapter

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
                v.setOnClickListener(new ItemListener());
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
                    hint.setText(item.getHint());
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

    private class ItemListener
        implements OnClickListener {
        
        public void onClick(View v) {
            openContextMenu(v);
        }
    }

    private void    updateAdapter() {
        ItemFactory ifactory = ItemFactory.getFactory(this);
        setListAdapter(new TemplatesAdapter(this, R.layout.template_item, ifactory.getItems()));

        isItemsUpdated = true;
    }

    @Override
    protected void     onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = getListView();

        lv.setItemsCanFocus(true);
        lv.setLongClickable(false);

        updateAdapter();

        // register context menu
        
        registerForContextMenu(lv);
    }
}
