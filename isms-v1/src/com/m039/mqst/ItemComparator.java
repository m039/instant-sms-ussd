package com.m039.mqst;

import java.util.Comparator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.m039.mqst.items.InstantItem;

public class ItemComparator implements Comparator<InstantItem> {
    private static final String TAG                 = "m039";
    private static final ItemComparator mInstance   = new ItemComparator();
    
    private String mStype;
    private String mSname;

    public static ItemComparator   getInstance() {
        return mInstance;
    }

    public void             update(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String stype = sp.getString("sort_type", "none");
        String sname = sp.getString("sort_name", "none");

        init(stype, sname);
            
        Log.d(TAG, "Sorting started:" + stype + " and " + sname);           
    }
        
    public void             init(String stype, String sname) {
        mStype = stype;
        mSname = sname;
    }

    public int              compare(InstantItem i1, InstantItem i2) {
        // type1 <=> type2
                
        if (!i1.getType().equals(i2.getType())) {
            // type1 != type2
                    
            if (mStype.equals("none")) {
                        
                if (mSname.equals("none")) {
                    return 0;
                }

                // help1 <=> help2
                        
                int cmp = i1.getHelp().compareTo(i2.getHelp());

                if (mSname.equals("a-z")) {
                    return cmp;
                } else {
                    return -cmp;
                }

            } else if (i1.getType().equals("sms")) {

                if (mStype.equals("sms")) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                if (mStype.equals("ussd")) {
                    return -1;
                } else {
                    return 1;
                }
            }
        } else {
            // type1 == type2
                    
            if (mSname.equals("none")) {
                return 0;
            }

            // help1 <=> help2
                    
            int cmp = i1.getHelp().compareTo(i2.getHelp());

            if (mSname.equals("a-z")) {
                return cmp;
            } else {
                return -cmp;
            }
        }
    }       
}