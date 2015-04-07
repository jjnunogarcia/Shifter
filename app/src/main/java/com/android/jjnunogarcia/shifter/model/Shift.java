package com.android.jjnunogarcia.shifter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class Shift implements Parcelable {
    public static final int NO_ID = -1;

    private int    id;
    private String name;
    private String description;
    private int    start;
    private int    duration;
    private String location;
    private int    color;

    public static final Creator<Shift> CREATOR;

    static {
        CREATOR = new Creator<Shift>() {

            @Override
            public Shift createFromParcel(Parcel source) {
                return new Shift(source);
            }

            @Override
            public Shift[] newArray(int size) {
                return new Shift[size];
            }
        };
    }

    public Shift() {
        id = NO_ID;
    }

    private Shift(Parcel source) {
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
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(start);
        dest.writeInt(duration);
        dest.writeString(location);
        dest.writeInt(color);
    }

    private void readFromParcel(Parcel source) {
        id = source.readInt();
        name = source.readString();
        description = source.readString();
        start = source.readInt();
        duration = source.readInt();
        location = source.readString();
        color = source.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Shift shift = (Shift) o;

        if (id != shift.id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
