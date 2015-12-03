package com.sss.android.criminalintent;

/**
 * Defines data base for criminal intent
 */
public class CrimeDbSchema
{
    /**
     * String constants used to describe the pieces of the table definition
     */
    public final static class CrimeTable
    {
        // name of table in data base
        public static final String NAME = "crimes";


        /**
         * String constants used to describe the table columns
         */
        public final static class Cols
        {
            public final static String UUID = "uuid";
            public final static String TITLE = "title";
            public final static String DATE = "date";
            public final static String SOLVED = "solved";
            public final static String SUSPECT = "suspect";
        }
    }   // end public final static class Cols
}   // end public class CrimeDbSchema
