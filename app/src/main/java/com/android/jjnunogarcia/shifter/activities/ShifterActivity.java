package com.android.jjnunogarcia.shifter.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.jjnunogarcia.shifter.DatePickerController;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.database.DBConstants;
import com.android.jjnunogarcia.shifter.database.ShifterProvider;
import com.android.jjnunogarcia.shifter.eventbus.OnDayClickEvent;
import com.android.jjnunogarcia.shifter.helpers.Utils;
import com.android.jjnunogarcia.shifter.model.DaySchedule;
import com.android.jjnunogarcia.shifter.views.DayPickerView;
import de.greenrobot.event.EventBus;

import java.util.AbstractList;
import java.util.GregorianCalendar;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShifterActivity extends ActionBarActivity implements DatePickerController, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG                     = ShifterActivity.class.getSimpleName();
    public static final int    DAY_SCHEDULES_LOADER_ID = 0;

    @InjectView(R.id.activity_shifter_picker_view) DayPickerView             dayPickerView;
    private                                        AbstractList<DaySchedule> daySchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifter);
        ButterKnife.inject(this);

        getSupportLoaderManager().initLoader(DAY_SCHEDULES_LOADER_ID, null, this);
        dayPickerView.setController(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_shifter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_shifter_action_show_shifts:
                ShiftsActivity.startNewActivity(getApplicationContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(), ShifterProvider.DAY_SCHEDULES_URI, DBConstants.DAY_SCHEDULES_PROJECTION, null, null, DBConstants.SORT_DAY_SCHEDULES_BY_ID_ASC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        daySchedules = Utils.getDaySchedules(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {}

    @Override
    public int getMaxYear() {
        return 2018;
    }

    public void onEvent(OnDayClickEvent onDayClickEvent) {
        Log.e("Day Selected", onDayClickEvent.getDay() + "/" + onDayClickEvent.getMonth() + "/" + onDayClickEvent.getYear());
        GregorianCalendar day = new GregorianCalendar(onDayClickEvent.getYear(), onDayClickEvent.getMonth(), onDayClickEvent.getDay());
        DaySchedule daySchedule = getDayScheduleFromDay(day);
        DayDetailActivity.startNewActivity(getApplicationContext(), daySchedule);
    }

    private DaySchedule getDayScheduleFromDay(GregorianCalendar day) {
        for (DaySchedule daySchedule : daySchedules) {
            if (daySchedule.getDate() == day.getTimeInMillis()) {
                return daySchedule;
            }
        }

        return new DaySchedule();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
