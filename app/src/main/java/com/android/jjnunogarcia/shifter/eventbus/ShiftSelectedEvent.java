package com.android.jjnunogarcia.shifter.eventbus;

import com.android.jjnunogarcia.shifter.model.Shift;

/**
 * User: jesus
 * Date: 07/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShiftSelectedEvent {
    private Shift   shift;
    private boolean selected;

    public ShiftSelectedEvent(Shift shift, boolean selected) {
        this.shift = shift;
        this.selected = selected;
    }

    public Shift getShift() {
        return shift;
    }

    public boolean isSelected() {
        return selected;
    }
}
