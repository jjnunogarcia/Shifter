package com.android.jjnunogarcia.shifter.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.android.jjnunogarcia.shifter.R;

/**
 * User: jesus
 * Date: 02/04/15
 *
 * @author j.nuno@klara.com
 */
public class DayView extends View {

    private Paint  circlePaint;
    private Paint  numberBorderPaint;
    private Paint  numberInsidePaint;
    private int    circleRadius;
    private String text;

    public DayView(Context context) {
        this(context, null);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        text = "27";

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(R.styleable.DayView);
            circleRadius = typedArray.getInteger(R.styleable.DayView_circleRadius, (int) getResources().getDimension(R.dimen.day_view_circle_radius));
            text = typedArray.getString(R.styleable.DayView_circleText);
            typedArray.recycle();
        }

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(15);
        circlePaint.setColor(Color.BLACK);

        numberInsidePaint = new Paint();
        numberInsidePaint.setAntiAlias(true);
        numberInsidePaint.setStyle(Paint.Style.FILL);
        numberInsidePaint.setColor(Color.MAGENTA);

        numberBorderPaint = new Paint();
        numberBorderPaint.setAntiAlias(true);
        numberBorderPaint.setStyle(Paint.Style.STROKE);
        numberBorderPaint.setColor(Color.CYAN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(circlePaint);
        int x = getWidth();
        int y = getHeight();
        canvas.drawCircle(x / 2, y / 2, circleRadius, circlePaint);

        int xPos = canvas.getWidth() / 2;
        int yPos = (int) (canvas.getHeight() / 2 - (numberInsidePaint.descent() + numberInsidePaint.ascent()) / 2);
        canvas.drawText(text, xPos, yPos, numberInsidePaint);
        canvas.drawText(text, xPos, yPos, numberBorderPaint);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }
}
