package com.bestretailers.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daniele on 24/06/15.
 */
public class BestRetailersDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "bestretailers.db";

    public BestRetailersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_STORE_TABLE = "CREATE TABLE " + BestRetailersContract.BestBuyStoreEntry.TABLE_NAME + " (" +
                BestRetailersContract.BestBuyStoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_STORE_ID + " INTEGER NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_REGION + " TEXT NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_LONG_NAME + " TEXT NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LONG + " REAL NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_STORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BestRetailersContract.BestBuyStoreEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
