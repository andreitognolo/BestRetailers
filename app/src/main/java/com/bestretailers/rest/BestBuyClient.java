package com.bestretailers.rest;

import android.content.Context;
import android.util.Log;

import com.bestretailers.Utils;
import com.bestretailers.model.BestBuyProduct;
import com.bestretailers.model.BestBuyStore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by daniele on 17/06/15.
 */
public class BestBuyClient {

    private static final String LOG_TAG = BestBuyClient.class.getSimpleName();
    public static final String REST_HOST = "\"http://best-retailers.appspot.com\"";

    // Singleton implementation

    private static BestBuyClient instance;

    /**
     * The client to be used for sync calls
     */
    private SyncHttpClient mSyncClient;
    /**
     * The client to be used for async calls
     */
    private AsyncHttpClient mAsyncClient;

    /**
     * Server host to be used in calls
     */
    private String mHost;

    public static synchronized BestBuyClient getInstance() {
        if (instance == null) {
            instance = new BestBuyClient();
            instance.init();
        }
        return instance;
    }

    private BestBuyClient() {
        mAsyncClient = new AsyncHttpClient();
        mAsyncClient.setTimeout(30 * 1000); // 30 seconds
        mSyncClient = new SyncHttpClient();
        mSyncClient.setTimeout(30 * 1000); // 30 seconds
    }

    private AsyncHttpClient getAsyncClient() {
        return mAsyncClient;
    }

    private SyncHttpClient getSyncClient() {
        return mSyncClient;
    }

    public String getHost() {
        return mHost;
    }

    public void init() {
        mHost = "http://best-retailers.appspot.com";
    }


    //http://api.remix.bestbuy.com/v1/stores(region=ut)?format=json&show=storeId,city,region&apiKey=YourAPIKey

    public synchronized void getStoresByCountry(Context context, String country, boolean async,
                                                final Utils.GetStoresCallback<BestBuyStore> callback){
        final String url = buildUrl("/bestbuy/stores/country/" + country);

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject stores) {
                super.onSuccess(statusCode, headers, stores);
                List<BestBuyStore> listStores = Utils.createStoresFromJSON(stores);
                if (callback != null) {
                    callback.onSuccess(listStores);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                super.onFailure(statusCode, headers, throwable, response);

                if (callback != null) {
                    callback.onError(throwable);
                }
                if (statusCode == 401) {

                }
            }
        };

        Log.i(LOG_TAG, "REQ GET " + url);

        if (async) {
            getAsyncClient().get(url, null, handler);
        } else {
            getSyncClient().get(url, null, handler);
        }
    }

    public synchronized void getStoresByRegion(Context context, String region, boolean async,
                                                final Utils.GetStoresCallback<BestBuyStore> callback){
        final String url = buildUrl("/bestbuy/stores/region/" + region);

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject stores) {
                super.onSuccess(statusCode, headers, stores);
                List<BestBuyStore> listStores = Utils.createStoresFromJSON(stores);
                if (callback != null) {
                    callback.onSuccess(listStores);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                super.onFailure(statusCode, headers, throwable, response);

                if (callback != null) {
                    callback.onError(throwable);
                }
                if (statusCode == 401) {

                }
            }
        };

        Log.i(LOG_TAG, "REQ GET " + url);

        if (async) {
            getAsyncClient().get(url, null, handler);
        } else {
            getSyncClient().get(url, null, handler);
        }
    }


    public synchronized void getBestBuyProducts(Context context, String searchQuery, int num, boolean async,
                                                final Utils.GetProductsCallback<BestBuyProduct> callback){
        final String url = buildUrl("/bestbuy/search_product/" + searchQuery + "/" + num);

        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject products) {
                super.onSuccess(statusCode, headers, products);
                List<BestBuyProduct> listProducts = Utils.createProductsFromJSON(products);
                if (callback != null) {
                    callback.onSuccess(listProducts);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                super.onFailure(statusCode, headers, throwable, response);

                if (callback != null) {
                    callback.onError(throwable);
                }
                if (statusCode == 401) {

                }
            }
        };

        Log.i(LOG_TAG, "REQ GET " + url);

        if (async) {
            getAsyncClient().get(url, null, handler);
        } else {
            getSyncClient().get(url, null, handler);
        }
    }

    public synchronized void getCountryByCoord(Context context, double lat, double lng, boolean async,
                                               final Utils.GetProductsCallback<BestBuyProduct> callback){
        //https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=API_KEY

        final String url = buildUrl("/maps/geocode/");
    }

    private String buildUrl(String path){
        if (Utils.validString(mHost)) {
            return String.format("%s%s", mHost, path);
        }
        else return null;
    }

}
