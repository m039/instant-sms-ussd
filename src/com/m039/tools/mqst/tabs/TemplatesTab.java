package com.m039.tools.mqst.tabs;

import com.m039.tools.mqst.R;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.os.Bundle;

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
    private static final String[] a = {"Hello", "World", "!"};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, R.layout.template_item, a));
    }    
}
