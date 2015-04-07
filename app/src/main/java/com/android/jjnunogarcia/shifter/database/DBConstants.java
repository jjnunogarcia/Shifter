package com.android.jjnunogarcia.shifter.database;

import com.android.jjnunogarcia.shifter.database.tables.DayScheduleTable;
import com.android.jjnunogarcia.shifter.database.tables.ShiftTable;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public final class DBConstants {
    // TODO think about adding creation date to all rows
    public static final String   DATABASE_NAME                = "shifter_database";
    public static final int      DATABASE_VERSION             = 1;
    public static final int      SHIFTS_TABLE_CODE            = 1;
    public static final String[] SHIFTS_PROJECTION            = {
            ShiftTable._ID,
            ShiftTable.NAME,
            ShiftTable.DESCRIPTION,
            ShiftTable.START,
            ShiftTable.DURATION,
            ShiftTable.LOCATION,
            ShiftTable.COLOR
    };
    public static final String   SORT_SHIFTS_BY_ID_ASC        = ShiftTable.FULL_ID + " COLLATE NOCASE ASC";
    public static final int      DAY_SCHEDULES_TABLE_CODE     = 2;
    public static final String[] DAY_SCHEDULES_PROJECTION     = {
            DayScheduleTable._ID,
            DayScheduleTable.DATE,
            DayScheduleTable.SHIFT_ID,
            ShiftTable.FULL_NAME,
            ShiftTable.FULL_DESCRIPTION,
            ShiftTable.FULL_START,
            ShiftTable.FULL_DURATION,
            ShiftTable.FULL_LOCATION,
            ShiftTable.FULL_COLOR
    };
    public static final String   SORT_DAY_SCHEDULES_BY_ID_ASC = DayScheduleTable.FULL_ID + " COLLATE NOCASE ASC";
//    public static final String   PATTERNS_TABLE_NAME          = "patterns";
//    public static final int      PATTERNS_TABLE_CODE          = 3;
//    public static final String[] PATTERNS_PROJECTION          = {
//            TODO fill the fields
//    };
//    TODO
//    public static final String   SORT_PATTERNS_BY_ID_ASC      = ID + " COLLATE NOCASE ASC";

    private DBConstants() {}
}
