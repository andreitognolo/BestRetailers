package com.bestretailers.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bestretailers.rest.BestBuyClient;
import com.bestretailers.R;
import com.bestretailers.Utils;
import com.bestretailers.data.BestRetailersContract;
import com.bestretailers.model.BestBuyStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by daniele on 25/06/15.
 */
public class BestBuyPlusSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = BestBuyPlusSyncAdapter.class.getSimpleName();
    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int STORES_NOTIFICATION_ID = 3004;   //use it if you will implement notifications


    /*
    private static final String[] NOTIFY_WEATHER_PROJECTION = new String[] {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC
    };


    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_MAX_TEMP = 1;
    private static final int INDEX_MIN_TEMP = 2;
    private static final int INDEX_SHORT_DESC = 3;
    */

    public BestBuyPlusSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        //TODO: avoid deleting every time (or not??)


        //checking the last update and update stores if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        String lastStoreUpdate = getContext().getString(R.string.pref_last_store_update);
        long lastSync = prefs.getLong(lastStoreUpdate, 0);

        Log.d(LOG_TAG, "Performing Sync - last sync was " + lastSync);

        //update store only if more than one day has passed
        if (System.currentTimeMillis() - lastSync >= DAY_IN_MILLIS) {

            int rowsDeleted = getContext().getContentResolver().delete(
                    BestRetailersContract.BestBuyStoreEntry.CONTENT_URI,
                    null,
                    null
            );

            Log.d(LOG_TAG, rowsDeleted + " rows have been deleted");

            final String countrySetting = Utils.getPreferredCountry(getContext());
            //get country from settings
            final ArrayList<BestBuyStore> stores = new ArrayList<>();
            BestBuyClient.getInstance().getStoresByCountry(getContext(), countrySetting, false,
                    new Utils.GetStoresCallback<BestBuyStore>() {

                        @Override
                        public void onSuccess(List<BestBuyStore> receivedStores) {

                            //TODO: Clear only if first time query (pageNum = 1 maybe?)
                            stores.addAll(receivedStores);
                            insertStoresInDb(countrySetting, stores);
                            Log.i(LOG_TAG, "BestBuy stores successfully received");
                        }

                        @Override
                        public void onError(Throwable th) {
                            Log.e(LOG_TAG, th.getMessage(), new Exception(th));
                        }

                    });

            //refreshing last sync
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(lastStoreUpdate, System.currentTimeMillis());
            editor.commit();

        }

        return;
    }

    private void insertStoresInDb(String countrySetting, ArrayList<BestBuyStore> stores) {

        Vector<ContentValues> cVVector = new Vector<ContentValues>(stores.size());

        for (int i = 0; i < stores.size(); i++) {

            BestBuyStore store = stores.get(i);

            Log.d(LOG_TAG, "Create content value for: " + store.getLongName());
            ContentValues storesValues = new ContentValues();

            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_STORE_ID, store.getStoreId());
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_COUNTRY, countrySetting);
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_REGION, store.getRegion());
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_CITY, store.getCity());
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_NAME, store.getName());
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_LONG_NAME, store.getLongName());
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_TYPE, store.getStoreType());
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LAT, store.getLat());
            storesValues.put(BestRetailersContract.BestBuyStoreEntry.COLUMN_COORD_LONG, store.getLng());

            cVVector.add(storesValues);
        }

        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = getContext().getContentResolver().bulkInsert(BestRetailersContract.BestBuyStoreEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "Store Retrieval Complete. " + inserted + " Inserted");
    }


    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        BestBuyPlusSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
