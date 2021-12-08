package com.example.remakepyszne.util;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopCart implements Parcelable {
    private int shopCartid;
    private int userid;
    private int productID;
    private int quantity;
    private int restaurantID;
    private float price;

    protected ShopCart(Parcel in) {
        shopCartid = in.readInt();
        userid = in.readInt();
        productID = in.readInt();
        quantity = in.readInt();
        restaurantID = in.readInt();
        price = in.readFloat();
    }

    public static final Creator<ShopCart> CREATOR = new Creator<ShopCart>() {
        @Override
        public ShopCart createFromParcel(Parcel in) {
            return new ShopCart(in);
        }

        @Override
        public ShopCart[] newArray(int size) {
            return new ShopCart[size];
        }
    };

    public ShopCart() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(shopCartid);
        parcel.writeInt(userid);
        parcel.writeInt(productID);
        parcel.writeInt(quantity);
        parcel.writeInt(restaurantID);
        parcel.writeFloat(price);
    }

    public static final class Builder {
        private int shopCartid;
        private int userid;
        private int productID;
        private int quantity;
        private int restaurantID;
        private float price;

        public Builder shopCartid(int shopCartid) {
            this.shopCartid = shopCartid;
            return this;
        }

        public Builder userid(int userid) {
            this.userid = userid;
            return this;
        }

        public Builder productID(int productID) {
            this.productID = productID;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder restaurantID(int restaurantID) {
            this.restaurantID = restaurantID;
            return this;
        }

        public Builder price(float price) {
            this.price = price;
            return this;
        }

        public ShopCart build() {
            ShopCart shopCart = new ShopCart();
            shopCart.shopCartid = this.shopCartid;
            shopCart.userid = this.userid;
            shopCart.productID = this.productID;
            shopCart.quantity = this.quantity;
            shopCart.restaurantID = this.restaurantID;
            shopCart.price = this.price;
            return shopCart;
        }
    }

    public int getShopCartid() {
        return shopCartid;
    }

    public void setShopCartid(int shopCartid) {
        this.shopCartid = shopCartid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
