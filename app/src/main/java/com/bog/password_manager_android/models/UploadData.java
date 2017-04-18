package com.bog.password_manager_android.models;

public class UploadData {
    String username;
    String password;
    String data;
    String vector;

    public UploadData(String username_, String password_, String data_, String vector_) {
        this.username = username_;
        this.password = password_;
        this.data = data_;
        this.vector = vector_;
    }
}
