package com.example.remakepyszne.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Addresses implements Parcelable {
    private int addressID;
    private int userID;
    private String street;
    private String number;
    private String city;
    private String zip;

    protected Addresses(Parcel in) {
        addressID = in.readInt();
        userID = in.readInt();
        street = in.readString();
        number = in.readString();
        city = in.readString();
        zip = in.readString();
    }

    public static final Creator<Addresses> CREATOR = new Creator<Addresses>() {
        @Override
        public Addresses createFromParcel(Parcel in) {
            return new Addresses(in);
        }

        @Override
        public Addresses[] newArray(int size) {
            return new Addresses[size];
        }
    };

    public Addresses() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(addressID);
        parcel.writeInt(userID);
        parcel.writeString(street);
        parcel.writeString(number);
        parcel.writeString(city);
        parcel.writeString(zip);
    }

    public static final class Builder {
        private String street;
        private String number;
        private String city;
        private String zip;
        private int addressID;
        private int userID;

        public Builder addressID(int addressID) {
            this.addressID = addressID;
            return this;
        }

        public Builder userID(int userID) {
            this.userID = userID;
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

        public Addresses build() {
            Addresses addresses = new Addresses();
            addresses.addressID = this.addressID;
            addresses.userID = this.userID;
            addresses.street = this.street;
            addresses.number = this.number;
            addresses.city = this.city;
            addresses.zip = this.zip;
            return addresses;
        }
    }

    public int getAddressID() {
        return addressID;
    }

    public int getUserID() {
        return userID;
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
