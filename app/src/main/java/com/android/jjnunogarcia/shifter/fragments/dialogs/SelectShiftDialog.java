package com.android.jjnunogarcia.shifter.fragments.dialogs;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.jjnunogarcia.shifter.R;
import com.android.jjnunogarcia.shifter.adapters.SelectShiftAdapter;
import com.android.jjnunogarcia.shifter.database.DBConstants;
import com.android.jjnunogarcia.shifter.database.ShifterProvider;
import com.android.jjnunogarcia.shifter.helpers.Utils;
import com.android.jjnunogarcia.shifter.model.DaySchedule;
import com.android.jjnunogarcia.shifter.model.Shift;

import java.util.ArrayList;

/**
 * User: jesus
 * Date: 07/04/15
 *
 * @author jjnunogarcia@gmail.com
 */
public class SelectShiftDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG              = SelectShiftDialog.class.getSimpleName();
    public static final String KEY_DAY_SCHEDULE = "key_day_schedule";
    public static final int    SHIFTS_LOADER_ID = 2;

    @InjectView(R.id.dialog_select_shift_shift_list) ListView shiftList;

    private SelectShiftAdapter adapter;
    private DaySchedule        daySchedule;

    public static SelectShiftDialog newInstance(DaySchedule daySchedule) {
        SelectShiftDialog selectShiftDialog = new SelectShiftDialog();
        Bundle args = new Bundle();
        args.putParcelable(KEY_DAY_SCHEDULE, daySchedule);
        selectShiftDialog.setArguments(args);
        return selectShiftDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_select_shift, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();

        if (args != null && args.containsKey(KEY_DAY_SCHEDULE)) {
            daySchedule = args.getParcelable(KEY_DAY_SCHEDULE);
            adapter = new SelectShiftAdapter(getActivity().getApplicationContext());
            shiftList.setAdapter(adapter);
            getActivity().getSupportLoaderManager().initLoader(SHIFTS_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity().getApplicationContext(), ShifterProvider.SHIFTS_URI, DBConstants.SHIFTS_PROJECTION, null, null, DBConstants.SORT_SHIFTS_BY_ID_ASC);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<Shift> shifts = Utils.getShifts(data);
        adapter.setContent(daySchedule, shifts);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
