package com.sss.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sss.android.criminalintent.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implements the CrimeLab singleton.  Holds the list of Crimes.
 */
public class CrimeLab
{
    private static final String TAG = "CrimeLab";

    private static  CrimeLab        sCrimeLab;       // one instance of CrimeLab
    private         Context         mContext;
    private         SQLiteDatabase  mDatabase;


    /**
     * Singleton private constructor
     *
     * @param context application level context
     */
    private CrimeLab(Context context)
    {
        mContext  = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Gets the one instance of the CrimeLab singleton.  If it does not already
     * exist it is created.
     *
     * @param context access to global information about the application
     * @return the one instance of the CrimeLab singleton.
     */
    public static CrimeLab get(Context context)
    {
        if(sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    /**
     * Add a crime to the list of crimes in the Database.
     * @param crime Crime to add to the Database
     */
    public void addCrime(Crime crime)
    {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    /**
     * Delete the last crime in the list of crimes if there is one to delete
     */
    public void deleteCrime()
    {
//        int num_crimes = mCrimes.size();
//        if(num_crimes > 0)
//        {
//            mCrimes.remove(num_crimes - 1);
//        }
    }




    /**
     * Updates an existing Crime in the database.  The
     *
     * @param crime
     */
    public void updateCrime(Crime crime)
    {
        String        uuidString = crime.getId().toString();
        ContentValues values     = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,      // table name
                values,                        // table ContentValues
                CrimeTable.Cols.UUID + " = ?", // if UUDI matches string
                new String[]{uuidString});     // string to match
    }


    /**
     * Creates a ContentValues instance containing a Crime database row.  The
     * row is populated with the specified Crime values.  ContentValues is a
     * Map like object specifically for databases.
     *
     * @param crime
     * @return ContentValues structure with the Crime values
     */
    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID,    crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,   crime.getTitle().toString());
        values.put(CrimeTable.Cols.DATE,    crime.getDate().toString());
        values.put(CrimeTable.Cols.SOLVED,  crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }


    /**
     * Get database row information in CrimeCursorWrapper format.
     *
     * @param whereClause identifies rows get, null is all rows
     * @param whereArgs row identification string
     *
     * @return Cursor wrapped in a utility class
     */
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,        // table to query
                null,                   // column values and order received - null is all columns
                whereClause,            // where row identification logic - null is all rows
                whereArgs,              // where arguments specify row
                null,                   // group by
                null,                   // having
                null,                   // order by
                null                    // limit
        );

        return new CrimeCursorWrapper(cursor);
    }

    /**
     * Get the list of crimes
     *
     * @return list of crimes
     */
    public List<Crime> getCrimes()
    {
        List<Crime> crimes = new ArrayList<>();

        // cursor points to particular place in query
        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();

            }
        }
        finally
        {
            cursor.close();
        }

        return crimes;
    }   // end public List<Crime> getCrimes()


    /**
     * Get Crime associated with specified id.  Null if it could not be found
     *
     * @param id identifies crime to get
     * @return Crime associated with id or null
     */
    public Crime getCrime(UUID id)
    {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                                                new String[] {id.toString()});

        try
        {
            if(cursor.getCount() == 0)
            {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally
        {
            cursor.close();
        }
    }
}   // end public class CrimeLab
