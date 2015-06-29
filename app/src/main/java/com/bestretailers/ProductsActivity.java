package com.bestretailers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bestretailers.model.BestBuyProduct;
import com.bestretailers.rest.BestBuyClient;

import java.util.ArrayList;
import java.util.List;


public class ProductsActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProductsActivity.class.getSimpleName();
    private List<BestBuyProduct> mProducts;
    ProductsAdapter mProductAdapter;
    private EditText mSearchText;
    private Button mSearchButton;
    private Context mContext;
    private Loader mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mContext = this;

        mSearchText = (EditText) findViewById(R.id.descriptionSearch);
        mSearchButton = (Button) findViewById(R.id.buttonSearch);

        mProducts = new ArrayList<BestBuyProduct>();

        mProductAdapter = new ProductsAdapter(this, mProducts);

        ListView productList = (ListView) findViewById(R.id.listview_product);
        productList.setAdapter(mProductAdapter);

        //GET PRODUCTS ON TAP
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String prodStr = mSearchText.getText().toString();

                if (prodStr.equals("") || prodStr == null)
                    return;

                //dialog = ProgressDialog.show(ProductListActivity.this, "",
                //       "Loading. Please wait...", true);

                prodStr = prodStr.trim().replace(" ", "%20");
                Log.d(LOG_TAG, "Requested product" + prodStr);
                ArrayList<BestBuyProduct> receivedProducts;
                getProducts(prodStr, 1);
            }
        });
        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MyFragment())
                    .commit();
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void getProducts(String searchQuery, int pageNum){
        final ArrayList<BestBuyProduct> products = new ArrayList<>();
        BestBuyClient.getInstance().getBestBuyProducts(this, searchQuery, pageNum, true,
                new Utils.GetProductsCallback<BestBuyProduct>() {

                    @Override
                    public void onSuccess(List<BestBuyProduct> receivedProducts) {

                        //TODO: Clear only if first time query (pageNum = 1 maybe?)
                        mProducts.clear();
                        mProducts.addAll(receivedProducts);
                        mProductAdapter.notifyDataSetChanged();
                        Log.i(LOG_TAG, "BestBuy products successfully received");
                    }

                    @Override
                    public void onError(Throwable th) {
                        Log.e(LOG_TAG, th.getMessage(), new Exception(th));
                    }

                });
    }
}

 /*
    public static class MyFragment extends Fragment {

        ProductsAdapter mProductAdapter;
        private EditText mSearchText;
        private Button mSearchButton;


        public MyFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mSearchText = (EditText) rootView.findViewById(R.id.descriptionSearch);
            mSearchButton = (Button) rootView.findViewById(R.id.buttonSearch);

            // Now that we have some dummy forecast data, create an ArrayAdapter.
            // The ArrayAdapter will take data from a source (like our dummy forecast) and
            // use it to populate the ListView it's attached to.
            mProductAdapter = new ProductsAdapter(getActivity(), mProducts);

            ListView productList = (ListView) rootView.findViewById(R.id.listview_product);
            productList.setAdapter(mProductAdapter);

            //GET PRODUCTS ON TAP
            mSearchButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String prodStr = mSearchText.getText().toString();

                    //if (!prodStr.equals(mSearchQuery)) {
                    //    mProducts.clear();
                    //    mListAdapter.notifyDataSetChanged();
                    //    mCurrentPage = 0;
                    //    mPreviousTotal = 0;
                    //}

                    if (prodStr.equals("") || prodStr == null)
                        return;


                    //dialog = ProgressDialog.show(ProductListActivity.this, "",
                    //       "Loading. Please wait...", true);


                    prodStr = prodStr.trim().replace(" ", "%20");
                    Log.d(LOG_TAG, "Requested product" + prodStr);
                    ArrayList<BestBuyProduct> receivedProducts;
                    receivedProducts = getProducts(prodStr ,1);
                    if(!receivedProducts.isEmpty()){
                        mProducts.addAll(receivedProducts);
                        mProductAdapter.notifyDataSetChanged();
                    }
                }
            });

            return rootView;
        }
        */


