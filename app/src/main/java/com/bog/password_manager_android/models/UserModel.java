package com.bog.password_manager_android.models;

public class UserModel {
    String username;
    String password;
    String data;
    String vector;

    public UserModel(String username_, String password_, String data_, String vector_) {
        this.username = username_;
        this.password = password_;
        this.data = data_;
        this.vector = vector_;
    }
}
