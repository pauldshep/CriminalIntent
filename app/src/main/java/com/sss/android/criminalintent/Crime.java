package com.sss.android.criminalintent;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Encapsulates office Crime parameters
 */
public class Crime
{
    private final static String TAG = "Crime";

    private UUID    mId;
    private String  mTitle;
    private Date    mDate;
    private boolean mSolved;
    private String  mSuspect;


    /**
     * Constructor: generates a random UUID and creates a new Crime object
     * with it by calling the other class constructor.
     */
    public Crime()
    {
        this(UUID.randomUUID());
    }

    /**
     * Constructor: creates a new Crime object with the specified UUID, the
     * current date, a dummy value for title, and solved set to false.
     *
     * @param id UUID for the Crime object
     */
    public Crime(UUID id)
    {
        mId     = id;
        mTitle  = "Crime Title";
        mDate   = new Date();
        mSolved = false;

        Log.d(TAG, "new Crime: " + toString());
    }

    /**
     * Implements toString()
     * @return
     */
    @Override
    public String toString()
    {
        String crime_str = "id="     + mId +   ", title=" + mTitle +
                           ", date=" + mDate + ", solved=" + mSolved;
        return crime_str;
    }

    public Date getDate()
    {
        return mDate;
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    public boolean isSolved()
    {
        return mSolved;
    }

    public void setSolved(boolean solved)
    {
        mSolved = solved;
    }

    public String getSuspect() {return mSuspect;}

    public void setSuspect(String suspect) {mSuspect = suspect;}

    public UUID getId()
    {
        return mId;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }
}   // end public class Crime
