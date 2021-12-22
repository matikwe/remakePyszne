package com.example.remakepyszne.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;

public class Orders implements Parcelable {

    private int orderID;
    private int userID;
    private int restaurantID;
    private int addressID;
    private String description;
    private Time orderTime;
    private Date orderDate;
    private String state;
    private float totalPrice;
    private int providerID;
    private int stateid;

    protected Orders(Parcel in) {
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    public Orders() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static final class Builder {
        private int orderID;
        private int userID;
        private int restaurantID;
        private int addressID;
        private String description;
        private Time orderTime;
        private Date orderDate;
        private String state;
        private float totalPrice;
        private int providerID;
        private int stateid;

        public Builder orderID(int orderID) {
            this.orderID = orderID;
            return this;
        }

        public Builder userID(int userID) {
            this.userID = userID;
            return this;
        }

        public Builder restaurantID(int restaurantID) {
            this.restaurantID = restaurantID;
            return this;
        }

        public Builder addressID(int addressID) {
            this.addressID = addressID;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder orderTime(Time orderTime) {
            this.orderTime = orderTime;
            return this;
        }

        public Builder orderDate(Date orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder totalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder providerID(int providerID) {
            this.providerID = providerID;
            return this;
        }

        public Builder stateid(int stateid) {
            this.stateid = stateid;
            return this;
        }

        public Orders build(){
            Orders orders=new Orders();
            orders.orderID=this.orderID;
            orders.userID=this.userID;
            orders.restaurantID=this.restaurantID;
            orders.addressID=this.addressID;
            orders.description=this.description;
            orders.orderTime=this.orderTime;
            orders.orderDate=this.orderDate;
            orders.state=this.state;
            orders.totalPrice=this.totalPrice;
            orders.providerID=this.providerID;
            orders.stateid=this.stateid;
            return orders;
        }
    }

    public int getOrderID() {
        return orderID;
    }

    public int getUserID() {
        return userID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public int getAddressID() {
        return addressID;
    }

    public String getDescription() {
        return description;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getState() {
        return state;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getProviderID() {
        return providerID;
    }

    public int stateid() {
        return stateid;
    }

    public static Creator<Orders> getCREATOR() {
        return CREATOR;
    }
}
