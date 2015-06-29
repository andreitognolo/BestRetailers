package com.bestretailers.model;

/**
 * Created by daniele on 17/06/15.
 */
public class BestBuyProduct {

    private String name;
    private String sku;
    private String regularPrice;
    private String thumbnailImage;

    public String getName(){
        return name;
    }

    public String getSku(){
        return sku;
    }

    public String getRegularPrice(){
        return regularPrice;
    }

    public String getThumbnailImage(){
        return thumbnailImage;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSku(String sku){
        this.sku = sku;
    }

    public void setRegularPrice(String regularPrice){
        this.regularPrice= regularPrice;
    }

    public void setThumbnailImage(String thumbnailImage){
        this.thumbnailImage = thumbnailImage;
    }


}
