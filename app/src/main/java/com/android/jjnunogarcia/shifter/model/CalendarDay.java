package com.android.jjnunogarcia.shifter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class CalendarDay implements Parcelable {
    private Calendar calendar;
    private int      day;
    private int      month;
    private int      year;

    public static final Creator<CalendarDay> CREATOR;

    static {
        CREATOR = new Creator<CalendarDay>() {

            @Override
            public CalendarDay createFromParcel(Parcel source) {
                return new CalendarDay(source);
            }

            @Override
            public CalendarDay[] newArray(int size) {
                return new CalendarDay[size];
            }
        };
    }

    public CalendarDay() {
        setTime(System.currentTimeMillis());
    }

    public CalendarDay(int year, int month, int day) {
        setDay(year, month, day);
    }

    public CalendarDay(long timeInMillis) {
        setTime(timeInMillis);
    }

    public CalendarDay(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private CalendarDay(Parcel source) {
        this();
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(calendar);
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
    }

    private void readFromParcel(Parcel source) {
        calendar = (Calendar) source.readSerializable();
        day = source.readInt();
        month = source.readInt();
        year = source.readInt();
    }

    private void setTime(long timeInMillis) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(timeInMillis);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
    }

    public void set(CalendarDay calendarDay) {
        day = calendarDay.getDay();
        month = calendarDay.getMonth();
        year = calendarDay.getYear();
    }

    public void setDay(int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date getDate() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "{ year: " + year + ", month: " + month + ", day: " + day + " }";
    }
}
