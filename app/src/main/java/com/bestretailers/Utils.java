package com.bestretailers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bestretailers.model.BestBuyProduct;
import com.bestretailers.model.BestBuyStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniele on 17/06/15.
 */
public class Utils {

    private static final String LOG_TAG = StoresActivity.class.getSimpleName();


    public static List<BestBuyProduct> createProductsFromJSON(JSONObject products) {
        ArrayList<BestBuyProduct> productsList = new ArrayList<>();
        try {
            JSONArray productsArray = products.getJSONArray("products");
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject element = productsArray.getJSONObject(i);
                productsList.add(Utils.createProductFromJSON(element));
            }
        } catch (JSONException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
        return productsList;
    }

    public static BestBuyProduct createProductFromJSON(JSONObject element) {
        String elementName = "";
        String elementSku = "";
        String elementReguarPrice = "";
        String elementThumbnailImage = "";
        BestBuyProduct product = new BestBuyProduct();
        try {
            if (element.has("name"))
                elementName = element.getString("name");
            if (element.has("sku"))
                elementSku = element.getString("sku");
            if (element.has("regularPrice"))
                elementReguarPrice = element.getString("regularPrice");
            if (element.has("thumbnailImage"))
                elementThumbnailImage = element.getString("thumbnailImage");
            product.setName(elementName);
            product.setSku(elementSku);
            product.setRegularPrice(elementReguarPrice);
            product.setThumbnailImage(elementThumbnailImage);
        } catch (JSONException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
        Log.d(LOG_TAG, "create element: " + elementName);
        return product;
    }

    public static List<BestBuyStore> createStoresFromJSON(JSONObject stores) {
        ArrayList<BestBuyStore> storesList = new ArrayList<>();
        try {
            JSONArray storesArray = stores.getJSONArray("stores");
            for (int i = 0; i < storesArray.length(); i++) {
                JSONObject element = storesArray.getJSONObject(i);
                storesList.add(Utils.createStoreFromJSON(element));
            }
        } catch (JSONException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
        return storesList;
    }

    public static BestBuyStore createStoreFromJSON(JSONObject element) {
        int elementId = 0;
        String elementRegion = "";
        String elementCity = "";
        String elementName = "";
        String elementLongName = "";
        String elementType = "";
        double elementLat = 0;
        double elementLng = 0;

        BestBuyStore store = new BestBuyStore();
        try {
            if (element.has("id"))
                elementId = element.getInt("id");
            if (element.has("region"))
                elementRegion = element.getString("region");
            if (element.has("city"))
                elementCity = element.getString("city");
            if (element.has("name"))
                elementName = element.getString("name");
            if (element.has("longName"))
                elementLongName = element.getString("longName");
            if (element.has("type"))
                elementType = element.getString("storeType");
            if (element.has("lat"))
                elementLat = element.getDouble("lat");
            if (element.has("lng"))
                elementLng = element.getDouble("lng");
            store.setId(elementId);
            store.setRegion(elementRegion);
            store.setCity(elementCity);
            store.setName(elementName);
            store.setLongName(elementLongName);
            store.setStoreType(elementType);
            store.setLat(elementLat);
            store.setLng(elementLng);
        } catch (JSONException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
        }
        Log.d(LOG_TAG, "create element: " + elementName);
        return store;
    }

    public static String getPreferredCountry(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_country_key),
                context.getString(R.string.pref_country_default));
    }


    public static boolean validString(String str) {
        if (str == null) {
            return false;
        }
        String string = str.trim();
        return string.length() > 0;
    }


    /**
     * Callback interface for get best buy products
     */
    public static interface GetProductsCallback<T> {

        public void onSuccess(List<BestBuyProduct> products);

        public void onError(Throwable th);

    }

    /**
     * Callback interface for get best buy stores
     */
    public static interface GetStoresCallback<T> {

        public void onSuccess(List<BestBuyStore> stores);

        public void onError(Throwable th);

    }
}
