package com.android.jjnunogarcia.shifter.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.eventbus.OnDayClickEvent;
import com.android.jjnunogarcia.shifter.helpers.CalendarUtils;
import com.android.jjnunogarcia.shifter.model.CalendarDay;
import de.greenrobot.event.EventBus;

import java.security.InvalidParameterException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class SimpleMonthView extends View {
    public static final String TAG                              = SimpleMonthView.class.getSimpleName();
    public static final String VIEW_PARAMS_HEIGHT               = "height";
    public static final String VIEW_PARAMS_MONTH                = "month";
    public static final String VIEW_PARAMS_YEAR                 = "year";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_DAY   = "selected_begin_day";
    public static final String VIEW_PARAMS_SELECTED_LAST_DAY    = "selected_last_day";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_MONTH = "selected_begin_month";
    public static final String VIEW_PARAMS_SELECTED_LAST_MONTH  = "selected_last_month";
    public static final String VIEW_PARAMS_SELECTED_BEGIN_YEAR  = "selected_begin_year";
    public static final String VIEW_PARAMS_SELECTED_LAST_YEAR   = "selected_last_year";
    public static final String VIEW_PARAMS_WEEK_START           = "week_start";

    private static final   int SELECTED_CIRCLE_ALPHA = 128;
    protected static final int DEFAULT_HEIGHT        = 32;
    protected static final int DEFAULT_NUM_ROWS      = 6;
    protected static int DAY_SELECTED_CIRCLE_SIZE;
    protected static int DAY_SEPARATOR_WIDTH = 1;
    protected static int MINI_DAY_NUMBER_TEXT_SIZE;
    protected static int MIN_HEIGHT = 10;
    protected static int MONTH_DAY_LABEL_TEXT_SIZE;
    protected static int MONTH_HEADER_SIZE;
    protected static int MONTH_LABEL_TEXT_SIZE;

    protected int padding = 0;

    private String dayOfWeekTypeface;
    private String monthTitleTypeface;

    protected Paint monthDayLabelPaint;
    protected Paint monthNumPaint;
    protected Paint monthTitleBGPaint;
    protected Paint monthTitlePaint;
    protected Paint dayCirclePaint;
    protected int   monthTextColor;
    protected int   dayTextColor;
    protected int   monthTitleBGColor;
    protected int   selectedDaysColor;

    protected boolean hasToday           = false;
    protected boolean isPrev             = false;
    protected int     selectedBeginDay   = -1;
    protected int     selectedLastDay    = -1;
    protected int     selectedBeginMonth = -1;
    protected int     selectedLastMonth  = -1;
    protected int     selectedBeginYear  = -1;
    protected int     selectedLastYear   = -1;
    protected int     today              = -1;
    protected int     weekStart          = 1;
    protected int     daysPerWeek        = 7;
    protected int     numCells           = daysPerWeek;
    private   int     dayOfWeekStart     = 0;
    protected int month;
    protected int rowHeight = DEFAULT_HEIGHT;
    protected       int  width;
    protected       int  year;
    protected final Time todayTime;

    private final Calendar calendar;
    private final Calendar dayLabelCalendar;
    private final Boolean  isPrevDayEnabled;

    private int numRows = DEFAULT_NUM_ROWS;

    private DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

    public SimpleMonthView(Context context, TypedArray typedArray) {
        super(context);

        Resources resources = context.getResources();
        dayLabelCalendar = Calendar.getInstance();
        calendar = Calendar.getInstance();
        todayTime = new Time(Time.getCurrentTimezone());
        todayTime.setToNow();
        dayOfWeekTypeface = resources.getString(R.string.sans_serif);
        monthTitleTypeface = resources.getString(R.string.sans_serif);
        monthTextColor = typedArray.getColor(R.styleable.DayPickerView_colorMonthName, resources.getColor(R.color.normal_day));
        dayTextColor = typedArray.getColor(R.styleable.DayPickerView_colorDayName, resources.getColor(R.color.normal_day));
        selectedDaysColor = typedArray.getColor(R.styleable.DayPickerView_colorSelectedDayBackground, resources.getColor(R.color.selected_day_background));
        monthTitleBGColor = typedArray.getColor(R.styleable.DayPickerView_colorSelectedDayText, resources.getColor(R.color.selected_day_text));

        MINI_DAY_NUMBER_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeDay, resources.getDimensionPixelSize(R.dimen.text_size_day));
        MONTH_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeMonth, resources.getDimensionPixelSize(R.dimen.text_size_month));
        MONTH_DAY_LABEL_TEXT_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_textSizeDayName, resources.getDimensionPixelSize(R.dimen.text_size_day_name));
        MONTH_HEADER_SIZE = typedArray.getDimensionPixelOffset(R.styleable.DayPickerView_headerMonthHeight, resources.getDimensionPixelOffset(R.dimen.header_month_height));
        DAY_SELECTED_CIRCLE_SIZE = typedArray.getDimensionPixelSize(R.styleable.DayPickerView_selectedDayRadius, resources.getDimensionPixelOffset(R.dimen.selected_day_radius));

        rowHeight = (typedArray.getDimensionPixelSize(R.styleable.DayPickerView_calendarHeight, resources.getDimensionPixelOffset(R.dimen.calendar_height)) - MONTH_HEADER_SIZE) / 6;

        isPrevDayEnabled = typedArray.getBoolean(R.styleable.DayPickerView_enablePreviousDay, true);

        initView();
    }

    private void initView() {
        monthTitlePaint = new Paint();
        monthTitlePaint.setFakeBoldText(true);
        monthTitlePaint.setAntiAlias(true);
        monthTitlePaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
        monthTitlePaint.setTypeface(Typeface.create(monthTitleTypeface, Typeface.BOLD));
        monthTitlePaint.setColor(monthTextColor);
        monthTitlePaint.setTextAlign(Align.CENTER);
        monthTitlePaint.setStyle(Style.FILL);

        monthTitleBGPaint = new Paint();
        monthTitleBGPaint.setFakeBoldText(true);
        monthTitleBGPaint.setAntiAlias(true);
        monthTitleBGPaint.setColor(monthTitleBGColor);
        monthTitleBGPaint.setTextAlign(Align.CENTER);
        monthTitleBGPaint.setStyle(Style.FILL);

        monthDayLabelPaint = new Paint();
        monthDayLabelPaint.setAntiAlias(true);
        monthDayLabelPaint.setTextSize(MONTH_DAY_LABEL_TEXT_SIZE);
        monthDayLabelPaint.setColor(dayTextColor);
        monthDayLabelPaint.setTypeface(Typeface.create(dayOfWeekTypeface, Typeface.NORMAL));
        monthDayLabelPaint.setStyle(Style.FILL);
        monthDayLabelPaint.setTextAlign(Align.CENTER);
        monthDayLabelPaint.setFakeBoldText(true);

        dayCirclePaint = new Paint();
        dayCirclePaint.setAntiAlias(true);
        dayCirclePaint.setColor(selectedDaysColor);
        dayCirclePaint.setStyle(Style.STROKE);

        monthNumPaint = new Paint();
        monthNumPaint.setAntiAlias(true);
        monthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        monthNumPaint.setStyle(Style.FILL);
        monthNumPaint.setTextAlign(Align.CENTER);
        monthNumPaint.setFakeBoldText(false);
    }

    private int calculateNumRows() {
        int offset = findDayOffset();
        int dividend = (offset + numCells) / daysPerWeek;
        int remainder = (offset + numCells) % daysPerWeek;
        return dividend + (remainder > 0 ? 1 : 0);
    }

    private void drawMonthDayLabels(Canvas canvas) {
        int y = MONTH_HEADER_SIZE - (MONTH_DAY_LABEL_TEXT_SIZE >> 1);
        int dayWidthHalf = (width - padding * 2) / (daysPerWeek * 2);

        for (int i = 0; i < daysPerWeek; i++) {
            int calendarDay = (i + weekStart) % daysPerWeek;
            int x = (2 * i + 1) * dayWidthHalf + padding;
            dayLabelCalendar.set(Calendar.DAY_OF_WEEK, calendarDay);
            canvas.drawText(dateFormatSymbols.getShortWeekdays()[dayLabelCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(Locale.getDefault()), x, y, monthDayLabelPaint);
        }
    }

    private void drawMonthTitle(Canvas canvas) {
        int x = (width + 2 * padding) / 2;
        int y = ((MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) >> 1) + (MONTH_LABEL_TEXT_SIZE / 3);
        StringBuilder stringBuilder = new StringBuilder(getMonthAndYearString().toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        canvas.drawText(stringBuilder.toString(), x, y, monthTitlePaint);
    }

    private int findDayOffset() {
        return (dayOfWeekStart < weekStart ? (dayOfWeekStart + daysPerWeek) : dayOfWeekStart) - weekStart;
    }

    private String getMonthAndYearString() {
        long millis = calendar.getTimeInMillis();
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }

    private void onDayClick(CalendarDay calendarDay) {
        if (isPrevDayEnabled || !((calendarDay.getMonth() == todayTime.month) && (calendarDay.getYear() == todayTime.year) && calendarDay.getDay() < todayTime.monthDay)) {
            if (calendarDay != null) {
                EventBus.getDefault().post(new OnDayClickEvent(calendarDay.getDay(), calendarDay.getMonth(), calendarDay.getYear()));
            }
        }
    }

    private boolean sameDay(int monthDay, Time time) {
        return (year == time.year) && (month == time.month) && (monthDay == time.monthDay);
    }

    private boolean prevDay(int monthDay, Time time) {
        return ((year < time.year)) || (year == time.year && month < time.month) || (month == time.month && monthDay < time.monthDay);
    }

    protected void drawMonthNums(Canvas canvas) {
        int y = (rowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 - DAY_SEPARATOR_WIDTH + MONTH_HEADER_SIZE;
        int paddingDay = (width - 2 * padding) / (2 * daysPerWeek);
        int dayOffset = findDayOffset();
        int day = 1;

        while (day <= numCells) {
            int x = paddingDay * (1 + dayOffset * 2) + padding;

//            DayView dayView = new DayView(getContext());
//            dayView.setText(String.valueOf(day));
//            dayView.draw(canvas);
            canvas.drawCircle(x, y, 40, dayCirclePaint);
            canvas.drawText(String.format("%d", day), x, y, monthNumPaint);

            dayOffset++;
            if (dayOffset == daysPerWeek) {
                dayOffset = 0;
                y += rowHeight;
            }
            day++;
        }
    }

    public CalendarDay getDayFromLocation(float x, float y) {
        if ((x < padding) || (x > width - padding)) {
            return null;
        }

        int yDay = (int) (y - MONTH_HEADER_SIZE) / rowHeight;
        int day = 1 + ((int) ((x - padding) * daysPerWeek / (width - padding - padding)) - findDayOffset()) + yDay * daysPerWeek;

        if (month > 11 || month < 0 || CalendarUtils.getDaysInMonth(month, year) < day || day < 1) {
            return null;
        }

        return new CalendarDay(year, month, day);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMonthTitle(canvas);
        drawMonthDayLabels(canvas);
        drawMonthNums(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), rowHeight * numRows + MONTH_HEADER_SIZE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            CalendarDay calendarDay = getDayFromLocation(event.getX(), event.getY());
            if (calendarDay != null) {
                onDayClick(calendarDay);
            }
        }
        return true;
    }

    public void reuse() {
        numRows = DEFAULT_NUM_ROWS;
        requestLayout();
    }

    public void setMonthParams(HashMap<String, Integer> params) {
        if (!params.containsKey(VIEW_PARAMS_MONTH) && !params.containsKey(VIEW_PARAMS_YEAR)) {
            throw new InvalidParameterException("You must specify month and year for this view");
        }
        setTag(params);

        if (params.containsKey(VIEW_PARAMS_HEIGHT)) {
            rowHeight = params.get(VIEW_PARAMS_HEIGHT);
            if (rowHeight < MIN_HEIGHT) {
                rowHeight = MIN_HEIGHT;
            }
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_DAY)) {
            selectedBeginDay = params.get(VIEW_PARAMS_SELECTED_BEGIN_DAY);
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_DAY)) {
            selectedLastDay = params.get(VIEW_PARAMS_SELECTED_LAST_DAY);
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_MONTH)) {
            selectedBeginMonth = params.get(VIEW_PARAMS_SELECTED_BEGIN_MONTH);
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_MONTH)) {
            selectedLastMonth = params.get(VIEW_PARAMS_SELECTED_LAST_MONTH);
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_BEGIN_YEAR)) {
            selectedBeginYear = params.get(VIEW_PARAMS_SELECTED_BEGIN_YEAR);
        }
        if (params.containsKey(VIEW_PARAMS_SELECTED_LAST_YEAR)) {
            selectedLastYear = params.get(VIEW_PARAMS_SELECTED_LAST_YEAR);
        }

        month = params.get(VIEW_PARAMS_MONTH);
        year = params.get(VIEW_PARAMS_YEAR);

        hasToday = false;
        today = -1;

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dayOfWeekStart = calendar.get(Calendar.DAY_OF_WEEK);

        if (params.containsKey(VIEW_PARAMS_WEEK_START)) {
            weekStart = params.get(VIEW_PARAMS_WEEK_START);
        } else {
            weekStart = calendar.getFirstDayOfWeek();
        }

        numCells = CalendarUtils.getDaysInMonth(month, year);
        for (int i = 0; i < numCells; i++) {
            int day = i + 1;
            if (sameDay(day, todayTime)) {
                hasToday = true;
                today = day;
            }

            isPrev = prevDay(day, todayTime);
        }

        numRows = calculateNumRows();
    }
}