package com.sss.android.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * CrimeFragment class implementation.
 */
public class CrimeFragment extends Fragment
{
    private static final String TAG          = "CrimeFragment";
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE  = "DialogDate";
    private static final String DIALOG_TIME  = "DialogTime";

    private static final int    REQUEST_DATE    = 1;
    private static final int    REQUEST_TIME    = 2;
    private static final int    REQUEST_CONTACT = 3;
    private static final int    PICK_CONTACT    = 4;
    private static final int    CALL_CONTACT    = 5;

    private Crime    mCrime;
    private EditText mTitleField;
    private Button   mDateButton;
    private Button   mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button   mReportButton;
    private Button   mSuspectButton;


    //==========================================================================
    /**
     * Call this instead of the constructor to get a more general instance
     * of CrimeFragment.  CrimeActivity should call this function when it
     * needs to create a CrimeFragment.
     *
     * @param crimeId identifies crime
     * @return an instance of CrimeFragment with crimeId set
     */
    public static CrimeFragment newInstance(UUID crimeId)
    {
        Log.d(TAG, "newInstance(id = " + crimeId);
        Bundle args = new Bundle();     // maps string to various types
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the crime fragment is created.  getActivity() returns the
     * FragmentActivity, CrimeActivity, that this fragment is currently
     * associated with.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime       = CrimeLab.get(getActivity()).getCrime(crimeId);
        Log.d(TAG, "onCreate(id = " + crimeId);
        Log.d(TAG, "onCreate(Crime = " + mCrime.toString());
    }   // end public void onCreate(Bundle savedInstanceState)


    /**
     * Override Android system state function onPause
     */
    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }


    //==========================================================================
    /**
     * Called when a view is created
     *
     * @param inflater converts XML to display
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        // Crime Title
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // This one too
            }
        });

        // Date Button
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * onClick message handler for the DateButton.  Creates a
             * DatePicker fragment and initialize its date with the
             * date from its associated Crime.
             *
             * @param v
             */
            @Override
            public void onClick(View v)
            {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        // Time Button
        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener()
        {
            /**
             * onClick message handler for the TimeButton.  Creates a
             * TimePicker fragment and initialize its date with the
             * date from its associated Crime.
             *
             * @param v
             */
            @Override
            public void onClick(View v)
            {
                FragmentManager    manager = getFragmentManager();
                TimePickerFragment dialog  = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        // Crime Solved Check Box
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        // Button: Send Crime Report
        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i, getString(R.string.send_report));

                // same as above but uses ShareCompat
                Intent i = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .getIntent();

                startActivity(i);
            }
        });


        //----------------------------------------------------------------------
        // Button: Call Suspect
        mReportButton = (Button)v.findViewById(R.id.call_suspect);
        mReportButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // get suspect from database
                Intent i = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });

        //----------------------------------------------------------------------
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect() != null)
        {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
        {
            mSuspectButton.setEnabled(false);
        }

        return v;
    }   // end public View onCreateView(LayoutInflater inflater, ViewGroup ...


    //==========================================================================
    /**
     * Receives the result from a previous call to startActivityForResult()
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "onActivityResult(result code = " + resultCode + ")");

        if(resultCode != Activity.RESULT_OK)
        {
            return;
        }

        if(requestCode == REQUEST_DATE)
        {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        else if(requestCode == REQUEST_TIME)
        {
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
        }
        else if(requestCode == REQUEST_CONTACT && data != null)
        {
            Uri contactUri = data.getData();

            // specify which fields you want or query to return values for
            String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME};

            // perform the query - the contact Uri is like a "where" clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try
            {
                // double-check that I actually got results
                if(c.getCount() == 0)  {return;}

                // pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
            }
            finally
            {
                c.close();
            }
        }

        else if(requestCode == PICK_CONTACT && data != null)
        {
            Log.d(TAG, "request code = PICK_CONTACT");
            Uri contactUri = data.getData();

            // specify which fields to query and return values for
            String[] queryFields = new String[]
                    {
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    };

            // perform the query - the contact Uri is like a "where" clause here
            Cursor c = getActivity().getContentResolver().query(contactUri,
                    queryFields, null, null, null);

            try
            {
                // verify there are results
                if(c.getCount() == 0)  {return;}

                // extract the phone information
                c.moveToFirst();                        // move to first row

                String disp_name = c.getString(0);      // get column 0 data
                Log.d(TAG, "Name = " + disp_name);

                String phone_num = c.getString(1);      // get column 1 data
                Log.d(TAG, "Phone Number = " + phone_num);

                // call the phone number
                Uri phone_uri = Uri.parse("tel:" + phone_num);
                //Uri phone_uri = Uri.parse("tel:234-567-8912");
                Intent i      = new Intent(Intent.ACTION_DIAL, phone_uri);
                startActivity(i);
            }
            finally
            {
                c.close();
            }
        }
    }

    /**
     *
     */
    private void updateDate()
    {
        mDateButton.setText(mCrime.getDate().toString());
    }

    //==========================================================================
    private String getCrimeReport()
    {
        String solvedString = null;
        if (mCrime.isSolved())
        {
            solvedString = getString(R.string.crime_report_solved);
        }
        else
        {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect    = mCrime.getSuspect();
        if(suspect == null)
        {
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else
        {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }


    /**
     *
     */
    private void updateTime()
    {
        mTimeButton.setText(DateFormat.format("HH:mm:ss", mCrime.getDate()));
    }

}   //  end public class CrimeFragment extends Fragment
