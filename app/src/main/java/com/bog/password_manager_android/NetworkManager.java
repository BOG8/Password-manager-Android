package com.bog.password_manager_android;


import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkManager {
    private static final NetworkManager MANAGER = new NetworkManager();
    private final Executor executor = Executors.newCachedThreadPool();
    private final String REGISTRATION_URL =  "https://backend-password-manager.herokuapp.com/api/user/";

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public interface Callback {
        void onLoaded(String request, String value);
    }

    public static NetworkManager getInstance() {
        return MANAGER;
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public void registrate(final String body) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String result = "";
                try {
                    result = sendPostRequest(REGISTRATION_URL, body);
                } catch (IOException e) {
                    result = null;
                }
                notifyLoaded(REGISTRATION_URL, result);
            }
        });
    }


    private void notifyLoaded(final String url, final String result) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onLoaded(url, result);
                }
            }
        });
    }


    public String sendPostRequest(String url, String json) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
              //  .addHeader("Content-type", "application/json")
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}
