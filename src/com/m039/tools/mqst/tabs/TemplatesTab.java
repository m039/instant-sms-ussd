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
                }

                InstantItem item = getItem(position);

                if (item != null) {
                        TextView help = (TextView) v.findViewById(R.id.template_item_help);

                        if (help != null) {
                            help.setText(item.getHelp());
                        }
                }

                return v;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ItemFactory ifactory = ItemFactory.parseTemplates(this);

        setListAdapter(new TemplatesAdapter(this, R.layout.template_item, ifactory.getItems()));
    }
}
