package com.android.jjnunogarcia.shifter.database.tables;

import android.provider.BaseColumns;

/**
 * User: jesus
 * Date: 04/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class DayScheduleTable implements BaseColumns {
    public static final String TABLENAME     = "day_schedules";
    public static final String FULL_ID       = TABLENAME + "." + _ID;
    public static final String DATE          = "date";
    public static final String FULL_DATE     = TABLENAME + "." + DATE;
    public static final String SHIFT_ID      = "shift_id";
    public static final String FULL_SHIFT_ID = TABLENAME + "." + SHIFT_ID;

    private DayScheduleTable() {}
}
