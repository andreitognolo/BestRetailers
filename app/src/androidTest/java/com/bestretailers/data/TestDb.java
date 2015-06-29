package com.bestretailers.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by daniele on 25/06/15.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(BestRetailersDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME);
        //tableNameHashSet.add(BestRetailers.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(BestRetailersDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new BestRetailersDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain the store entry
        // and other tables... (add them upon implementing)
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + BestRetailersContract.BestBuyStoreEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> storeColumnHashSet = new HashSet<String>();
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry._ID);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_STORE_ID);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_COUNTRY);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_REGION);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_CITY);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_NAME);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_LONG_NAME);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_TYPE);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LAT);
        storeColumnHashSet.add(BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LONG);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            storeColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required store
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required store entry columns",
                storeColumnHashSet.isEmpty());
        db.close();
    }


    public void testStoreTable(){
        insertStore();
    }

    public long insertStore(){
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        BestRetailersDbHelper dbHelper = new BestRetailersDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = TestUtilities.createFakeStoreValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long storeRowId;
        storeRowId = db.insert(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(storeRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                BestRetailersContract.BestBuyStoreEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from store query",
                cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return storeRowId;
    }


}
