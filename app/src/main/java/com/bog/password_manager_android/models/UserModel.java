package com.bog.password_manager_android.models;

public class UserModel {
    String username;
    String password;
    String data;
    String vector;

    public UserModel(String username, String password, String data, String vector) {
        this.username = username;
        this.password = password;
        this.data = data;
        this.vector = vector;
    }
}
