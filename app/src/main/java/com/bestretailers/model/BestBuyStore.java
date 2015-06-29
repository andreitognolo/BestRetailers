package com.bestretailers.model;

/**
 * Created by daniele on 24/06/15.
 */
public class BestBuyStore {

    private int storeId;
    private String region;
    private String city;
    private String name;
    private String longName;
    private String storeType;
    private double lat;
    private double lng;

    public int getStoreId(){
        return storeId;
    }

    public String getRegion(){
        return region;
    }

    public String getCity(){
        return city;
    }

    public String getName(){
        return name;
    }

    public String getLongName(){
        return longName;
    }

    public String getStoreType(){
        return storeType;
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }

    public void setId(int storeId){
        this.storeId = storeId;
    }

    public void setRegion(String region){
        this.region = region;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setLongName(String longName){
        this.longName = longName;
    }

    public void setStoreType(String storeType){
        this.storeType = storeType;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public void setLng(double lng){
        this.lng = lng;
    }
}
