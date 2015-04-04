package com.android.jjnunogarcia.shifter.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.adapters.ShiftsAdapter;
import com.android.jjnunogarcia.shifter.database.DBConstants;
import com.android.jjnunogarcia.shifter.database.ShifterProvider;
import com.android.jjnunogarcia.shifter.eventbus.ShiftSavedEvent;
import com.android.jjnunogarcia.shifter.fragments.dialogs.ShiftDialog;
import com.android.jjnunogarcia.shifter.helpers.Utils;
import com.android.jjnunogarcia.shifter.model.Shift;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;

/**
 * User: jesus
 * Date: 04/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShiftsActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG              = DayDetailActivity.class.getSimpleName();
    public static final int    SHIFTS_LOADER_ID = 1;

    @InjectView(R.id.activity_shifts_shift_list) ListView shiftList;

    private ShiftsAdapter adapter;

    public static void startNewActivity(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), ShiftsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ShiftsAdapter(getApplicationContext());
        shiftList.setAdapter(adapter);
        getSupportLoaderManager().initLoader(SHIFTS_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(), ShifterProvider.SHIFTS_URI, DBConstants.SHIFTS_PROJECTION, null, null, DBConstants.SORT_SHIFTS_BY_ID_ASC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Shift> shifts = Utils.getShifts(data);
        adapter.setContent(shifts);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_shifts_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_shifts_action_create_shift:
                ShiftDialog.newInstance().show(getSupportFragmentManager().beginTransaction(), ShiftDialog.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEvent(ShiftSavedEvent shiftSavedEvent) {
        adapter.addContent(shiftSavedEvent.getShift());
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
