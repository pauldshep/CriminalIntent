package com.sss.android.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.sss.android.criminalintent.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Creates a thin wrapper around a database Cursor object
 */
public class CrimeCursorWrapper extends CursorWrapper
{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }


    /**
     * Get specified Crime from database.
     * @return
     */
    public Crime getCrime()
    {
        // get Crime info from database
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title      = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long   date       = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int    isSolved   = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect    = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        // build and return Crime object from database information
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);

        return crime;
    }
}
