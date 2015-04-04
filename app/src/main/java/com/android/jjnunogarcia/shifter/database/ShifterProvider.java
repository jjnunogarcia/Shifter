package com.android.jjnunogarcia.shifter.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.android.jjnunogarcia.shifter.database.tables.DayScheduleTable;
import com.android.jjnunogarcia.shifter.database.tables.ShiftTable;

import java.util.AbstractMap;
import java.util.HashMap;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShifterProvider extends ContentProvider {
    private static final String AUTHORITY         = "com.android.jjnunogarcia.shifter.shifterprovider";
    public static final  Uri    SHIFTS_URI        = Uri.parse("content://" + AUTHORITY + "/" + ShiftTable.TABLENAME);
    public static final  Uri    DAY_SCHEDULES_URI = Uri.parse("content://" + AUTHORITY + "/" + DayScheduleTable.TABLENAME);
    //    public static final  Uri    PATTERNS_URI      = Uri.parse("content://" + AUTHORITY + "/" + DBConstants.PATTERNS_TABLE_NAME);
    private static final UriMatcher                  uriMatcher;
    private static final AbstractMap<String, String> SHIFTS_PROJECTION_MAP;
    private static final AbstractMap<String, String> DAY_SCHEDULES_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ShiftTable.TABLENAME, DBConstants.SHIFTS_TABLE_CODE);
        uriMatcher.addURI(AUTHORITY, DayScheduleTable.TABLENAME, DBConstants.DAY_SCHEDULES_TABLE_CODE);
//        uriMatcher.addURI(AUTHORITY, DBConstants.PATTERNS_TABLE_NAME, DBConstants.PATTERNS_TABLE_CODE);

        SHIFTS_PROJECTION_MAP = new HashMap<>();
        SHIFTS_PROJECTION_MAP.put(ShiftTable._ID, ShiftTable.FULL_ID);
        SHIFTS_PROJECTION_MAP.put(ShiftTable.NAME, ShiftTable.FULL_NAME);
        SHIFTS_PROJECTION_MAP.put(ShiftTable.DESCRIPTION, ShiftTable.FULL_DESCRIPTION);
        SHIFTS_PROJECTION_MAP.put(ShiftTable.START, ShiftTable.FULL_START);
        SHIFTS_PROJECTION_MAP.put(ShiftTable.DURATION, ShiftTable.FULL_DURATION);
        SHIFTS_PROJECTION_MAP.put(ShiftTable.LOCATION, ShiftTable.FULL_LOCATION);
        SHIFTS_PROJECTION_MAP.put(ShiftTable.COLOR, ShiftTable.FULL_COLOR);

        DAY_SCHEDULES_PROJECTION_MAP = new HashMap<>();
        DAY_SCHEDULES_PROJECTION_MAP.put(DayScheduleTable._ID, DayScheduleTable.FULL_ID);
        DAY_SCHEDULES_PROJECTION_MAP.put(DayScheduleTable.DATE, DayScheduleTable.FULL_DATE);
        DAY_SCHEDULES_PROJECTION_MAP.put(DayScheduleTable.SHIFT_ID, DayScheduleTable.FULL_SHIFT_ID);
        DAY_SCHEDULES_PROJECTION_MAP.put("shift_name", ShiftTable.FULL_NAME + " AS shift_name");
        DAY_SCHEDULES_PROJECTION_MAP.put("shift_description", ShiftTable.FULL_DESCRIPTION + " AS shift_description");
        DAY_SCHEDULES_PROJECTION_MAP.put("shit_start", ShiftTable.FULL_START + " AS shit_start");
        DAY_SCHEDULES_PROJECTION_MAP.put("shift_duration", ShiftTable.FULL_DURATION + " AS shift_duration");
        DAY_SCHEDULES_PROJECTION_MAP.put("shift_location", ShiftTable.FULL_LOCATION + " AS shift_location");
        DAY_SCHEDULES_PROJECTION_MAP.put("shift_color", ShiftTable.FULL_COLOR + " AS shift_color");
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        int code = uriMatcher.match(uri);

        if (code == DBConstants.SHIFTS_TABLE_CODE) {
            sqlBuilder.setTables(ShiftTable.TABLENAME);
            sqlBuilder.setProjectionMap(SHIFTS_PROJECTION_MAP);
        } else if (code == DBConstants.DAY_SCHEDULES_TABLE_CODE) {
            sqlBuilder.setTables(DayScheduleTable.TABLENAME + " INNER JOIN " + ShiftTable.TABLENAME + " ON " + DayScheduleTable.FULL_SHIFT_ID + " = " + ShiftTable.FULL_ID);
            sqlBuilder.setProjectionMap(DAY_SCHEDULES_PROJECTION_MAP);
//        } else if (code == DBConstants.PATTERNS_TABLE_CODE) {
            // TODO
        } else {
            throw new SQLException("Unknown URI " + uri);
        }

        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri;
        int code = uriMatcher.match(uri);

        if (code == DBConstants.SHIFTS_TABLE_CODE) {
            long rowID = db.insert(ShiftTable.TABLENAME, null, values);
            if (rowID > 0) {
                _uri = ContentUris.withAppendedId(SHIFTS_URI, rowID);
                getContext().getContentResolver().notifyChange(uri, null);
            } else {
                throw new SQLException("Failed to insert row into " + uri);
            }
        } else if (code == DBConstants.DAY_SCHEDULES_TABLE_CODE) {
            long rowID = db.insert(DayScheduleTable.TABLENAME, null, values);
            if (rowID > 0) {
                _uri = ContentUris.withAppendedId(DAY_SCHEDULES_URI, rowID);
                getContext().getContentResolver().notifyChange(uri, null);
            } else {
                throw new SQLException("Failed to insert row into " + uri);
            }
//        } else if (code == DBConstants.PATTERNS_TABLE_CODE) {
//            long rowID = db.insert(DBConstants.PATTERNS_TABLE_NAME, null, values);
//            if (rowID > 0) {
//                _uri = ContentUris.withAppendedId(PATTERNS_URI, rowID);
//                getContext().getContentResolver().notifyChange(uri, null);
//            } else {
//                throw new SQLException("Failed to insert row into " + uri);
//            }
        } else {
            throw new SQLException("Unknown URI " + uri);
        }

        return _uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        int code = uriMatcher.match(uri);

        if (code == DBConstants.SHIFTS_TABLE_CODE) {
            count = db.delete(ShiftTable.TABLENAME, selection, selectionArgs);
        } else if (code == DBConstants.DAY_SCHEDULES_TABLE_CODE) {
            count = db.delete(DayScheduleTable.TABLENAME, selection, selectionArgs);
//        } else if (code == DBConstants.PATTERNS_TABLE_CODE) {
//            count = db.delete(DBConstants.PATTERNS_TABLE_NAME, selection, selectionArgs);
        } else {
            throw new SQLException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        int code = uriMatcher.match(uri);

        if (code == DBConstants.SHIFTS_TABLE_CODE) {
            count = db.update(ShiftTable.TABLENAME, values, selection, selectionArgs);
        } else if (code == DBConstants.DAY_SCHEDULES_TABLE_CODE) {
            count = db.update(DayScheduleTable.TABLENAME, values, selection, selectionArgs);
//        } else if (code == DBConstants.PATTERNS_TABLE_CODE) {
//            count = db.update(DBConstants.PATTERNS_TABLE_NAME, values, selection, selectionArgs);
        } else {
            throw new SQLException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
