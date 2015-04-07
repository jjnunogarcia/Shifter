package com.android.jjnunogarcia.shifter.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class CalendarView extends RecyclerView {
    public static final String TAG = CalendarView.class.getSimpleName();

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            setHasFixedSize(true);
            setLayoutManager(new LinearLayoutManager(context));
            setVerticalScrollBarEnabled(false);
            setFadingEdgeLength(0);
        }
    }
}