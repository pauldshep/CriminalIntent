package com.sss.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;


/**
 * Creates a date picker dialog that allows the user to choose a date.
 */
public class TimePickerFragment extends DialogFragment
{
    public  static final String EXTRA_TIME = "com.sss.android.criminalintent.time";
    private static final String ARG_TIME   = "time";

    private TimePicker mTimePicker;

    /**
     * Static function that Creates a DatePickerFragment with an entry for
     * the user selected date in its bundle.
     *
     * @param date
     * @return
     */
    public static TimePickerFragment newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Android state function called when the dialog is created.  This creates
     * a dialog with an initialized date picker.
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Date date = (Date)getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour   = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker)v.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
            .setView(v)
            .setTitle(R.string.time_picker_title)
            .setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR,   mTimePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                        sendResult(Activity.RESULT_OK, calendar.getTime());
                    }
                })
            .create();
    }   // end public Dialog onCreateDialog(Bundle savedInstanceState)

    /**
     * Returns the user selected date to the calling activity through an
     * intent.
     *
     * @param resultCode Activity.RESULT_OK or Activity.RESULT_CANCELED
     * @param date
     */
    private void sendResult(int resultCode, Date date)
    {
        if(getTargetFragment() == null)
        {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}   // end public class DatePickerFragment extends DialogFragment
