package com.bestretailers.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daniele on 24/06/15.
 */
public class BestRetailersContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.bestretailers";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_BEST_BUY_STORE = "store";


    //TODO: create method buildUriMatcher within ContentProvider class
    //private static final UriMatcher sUriMatcher = buildUriMatcher();


    /*
        Inner class that defines the table contents of the location table
        Students: This is where you will add the strings.  (Similar to what has been
        done for WeatherEntry)
     */
    public static final class BestBuyStoreEntry implements BaseColumns {

        public static final String TABLE_NAME = "store";

        public static final String COLUMN_STORE_ID = "storeId";

        public static final String COLUMN_COUNTRY = "country";

        public static final String COLUMN_REGION = "region";

        public static final String COLUMN_CITY = "city";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_LONG_NAME = "longName";

        public static final String COLUMN_TYPE = "storeType";

        public static final String COLUMN_COORD_LAT = "lat";

        public static final String COLUMN_COORD_LONG = "lng";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BEST_BUY_STORE).build();

        //Cursors that return more than one item are prefixed by CURSOR_DIR_BASE_TYPE
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BEST_BUY_STORE;
        //Cursors that return a single item are prefixed by CURSOR_DIR_BASE_TYPE
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BEST_BUY_STORE;

        //This function helps to build the Content Provider Query
        public static Uri buildStoreUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildStoreWithCountry(String countrySetting) {
            return CONTENT_URI.buildUpon().appendPath(countrySetting).build();
        }

        public static Uri buildStoreCountryWithRegion(String countrySetting, String region) {
            return CONTENT_URI.buildUpon().appendPath(countrySetting).appendQueryParameter(COLUMN_REGION, region).build();
        }

        public static Uri buildStoreCountryWithRegionAndCity(String countrySetting, String region, String city) {
            return CONTENT_URI.buildUpon().appendPath(countrySetting).appendQueryParameter(COLUMN_REGION, region).
                    appendQueryParameter(COLUMN_CITY, city).build();
        }

        public static String getCountrySettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

}
