package com.bog.password_manager_android;


import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private final String LOAD_DATA_URL = "https://backend-password-manager.herokuapp.com/api/data/";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public interface RegistrationCallback {
        void onLoaded(Integer resultCode);
    }

    public interface DownloadCallback {
        void onDownLoaded(String cipherData, String iv, int resultCode);
    }

    public static NetworkManager getInstance() {
        return MANAGER;
    }

    private RegistrationCallback registrationCallback;
    private DownloadCallback downloadCallback;

    public void setRegistrationCallback(RegistrationCallback callback) {
        this.registrationCallback = callback;
    }

    public void setDownloadCallback(DownloadCallback callback) {
        this.downloadCallback = callback;
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
                notifyRegisrationResult(resultCode);
            }
        });
    }

    private class DoubleStringStructure {
        public String cipherData;
        public String password;
    }

    public void downloadData(final String username, final String password) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String cipherData;
                String iv;

                try {
                    String url = LOAD_DATA_URL + "?username=" + username  + "&password" + password;
                    DoubleStringStructure result = sendGetRequest(url);
                    notifyDownloadResult(result.cipherData, result.password, 200);
                } catch (IOException e) {
                    notifyDownloadResult(null, null, 0);
                }
            }
        });
    }



    private void notifyRegisrationResult(final Integer resultCode) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (registrationCallback != null) {
                    registrationCallback.onLoaded(resultCode);
                }
            }
        });
    }

    private void notifyDownloadResult(final String cipherData, final String iv, final Integer resultCode) {
        Ui.run(new Runnable() {
            @Override
            public void run() {
                if (downloadCallback != null) {
                    downloadCallback.onDownLoaded(cipherData, iv, resultCode);
                }
            }
        });
    }

    private DoubleStringStructure sendGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        try {
            String responseStr =  response.body().string();
            return new GsonBuilder().create().fromJson(responseStr, DoubleStringStructure.class);
        } catch (IOException e) {
            return null;
        }
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
