package com.android.jjnunogarcia.shifter.eventbus;

/**
 * User: jesus
 * Date: 02/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class OnDayClickEvent {
    private int day;
    private int month;
    private int year;

    public OnDayClickEvent(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
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
}
