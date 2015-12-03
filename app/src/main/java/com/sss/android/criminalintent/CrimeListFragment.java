package com.sss.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Implementation of the CrimeListFragment class
 */
public class CrimeListFragment extends Fragment
{
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String TAG                    = "CrimeListFragment";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean      mSubtitleVisible;


    /**
     * Override onCreate to create options menu
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // tell FragmentManager to create the options menu (call
        // onCreateOptonsMenu(...) and expect menu callbacks.
        setHasOptionsMenu(true);
    }

    /**
     * Android System state function called when the Crime List  isis created
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null)
        {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }


    /**
     * Android system state function called when the user presses the back
     * button from a CrimeActivity view and returns to the CrimeActivityList.
     * updateUI moves any user changes made to CrimeActivity back to
     * CrimeActivityList.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    /**
     * Override system state command onSaveInstanceState.  Called by system
     * before onPause(), onStop(), and onDestroy().
     */
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }


    /**
     * Override the system state onCreateOptionsMenu() to create the menu for
     * this fragment.  This function is called by the FragmentManager if
     * setHasOptionsMenu() has been called.  This is done in onCreate().
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else
        {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }


    /**
     * Override onOptionsItemSelected(menuItem item) to handle the selection
     * of a menu item: New Crime, Show Subtitle.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;

            case R.id.menu_item_delete_crime:
                Log.d(TAG, "Delete Crime");
                CrimeLab.get(getActivity()).deleteCrime();
                updateUI();
                return true;

            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }   // end public boolean onOptionsItemSelected(MenuItem item)


    /**
     * Generates subtitle string with number of crimes and sets the subtitle
     */
    private void updateSubtitle()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleVisible)
        {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    /**
     * Updates the CrimeRecyclerView with new or modified crimes and the
     * number of entries in the menu Subtitle.
     */
    private void updateUI()
    {
        CrimeLab    crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes   = crimeLab.getCrimes();

        if(mAdapter == null)
        {
            // first time through no CrimeAdapter, create one
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else
        {
            // the adapter exists, notify it that its data may have changed
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }


    ////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// INNER CLASSES //////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /***************************************************************************
     * Internal class Crime Holder / View Holder.  Holds crime view and
     * processes user selection.
     **************************************************************************/
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Crime    mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        /***********************************************************************
         * Constructor, gets view widgets
         *
         * @param itemView
         **********************************************************************/
        public CrimeHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            // get access to the individual crime display widgets
            mTitleTextView  = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView   = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        /**
         * Sets view widget parameters to specified crime
         *
         * @param crime
         */
        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        /**
         * Implements On Click message handler for Crime Holder.  Starts a
         * Crime Pager activity when the user presses a list item in
         * CrimeListFragment.
         *
         * @param v view
         */
        @Override
        public void onClick(View v)
        {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }


    }   // end private class CrimeHolder extends RecyclerView.ViewHolder


    /**
     * Crime Adapter private internal class.  Contains crime details
     */
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes)
        {
            mCrimes = crimes;
        }

        /***********************************************************************
         * Overrides RecyclerView onCreateView.  View is the defined in
         * list_item_crime.xml file.
         *
         * @param parent
         * @param viewType
         * @return Crime Holder
         **********************************************************************/
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        /***********************************************************************
         * Connects view to a crime
         *
         * @param holder
         * @param position
         **********************************************************************/
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position)
        {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount()
        {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
    }   // end private class  CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>



}   // end public class CrimeListFragment extends Fragment
