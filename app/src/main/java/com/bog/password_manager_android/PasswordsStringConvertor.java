package com.bog.password_manager_android;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by alex on 14.04.2017.
 */

class PasswordsStringConvertor {
    static List<PasswordModel> deserialize(String str) {
        Type itemListType = new TypeToken<List<PasswordModel>>() {}.getType();
        return new Gson().fromJson(str, itemListType);
    }

    static String serialize(List<PasswordModel> passwords) {
        return new Gson().toJson(passwords);
    }
}