package com.android.jjnunogarcia.shifter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.android.jjnunogarcia.shifter.database.tables.DayScheduleTable;
import com.android.jjnunogarcia.shifter.database.tables.ShiftTable;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ShiftTable.TABLENAME);
        db.execSQL("DROP TABLE IF EXISTS " + DayScheduleTable.TABLENAME);
//        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.PATTERNS_TABLE_NAME);
        onCreate(db);
    }

    private void createTables(SQLiteDatabase db) {
        // TODO it's necessary to create two tables: one for all the different shift types and the other to store which shifts are in which days (and other for patterns)
        db.execSQL("CREATE TABLE " + ShiftTable.TABLENAME + "(" +
                   ShiftTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   ShiftTable.NAME + " TEXT, " +
                   ShiftTable.DESCRIPTION + " TEXT, " +
                   ShiftTable.START + " INTEGER, " +
                   ShiftTable.DURATION + " INTEGER, " +
                   ShiftTable.LOCATION + " TEXT, " +
                   ShiftTable.COLOR + " INTEGER);");
        db.execSQL("CREATE TABLE " + DayScheduleTable.TABLENAME + "(" +
                   DayScheduleTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   DayScheduleTable.DATE + " LONG, " +
                   DayScheduleTable.SHIFT_ID + " INTEGER);");
//        db.execSQL("CREATE TABLE " + DBConstants.PATTERNS_TABLE_NAME + "(" +
//                   DBConstants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT);");
    }
}
