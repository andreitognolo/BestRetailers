package com.bestretailers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestretailers.model.BestBuyProduct;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by daniele on 23/06/15.
 */
public class ProductsAdapter extends BaseAdapter {

    private List<BestBuyProduct> mProducts;

    private class ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productSku;
        TextView productRegularPrice;
        TextView productPriceText;
    }

    private Activity mContext;
    private LayoutInflater mLayoutInflater;

    public ProductsAdapter(Activity context, List<BestBuyProduct> products) {
        mContext = context;
        mProducts = products;
        mLayoutInflater = mContext.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            //Pass to the adapter the layout of each element
            view = mLayoutInflater.inflate(R.layout.list_item_product, parent, false);
            holder.productImage = (ImageView) view.findViewById(R.id.imageView);
            holder.productName = (TextView) view.findViewById(R.id.txt_product_name);
            holder.productSku = (TextView) view.findViewById(R.id.txt_product_sku);
            holder.productPriceText = (TextView) view.findViewById(R.id.txt_product_price_txt);
            holder.productRegularPrice = (TextView) view.findViewById(R.id.txt_product_price);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final BestBuyProduct product = (BestBuyProduct) getItem(position);

        if (product != null) {

            Picasso.with(mContext).load(product.getThumbnailImage()).into(holder.productImage);
            holder.productName.setText(product.getName());
            holder.productSku.setText(product.getSku());
            holder.productRegularPrice.setText("$" + product.getRegularPrice());
        }

        return view;
    }

}