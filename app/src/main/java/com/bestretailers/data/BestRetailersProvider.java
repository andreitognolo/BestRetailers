package com.bestretailers.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by daniele on 24/06/15.
 */
public class BestRetailersProvider extends ContentProvider {

    private static final String LOG_TAG = BestRetailersProvider.class.getSimpleName();
    //Provider is used to read and write the DB

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BestRetailersDbHelper mOpenHelper;

    static final int STORE = 100;
    static final int STORE_WITH_COUNTRY = 101;
    //static final int STORE_WITH_COUNTRY_AND_REGION = 102;
    //static final int STORE__WITH_COUNTRY_AND_CITY = 103;
    //static final int LOCATION = 300;


    private static final String sCountrySettingSelection =
            BestRetailersContract.BestBuyStoreEntry.TABLE_NAME +
                    "." + BestRetailersContract.BestBuyStoreEntry.COLUMN_COUNTRY + " = ? ";

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        //We don't want the root node to match anything
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BestRetailersContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        uriMatcher.addURI(authority, BestRetailersContract.PATH_BEST_BUY_STORE, STORE);
        //uriMatcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);
        uriMatcher.addURI(authority, BestRetailersContract.PATH_BEST_BUY_STORE + "/*", STORE_WITH_COUNTRY);

        // 3) Return the new matcher!
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BestRetailersDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            /*
            case WEATHER_WITH_LOCATION_AND_DATE:
            {
                retCursor = getWeatherByLocationSettingAndDate(uri, projection, sortOrder);
                break;
            }
            // "weather/*"
            case WEATHER_WITH_LOCATION: {
                retCursor = getWeatherByLocationSetting(uri, projection, sortOrder);
                break;
            }
            */
            // "weather"
            case STORE: {
                retCursor = mOpenHelper.getReadableDatabase().query(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case STORE_WITH_COUNTRY: {
                String countrySetting = BestRetailersContract.BestBuyStoreEntry.getCountrySettingFromUri(uri);

                selection = sCountrySettingSelection;
                selectionArgs = new String[]{countrySetting};
                retCursor = mOpenHelper.getReadableDatabase().query(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is. (Item or List)
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            /*
            case WEATHER_WITH_LOCATION_AND_DATE:
                //It returns only a single row, that's why Item Type
                return WeatherContract.WeatherEntry.CONTENT_ITEM_TYPE;
            case WEATHER_WITH_LOCATION:
                return WeatherContract.WeatherEntry.CONTENT_TYPE;
                */
            case STORE:
                return BestRetailersContract.BestBuyStoreEntry.CONTENT_TYPE;
            case STORE_WITH_COUNTRY:
                return BestRetailersContract.BestBuyStoreEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        //we want to make sure the records end up to the correct table
        switch (match) {
            case STORE: {
                //normalizeDate(values);
                long _id = db.insert(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BestRetailersContract.BestBuyStoreEntry.buildStoreUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            /*
            case LOCATION:
                long _id = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = WeatherContract.LocationEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            */
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //This notifies all registered observers!
        getContext().getContentResolver().notifyChange(uri, null);
        //Don't close the DB
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.
        final int match = sUriMatcher.match(uri);
        //this makes delete all rows return the number of rows deleted
        if (selection == null)
            selection = "1";
        int rowsDeleted;
        switch (match) {
            case STORE:
                rowsDeleted = db.delete(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            /*
            case LOCATION:
                rowsDeleted = db.delete(WeatherContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
                */
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        //Don't close the DB

        // Student: return the actual rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case STORE:
                rowsUpdated = db.update(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.
        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        //Don't close the DB

        // Student: return the actual rows updated
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STORE:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        Log.d(LOG_TAG, "Insert row for " + values.toString());
                        long _id = db.insert(BestRetailersContract.BestBuyStoreEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
