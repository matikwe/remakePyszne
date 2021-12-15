package com.example.remakepyszne.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    private int id;
    private String login;
    private String password;
    private String role;
    private String email;
    private int phoneNumber;

    protected Users(Parcel in) {
        id = in.readInt();
        login = in.readString();
        password = in.readString();
        role = in.readString();
        email = in.readString();
        phoneNumber = in.readInt();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public Users() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(login);
        parcel.writeString(password);
        parcel.writeString(role);
        parcel.writeString(email);
        parcel.writeInt(phoneNumber);
    }


    public static final class Builder {
        private int id;
        private String login;
        private String password;
        private String role;
        private String email;
        private int phoneNumber;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(int phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Users build() {
            Users users = new Users();
            users.id = this.id;
            users.login = this.login;
            users.password = this.password;
            users.role = this.role;
            users.email = this.email;
            users.phoneNumber = this.phoneNumber;
            return users;
        }
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }
}
