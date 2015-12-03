package com.sss.android.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sss.android.criminalintent.CrimeDbSchema.CrimeTable;

/**
 * Created by Paul Shepherd on 10/5/2015.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper
{
    private static final int    VERSION       = 1;
    private static final String DATABASE_NAME = "crimeBase.db";


    public CrimeBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Override system state function onCreate.  Create SQL database
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID    + ", " +
                CrimeTable.Cols.TITLE   + ", " +
                CrimeTable.Cols.DATE    + ", " +
                CrimeTable.Cols.SOLVED  + ", " +
                CrimeTable.Cols.SUSPECT + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
