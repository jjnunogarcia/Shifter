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
import com.android.jjnunogarcia.shifter.helpers.Utils;
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
    public static final String TAG                    = SimpleMonthView.class.getSimpleName();
    public static final String VIEW_PARAMS_MONTH      = "month";
    public static final String VIEW_PARAMS_YEAR       = "year";
    public static final String VIEW_PARAMS_WEEK_START = "week_start";

    protected static final int DEFAULT_HEIGHT   = 32;
    protected static final int DEFAULT_NUM_ROWS = 6;
    protected static int DAY_SELECTED_CIRCLE_SIZE;
    protected static int MINI_DAY_NUMBER_TEXT_SIZE;
    protected static int MIN_HEIGHT = 10;
    protected static int MONTH_DAY_LABEL_TEXT_SIZE;
    protected static int MONTH_HEADER_SIZE;
    protected static int MONTH_LABEL_TEXT_SIZE;

    private   String dayOfWeekTypeface;
    private   String monthTitleTypeface;
    protected Paint  weekDayNamePaint;
    protected Paint  monthNumPaint;
    protected Paint  monthNamePaint;
    protected Paint  dayCirclePaint;
    protected int    monthTextColor;
    protected int    dayTextColor;
    protected int    monthTitleBGColor;
    protected int    selectedDaysColor;

    protected int               today             = -1;
    protected int               padding           = 0;
    protected int               weekStart         = 1;
    protected int               daysPerWeek       = 7;
    protected int               numCells          = daysPerWeek;
    private   int               dayOfWeekStart    = 0;
    private   DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();

    protected int rowHeight = DEFAULT_HEIGHT;
    private   int numRows   = DEFAULT_NUM_ROWS;
    protected       int      month;
    protected       int      width;
    protected       int      year;
    protected final Time     todayTime;
    private final   Calendar calendar;
    private final   Calendar weekDayNameCalendar;


    public SimpleMonthView(Context context, TypedArray typedArray) {
        super(context);

        Resources resources = context.getResources();
        weekDayNameCalendar = Calendar.getInstance();
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

        initView();
    }

    private void initView() {
        monthNamePaint = new Paint();
        monthNamePaint.setFakeBoldText(true);
        monthNamePaint.setAntiAlias(true);
        monthNamePaint.setTextSize(MONTH_LABEL_TEXT_SIZE);
        monthNamePaint.setTypeface(Typeface.create(monthTitleTypeface, Typeface.BOLD));
        monthNamePaint.setColor(monthTextColor);
        monthNamePaint.setTextAlign(Align.CENTER);
        monthNamePaint.setStyle(Style.FILL);

        weekDayNamePaint = new Paint();
        weekDayNamePaint.setAntiAlias(true);
        weekDayNamePaint.setTextSize(MONTH_DAY_LABEL_TEXT_SIZE);
        weekDayNamePaint.setColor(dayTextColor);
        weekDayNamePaint.setTypeface(Typeface.create(dayOfWeekTypeface, Typeface.NORMAL));
        weekDayNamePaint.setStyle(Style.FILL);
        weekDayNamePaint.setTextAlign(Align.CENTER);
        weekDayNamePaint.setFakeBoldText(true);

        dayCirclePaint = new Paint();
        dayCirclePaint.setAntiAlias(true);
        dayCirclePaint.setColor(selectedDaysColor);
        dayCirclePaint.setStyle(Style.STROKE);

        monthNumPaint = new Paint();
        monthNumPaint.setAntiAlias(true);
        monthNumPaint.setTextSize(MINI_DAY_NUMBER_TEXT_SIZE);
        monthNumPaint.setStyle(Style.FILL);
        monthNumPaint.setTextAlign(Align.CENTER);
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
    protected void onDraw(Canvas canvas) {
        drawMonthName(canvas);
        drawWeekDayName(canvas);
        drawMonthNums(canvas);
    }

    private void drawMonthName(Canvas canvas) {
        int x = (width + 2 * padding) / 2;
        int y = ((MONTH_HEADER_SIZE - MONTH_DAY_LABEL_TEXT_SIZE) >> 1) + (MONTH_LABEL_TEXT_SIZE / 3);
        StringBuilder stringBuilder = new StringBuilder(getMonthAndYearString().toLowerCase());
        stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        canvas.drawText(stringBuilder.toString(), x, y, monthNamePaint);
    }

    private void drawWeekDayName(Canvas canvas) {
        int y = MONTH_HEADER_SIZE - (MONTH_DAY_LABEL_TEXT_SIZE >> 1);
        int dayWidthHalf = (width - 2 * padding) / (2 * daysPerWeek);

        for (int i = 0; i < daysPerWeek; i++) {
            int calendarDay = (i + weekStart) % daysPerWeek;
            int x = (2 * i + 1) * dayWidthHalf + padding;
            weekDayNameCalendar.set(Calendar.DAY_OF_WEEK, calendarDay);
            canvas.drawText(dateFormatSymbols.getShortWeekdays()[weekDayNameCalendar.get(Calendar.DAY_OF_WEEK)].toUpperCase(Locale.getDefault()), x, y, weekDayNamePaint);
        }
    }

    private void drawMonthNums(Canvas canvas) {
        int textY = (rowHeight + MINI_DAY_NUMBER_TEXT_SIZE) / 2 + MONTH_HEADER_SIZE;
        int circleY = rowHeight / 2 + MONTH_HEADER_SIZE;
        int dayWidthHalf = (width - 2 * padding) / (2 * daysPerWeek);
        int dayOffset = findDayOffset();

        for (int day = 1; day <= numCells; day++) {
            int x = dayWidthHalf * (1 + dayOffset * 2) + padding;

//            DayView dayView = new DayView(getContext());
//            dayView.setText(String.valueOf(day));
//            dayView.draw(canvas);
            canvas.drawCircle(x, circleY, 50, dayCirclePaint);
            canvas.drawText(String.format("%d", day), x, textY, monthNumPaint);

            dayOffset++;
            if (dayOffset == daysPerWeek) {
                dayOffset = 0;
                textY += rowHeight;
                circleY += rowHeight;
            }
        }
    }

    private int findDayOffset() {
        return (dayOfWeekStart < weekStart ? dayOfWeekStart + daysPerWeek : dayOfWeekStart) - weekStart;
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

    private String getMonthAndYearString() {
        long millis = calendar.getTimeInMillis();
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY;
        return DateUtils.formatDateRange(getContext(), millis, millis, flags);
    }

    private void onDayClick(CalendarDay calendarDay) {
        if (calendarDay != null) {
            EventBus.getDefault().post(new OnDayClickEvent(calendarDay.getDay(), calendarDay.getMonth(), calendarDay.getYear()));
        }
    }

    private boolean sameDay(int monthDay, Time time) {
        return (year == time.year) && (month == time.month) && (monthDay == time.monthDay);
    }

    public CalendarDay getDayFromLocation(float x, float y) {
        if ((x <= padding) || (x >= width - 2 * padding)) {
            return null;
        }

        int row = (int) (y - MONTH_HEADER_SIZE) / rowHeight;
        int day = 1 + ((int) ((x - padding) * daysPerWeek / (width - 2 * padding)) - findDayOffset()) + row * daysPerWeek;

        if (month > 11 || month < 0 || Utils.getDaysInMonth(month, year) < day || day < 1) {
            return null;
        }

        return new CalendarDay(year, month, day);
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

        month = params.get(VIEW_PARAMS_MONTH);
        year = params.get(VIEW_PARAMS_YEAR);

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

        numCells = Utils.getDaysInMonth(month, year);
        for (int i = 0; i < numCells; i++) {
            int day = i + 1;
            if (sameDay(day, todayTime)) {
                today = day;
            }

        }

        numRows = calculateNumRows();

        invalidate();
    }

    private int calculateNumRows() {
        int offset = findDayOffset();
        int dividend = (offset + numCells) / daysPerWeek;
        int remainder = (offset + numCells) % daysPerWeek;
        return dividend + (remainder > 0 ? 1 : 0);
    }
}