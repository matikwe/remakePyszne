package com.example.remakepyszne.util;

public class Users {
    private int id;
    private String login;
    private String password;
    private String mail;
    private int phoneNumber;

    public static final class Builder {
        private int id;
        private String login;
        private String password;
        private String mail;
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

        public Builder mail(String mail) {
            this.mail = mail;
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
            users.mail = this.mail;
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

    public String getMail() {
        return mail;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
