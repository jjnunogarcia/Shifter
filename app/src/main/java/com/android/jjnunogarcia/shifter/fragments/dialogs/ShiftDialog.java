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
    public static final String TAG       = ShiftDialog.class.getSimpleName();
    public static final String KEY_SHIFT = "key_shift";

    @InjectView(R.id.dialog_shift_name)        EditText name;
    @InjectView(R.id.dialog_shift_description) EditText description;
    @InjectView(R.id.dialog_shift_start)       EditText start;
    @InjectView(R.id.dialog_shift_duration)    EditText duration;
    @InjectView(R.id.dialog_shift_location)    EditText location;
    @InjectView(R.id.dialog_shift_color)       EditText color;
    @InjectView(R.id.dialog_shift_save)        Button   saveShift;

    private Shift shiftToEdit;

    public static ShiftDialog newInstance() {
        return new ShiftDialog();
    }

    public static ShiftDialog newInstance(Shift shift) {
        ShiftDialog shiftDialog = new ShiftDialog();
        Bundle args = new Bundle();
        args.putParcelable(KEY_SHIFT, shift);
        shiftDialog.setArguments(args);
        return shiftDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shiftToEdit = new Shift();
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

        Bundle args = getArguments();

        if (args != null && args.containsKey(KEY_SHIFT)) {
            shiftToEdit = args.getParcelable(KEY_SHIFT);

            name.setText(shiftToEdit.getName());
            description.setText(shiftToEdit.getDescription());
            start.setText(String.valueOf(shiftToEdit.getStart()));
            duration.setText(String.valueOf(shiftToEdit.getDuration()));
            location.setText(shiftToEdit.getLocation());
            color.setText(String.valueOf(shiftToEdit.getColor()));
        }
    }

    @OnClick(R.id.dialog_shift_save)
    void saveShift() {
        String name = this.name.getText().toString();
        String description = this.description.getText().toString();
        int start = Integer.parseInt(this.start.getText().toString());
        int duration = Integer.parseInt(this.duration.getText().toString());
        String location = this.location.getText().toString();
        int color = Integer.parseInt(this.color.getText().toString());

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ShiftTable.NAME, name);
        contentValues.put(ShiftTable.DESCRIPTION, description);
        contentValues.put(ShiftTable.START, start);
        contentValues.put(ShiftTable.DURATION, duration);
        contentValues.put(ShiftTable.LOCATION, location);
        contentValues.put(ShiftTable.COLOR, color);
        Uri uriInserted = contentResolver.insert(ShifterProvider.SHIFTS_URI, contentValues);

        shiftToEdit.setName(name);
        shiftToEdit.setDescription(description);
        shiftToEdit.setStart(start);
        shiftToEdit.setDuration(duration);
        shiftToEdit.setLocation(location);
        shiftToEdit.setColor(color);

        if (shiftToEdit.getId() == Shift.NO_ID) { // new shift to insert
            if (uriInserted != null) { // success
                shiftToEdit.setId(Integer.valueOf(uriInserted.getLastPathSegment())); // set the id of the task according to where it is inserted
                EventBus.getDefault().post(new ShiftSavedEvent(shiftToEdit));
            }
        } else { // shift to update
            String where = ShiftTable._ID + "=?";
            String[] selectionArgs = {String.valueOf(shiftToEdit.getId())};
            int rowsUpdated = contentResolver.update(ShifterProvider.SHIFTS_URI, contentValues, where, selectionArgs);

            if (rowsUpdated > 0) { // success
                EventBus.getDefault().post(new ShiftSavedEvent(shiftToEdit));
            }
        }


        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
