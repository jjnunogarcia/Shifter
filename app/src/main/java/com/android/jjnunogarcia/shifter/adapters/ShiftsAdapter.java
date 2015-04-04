package com.android.jjnunogarcia.shifter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.model.Shift;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author j.nuno@klara.com
 */
public class ShiftsAdapter extends BaseAdapter {
    private Context          context;
    private ArrayList<Shift> shifts;

    public ShiftsAdapter(Context context) {
        this.context = context;
        shifts = new ArrayList<>();
    }

    public void setContent(ArrayList<Shift> shifts) {
        this.shifts = shifts;
        notifyDataSetChanged();
    }

    public void addContent(Shift shift) {
        this.shifts.add(shift);
        notifyDataSetChanged();
    }

    public void addContent(Collection<Shift> shifts) {
        this.shifts.addAll(shifts);
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
        ShiftViewHolder shiftViewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.day_detail_shift_row, parent, false);
            shiftViewHolder = new ShiftViewHolder(view);
            view.setTag(shiftViewHolder);
        } else {
            shiftViewHolder = (ShiftViewHolder) view.getTag();
        }

        Shift shift = shifts.get(position);
        shiftViewHolder.id.setText(shift.getId());
        shiftViewHolder.name.setText(shift.getName());
        shiftViewHolder.description.setText(shift.getDescription());
        shiftViewHolder.start.setText(shift.getStart());
        shiftViewHolder.duration.setText(shift.getDuration());
        shiftViewHolder.location.setText(shift.getLocation());
        shiftViewHolder.color.setText(shift.getColor());

        return view;
    }

    static class ShiftViewHolder {
        @InjectView(R.id.day_detail_shift_row_id)          TextView id;
        @InjectView(R.id.day_detail_shift_row_name)        TextView name;
        @InjectView(R.id.day_detail_shift_row_description) TextView description;
        @InjectView(R.id.day_detail_shift_row_start)       TextView start;
        @InjectView(R.id.day_detail_shift_row_duration)    TextView duration;
        @InjectView(R.id.day_detail_shift_row_location)    TextView location;
        @InjectView(R.id.day_detail_shift_row_color)       TextView color;

        private ShiftViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
