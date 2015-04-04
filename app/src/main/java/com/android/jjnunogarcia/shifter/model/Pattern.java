package com.android.jjnunogarcia.shifter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class Pattern implements Parcelable {
    private int id;

    public static final Creator<Pattern> CREATOR;

    static {
        CREATOR = new Creator<Pattern>() {

            @Override
            public Pattern createFromParcel(Parcel source) {
                return new Pattern(source);
            }

            @Override
            public Pattern[] newArray(int size) {
                return new Pattern[size];
            }
        };
    }

    public Pattern() {}

    private Pattern(Parcel source) {
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
    }

    private void readFromParcel(Parcel source) {
        id = source.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
