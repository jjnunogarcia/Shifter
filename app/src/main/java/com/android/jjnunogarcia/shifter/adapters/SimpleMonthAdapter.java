package com.android.jjnunogarcia.shifter.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import com.android.jjnunogarcia.shifter.DatePickerController;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.views.SimpleMonthView;

import java.util.Calendar;
import java.util.HashMap;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class SimpleMonthAdapter extends RecyclerView.Adapter<SimpleMonthAdapter.CalendarViewHolder> {
    private static final int MONTHS_IN_YEAR = 12;

    private final TypedArray           typedArray;
    private final Context              context;
    private final DatePickerController controller;
    private final Calendar             calendar;
    private final Integer              firstMonth;
    private final Integer              lastMonth;

    public SimpleMonthAdapter(Context context, DatePickerController datePickerController, TypedArray typedArray) {
        this.typedArray = typedArray;
        calendar = Calendar.getInstance();
        firstMonth = typedArray.getInt(R.styleable.DayPickerView_firstMonth, calendar.get(Calendar.MONTH));
        lastMonth = typedArray.getInt(R.styleable.DayPickerView_lastMonth, (calendar.get(Calendar.MONTH) - 1) % MONTHS_IN_YEAR);
        this.context = context;
        controller = datePickerController;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        SimpleMonthView simpleMonthView = new SimpleMonthView(context, typedArray);
        return new CalendarViewHolder(simpleMonthView);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder calendarViewHolder, int position) {
        SimpleMonthView v = calendarViewHolder.simpleMonthView;
        HashMap<String, Integer> drawingParams = new HashMap<>();

        int month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        int year = position / MONTHS_IN_YEAR + calendar.get(Calendar.YEAR) + ((firstMonth + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);

        v.reuse();

        drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        v.setMonthParams(drawingParams);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int itemCount = (controller.getMaxYear() - calendar.get(Calendar.YEAR) + 1) * MONTHS_IN_YEAR;

        if (firstMonth != -1) {
            itemCount -= firstMonth;
        }

        if (lastMonth != -1) {
            itemCount -= MONTHS_IN_YEAR - lastMonth - 1;
        }

        return itemCount;
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        private SimpleMonthView simpleMonthView;

        public CalendarViewHolder(SimpleMonthView itemView) {
            super(itemView);
            simpleMonthView = itemView;
            simpleMonthView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            simpleMonthView.setClickable(true);
        }
    }
}