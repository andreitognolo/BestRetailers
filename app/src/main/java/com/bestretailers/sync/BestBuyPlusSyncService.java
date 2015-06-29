package com.bestretailers.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BestBuyPlusSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static BestBuyPlusSyncAdapter sBestBuyPlusSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("BestBuyPlusSyncService", "onCreate - BestBuyPlusSyncService");
        synchronized (sSyncAdapterLock) {
            if (sBestBuyPlusSyncAdapter == null) {
                sBestBuyPlusSyncAdapter = new BestBuyPlusSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sBestBuyPlusSyncAdapter.getSyncAdapterBinder();
    }
}