package com.android.jjnunogarcia.shifter.eventbus;

import com.android.jjnunogarcia.shifter.model.Shift;

/**
 * User: jesus
 * Date: 04/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShiftSavedEvent {
    private Shift shift;

    public ShiftSavedEvent(Shift shift) {
        this.shift = shift;
    }

    public Shift getShift() {
        return shift;
    }
}
