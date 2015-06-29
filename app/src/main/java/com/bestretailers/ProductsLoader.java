package com.bestretailers;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.bestretailers.model.BestBuyProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniele on 23/06/15.
 */
public class ProductsLoader extends AsyncTaskLoader<List<BestBuyProduct>> {

    private static final String LOG_TAG = ProductsLoader.class.getSimpleName();

    private String mQuery;
    private int mPage;

    public ProductsLoader(Context context, String query, int page) {
        super(context);
        mQuery = query;
        mPage = page;
    }

    @Override
    public List<BestBuyProduct> loadInBackground() {


        final List<BestBuyProduct> products;
        products = new ArrayList<BestBuyProduct>();

        /*
        BestBuyClient.getInstance().getBestBuyProducts(getContext(), mQuery, mPage, false,
                new Utils.GetProductsCallback<BestBuyProduct>() {

                    @Override
                    public void onSuccess(List<BestBuyProduct> receivedProducts) {

                        //TODO: Clear only if first time query (pageNum = 1 maybe?)
                        products.addAll(receivedProducts);
                        Log.i(LOG_TAG, "BestBuy products successfully received");
                    }

                    @Override
                    public void onError(Throwable th) {
                        Log.e(LOG_TAG, th.getMessage(), new Exception(th));
                    }

                });
                */
        return products;
    }


}
