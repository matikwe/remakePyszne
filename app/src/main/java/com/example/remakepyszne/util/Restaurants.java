package com.example.remakepyszne.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurants implements Parcelable {
    private int restaurantID;
    private String nameRestaurant;
    private String type;
    private String street;
    private String number;
    private String city;
    private String zip;

    protected Restaurants(Parcel in) {
        restaurantID = in.readInt();
        nameRestaurant = in.readString();
        type = in.readString();
        street = in.readString();
        number = in.readString();
        city = in.readString();
        zip = in.readString();
    }

    public static final Creator<Restaurants> CREATOR = new Creator<Restaurants>() {
        @Override
        public Restaurants createFromParcel(Parcel in) {
            return new Restaurants(in);
        }

        @Override
        public Restaurants[] newArray(int size) {
            return new Restaurants[size];
        }
    };

    public Restaurants() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(restaurantID);
        parcel.writeString(nameRestaurant);
        parcel.writeString(type);
        parcel.writeString(street);
        parcel.writeString(number);
        parcel.writeString(city);
        parcel.writeString(zip);
    }

    public static final class Builder {
        private int restaurantID;
        private String nameRestaurant;
        private String type;
        private String street;
        private String number;
        private String city;
        private String zip;

        public Builder restaurantID(int restaurantID) {
            this.restaurantID = restaurantID;
            return this;
        }

        public Builder nameRestaurant(String nameRestaurant) {
            this.nameRestaurant = nameRestaurant;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder zip(String zip) {
            this.zip = zip;
            return this;
        }

        public Restaurants build() {
            Restaurants restaurants = new Restaurants();
            restaurants.restaurantID = this.restaurantID;
            restaurants.nameRestaurant = this.nameRestaurant;
            restaurants.type = this.type;
            restaurants.street = this.street;
            restaurants.number = this.number;
            restaurants.city = this.city;
            restaurants.zip = this.zip;
            return restaurants;
        }
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public String getType() {
        return type;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }
}
