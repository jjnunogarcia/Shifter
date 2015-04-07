package com.android.jjnunogarcia.shifter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.eventbus.ShiftSelectedEvent;
import com.android.jjnunogarcia.shifter.model.DaySchedule;
import com.android.jjnunogarcia.shifter.model.Shift;
import de.greenrobot.event.EventBus;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class SelectShiftAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private Context             context;
    private AbstractList<Shift> shifts;
    private DaySchedule         daySchedule;

    public SelectShiftAdapter(Context context) {
        this.context = context;
        shifts = new ArrayList<>();
        daySchedule = new DaySchedule();
    }

    public void setContent(DaySchedule daySchedule, AbstractList<Shift> shifts) {
        this.daySchedule = daySchedule;
        this.shifts = shifts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return shifts.size();
    }

    @Override
    public Shift getItem(int position) {
        return shifts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SelectShiftViewHolder selectShiftViewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.select_shift_row, parent, false);
            selectShiftViewHolder = new SelectShiftViewHolder(view);
            selectShiftViewHolder.checkBox.setOnCheckedChangeListener(this);
            view.setTag(selectShiftViewHolder);
        } else {
            selectShiftViewHolder = (SelectShiftViewHolder) view.getTag();
        }

        Shift shift = shifts.get(position);
        selectShiftViewHolder.checkBox.setTag(shift);

        if (daySchedule.getShifts().contains(shift)) {
            selectShiftViewHolder.checkBox.setSelected(true);
        } else {
            selectShiftViewHolder.checkBox.setSelected(false);
        }

        selectShiftViewHolder.id.setText(String.valueOf(shift.getId()));
        selectShiftViewHolder.name.setText(shift.getName());
        selectShiftViewHolder.description.setText(shift.getDescription());
        selectShiftViewHolder.start.setText(String.valueOf(shift.getStart()));
        selectShiftViewHolder.duration.setText(String.valueOf(shift.getDuration()));
        selectShiftViewHolder.location.setText(shift.getLocation());
        selectShiftViewHolder.color.setText(String.valueOf(shift.getColor()));

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Shift shift = (Shift) buttonView.getTag();
        EventBus.getDefault().post(new ShiftSelectedEvent(shift, isChecked));
    }

    static class SelectShiftViewHolder {
        @InjectView(R.id.select_shift_row_check_box)   CheckBox checkBox;
        @InjectView(R.id.select_shift_row_id)          TextView id;
        @InjectView(R.id.select_shift_row_name)        TextView name;
        @InjectView(R.id.select_shift_row_description) TextView description;
        @InjectView(R.id.select_shift_row_start)       TextView start;
        @InjectView(R.id.select_shift_row_duration)    TextView duration;
        @InjectView(R.id.select_shift_row_location)    TextView location;
        @InjectView(R.id.select_shift_row_color)       TextView color;

        private SelectShiftViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
