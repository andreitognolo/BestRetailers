package com.bestretailers.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by daniele on 29/06/15.
 */
public class TestUtilities extends AndroidTestCase {



    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }


    static ContentValues createFakeStoreValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();

        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_STORE_ID, 1234);
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_COUNTRY, "United States");
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_REGION, "California");
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_CITY, "San francisco");
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_NAME, "B B S F");
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_LONG_NAME, "B B S F - Mountain View");
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_TYPE, "Stolen Objects Retailer");
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LAT, 64.7488);
        testValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LONG, -147.353);

        return testValues;
    }


}
