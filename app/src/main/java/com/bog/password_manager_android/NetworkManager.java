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
        void onLoaded(Integer resultCode);
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
                Integer resultCode;
                try {
                    resultCode = sendPostRequest(REGISTRATION_URL, body);
                } catch (IOException e) {
                    resultCode = null;
                }
                notifyLoaded(resultCode);
            }
        });
    }


    private void notifyLoaded(final Integer resultCode) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onLoaded(resultCode);
                }
            }
        });
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
