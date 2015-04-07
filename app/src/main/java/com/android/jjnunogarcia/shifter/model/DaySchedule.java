package com.android.jjnunogarcia.shifter.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class DaySchedule implements Parcelable {
    private int              id;
    private long             date;
    private ArrayList<Shift> shifts;

    public static final Creator<DaySchedule> CREATOR;

    static {
        CREATOR = new Creator<DaySchedule>() {

            @Override
            public DaySchedule createFromParcel(Parcel source) {
                return new DaySchedule(source);
            }

            @Override
            public DaySchedule[] newArray(int size) {
                return new DaySchedule[size];
            }
        };
    }

    public DaySchedule() {
        shifts = new ArrayList<>();
    }

    public DaySchedule(DaySchedule daySchedule) {
        shifts = new ArrayList<>();
        id = daySchedule.getId();
        date = daySchedule.getDate();
        shifts.addAll(daySchedule.getShifts());
    }

    private DaySchedule(Parcel source) {
        this();
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(date);
        dest.writeTypedList(shifts);
    }

    private void readFromParcel(Parcel source) {
        id = source.readInt();
        date = source.readLong();
        source.readTypedList(shifts, Shift.CREATOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DaySchedule that = (DaySchedule) o;

        if (id != that.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(ArrayList<Shift> shifts) {
        this.shifts = shifts;
    }
}
