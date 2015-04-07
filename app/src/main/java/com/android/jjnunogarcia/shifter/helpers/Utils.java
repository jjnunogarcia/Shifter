package com.android.jjnunogarcia.shifter.helpers;

import android.database.Cursor;
import com.android.jjnunogarcia.shifter.database.tables.DayScheduleTable;
import com.android.jjnunogarcia.shifter.database.tables.ShiftTable;
import com.android.jjnunogarcia.shifter.model.DaySchedule;
import com.android.jjnunogarcia.shifter.model.Shift;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public final class Utils {
    public static int getDaysInMonth(int month, int year) {
        Calendar calendar = new GregorianCalendar(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static ArrayList<Shift> getShifts(Cursor cursor) {
        ArrayList<Shift> shifts = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                shifts.add(createShiftFromCursor(cursor));
            }
        }

        return shifts;
    }

    private static Shift createShiftFromCursor(Cursor cursor) {
        Shift shift = new Shift();
        shift.setId(cursor.getInt(cursor.getColumnIndex(ShiftTable._ID)));
        shift.setName(cursor.getString(cursor.getColumnIndex(ShiftTable.NAME)));
        shift.setDescription(cursor.getString(cursor.getColumnIndex(ShiftTable.DESCRIPTION)));
        shift.setStart(cursor.getInt(cursor.getColumnIndex(ShiftTable.START)));
        shift.setDuration(cursor.getInt(cursor.getColumnIndex(ShiftTable.DURATION)));
        shift.setLocation(cursor.getString(cursor.getColumnIndex(ShiftTable.LOCATION)));
        shift.setColor(cursor.getInt(cursor.getColumnIndex(ShiftTable.COLOR)));

        return shift;
    }

    public static ArrayList<DaySchedule> getDaySchedules(Cursor cursor) {
        ArrayList<DaySchedule> daySchedules = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                daySchedules.add(createDayScheduleFromCursor(cursor));
            }

            daySchedules = groupDaysAndShifts(daySchedules);
        }

        return daySchedules;
    }

    private static DaySchedule createDayScheduleFromCursor(Cursor cursor) {
        DaySchedule daySchedule = new DaySchedule();
        daySchedule.setId(cursor.getInt(cursor.getColumnIndex(DayScheduleTable._ID)));
        daySchedule.setDate(cursor.getLong(cursor.getColumnIndex(DayScheduleTable.DATE)));

        Shift shift = new Shift();
        shift.setId(cursor.getInt(cursor.getColumnIndex(DayScheduleTable.SHIFT_ID)));
        shift.setName(cursor.getString(cursor.getColumnIndex(ShiftTable.NAME)));
        shift.setDescription(cursor.getString(cursor.getColumnIndex(ShiftTable.DESCRIPTION)));
        shift.setStart(cursor.getInt(cursor.getColumnIndex(ShiftTable.START)));
        shift.setDuration(cursor.getInt(cursor.getColumnIndex(ShiftTable.DURATION)));
        shift.setLocation(cursor.getString(cursor.getColumnIndex(ShiftTable.LOCATION)));
        shift.setColor(cursor.getInt(cursor.getColumnIndex(ShiftTable.COLOR)));

        ArrayList<Shift> shifts = new ArrayList<>();
        shifts.add(shift);
        daySchedule.setShifts(shifts);

        return daySchedule;
    }

    private static ArrayList<DaySchedule> groupDaysAndShifts(ArrayList<DaySchedule> daySchedules) {
        ArrayList<DaySchedule> groupedDaySchedules = new ArrayList<>();

        for (int i = 0, size = daySchedules.size(); i < size; i++) {
            DaySchedule daySchedule = daySchedules.get(i);

            if (!dayScheduleExistsInGroup(groupedDaySchedules, daySchedule)) {
                DaySchedule groupedDaySchedule = new DaySchedule(daySchedule);

                for (int j = i; j < size; j++) {
                    DaySchedule dayScheduleToGroup = daySchedules.get(j);

                    if (daySchedule.getDate() == dayScheduleToGroup.getDate()) {
                        groupedDaySchedule.getShifts().addAll(dayScheduleToGroup.getShifts());
                    }
                }

                groupedDaySchedules.add(groupedDaySchedule);
            }
        }

        return groupedDaySchedules;
    }

    private static boolean dayScheduleExistsInGroup(ArrayList<DaySchedule> daySchedules, DaySchedule dayScheduleToCheck) {
        for (DaySchedule daySchedule : daySchedules) {
            if (daySchedule.getDate() == dayScheduleToCheck.getDate()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNumber(String string) {
        String regex = "[0-9]+";
        return string.matches(regex);
    }
}
