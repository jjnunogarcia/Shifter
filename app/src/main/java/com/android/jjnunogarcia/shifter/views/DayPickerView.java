package com.android.jjnunogarcia.shifter.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import com.android.jjnunogarcia.shifter.DatePickerController;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.adapters.SimpleMonthAdapter;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class DayPickerView extends RecyclerView {
    public static final String TAG = DayPickerView.class.getSimpleName();

    private Context              context;
    private SimpleMonthAdapter   adapter;
    private DatePickerController controller;
    private int                  currentScrollState;
    private long                 previousScrollPosition;
    private int                  previousScrollState;
    private TypedArray           typedArray;
    private OnScrollListener     onScrollListener;

    public DayPickerView(Context context) {
        this(context, null);
    }

    public DayPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        currentScrollState = 0;
        previousScrollState = 0;

        if (!isInEditMode()) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);
//            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            init(context);
        }
    }

    public void init(Context context) {
        this.context = context;
        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(context));
        setUpListView();

        // TODO what is this for? Also, extract class
        onScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                SimpleMonthView child = (SimpleMonthView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }

                previousScrollPosition = dy;
                previousScrollState = currentScrollState;
            }
        };
    }

    protected void setUpListView() {
        setVerticalScrollBarEnabled(false);
        setOnScrollListener(onScrollListener);
        setFadingEdgeLength(0);
    }

    public void setController(DatePickerController controller) {
        this.controller = controller;
        setUpAdapter();
        setAdapter(adapter);
    }

    protected void setUpAdapter() {
        if (adapter == null) {
            adapter = new SimpleMonthAdapter(getContext(), controller, typedArray);
        }
        adapter.notifyDataSetChanged();
    }

    protected DatePickerController getController() {
        return controller;
    }

    protected TypedArray getTypedArray() {
        return typedArray;
    }
}