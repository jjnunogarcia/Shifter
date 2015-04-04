package com.android.jjnunogarcia.shifter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.adapters.DayDetailShiftsAdapter;
import com.android.jjnunogarcia.shifter.model.DaySchedule;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class DayDetailActivity extends ActionBarActivity {
    public static final String TAG              = DayDetailActivity.class.getSimpleName();
    public static final String KEY_DAY_SCHEDULE = "key_day_schedule";

    @InjectView(R.id.activity_day_detail_shift_list) ListView scheduleList;

    public static void startNewActivity(Context context, DaySchedule daySchedule) {
        Intent intent = new Intent(context.getApplicationContext(), DayDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle args = new Bundle();
        args.putParcelable(KEY_DAY_SCHEDULE, daySchedule);
        intent.putExtras(args);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();

        if (args != null && args.containsKey(KEY_DAY_SCHEDULE)) {
            DaySchedule daySchedule = args.getParcelable(KEY_DAY_SCHEDULE);
            DayDetailShiftsAdapter adapter = new DayDetailShiftsAdapter(getApplicationContext());
            adapter.setContent(daySchedule.getShifts());
            scheduleList.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_day_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_day_detail_action_add_shift:
                // TODO Dialog or whatever
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
