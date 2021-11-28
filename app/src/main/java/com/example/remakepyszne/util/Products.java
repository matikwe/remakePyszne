package com.example.remakepyszne.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Products implements Parcelable {
    private int productID;
    private int restaurantID;
    private String nameProduct;
    private String category;
    private float price;
    private String logo;

    protected Products(Parcel in) {
        productID = in.readInt();
        restaurantID = in.readInt();
        nameProduct = in.readString();
        category = in.readString();
        price = in.readInt();
        logo = in.readString();
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public Products() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(productID);
        parcel.writeInt(restaurantID);
        parcel.writeString(nameProduct);
        parcel.writeString(category);
        parcel.writeFloat(price);
        parcel.writeString(logo);
    }

    public static final class Builder {
        private int productID;
        private int restaurantID;
        private String nameProduct;
        private String category;
        private float price;
        private String logo;

        public Builder productID(int productID) {
            this.productID = productID;
            return this;
        }

        public Builder restaurantID(int restaurantID) {
            this.restaurantID = restaurantID;
            return this;
        }

        public Builder nameProduct(String nameProduct) {
            this.nameProduct = nameProduct;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder price(float price) {
            this.price = price;
            return this;
        }

        public Builder logo(String logo) {
            this.logo = logo;
            return this;
        }

        public Products build() {
            Products products = new Products();
            products.productID = this.productID;
            products.restaurantID = this.restaurantID;
            products.nameProduct = this.nameProduct;
            products.category = this.category;
            products.price = this.price;
            products.logo = this.logo;
            return products;
        }
    }
    public int getProductID() {
        return productID;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    public String getLogo() {
        return logo;
    }
}
