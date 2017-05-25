package com.bog.password_manager_android.network;


import android.content.SharedPreferences;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.bog.password_manager_android.MainActivity;
import com.bog.password_manager_android.ResourceActivity;
import com.bog.password_manager_android.Ui;
import com.bog.password_manager_android.models.RegistrationModel;
import com.bog.password_manager_android.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bog.password_manager_android.MainActivity.CIPHER_DATA;
import static com.bog.password_manager_android.MainActivity.IV;
import static com.bog.password_manager_android.MainActivity.PASSWORD_MANAGER;

public class NetworkManager {
    public static final int NETWORK_ERROR = 0;
    public static final int NETWORK_SUCCESS = 200;
    private static final NetworkManager MANAGER = new NetworkManager();
    private final Gson gson = new Gson();
    private final String REGISTRATION_URL =  "https://backend-password-manager.herokuapp.com/api/user/";
    private final String LOAD_DATA_URL = "https://backend-password-manager.herokuapp.com/api/data/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static NetworkManager getInstance() {
        return MANAGER;
    }

    public boolean registrate(final String username, final String password) {
        Integer resultCode;
        try {
            RegistrationModel model = new RegistrationModel(username, password);
            String body = gson.toJson(model);
            resultCode = sendPostRequest(REGISTRATION_URL, body);
        } catch (IOException e) {
            resultCode = NETWORK_ERROR;
        }
        return (resultCode == NETWORK_SUCCESS);
    }

    private class DoubleStringStructure {
        public String data;
        public String vector;

        public DoubleStringStructure() {
        }
    }

    void saveData(String data, String iv) {
        SharedPreferences.Editor editor = ResourceActivity.preferences.edit();
        editor.putString(MainActivity.CIPHER_DATA, data);
        editor.putString(MainActivity.IV, iv);
        editor.apply();
    }

    public boolean downloadData(final String username, final String password) {
        try {
            String url = LOAD_DATA_URL + "?username=" + username + "&password=" + password;
            Response response = sendGetRequest(url);
            if (response.code() != NETWORK_SUCCESS) {
                return false;
            } else {
                String jsonStr = response.body().string();
                DoubleStringStructure result = gson.fromJson(jsonStr, DoubleStringStructure.class);
                saveData(result.data, result.vector);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }


    public boolean uploadData(final String username, final String password, final String cipherData, final String iv)  {
        Integer resultCode;
        try {
            UserModel data = new UserModel(username, password, cipherData, iv);
            String body = gson.toJson(data);
            resultCode = sendPostRequest(LOAD_DATA_URL, body);
        } catch (IOException e) {
            resultCode = 0;
        }
        return (resultCode == NETWORK_SUCCESS);
    }


    private Response sendGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public Integer sendPostRequest(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .addHeader("Content-type", "application/json")
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        try {
            return response.code();
        } finally {
            response.close();
        }
    }
}
