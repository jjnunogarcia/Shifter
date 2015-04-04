package com.android.jjnunogarcia.shifter.database.tables;

import android.provider.BaseColumns;

/**
 * User: jesus
 * Date: 04/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShiftTable implements BaseColumns {
    public static final String TABLENAME        = "shifts";
    public static final String FULL_ID          = TABLENAME + "." + _ID;
    public static final String NAME             = "name";
    public static final String FULL_NAME        = TABLENAME + "." + NAME;
    public static final String DESCRIPTION      = "description";
    public static final String FULL_DESCRIPTION = TABLENAME + "." + DESCRIPTION;
    public static final String START            = "start";
    public static final String FULL_START       = TABLENAME + "." + START;
    public static final String DURATION         = "duration";
    public static final String FULL_DURATION    = TABLENAME + "." + DURATION;
    public static final String LOCATION         = "location";
    public static final String FULL_LOCATION    = TABLENAME + "." + LOCATION;
    public static final String COLOR            = "color";
    public static final String FULL_COLOR       = TABLENAME + "." + COLOR;

    private ShiftTable() {}
}
