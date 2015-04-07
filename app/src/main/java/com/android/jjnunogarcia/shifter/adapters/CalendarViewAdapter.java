package com.android.jjnunogarcia.shifter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import com.android.jjnunogarcia.shifter.DatePickerController;
import com.android.jjnunogarcia.shifter.model.DaySchedule;
import com.android.jjnunogarcia.shifter.views.SimpleMonthView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewAdapter.CalendarViewHolder> {
    private static final int MONTHS_IN_YEAR = 12;

    private Context                context;
    private DatePickerController   controller;
    private Calendar               calendar;
    private Integer                firstMonth;
    private Integer                lastMonth;
    private ArrayList<DaySchedule> daySchedules;

    public CalendarViewAdapter(Context context, DatePickerController datePickerController) {
        this.context = context;
        daySchedules = new ArrayList<>();
        calendar = Calendar.getInstance();
        firstMonth = calendar.get(Calendar.MONTH);
        lastMonth = (calendar.get(Calendar.MONTH) - 1) % MONTHS_IN_YEAR;
        controller = datePickerController;
    }

    public void setDaySchedules(ArrayList<DaySchedule> daySchedules) {
        this.daySchedules = daySchedules;
        notifyDataSetChanged();
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        SimpleMonthView simpleMonthView = new SimpleMonthView(context);
        return new CalendarViewHolder(simpleMonthView);
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder calendarViewHolder, int position) {
        SimpleMonthView v = calendarViewHolder.simpleMonthView;
        HashMap<String, Integer> drawingParams = new HashMap<>();

        int month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        int year = position / MONTHS_IN_YEAR + calendar.get(Calendar.YEAR) + (firstMonth + position % MONTHS_IN_YEAR) / MONTHS_IN_YEAR;
        ArrayList<DaySchedule> daySchedulesForDate = getDaySchedulesForDate(month, year);

        drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        v.setMonthParams(drawingParams);
        v.setDaySchedules(daySchedulesForDate);
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

    private ArrayList<DaySchedule> getDaySchedulesForDate(int month, int year) {
        ArrayList<DaySchedule> daySchedulesForDate = new ArrayList<>();
        GregorianCalendar dateToCheck = new GregorianCalendar();

        for (DaySchedule daySchedule : daySchedules) {
            dateToCheck.setTimeInMillis(daySchedule.getDate());
            int monthToCheck = dateToCheck.get(GregorianCalendar.MONTH);
            int yearToCheck = dateToCheck.get(GregorianCalendar.YEAR);

            if (monthToCheck == month && yearToCheck == year) {
                daySchedulesForDate.add(daySchedule);
            }
        }

        return daySchedulesForDate;
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        private SimpleMonthView simpleMonthView;

        CalendarViewHolder(SimpleMonthView itemView) {
            super(itemView);
            simpleMonthView = itemView;
            simpleMonthView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            simpleMonthView.setClickable(true);
        }
    }
}