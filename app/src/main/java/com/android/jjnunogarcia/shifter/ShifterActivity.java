package com.android.jjnunogarcia.shifter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.jjnunogarcia.shifter.eventbus.OnDayClickEvent;
import com.android.jjnunogarcia.shifter.views.DayPickerView;
import de.greenrobot.event.EventBus;

/**
 * User: jesus
 * Date: 24/03/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShifterActivity extends ActionBarActivity implements DatePickerController {
    public static final String TAG = ShifterActivity.class.getSimpleName();

    @InjectView(R.id.activity_shifter_picker_view) DayPickerView dayPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifter);
        ButterKnife.inject(this);

        dayPickerView.setController(this);
    }

    @Override
    public int getMaxYear() {
        return 2018;
    }

    public void onEvent(OnDayClickEvent onDayClickEvent) {
        Log.e("Day Selected", onDayClickEvent.getDay() + "/" + onDayClickEvent.getMonth() + "/" + onDayClickEvent.getYear());
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
