package com.android.jjnunogarcia.shifter.fragments.dialogs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.database.ShifterProvider;
import com.android.jjnunogarcia.shifter.database.tables.ShiftTable;
import com.android.jjnunogarcia.shifter.eventbus.ShiftSavedEvent;
import com.android.jjnunogarcia.shifter.model.Shift;
import de.greenrobot.event.EventBus;

/**
 * User: jesus
 * Date: 03/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class ShiftDialog extends DialogFragment {
    public static final String TAG = ShiftDialog.class.getSimpleName();

    @InjectView(R.id.dialog_shift_name)        EditText name;
    @InjectView(R.id.dialog_shift_description) EditText description;
    @InjectView(R.id.dialog_shift_start)       EditText start;
    @InjectView(R.id.dialog_shift_duration)    EditText duration;
    @InjectView(R.id.dialog_shift_location)    EditText location;
    @InjectView(R.id.dialog_shift_color)       EditText color;
    @InjectView(R.id.dialog_shift_save)        Button   saveShift;

    public static ShiftDialog newInstance() {
        return new ShiftDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_shift, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.dialog_shift_save)
    void saveShift() {
        Shift shift = new Shift();
        String name = this.name.getText().toString();
        String description = this.description.getText().toString();
        int start = Integer.parseInt(this.start.getText().toString());
        int duration = Integer.parseInt(this.duration.getText().toString());
        String location = this.location.getText().toString();
        int color = Integer.parseInt(this.color.getText().toString());

        shift.setName(name);
        shift.setDescription(description);
        shift.setStart(start);
        shift.setDuration(duration);
        shift.setLocation(location);
        shift.setColor(color);

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ShiftTable.NAME, name);
        contentValues.put(ShiftTable.DESCRIPTION, description);
        contentValues.put(ShiftTable.START, start);
        contentValues.put(ShiftTable.DURATION, duration);
        contentValues.put(ShiftTable.LOCATION, location);
        contentValues.put(ShiftTable.COLOR, color);
        Uri uriInserted = contentResolver.insert(ShifterProvider.SHIFTS_URI, contentValues);

        if (uriInserted != null) { // success
            shift.setId(Integer.valueOf(uriInserted.getLastPathSegment())); // set the id of the task according to where it is inserted
            EventBus.getDefault().post(new ShiftSavedEvent(shift));
        }

        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
