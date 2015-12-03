package com.sss.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Project main activity
 */
public class CrimeListActivity extends SingleFragmentActivity
{
    /**
     * Creates and returns a CrimeListFragment instance
     *
     * @return the crime list fragment
     */
    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }
}
