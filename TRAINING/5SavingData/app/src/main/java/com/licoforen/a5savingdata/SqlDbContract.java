package com.licoforen.a5savingdata;

import android.provider.BaseColumns;

/**
 * Created by lbenc on 6.8.2016..
 */

public class SqlDbContract {

    public SqlDbContract() {}

    public static abstract class SqlDb implements BaseColumns {
        public static final String TABLE_NAME = "myTable";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
