package com.example.remakepyszne.util;

public class Users {
    private int id;
    private String login;
    private String password;
    private String email;
    private int phoneNumber;

    public static final class Builder {
        private int id;
        private String login;
        private String password;
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

    public String getEmail() {
        return email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }
}
