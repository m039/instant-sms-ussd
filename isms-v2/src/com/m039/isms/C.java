/** C.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms;

/**
 * Constants
 *
 * Created: 11/28/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class C {

    public static final String PACKAGE = "com.m039.mqst"; // don't change it.. ever and ever

    public static class Preferences {
        private static final String PREFIX = "pref__";

        public static final String IS_SHOW_BANNER = PREFIX + "is_show_banner";
        public static final String IS_SHOW_ADDRESS = PREFIX + "is_show_address";
        public static final String DESCRIPTION_LINES = PREFIX + "description_lines";

        public static final String SORT_TYPE_ORDER = PREFIX + "sort_type_order";
        public static final String SORT_DESC_ORDER = PREFIX + "sort_desc_order";
    }

    public static class PreferenceValues {
        public static final String AZ = "a-z";
        public static final String ZA = "z-a";
        public static final String NONE = "none";
    }



} // C
