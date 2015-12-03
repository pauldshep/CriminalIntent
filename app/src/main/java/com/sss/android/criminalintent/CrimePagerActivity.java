package com.sss.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Creates and manages the view pager fragment
 */
public class CrimePagerActivity extends AppCompatActivity
{
    private static final String EXTRA_CRIME_ID = "com.sss.android.criminalintent.crime_id";

    private ViewPager           mViewPager;
    private List<Crime>         mCrimes;


    /**
     * Statically creates intent for the CrimePagerActivity with crimeId extra.
     * This is used externally to create the intent for this activity.
     *
     * @param packageContext
     * @param crimeId identifies crime, saved as extra in intent
     * @return the intent with crimeId extra
     */
    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }


    /**
     * Android system state command called when the CrimePagerActivity is
     * created.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        // find ViewPager in the activity's view and get data from CrimeLab
        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes    = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        // FragmentPagerAdapter can be used here as well but is not as
        // frugal with memory.
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager)
        {
            @Override
            public Fragment getItem(int position)
            {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount()
            {
                return mCrimes.size();
            }
        }); // end class FragmentStatePagerAdapter(fragmentManager)

        // display user selected crime, not the first crime in the list
        for(int i = 0; i < mCrimes.size(); i++)
        {
            if(mCrimes.get(i).getId().equals(crimeId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }   // end protected void onCreate(Bundle savedInstanceState)
}   // end public class CrimePagerActivity extends FragmentActivity
