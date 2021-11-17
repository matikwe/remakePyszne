package com.example.remakepyszne.util;

public class Addresses {
    private int addressID;
    private int userID;
    private String street;
    private String number;
    private String city;
    private String zip;

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
